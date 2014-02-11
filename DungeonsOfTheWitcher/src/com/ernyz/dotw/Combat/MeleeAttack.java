package com.ernyz.dotw.Combat;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Model.MoveableEntity;

/**
 * Holds all the data needed for a melee attack.
 * 
 * @author Ernyz
 */
public class MeleeAttack implements Attack {
	
	/*
	 * Attack's hit boundaries and used for collisions with enemies, walls and etc.
	 * All the information needed to create bounds is in the attackers weapon HashMap.
	 */
	private Polygon bounds;
	
	/*
	 * Value of how much damage will this attack do. It depends on weapon, attacker's stats and etc.
	 * Note, that enemies will probably resist some/all of this damage.
	 * Also, if damage is positive, it means that it will heal the target, instead of reducing it's hp.
	 */
	private float damage;
	
	private float startRot;  //Attack's starting rotation.
	private float currentRot;  //Attack's current rotation. TODO i should probably just use bounds rotation...
	private float range;  //Range of an attack
	private float distCovered;  //Distance covered.
	private float endRot;  //Attack's finishing rotation.
	private boolean isFinishing;  //If true, attack has ended and is now fading away
	private float fadeTimer;  //Time it takes for this attack to fade and be removed from entities attacks' array
	private float fadeSpeed;  //The bigger this is, the faster the attack will fade out.
	private boolean isFinished;  //If true, attack is finished and should be removed.
	private MoveableEntity attacker;
	
	/*
	 * Stuff needed for 'swipe' attack animation.
	 */
	private float alpha;
	private float thickness;
	private Array<Vector2> path;
	private int capacity;
	
	/**
	 * Creates melee attack.
	 * @param attacker - an attacking {@link MoveableEntity}
	 * @param attackType - the type of the attack (slash, stab, crush)
	 */
	public MeleeAttack(MoveableEntity attacker, String attackType) {
		this.attacker = attacker;
		isFinishing = false;
		isFinished = false;
		fadeTimer = 0.2f;
		fadeSpeed = 0.05f;
		
		alpha = 1;
		thickness = 5f;
		capacity = 15;
		path = new Array<Vector2>();
		
		/*
		 * Attack's damage, rotation, position and other values depend on attack type and weapon
		 */
		if(attackType.equals("Stab")) {
			if(GameWorld.items.get((int)attacker.getEquipedItem("RightHand")).getName().equals("Dagger")) {
				//Set damage
				damage = (-1)*(attacker.getDexterity() + GameWorld.items.get((int)attacker.getEquipedItem("RightHand")).getFloat("Damage"));
				//Set boundaries TODO make bounds dependent on weapon texture dimensions?
				bounds = new Polygon(new float[] {0, 0, 30, 0, 30, 10, 0, 10});
				bounds.setOrigin(0, 0);
				bounds.setPosition(attacker.getPosition().x, attacker.getPosition().y);
				distCovered = 0;
				//range = 8;
				range = 20;
			}
		}
		
		//Set starting rotation
		startRot = attacker.getRotation();
		currentRot = startRot;
		//Set position according to attackers rotation
		bounds.setPosition(
				attacker.getPosition().x + attacker.getWidth()/2 + 
					MathUtils.cosDeg(attacker.getRotation()+attacker.getRightHand().x)*attacker.getRightHand().y, 
				attacker.getPosition().y + attacker.getHeight()/2 + 
					MathUtils.sinDeg(attacker.getRotation()+attacker.getRightHand().x)*attacker.getRightHand().y);
		
		//Point shouldn't point to the origin of the rectangle bound, but to the 'middle' of the rectangle bound
		float dX = (float)(Math.cos(Math.atan2(5, 30)+bounds.getRotation()*Math.PI/180) * Math.sqrt(0*30+5*5));
		float dY = (float)(Math.sin(Math.atan2(5, 30)+bounds.getRotation()*Math.PI/180) * Math.sqrt(0*30+5*5));
		path.insert(0, new Vector2(bounds.getX()+dX, bounds.getY()+dY));
		//Ensure that array size does not exceed capacity
		while(path.size > capacity) {
			path.pop();
		}
	}

	@Override
	public void update() {
		if(isFinished) return;
		
		if(isFinishing) {
			alpha -= fadeSpeed;
			fadeTimer -= Gdx.graphics.getDeltaTime();
			if(fadeTimer <= 0) {
				isFinished = true;
			}
			return;
		}
		//Set rotation
		bounds.setRotation(attacker.getRotation());
		//Set position according to attackers rotation
		bounds.setPosition(
				attacker.getPosition().x + attacker.getWidth()/2 + 
					MathUtils.cosDeg(attacker.getRotation()+attacker.getRightHand().x)*attacker.getRightHand().y, 
				attacker.getPosition().y + attacker.getHeight()/2 + 
					MathUtils.sinDeg(attacker.getRotation()+attacker.getRightHand().x)*attacker.getRightHand().y);
		//Move the attack according to attackers rotation
		float dX = MathUtils.cosDeg(attacker.getRotation()) * Gdx.graphics.getDeltaTime() * GameWorld.items.get((int)attacker.getEquipedItem("RightHand")).getFloat("Speed");
		float dY = MathUtils.sinDeg(attacker.getRotation()) * Gdx.graphics.getDeltaTime() * GameWorld.items.get((int)attacker.getEquipedItem("RightHand")).getFloat("Speed");
		distCovered += Math.sqrt(dX*dX + dY*dY);
		bounds.setPosition(bounds.getX()+dX*distCovered, bounds.getY()+dY*distCovered);
		if(distCovered >= range) {
			//Stop the attack if it's out of range
			isFinishing = true;
			return;
		}
		//Check for collisions with entities
		for(int i = 0; i < attacker.getSurroundingEntities().size; i++) {
			if(!attacker.getSurroundingEntities().get(i).equals(attacker)) {
				if(Intersector.overlapConvexPolygons(bounds, attacker.getSurroundingEntities().get(i).getBounds())) {
					hitEnemy(attacker.getSurroundingEntities().get(i));
					isFinishing = true;
					return;
				}
			}
		}
		//Check for collisions with walls
		for(int i = 0; i < attacker.getSurroundingTiles().size; i++) {
			if(!attacker.getSurroundingTiles().get(i).getWalkable()) {
				if(Intersector.overlapConvexPolygons(bounds, attacker.getSurroundingTiles().get(i).getBounds())) {
					isFinishing = true;
					return;
				}
			}
		}
		//Update 'swipe'
		//Point shouldn't point to the origin of the rectangle bound, but to the middle of the rectangle bound
		dX = (float)(Math.cos(Math.atan2(5, 30)+bounds.getRotation()*Math.PI/180) * Math.sqrt(30*30+5*5));
		dY = (float)(Math.sin(Math.atan2(5, 30)+bounds.getRotation()*Math.PI/180) * Math.sqrt(30*30+5*5));
		path.insert(0, new Vector2(bounds.getX()+dX, bounds.getY()+dY));
		//Ensure that array size does not exceed capacity
		while(path.size > capacity) {
			path.pop();
		}
	}
	
	private float hitEnemy(MoveableEntity target) {
		//Calculate damage according to targets resistances and other stuff, then apply it
		//TODO dmg recalculation will be here when resistances are implemented
		target.setHealth(target.getHealth()+damage);
		GameWorld.addMessage(attacker.getName() + " hits "+ target.getName() + " for " + String.valueOf(damage) + " damage!");
		return damage;
	}
	
	@Override
	public Polygon getBounds() {
		return bounds;
	}
	
	@Override
	public boolean getIsFinished() {
		return isFinished;
	}
	
	@Override
	public Array<Vector2> getPath() {
		return path;
	}
	
	@Override
	public float getAlpha() {
		return alpha;
	}
	
	@Override
	public float getThickness() {
		return thickness;
	}

}
