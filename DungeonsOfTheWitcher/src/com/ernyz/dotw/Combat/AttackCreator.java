package com.ernyz.dotw.Combat;

import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Model.MoveableEntity;
import com.ernyz.dotw.Model.Items.Item;

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
		Item rightHand = null;
		Item leftHand = null;
		if(attacker.getEquipedItem("RightHand") != -1)
			rightHand = GameWorld.items.get((int)attacker.getEquipedItem("RightHand"));  //Item held in right hand
		if(attacker.getEquipedItem("LeftHand") != -1)
			leftHand = GameWorld.items.get((int)attacker.getEquipedItem("LeftHand"));  //Item held in left hand
		
		//Check if attacker has no weapon and is going to use unarmed combat
		if(rightHand == null && leftHand == null) {
			//TODO Implement unarmed combat
			return null;
		}
		
		//Check if attacker is armed
		if(rightHand != null && rightHand.getBool("IsMelee")) {  //If right hand is equipped with melee weapon
			if(rightHand.getName().equals("Dagger")) {
				a = new MeleeAttack(attacker, "Stab");
				return a;
			}
		}
		else if(leftHand != null && leftHand.getBool("IsMelee")) {  //If left hand is equipped with melee weapon
			if(leftHand.getName().equals("Dagger")) {
				a = new MeleeAttack(attacker, "Stab");
				return a;
			}
		}
		return null;
	}
	
}
