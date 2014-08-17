package com.ernyz.dotw.Model;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Combat.BasicAttack;
import com.ernyz.dotw.Combat.BasicAttackCreator;
import com.ernyz.dotw.Model.Items.Item;
import com.ernyz.dotw.Model.Tiles.Tile;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;

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
	
	protected Skeleton skeleton;
	protected SkeletonData skeletonData;
	protected TextureAtlas atlas;
	protected SkeletonJson skeletonJson;
	
	protected Polygon bounds;
	protected int radius;
	private float targetRotation;
	private float rotationalVelocity = 340;  //FIXME: init not here

	protected Vector2 velocity;
	protected Vector2 lastPos;  //Position before moving, needed for collision checking
	private float lastPosX;
	private float lastPosY;
	protected float activeSurroundingsRange;//Everything which is in this range of any moveable entity is included in its calculations
	private boolean isDead;
	protected Array<Integer> inventory;  //Holds id's of items player possesses
	/*
	 * Holds id's of items which are equipped in these slots.
	 */
	protected HashMap<String, Integer> equipmentSlots;
	
	/*
	 * Stats
	 */
	protected int dungeonLevel;
	protected int experienceLevel;
	
	protected int backpackCapacity = 40;  //TODO remove hardcoding
	
	protected int strength = 1;
	protected int dexterity = 1;
	protected int constitution = 1;
	protected int intelligence = 1;
	protected int spirit = 1;
	
	protected float health;
	protected float maxHealth = 100;  //TODO Values in this line and below should be set during generation/load process.
	protected float mana = 10;
	protected float maxMana = 10;
	protected float stamina = 50;
	protected float maxStamina = 50;
	protected float speed;

	public MoveableEntity(Vector2 position, Vector2 velocity, float rotation, float speed, GameWorld gameWorld) {
		super(position, rotation);
		this.gameWorld = gameWorld;
		this.velocity = velocity;
		this.lastPos = position.cpy();
		this.speed = speed;
		bounds = new Polygon();
		isDead = false;
		
		inventory = new Array<Integer>();
		
		surroundingTiles = new Array<Tile>();
		surroundingEntities = new Array<MoveableEntity>();
	}
	
	public void update() {
		//Check to see if this entity is still alive
		if(health <= 0) {
			isDead = true;
			return;
		}
		
		//Turn player towards target rotation
		//System.out.println("tarRot: "+ targetRotation + "; rot: " + rotation);
		float cwAngle = 0;
		if(rotation > targetRotation)
			cwAngle = rotation-targetRotation;
		else
			cwAngle = 360-targetRotation+rotation;
		//System.out.println(cwAngle);
		if(cwAngle <= 360 - cwAngle) {  //CW
			if(cwAngle < Gdx.graphics.getDeltaTime() * rotationalVelocity) {
				rotation = targetRotation;
			} else {
				rotation += Gdx.graphics.getDeltaTime() * rotationalVelocity*(-1);
			}
		} else {  //CCW
			if(360-cwAngle < Gdx.graphics.getDeltaTime() * rotationalVelocity) {
				rotation = targetRotation;
			} else {
				rotation += Gdx.graphics.getDeltaTime() * rotationalVelocity;
			}
		}
		if(rotation < 0)
			rotation += 360;
		else if(rotation > 360)
			rotation -= 360;
		
		
		//Update weapon attack timers
		for(String slot : equipmentSlots.keySet()) {
			//equipmentSlots.get(slot)
			if(equipmentSlots.get(slot) != -1) {
				Item item = gameWorld.getItemById(equipmentSlots.get(slot));
				if(item.getBool("IsWeapon") && item.getFloat("TimeUntilAttack") > 0) {  //TODO: Check Type.WEAPON instead of "IsWeapon"
					item.set("TimeUntilAttack", item.getFloat("TimeUntilAttack") - Gdx.graphics.getDeltaTime());
				}
			}
		}
		for(int i = 0; i < inventory.size; i++) {
			Item item = gameWorld.getItemById(inventory.get(i));
			if(item.getBool("IsWeapon") && item.getFloat("TimeUntilAttack") > 0) {  //TODO: Check Type.WEAPON instead of "IsWeapon"
				item.set("TimeUntilAttack", item.getFloat("TimeUntilAttack") - Gdx.graphics.getDeltaTime());
			}
			//System.out.println(item.getFloat("TimeUntilAttack"));
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
	
	public void moveX() {
		lastPosX = position.x;
		position.x += velocity.cpy().x * Gdx.graphics.getDeltaTime() * speed;
		bounds.setPosition(position.x-radius, bounds.getY());
	}
	
	public void moveY() {
		lastPosY = position.y;
		position.y += velocity.cpy().y * Gdx.graphics.getDeltaTime() * speed;
		bounds.setPosition(bounds.getX(), position.y-radius);
	}
	
	public void handleMouseClick(int button) {
		/* 0-LMB; 1-RMB; 2-ScrollButton */
		/*
		 * *Check to see whether held weapon or spell slot is active; TODO
		 * *Check if selected action is possible (not on cd. and etc.);
		 * *Perform that action.
		 */
		if(button == 0) {  //Primary action
			gameWorld.basicAttacks.add(BasicAttackCreator.createBasicAttack(this, true, gameWorld));
		}
		else if(button == 1) {  //Secondary action
			gameWorld.basicAttacks.add(BasicAttackCreator.createBasicAttack(this, false, gameWorld));
		}
	}
	
	public boolean canTakeItem(Item item) {
		if(inventory.size+1 > backpackCapacity) {
			GameWorld.addMessage("Your backpack is full!");
			return false;
		}
		
		float maxWeight = strength*3;
		float weightCarried = calculateWeightCarried();
		
		if(maxWeight-weightCarried >= item.getWeight()) {
			return true;
		} else {
			GameWorld.addMessage("You can not carry that much weight!");
			return false;
		}
		
	}
	
	private float calculateWeightCarried() {
		float result = 0;
		
		for(int i = 0; i < inventory.size; i++) {
			result += gameWorld.getItemById(inventory.get(i)).getWeight();
		}
		HashMap<String, Integer> equipment = gameWorld.getPlayer().getEquipmentSlots();
		for(String equipmentSlot : equipment.keySet()) {
			if(!equipment.get(equipmentSlot).equals(-1)) {
				result += gameWorld.getItemById(equipment.get(equipmentSlot)).getWeight();
			}
		}
		
		return result;
	}
	
	public Array<Integer> getInventory() {
		return inventory;
	}
	public void setInventory(Array<Integer> inventory) {
		this.inventory = inventory;
	}
	public HashMap<String, Integer> getEquipmentSlots() {
		return equipmentSlots;
	}
	public void setEquipmentSlots(HashMap<String, Integer> equipmentSlots) {
		this.equipmentSlots = equipmentSlots;
	}
	
	public Polygon getBounds() {
		return bounds;
	}

	/*public void setBounds(Polygon bounds) {
		this.bounds = bounds;
	}*/

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
	
	public boolean getIsDead() {
		return isDead;
	}
	
	public float getHealth() {
		return health;
	}
	
	public void setHealth(float health) {
		this.health = health;
		if(this.health <= 0)
			this.health = 0;
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

	public float getMaxHealth() {
		return maxHealth;
	}

	public void setMaxHealth(float maxHealth) {
		this.maxHealth = maxHealth;
	}

	public float getMana() {
		return mana;
	}

	public void setMana(float mana) {
		this.mana = mana;
		if(this.mana <= 0)
			this.mana = 0;
	}

	public float getMaxMana() {
		return maxMana;
	}

	public void setMaxMana(float maxMana) {
		this.maxMana = maxMana;
	}

	public float getStamina() {
		return stamina;
	}

	public void setStamina(float stamina) {
		this.stamina = stamina;
		if(this.stamina <= 0)
			this.stamina = 0;
	}

	public float getMaxStamina() {
		return maxStamina;
	}

	public void setMaxStamina(float maxStamina) {
		this.maxStamina = maxStamina;
	}
	
	public int getRadius() {
		return radius;
	}
	
	public Integer getBackpackCapacity() {
		return backpackCapacity;
	}
	
	public Skeleton getSkeleton() {
		return skeleton;
	}
	
	public float getLastPosX() {
		return lastPosX;
	}
	
	public float getLastPosY() {
		return lastPosY;
	}
	
	public float getTargetRotation() {
		return targetRotation;
	}

	public void setTargetRotation(float targetRotation) {
		this.targetRotation = targetRotation;
	}
	
}
