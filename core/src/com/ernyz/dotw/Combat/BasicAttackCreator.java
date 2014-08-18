package com.ernyz.dotw.Combat;

import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Model.MoveableEntity;
import com.ernyz.dotw.Model.Resources;
import com.ernyz.dotw.Model.Items.Item;

/**
 * Static class responsible for creating basic attacks.
 * 
 * @author Ernyz
 */
public class BasicAttackCreator {

	public static BasicAttack createBasicAttack(MoveableEntity attacker, boolean primary, GameWorld gameWorld) {
		BasicAttack attack = null;
		//Determine weapon which attacker uses to attack
		Item rightHandItem = null;
		Item leftHandItem = null;
		if(attacker.getEquipmentSlots().get(Resources.BODY_RIGHT_HAND) != -1) {
			rightHandItem = gameWorld.getItemById(attacker.getEquipmentSlots().get(Resources.BODY_RIGHT_HAND));
		} else {
			//create 'fist' item
		}
		if(attacker.getEquipmentSlots().get(Resources.BODY_LEFT_HAND) != -1) {
			leftHandItem = gameWorld.getItemById(attacker.getEquipmentSlots().get(Resources.BODY_LEFT_HAND));
		} else {
			//create 'fist' item
		}
		
		//TODO: Temporary, until unarmed combat is implemented
		if(rightHandItem == null && leftHandItem == null) {
			return null;
		}
		
		//TODO: Determine which weapon will be used to attack (left or right hand weapon) (depends on primary==true/false)
		//Default is right hand. More functionality will be added later.
		if(primary) {
			if(rightHandItem == null) return null;//TODO: Temporary, until unarmed combat is implemented
			//Determine the type of the attack (ranged, melee, etc.)
			if(rightHandItem.getBool("IsMelee")) {
//				if(rightHandItem.getFloat("TimeUntilAttack") <= 0) {
				attack = new MeleeBasicAttack(attacker, rightHandItem, Resources.BODY_RIGHT_HAND);
				rightHandItem.set("TimeUntilAttack", rightHandItem.getFloat("AttackInterval"));
//				}
			}
		} else {
			if(leftHandItem == null) return null;//TODO: Temporary, until unarmed combat is implemented
			//Determine the type of the attack (ranged, melee, etc.)
			if(leftHandItem.getBool("IsMelee")) {
				if(leftHandItem.getFloat("TimeUntilAttack") <= 0) {
					attack = new MeleeBasicAttack(attacker, leftHandItem, Resources.BODY_LEFT_HAND);
					leftHandItem.set("TimeUntilAttack", leftHandItem.getFloat("AttackInterval"));
				}
			}
		}
		
		return attack;
	}
	
}
