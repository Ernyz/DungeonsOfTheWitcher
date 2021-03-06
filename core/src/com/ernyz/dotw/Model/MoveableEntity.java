package com.ernyz.dotw.Model;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Combat.BasicAttack;
import com.ernyz.dotw.Combat.BasicAttackCreator;
import com.ernyz.dotw.Factories.EffectFactory;
import com.ernyz.dotw.Factories.FloatingTextFactory;
import com.ernyz.dotw.Model.Effects.Effect;
import com.ernyz.dotw.Model.Items.Item;
import com.ernyz.dotw.Model.Items.ItemEnchantments.Enchantment;
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
	
	protected Texture texture;
	protected Polygon bounds;
	protected int radius;
	private float targetRotation;
	private float rotationalVelocity = 450;  //FIXME: init not here

	private Vector2 velocity = new Vector2(0, 0);
	public int velocityUp = 0;
	public int velocityLeft = 0;
	public int velocityDown = 0;
	public int velocityRight = 0;
	private Vector2 velocityEnforced = new Vector2(0, 0);
	protected Vector2 lastPos;  //Position before moving, needed for collision checking
	private float lastPosX;
	private float lastPosY;
	protected float activeSurroundingsRange;//Everything which is in this range of any moveable entity is included in its calculations
	private boolean isDead;
	protected Array<Integer> inventory;  //Holds id's of items player possesses
	public HashMap<String, Item> unarmedLimbs;  //TODO: change to protected
	/*
	 * Holds id's of items which are equipped in these slots.
	 */
	protected HashMap<String, Integer> equipmentSlots;
	
	private Array<Effect> effects = new Array<Effect>();
	
	private boolean blocking = false;
	
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
	
	private float tickInterval = 1;
	private float timeUntilNextTick = 0;
	private float healthPerScond = 0.2f;
	private float manaPerScond = 0.5f;
	private float staminaPerScond = 0.8f;

	public MoveableEntity(Vector2 position, float rotation, GameWorld gameWorld) {
		super(position, rotation);
		this.gameWorld = gameWorld;
		this.lastPos = position.cpy();
		bounds = new Polygon();
		isDead = false;
		
		inventory = new Array<Integer>();
		unarmedLimbs = new HashMap<String, Item>();
		
		surroundingTiles = new Array<Tile>();
		surroundingEntities = new Array<MoveableEntity>();
	}
	
	public void update(float delta) {
		//Check to see if this entity is still alive
		if(health <= 0) {
			isDead = true;
			return;
		}
		
		timeUntilNextTick -= delta;
		if(timeUntilNextTick <= 0) {
			timeUntilNextTick = tickInterval;
			regenerateStats();  //TODO: i dont really like this name
		}
		
		//Turn player towards target rotation
		float cwAngle = 0;
		if(rotation > targetRotation)
			cwAngle = rotation-targetRotation;
		else
			cwAngle = 360-targetRotation+rotation;
		if(cwAngle <= 360 - cwAngle) {  //CW
			if(cwAngle < delta * rotationalVelocity) {
				rotation = targetRotation;
			} else {
				rotation += delta * rotationalVelocity*(-1);
			}
		} else {  //CCW
			if(360-cwAngle < delta * rotationalVelocity) {
				rotation = targetRotation;
			} else {
				rotation += delta * rotationalVelocity;
			}
		}
		if(rotation < 0)
			rotation += 360;
		else if(rotation > 360)
			rotation -= 360;
		
		//Update velocity
		velocity.set(velocityLeft+velocityRight, velocityUp+velocityDown);
		
		//Update effects
		for(Effect e : effects) {
			e.update(delta);
			if(e.isFinished())
				effects.removeValue(e, false);
		}
		
		//Update weapon attack timers
		for(String slot : equipmentSlots.keySet()) {
			if(equipmentSlots.get(slot) != -1) {
				Item item = gameWorld.getItemById(equipmentSlots.get(slot));
				if(item.getBool("IsWeapon") && item.getFloat("TimeUntilAttack") > 0) {  //TODO: Check Type.WEAPON instead of "IsWeapon"
					item.set("TimeUntilAttack", item.getFloat("TimeUntilAttack") - delta);
				}
			}
		}
		for(int i = 0; i < inventory.size; i++) {
			Item item = gameWorld.getItemById(inventory.get(i));
			if(item.getBool("IsWeapon") && item.getFloat("TimeUntilAttack") > 0) {  //TODO: Check Type.WEAPON instead of "IsWeapon"
				item.set("TimeUntilAttack", item.getFloat("TimeUntilAttack") - delta);
			}
		}
		for(String slot : unarmedLimbs.keySet()) {
			Item item = unarmedLimbs.get(slot);
			if(item.getBool("IsWeapon") && item.getFloat("TimeUntilAttack") > 0) {  //TODO: Check Type.WEAPON instead of "IsWeapon"
				item.set("TimeUntilAttack", item.getFloat("TimeUntilAttack") - delta);
			}
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
		if(velocityEnforced.x == 0)
			position.x += velocity.cpy().x * Gdx.graphics.getDeltaTime() * speed;
		else
			position.x += velocityEnforced.cpy().x * Gdx.graphics.getDeltaTime() * speed;
		bounds.setPosition(position.x-radius, bounds.getY());
	}
	
	public void moveY() {
		lastPosY = position.y;
		if(velocityEnforced.y == 0)
			position.y += velocity.cpy().y * Gdx.graphics.getDeltaTime() * speed;
		else
			position.y += velocityEnforced.cpy().y * Gdx.graphics.getDeltaTime() * speed;
		bounds.setPosition(bounds.getX(), position.y-radius);
	}
	
	public void handleMouseClick(int button) {
		/* 0-LMB; 1-RMB; 2-ScrollButton */
		/*
		 * *Check to see whether held weapon or spell slot is active; TODO
		 * *Check if selected action is possible (not on cd. and etc.);
		 * *Perform that action.
		 */
		if(button == 0) {  //Secondary action
			if(this.canAttack(Resources.BODY_LEFT_HAND) == 1) {
				BasicAttack ba = BasicAttackCreator.createBasicAttack(this, false, gameWorld);
				//temp
				ba.getEnchantments().add(new Enchantment("BasicAttack"));
				gameWorld.basicAttacks.add(ba);
				setStamina(getStamina()-1);
			} else if(this.canAttack(Resources.BODY_LEFT_HAND) == 2) {
				BasicAttack ba = BasicAttackCreator.createBasicAttack(this, false, gameWorld);
				ba.getEnchantments().add(new Enchantment("CounterAttack"));
				gameWorld.basicAttacks.add(ba);
				removeEffect("CanCounterAttack");
				setStamina(getStamina()-2);
			}
		}
		else if(button == 1) {  //Primary action
			if(this.canAttack(Resources.BODY_RIGHT_HAND) == 1) {
				BasicAttack ba = BasicAttackCreator.createBasicAttack(this, true, gameWorld);
				//temp
				ba.getEnchantments().add(new Enchantment("BasicAttack"));
				gameWorld.basicAttacks.add(ba);
				setStamina(getStamina()-1);
			} else if(this.canAttack(Resources.BODY_RIGHT_HAND) == 2) {
				BasicAttack ba = BasicAttackCreator.createBasicAttack(this, true, gameWorld);
				ba.getEnchantments().add(new Enchantment("CounterAttack"));
				gameWorld.basicAttacks.add(ba);
				removeEffect("CanCounterAttack");
				setStamina(getStamina()-2);
			}
		}
	}
	
	public boolean canMove() {
		if(hasEffect("KnockBack")) {
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @return - 0 if can not attack;\n
	 * 1 if can attack;
	 * 2 if can counter attack;
	 */
	protected int canAttack(String hand) {
		if(isBlocking()) return 0;
		
		if(hasEffect("CanCounterAttack")) {
			return 2;
		} else if(hasEffect("RecoveringFromAttack")) {
			return 0;
		}
		
		Item rightHandItem = null;
		Item leftHandItem = null;
		if(hand.equals(Resources.BODY_RIGHT_HAND)) {
			if(equipmentSlots.get(hand) != -1)
				rightHandItem = gameWorld.getItemById(equipmentSlots.get(hand));
			else
				rightHandItem = unarmedLimbs.get(Resources.BODY_RIGHT_HAND);
			
//			if(rightHandItem.getBool("CanAttack")) {
//				return 1;
//			}
			if(rightHandItem.getFloat("TimeUntilAttack") <= 0) {
				return 1;
			}
		} else if(hand.equals(Resources.BODY_LEFT_HAND)) {
			if(equipmentSlots.get(hand) != -1)
				leftHandItem = gameWorld.getItemById(equipmentSlots.get(hand));
			else
				leftHandItem = unarmedLimbs.get(Resources.BODY_LEFT_HAND);
			
//			if(leftHandItem.getBool("CanAttack")) {
//				return 1;
//			}
			if(leftHandItem.getFloat("TimeUntilAttack") <= 0) {
				return 1;
			}
		}
		return 0;
	}
	
	public boolean canBlock() {
		Item rightHandItem = null;
		Item leftHandItem = null;
		
		if(equipmentSlots.get(Resources.BODY_RIGHT_HAND) != -1)
			rightHandItem = gameWorld.getItemById(equipmentSlots.get(Resources.BODY_RIGHT_HAND));
		else
			rightHandItem = unarmedLimbs.get(Resources.BODY_RIGHT_HAND);
		
		if(equipmentSlots.get(Resources.BODY_LEFT_HAND) != -1)
			leftHandItem = gameWorld.getItemById(equipmentSlots.get(Resources.BODY_LEFT_HAND));
		else
			leftHandItem = unarmedLimbs.get(Resources.BODY_LEFT_HAND);
		
//		if(rightHandItem.getBool("CanAttack") && leftHandItem.getBool("CanAttack")) {
//			return true;
//		}
		if(rightHandItem.getFloat("TimeUntilAttack") <= 0 && leftHandItem.getFloat("TimeUntilAttack") <= 0) {
			return true;
		}
		return false;
	}
	
	protected float calculateBasicAttackDistance() {
		Item rightHandItem = null;
		Item leftHandItem = null;
		if(equipmentSlots.get(Resources.BODY_RIGHT_HAND) != -1)
			rightHandItem = gameWorld.getItemById(equipmentSlots.get(Resources.BODY_RIGHT_HAND));
		else
			rightHandItem = unarmedLimbs.get(Resources.BODY_RIGHT_HAND);
		
		if(equipmentSlots.get(Resources.BODY_LEFT_HAND) != -1)
			leftHandItem = gameWorld.getItemById(equipmentSlots.get(Resources.BODY_LEFT_HAND));
		else
			leftHandItem = unarmedLimbs.get(Resources.BODY_LEFT_HAND);
		
		return (rightHandItem.getFloat("Range") > leftHandItem.getFloat("Range")) ? rightHandItem.getFloat("Range") : leftHandItem.getFloat("Range");
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
	
	public void onCollision(BasicAttack ba) {  //TODO: abstract attacks to one type
		if(!blocking) {
			setHealth(getHealth()-ba.getWeapon().getFloat("Damage"));
			GameWorld.addFloatingText(FloatingTextFactory.createFloatingText(String.valueOf(ba.getWeapon().getFloat("Damage")), getPosition().x-getWidth()/2, getPosition().y));
			addEffect(EffectFactory.recoveringFromAttack(ba.getAttacker(), this));
		} else if(blocking) {
			if(hasEffect("RecoveringFromAttack")) {
				if(MathUtils.randomBoolean(0.75f)) {
					GameWorld.addFloatingText(FloatingTextFactory.createFloatingText("Blocked", getPosition().x-getWidth()/2, getPosition().y));
					ba.setBlocked(true);
					addEffect(EffectFactory.canCounterAttack(this, this));
				} else {
					setHealth(getHealth()-ba.getWeapon().getFloat("Damage"));
					GameWorld.addFloatingText(FloatingTextFactory.createFloatingText(String.valueOf(ba.getWeapon().getFloat("Damage")), getPosition().x-getWidth()/2, getPosition().y));
					addEffect(EffectFactory.recoveringFromAttack(ba.getAttacker(), this));
				}
			} else {
				if(MathUtils.randomBoolean(0.85f)) {
					GameWorld.addFloatingText(FloatingTextFactory.createFloatingText("Blocked", getPosition().x-getWidth()/2, getPosition().y));
					ba.setBlocked(true);
					addEffect(EffectFactory.canCounterAttack(this, this));
				} else {
					setHealth(getHealth()-ba.getWeapon().getFloat("Damage"));
					GameWorld.addFloatingText(FloatingTextFactory.createFloatingText(String.valueOf(ba.getWeapon().getFloat("Damage")), getPosition().x-getWidth()/2, getPosition().y));
					addEffect(EffectFactory.recoveringFromAttack(ba.getAttacker(), this));
				}
			}
		}
	}
	
	protected Effect getEffectByName(String name) {
		Effect result = null;
		for(Effect e : effects) {
			if(e.getEffectName().equals(name))
				result = e;
		}
		return result;
	}
	
	protected boolean hasEffect(String name) {
		for(Effect e : effects) {
			if(e.getEffectName().equals(name))
				return true;
		}
		return false;
	}
	
	public void addEffect(Effect e) {
		Effect effect = getEffectByName(e.getEffectName());
		if(effect == null) {
			effects.add(e);
		} else {
			if(effect.getStacks() >= effect.getMaxStacks()) {
				effect.setStacks(effect.getMaxStacks());
				effect.setTimeWorking(0f);
			} else {
				effect.setStacks(effect.getStacks()+1);
				effect.setTimeWorking(0f);
			}
		}
	}
	
	public void removeEffect(String name) {
		for(Effect e : effects) {
			if(e.getEffectName().equals(name))
				e.destroy();
				effects.removeValue(e, false);
		}
	}
	
	private void regenerateStats() {
		setHealth(getHealth()+healthPerScond);
		setMana(getMana()+manaPerScond);
		setStamina(getStamina()+staminaPerScond);
	}
	
	public Array<Effect> getEffects() {
		return effects;
	}
	
	/*private float calculateAttackRating() {
		return dexterity;
	}
	
	private float calculateDefenceRating() {
		return dexterity;
	}*/
	
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
	
	public Texture getTexture() {
		return texture;
	}
	
	public Polygon getBounds() {
		return bounds;
	}

	/*public void setBounds(Polygon bounds) {
		this.bounds = bounds;
	}*/

	public Vector2 getVelocity() {
		//velocity.set(velocityLeft+velocityRight, velocityUp+velocityDown);
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public Vector2 getVelocityEnforced() {
		return velocityEnforced;
	}

	public void setVelocityEnforced(Vector2 velocityEnforced) {
		this.velocityEnforced = velocityEnforced;
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
		if(this.health >= maxHealth)
			this.health = maxHealth;
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
		if(this.mana > maxMana)
			this.mana = maxMana;
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
		if(this.stamina > maxStamina)
			this.stamina = maxStamina;
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

	public boolean isBlocking() {
		return blocking;
	}

	public void setBlocking(boolean blocking) {
		//TODO add check if(this.blocking and blocking) return;
		float rotationAmount = 30;
		if(blocking) {
			getSkeleton().findBone("RightHand").setRotation(getSkeleton().findBone("RightHand").getRotation()+rotationAmount);
			getSkeleton().findBone("LeftHand").setRotation(getSkeleton().findBone("LeftHand").getRotation()-rotationAmount);
		} else {
			getSkeleton().findBone("RightHand").setRotation(getSkeleton().findBone("RightHand").getRotation()-rotationAmount);
			getSkeleton().findBone("LeftHand").setRotation(getSkeleton().findBone("LeftHand").getRotation()+rotationAmount);
		}
		this.blocking = blocking;
	}
	
}
