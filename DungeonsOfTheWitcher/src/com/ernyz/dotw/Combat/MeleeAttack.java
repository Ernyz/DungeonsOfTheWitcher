package com.ernyz.dotw.Combat;

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
	
	/*
	 * Attack's starting coordinates.
	 */
	private float startX, startY;
	
	/*
	 * Attack's starting rotation.
	 */
	private float startRot;
	
	/*
	 * Attack's current coordinates.
	 */
	private float currentX, currentY;
	
	/*
	 * Attack's current rotation.
	 */
	private float currentRot;
	
	/*
	 * Attack's finishing coordinates.
	 */
	private float endX, endY;
	
	/*
	 * Attack's finishing rotation.
	 */
	private float endRot;
	
	private MoveableEntity attacker;
	
	/**
	 * Creates melee attack.
	 * @param attacker - an attacking {@link MoveableEntity}
	 * @param attackType - the type of the attack (slash, stab, crush)
	 */
	public MeleeAttack(MoveableEntity attacker, String attackType) {
		this.attacker = attacker;
		//Set damage
		damage = attacker.getStrength() + attacker.currentWeapon.getInt("Damage");
		//Set starting position and rotation
		startRot = attacker.getRotation();
		startX = attacker.getPosition().x;
		startY = attacker.getPosition().y;
		currentRot = startRot;
		currentX = startX;
		currentY = startY;
		//Initialise boundaries TODO make bounds dependant on weapon texture dimensions?
		bounds = new Polygon(new float[] {0, 0, 20, 0, 20, 7, 0, 7});
		bounds.setOrigin(0, bounds.getBoundingRectangle().height/2);
		bounds.setPosition(currentX, currentY);
	}

	@Override
	public void update() {
		bounds.setRotation(attacker.getRotation());
		bounds.setPosition(attacker.getPosition().x, attacker.getPosition().y);
	}
	
	@Override
	public Polygon getBounds() {
		return bounds;
	}

}
