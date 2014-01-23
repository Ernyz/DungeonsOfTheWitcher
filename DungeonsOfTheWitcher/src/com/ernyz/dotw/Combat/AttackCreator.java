package com.ernyz.dotw.Combat;

import com.ernyz.dotw.Model.MoveableEntity;

/**
 * All attacks (magical or non-magical) are created in here.
 * For example, if LMB is pressed and player wields dagger,
 * this class creates an attack with daggers dmg type, damage and so on.
 * Also, this class is used by other entities, not only player.
 * 
 * @author Ernyz
 */
public class AttackCreator {
	
	public AttackCreator() {
		
	}
	
	public static Attack primaryAttack(MoveableEntity attacker) {
		Attack a;
		if(attacker.currentWeapon.getBool("IsMelee")) {
			if(attacker.currentWeapon.getName().equals("Dagger")) {
				a = new MeleeAttack(attacker, "Stab");
				return a;
			}
		}
		return null;  //TODO I probably shouldn't return null...
	}
	
}
