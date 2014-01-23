package com.ernyz.dotw.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Combat.Attack;
import com.ernyz.dotw.Combat.AttackCreator;
import com.ernyz.dotw.Model.Items.Item;
import com.ernyz.dotw.Model.Tiles.Tile;

/**
 * All entities which can move in anyway are subclassed from this class. This class subclasses {@link Entity}.
 * 
 * @author Ernyz
 */
public class MoveableEntity extends Entity {
	
	protected GameWorld gameWorld;
	/*
	 * Following two fields are used to hold objects around this MoveableEntity.
	 * Radius is set in particular Entities classes (like Goblin, Player, etc.)
	 * This results in improved performance, since these arrays are small portions of tile and entities arrays.
	 */
	protected Array<Tile> surroundingTiles;  //Used for collisions and other logic
	protected Array<MoveableEntity> surroundingEntities;  //Used for collisions and other logic
	
	protected Polygon bounds;
	protected Vector2 velocity;
	protected Vector2 lastPos;  //Position before moving, needed for collision checking
	protected float activeSurroundingsRange;//Everything which is in this range of any moveable entity is included in its calculations
	private Array<Attack> attacks;  //All attacks in progress are held in here
	
	//TODO type should be an integer, an id of an item TODO should be private/protected
	public Item currentWeapon;
	
	/*
	 * Item slots. The first digit of the vector is the rotation offset from centre of the entity to the slot;
	 * The second is the distance from centre of entity to the slot.
	 */
	protected Vector2 rightHand;  //TODO Change to private later
	
	/*
	 * Stats
	 */
	protected int dungeonLevel;
	//protected int experienceLevel;
	
	protected int strength;
	protected int dexterity;
	protected int constitution;
	protected int intelligence;
	protected int spirit;
	
	protected float health;
	protected float speed;
	protected float damage;

	public MoveableEntity(Vector2 position, Vector2 velocity, float rotation, float speed, GameWorld gameWorld) {
		super(position, rotation);
		this.gameWorld = gameWorld;
		this.velocity = velocity;
		this.lastPos = position.cpy();
		this.speed = speed;
		bounds = new Polygon();
		attacks = new Array<Attack>();
	}
	
	public void update() {
		//Dispose of finished attacks
		for(int i = 0; i < attacks.size; i++) {
			if(attacks.get(i).getIsFinished())
				attacks.removeIndex(i);
		}
		//Update unfinished ones
		for(int i = 0; i < attacks.size; i++) {
			attacks.get(i).update();
		}
		
		//Get new surrounding tiles
		surroundingTiles.clear();
		for(int i = 0; i < gameWorld.getTiles().size; i++) {
			Tile tile = gameWorld.getTiles().get(i);
			if(tile.getPosition().dst(this.getPosition()) <= activeSurroundingsRange) {
				surroundingTiles.add(tile);
			}
		}
		//Get new surrounding entities
		surroundingEntities.clear();
		for(int i = 0; i < gameWorld.getEntities().size; i++) {
			MoveableEntity entity = gameWorld.getEntities().get(i);
			if(entity.getPosition().dst(this.getPosition()) <= activeSurroundingsRange) {
				surroundingEntities.add(entity);
			}
		}
	}
	
	public void checkCollisions() {
		//Update last position
		lastPos.set(position);
		
		//Move this entity in x axis
		position.x += velocity.cpy().x * Gdx.graphics.getDeltaTime() * speed;
		bounds.setPosition(position.x, bounds.getY());
		//Check collisions with tiles and then with entities and then with player
		for(int i = 0; i < surroundingTiles.size; i++) {
			if(!surroundingTiles.get(i).getWalkable() && Intersector.overlapConvexPolygons(bounds, surroundingTiles.get(i).getBounds())) {
				position.x = lastPos.x;
				bounds.setPosition(lastPos.x, bounds.getY());
				break;
			}
		}
		for(int i = 0; i < surroundingEntities.size; i++) {
			//Entity shouldn't check collisions with itself
			if(!this.equals(surroundingEntities.get(i))) {
				//Entity vs. other entities
				if(Intersector.overlapConvexPolygons(bounds, surroundingEntities.get(i).getBounds())) {
					position.x = lastPos.x;
					bounds.setPosition(lastPos.x, bounds.getY());
					break;
					//Move player in other axis, so it wont get stuck when in contact with entity
					/*if(this.position.y <= surroundingEntities.get(i).getPosition().y)
						this.position.y -= 1.5f;
					else
						this.position.y += 1.5f;*/
				}
			}
		}
		//If this entity is not player
		if(!gameWorld.getPlayer().equals(this)) {
			//Entity vs player
			if(Intersector.overlapConvexPolygons(bounds, gameWorld.getPlayer().getBounds())) {
				position.x = lastPos.x;
				bounds.setPosition(lastPos.x, bounds.getY());
			}
		}
				
		//Move this entity in y axis
		position.y += velocity.cpy().y * Gdx.graphics.getDeltaTime() * speed;
		bounds.setPosition(bounds.getX(), position.y);
		//Check collisions with tiles and then with entities
		for(int i = 0; i < surroundingTiles.size; i++) {
			if(!surroundingTiles.get(i).getWalkable() && Intersector.overlapConvexPolygons(bounds, surroundingTiles.get(i).getBounds())) {
				position.y = lastPos.y;
				bounds.setPosition(bounds.getX(), lastPos.y);
				break;
			}
		}
		for(int i = 0; i < surroundingEntities.size; i++) {
			if(!this.equals(surroundingEntities.get(i))) { 
				if(Intersector.overlapConvexPolygons(bounds, surroundingEntities.get(i).getBounds())) {
					position.y = lastPos.y;
					bounds.setPosition(bounds.getX(), lastPos.y);
					break;
					//Move player in other axis, so it wont get stuck when in contact with entity
					/*if(this.position.x <= surroundingEntities.get(i).getPosition().x)
						this.position.x -= 1.5f;
					else
						this.position.x += 1.5f;*/
				}
			}
		}
		//If this entity is not player
		if(!gameWorld.getPlayer().equals(this)) {
			//Entity vs player
			if(Intersector.overlapConvexPolygons(bounds, gameWorld.getPlayer().getBounds())) {
				position.y = lastPos.y;
				bounds.setPosition(bounds.getX(), lastPos.y);
			}
		}
	}
	
	public void attack(int button) {
		if(button == 0) {  //LMB
			attacks.add(AttackCreator.primaryAttack(this));
		}
	}
	
	//Attacks are needed for renderer when it is in debug mode.
	public Array<Attack> getAttacks() {
		return attacks;
	}
	
	public Vector2 getRightHand() {
		return rightHand;
	}

	public Polygon getBounds() {
		return bounds;
	}

	public void setBounds(Polygon bounds) {
		this.bounds = bounds;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public Vector2 getLastPos() {
		return lastPos;
	}

	public void setLastPos(Vector2 position) {
		lastPos = position;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public float getHealth() {
		return health;
	}
	
	public void setHealth(float health) {
		this.health = health;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public Array<Tile> getSurroundingTiles() {
		return surroundingTiles;
	}
	
	public Array<MoveableEntity> getSurroundingEntities() {
		return surroundingEntities;
	}

	public int getDungeonLevel() {
		return dungeonLevel;
	}

	public void setDungeonLevel(int dungeonLevel) {
		this.dungeonLevel = dungeonLevel;
	}

	public int getStrength() {
		return strength;
	}

	public void setStrength(int strength) {
		this.strength = strength;
	}

	public int getDexterity() {
		return dexterity;
	}

	public void setDexterity(int dexterity) {
		this.dexterity = dexterity;
	}

	public int getConstitution() {
		return constitution;
	}

	public void setConstitution(int constitution) {
		this.constitution = constitution;
	}

	public int getIntelligence() {
		return intelligence;
	}

	public void setIntelligence(int intelligence) {
		this.intelligence = intelligence;
	}

	public int getSpirit() {
		return spirit;
	}

	public void setSpirit(int spirit) {
		this.spirit = spirit;
	}
	
}
