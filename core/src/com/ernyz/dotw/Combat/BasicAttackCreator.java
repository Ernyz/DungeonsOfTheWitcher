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

	public static Attack createBasicAttack(MoveableEntity attacker, boolean primary, GameWorld gameWorld) {
		Attack attack = null;
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
		
		//TODO: Determine which weapon will be used to attack (left or right hand weapon) (depends on primary==true/false)
		//Default is right hand. More functionality will be added later.
		
		//Determine the type of the attack (ranged, melee, etc.)
		if(rightHandItem.getBool("IsMelee")) {
			attack = new MeleeBasicAttack(attacker, rightHandItem);
		}
		
		return attack;
	}
	
}
