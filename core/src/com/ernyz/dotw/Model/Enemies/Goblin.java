package com.ernyz.dotw.Model.Enemies;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Model.Resources;
import com.ernyz.dotw.Model.Items.Item;
import com.ernyz.dotw.Model.Items.Item.ItemType;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonJson;

/**
 * This class can represent any kind of goblin.
 * 
 * @author Ernyz
 */
public class Goblin extends Enemy {
	
	private Vector2 dirVector = new Vector2();  //Direction that entity faces
	
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
	public void update() {
		super.update();
		
		examineSurroundings();
	}

	@Override
	public void examineSurroundings() {
		//Set the state of the monster
		if(this.getPosition().dst(gameWorld.getPlayer().getPosition()) <= 200) {
			state = StateEnum.ATTACK;
		}
		else {
			state = StateEnum.WANDER;
		}
		
		//Now that we know the state, execute actions
		if(state == StateEnum.ATTACK) {
			//Turn to target
			dirVector.set(gameWorld.getPlayer().getPosition().x, gameWorld.getPlayer().getPosition().y);		
			this.setRotation(new Vector2(dirVector.sub(this.getPosition()).nor()).angle());
			//Decide how to attack player (with spell, melee or ranged weapon)
			//See if target is in attack range
			if(this.getPosition().dst(gameWorld.getPlayer().getPosition()) <= 80) {  //FIXME hardcoding
				//attack(0);  //Primary attack
				handleMouseClick(0);
			}
			else {
				//Attack or move closer if needed
				velocity.set(dirVector.x/Math.abs(dirVector.x), dirVector.y/Math.abs(dirVector.y));  //Velocity only needs values of +/- 0 or 1 to indicate direction
			}
		}
		else if(state == StateEnum.WANDER) {
			velocity.set(0, 0);
		}
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
		lHand.set("Damage", 8f);
		lHand.set("Range", 20f);//20f
		lHand.setTexture(new Texture("data/items/unarmed.png"));
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
		rHand.set("Damage", 8f);
		rHand.set("Range", 20f);//20f
		rHand.setTexture(new Texture("data/items/unarmed.png"));
		unarmedLimbs.put(Resources.BODY_RIGHT_HAND, rHand);
	}
}
