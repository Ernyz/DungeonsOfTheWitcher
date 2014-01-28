package com.ernyz.dotw.Combat;

import com.ernyz.dotw.Model.GameWorld;
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
	
	//TODO for now, this function assumes, that weapon is in right hand
	public static Attack primaryAttack(MoveableEntity attacker) {
		Attack a;
		//Check if attacker has no weapon and is going to use unarmed combat
		if(attacker.getEquipedItem("RightHand") == -1 && attacker.getEquipedItem("LeftHand") == -1) {
			//TODO Implement unarmed combat
		}
		//Check if attacker is armed
		else if(GameWorld.items.get((int)attacker.getEquipedItem("RightHand")).getBool("IsMelee")) {
			if(GameWorld.items.get((int)attacker.getEquipedItem("RightHand")).getName().equals("Dagger")) {
				a = new MeleeAttack(attacker, "Stab");
				return a;
			}
		}
		return null;  //TODO I probably shouldn't return null...
	}
	
}
