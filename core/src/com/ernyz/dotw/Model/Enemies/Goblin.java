package com.ernyz.dotw.Model.Enemies;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Model.Resources;
import com.ernyz.dotw.Model.Items.Item;
import com.ernyz.dotw.Model.Items.Item.ItemType;
import com.ernyz.dotw.Model.Tiles.Tile;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonJson;

/**
 * This class can represent any kind of goblin.
 * 
 * @author Ernyz
 */
public class Goblin extends Enemy {
	
	private Vector2 dirVector = new Vector2();  //Direction that entity faces
	
	//Tmp vars for AI
	Tile targetTile = null;
	//---------------
	
	public Goblin(Vector2 position, Vector2 velocity, float rotation, float speed, GameWorld gameWorld) {
		super(position, velocity, rotation, speed, gameWorld);

		atlas = new TextureAtlas(Gdx.files.internal("data/entities/goblin/skeleton.atlas"));
		skeletonJson = new SkeletonJson(atlas);
		skeletonData = skeletonJson.readSkeletonData(Gdx.files.internal("data/entities/goblin/skeleton.json"));
		skeleton = new Skeleton(skeletonData);
		
		Sprite sprite = atlas.createSprite("body");
		width = sprite.getWidth();
		height = sprite.getHeight();
		
		radius = (int) height/2;
		int a = (int) width;
		float[] tmp = new float[]{
				radius-a/2, 0,
				radius+a/2, 0,
				2*radius, radius-a/2,
				2*radius, radius+a/2,
				radius+a/2, 2*radius,
				radius-a/2, 2*radius,
				0, radius+a/2,
				0, radius-a/2
		};
		bounds.setVertices(tmp);
		bounds.setOrigin(radius, radius);
		
		//Create equipment slots
		equipmentSlots = new HashMap<String, Integer>();
		equipmentSlots.put(Resources.BODY_HEAD, -1);
		equipmentSlots.put(Resources.BODY_NECK, -1);
		equipmentSlots.put(Resources.BODY_SHOULDERS, -1);
		equipmentSlots.put(Resources.BODY_CHEST, -1);
		equipmentSlots.put(Resources.BODY_LEFT_HAND, -1);
		equipmentSlots.put(Resources.BODY_RIGHT_HAND, -1);
		equipmentSlots.put(Resources.BODY_PALMS, -1);
		//equipmentSlots.put("LeftHandRing", -1);
		//equipmentSlots.put("RightHandRing", -1);
		equipmentSlots.put(Resources.BODY_WAIST, -1);
		equipmentSlots.put(Resources.BODY_LEGS, -1);
		equipmentSlots.put(Resources.BODY_FEET, -1);
		
		createUnarmedLimbs();
		
		//Some stats should be set manually
		activeSurroundingsRange = 500;
		
		state = StateEnum.WANDER;
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		
		setState();
		act(delta);
	}
	
	@Override
	protected void act(float delta) {
		if(state == StateEnum.WANDER) {
			float currX = MathUtils.floor(getPosition().x/50)*50;
			float currY = MathUtils.floor(getPosition().y/50)*50;
			if(targetTile == null || 
			(currX == targetTile.getPosition().x && currY == targetTile.getPosition().y)) {
				//Get new target tile
				targetTile = getRandomTargetTile();
			} else {
				//Move to target tile
				dirVector.set(targetTile.getPosition().cpy().x+25, targetTile.getPosition().cpy().y+25);
				if(canMove()) {
					this.setTargetRotation(new Vector2(dirVector.sub(this.getPosition()).nor()).angle());
					velocity.set(dirVector.x, dirVector.y);
				}
			}
		}
		else if(state == StateEnum.COMBAT_DEFENSIVE) {  //TODO: Grouping up when in defensive stance, is probably a nice idea.
			this.setTargetRotation(new Vector2(gameWorld.getPlayer().getPosition().cpy().sub(this.getPosition()).nor()).angle());
			if(hasEffect("CanCounterAttack")) {
				setBlocking(false);
				handleMouseClick(1);
				state = StateEnum.COMBAT_OFFENSIVE;
			}
			else if(!isBlocking() && this.canBlock()) {
				setBlocking(true);
			}
		}
		else if(state == StateEnum.COMBAT_OFFENSIVE) {
			this.setTargetRotation(new Vector2(gameWorld.getPlayer().getPosition().cpy().sub(this.getPosition()).nor()).angle());
			
			float distToTargetEntity = gameWorld.getPlayer().getPosition().dst(getPosition());
			float basicAttackDist = calculateBasicAttackDistance()+radius+radius/2;
			System.out.println(distToTargetEntity +" > "+ basicAttackDist);
			if(distToTargetEntity > basicAttackDist) {  //Target is too far, move closer
				if(canMove()) {
					if(isBlocking()) {
						setBlocking(false);
					}
					dirVector.set(gameWorld.getPlayer().getPosition()).sub(this.getPosition());
					velocity.x = (dirVector.x != 0) ? dirVector.x/Math.abs(dirVector.x) : 0;
					velocity.y = (dirVector.y != 0) ? dirVector.y/Math.abs(dirVector.y) : 0;
				}
			}
			else if(Math.abs(distToTargetEntity-basicAttackDist) < delta*speed) {  //Distance is right, stay
				if(isBlocking()) {
					setBlocking(false);
				}
				velocity.set(0, 0);
				handleMouseClick(1);
			}
			else if(distToTargetEntity < basicAttackDist) {  //Target is too close, back off
				if(!isBlocking() && this.canBlock()) {
					setBlocking(true);
				}
				if(canMove()) {
					dirVector.set(gameWorld.getPlayer().getPosition()).sub(this.getPosition());
					velocity.x = (dirVector.x != 0) ? -1*dirVector.x/Math.abs(dirVector.x) : 0;
					velocity.y = (dirVector.y != 0) ? -1*dirVector.y/Math.abs(dirVector.y) : 0;
				}
			}
		}
		else if(state == StateEnum.FLEE) {
			//Turn back to player?
			//Increase movement speed?
			//Decrease defence rating?
			if(canMove()) {
				dirVector.set(gameWorld.getPlayer().getPosition()).sub(this.getPosition());
				velocity.x = (dirVector.x != 0) ? -1*dirVector.x/Math.abs(dirVector.x) : 0;
				velocity.y = (dirVector.y != 0) ? -1*dirVector.y/Math.abs(dirVector.y) : 0;
			}
		}
	}

	@Override
	protected void setState() {
		//Set the state of the monster
		if(state == StateEnum.WANDER) {
			if(isPlayerNearby()) {
				if(isHealthLow()) {
					state = StateEnum.FLEE;
				} else if(isArmedWell()) {
					state = StateEnum.COMBAT_OFFENSIVE;
				} else {
					state = StateEnum.COMBAT_DEFENSIVE;
					velocity.set(0, 0);
				}
			} else if(itemsNearby().size > 0) {
				state = StateEnum.GET_ITEM;
			} else if(itemsNearby().size == 0) {
				state = StateEnum.WANDER;
			}
		}
		else if(state == StateEnum.COMBAT_OFFENSIVE) {
			if(isHealthLow()) {
				state = StateEnum.FLEE;
			} else if(!isPlayerNearby()) {
				state = StateEnum.WANDER;
			}
		}
		else if(state == StateEnum.COMBAT_DEFENSIVE) {
			if(isHealthLow()) {
				state = StateEnum.FLEE;
			} else if(!isPlayerNearby()) {
				state = StateEnum.WANDER;
			}
		}
		else if(state == StateEnum.GET_ITEM) {
			if(isPlayerNearby()) {
				if(isHealthLow()) {
					state = StateEnum.FLEE;
				} else if(isArmedWell()) {
					state = StateEnum.COMBAT_OFFENSIVE;
				} else {
					state = StateEnum.COMBAT_DEFENSIVE;
				}
			}
		}
		else if(state == StateEnum.FLEE) {
			if(!isHealthLow() && isPlayerNearby()) {
				state = StateEnum.COMBAT_DEFENSIVE;
			} else if(!isPlayerNearby() ) {
				state = StateEnum.WANDER;
			}
		}
	}
	
	private boolean isPlayerNearby() {
		if(this.getPosition().dst(gameWorld.getPlayer().getPosition()) <= 200) {
			return true;
		}
		return false;
	}
	private boolean isHealthLow() {
		if(health*100/maxHealth <= 30) {
			return true;
		}
		return false;
	}
	private boolean isArmedWell() {//TODO
		return false;
	}
	private Array<Item> itemsNearby() {//TODO
		Array<Item> items = new Array<Item>();
		return items;
	}
	private Tile getRandomTargetTile() {
		Tile t = null;
		Array<Tile> tmp = new Array<Tile>();
		float currX = MathUtils.floor(getPosition().x/50)*50;
		float currY = MathUtils.floor(getPosition().y/50)*50;
		
		for(Tile tile : surroundingTiles) {
			if(tile.getPosition().x == currX-50 && tile.getPosition().y == currY) {
				if(tile.getWalkable())
					tmp.add(tile);
			}
			else if(tile.getPosition().x == currX && tile.getPosition().y == currY+50) {
				if(tile.getWalkable())
					tmp.add(tile);
			}
			else if(tile.getPosition().x == currX+50 && tile.getPosition().y == currY) {
				if(tile.getWalkable())
					tmp.add(tile);
			}
			else if(tile.getPosition().x == currX && tile.getPosition().y == currY-50) {
				if(tile.getWalkable())
					tmp.add(tile);
			}
		}
		/*for(Tile target : tmp) {
			target.switchToDebugTexture();
		}*/
		//System.out.println(tmp.size);
		t = tmp.get(MathUtils.random(tmp.size-1));
		//t.switchToDebugTexture();
		
		return t;
	}
	
	private void createUnarmedLimbs() {
		Item lHand = new Item();
		lHand.setName(Resources.BODY_LEFT_HAND);
		lHand.setType(ItemType.WEAPON);
		lHand.set("PrimaryAttack", "Punch");
		lHand.set("Speed", 170f);  //170
		lHand.set("AttackInterval", .5f);  //Interval between attacks
		lHand.set("TimeUntilAttack", 0f);
		lHand.set("IsWeapon", true);
		lHand.set("IsMelee", true);
		lHand.set("CanAttack", true);
		lHand.set("Damage", 6f);
		lHand.set("Range", 20f);//20f
		lHand.setTexture(new Texture("data/items/goblinUnarmed.png"));
		unarmedLimbs.put(Resources.BODY_LEFT_HAND, lHand);
		
		Item rHand = new Item();
		rHand.setName(Resources.BODY_RIGHT_HAND);
		rHand.setType(ItemType.WEAPON);
		rHand.set("PrimaryAttack", "Punch");
		rHand.set("Speed", 170f);  //170
		rHand.set("AttackInterval", .5f);  //Interval between attacks
		rHand.set("TimeUntilAttack", 0f);
		rHand.set("IsWeapon", true);
		rHand.set("IsMelee", true);
		rHand.set("CanAttack", true);
		rHand.set("Damage", 6f);
		rHand.set("Range", 20f);//20f
		rHand.setTexture(new Texture("data/items/goblinUnarmed.png"));
		unarmedLimbs.put(Resources.BODY_RIGHT_HAND, rHand);
	}
}
