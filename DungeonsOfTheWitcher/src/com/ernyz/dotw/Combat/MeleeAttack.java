package com.ernyz.dotw.Combat;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
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
	 */
	private float damage;
	
	private float startRot;  //Attack's starting rotation.
	private float currentRot;  //Attack's current rotation.
	private float range;  //Range of an attack
	private float distCovered;  //Distance covered.
	private float endRot;  //Attack's finishing rotation.
	private boolean isFinished;  //If true, attack is finished and should be removed.
	private MoveableEntity attacker;
	
	/**
	 * Creates melee attack.
	 * @param attacker - an attacking {@link MoveableEntity}
	 * @param attackType - the type of the attack (slash, stab, crush)
	 */
	public MeleeAttack(MoveableEntity attacker, String attackType) {
		this.attacker = attacker;
		isFinished = false;
		if(attacker.currentWeapon.getName().equals("Dagger")) {
			//Set damage
			damage = attacker.getDexterity() + attacker.currentWeapon.getInt("Damage");
			//Set boundaries TODO make bounds dependent on weapon texture dimensions?
			bounds = new Polygon(new float[] {0, 0, 30, 0, 30, 10, 0, 10});
			bounds.setOrigin(0, 0);
			bounds.setPosition(attacker.getPosition().x, attacker.getPosition().y);
			distCovered = 0;
			range = 25;
		}
		//Set starting rotation
		startRot = attacker.getRotation();
		currentRot = startRot;
	}

	@Override
	public void update() {
		System.out.println("update");
		if(isFinished) return;
		//Set rotation
		bounds.setRotation(attacker.getRotation());
		//Set position according to attackers rotation
		bounds.setPosition(
				attacker.getPosition().x + attacker.getWidth()/2 + 
					MathUtils.cosDeg(attacker.getRotation()+attacker.getRightHand().x)*attacker.getRightHand().y, 
				attacker.getPosition().y + attacker.getHeight()/2 + 
					MathUtils.sinDeg(attacker.getRotation()+attacker.getRightHand().x)*attacker.getRightHand().y);
		//Move projectile
		float dX = MathUtils.cosDeg(attacker.getRotation());
		float dY = MathUtils.sinDeg(attacker.getRotation());
		distCovered += Math.sqrt(dX*dX + dY*dY);
		bounds.setPosition(bounds.getX()+dX*distCovered, bounds.getY()+dY*distCovered);
		if(distCovered >= range) {
			//Stop the attack
			isFinished = true;
		}
		//Check for collisions with enemies and walls
		//TODO ...
	}
	
	@Override
	public Polygon getBounds() {
		return bounds;
	}
	
	@Override
	public boolean getIsFinished() {
		return isFinished;
	}

}
