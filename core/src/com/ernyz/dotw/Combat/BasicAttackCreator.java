package com.ernyz.dotw.Combat;

import com.ernyz.dotw.Combat.BasicAttack.StateEnum;
import com.ernyz.dotw.Combat.BasicAttack.TypeEnum;
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
		
		if(attacker.getEquipmentSlots().get(Resources.BODY_RIGHT_HAND) != -1)
			rightHandItem = gameWorld.getItemById(attacker.getEquipmentSlots().get(Resources.BODY_RIGHT_HAND));
		else
			rightHandItem = attacker.unarmedLimbs.get(Resources.BODY_RIGHT_HAND);
		
		if(attacker.getEquipmentSlots().get(Resources.BODY_LEFT_HAND) != -1)
			leftHandItem = gameWorld.getItemById(attacker.getEquipmentSlots().get(Resources.BODY_LEFT_HAND));
		else
			leftHandItem = attacker.unarmedLimbs.get(Resources.BODY_LEFT_HAND);
		
		
		//TODO: Determine which weapon will be used to attack (left or right hand weapon) (depends on primary==true/false)
		//Default is right hand. More functionality will be added later.
		if(primary) {
			//Determine the type of the attack (ranged, melee, etc.)
			if(rightHandItem.getBool("IsMelee")) {
				attack = new MeleeBasicAttack(attacker, rightHandItem, Resources.BODY_RIGHT_HAND);
				attack.setType(TypeEnum.MELEE);
				attack.setState(StateEnum.ATTACKING);
				rightHandItem.set("TimeUntilAttack", rightHandItem.getFloat("AttackInterval"));
			}
		} else {
			//Determine the type of the attack (ranged, melee, etc.)
			if(leftHandItem.getBool("IsMelee")) {
				attack = new MeleeBasicAttack(attacker, leftHandItem, Resources.BODY_LEFT_HAND);
				attack.setType(TypeEnum.MELEE);
				attack.setState(StateEnum.ATTACKING);
				leftHandItem.set("TimeUntilAttack", leftHandItem.getFloat("AttackInterval"));
			}
		}
		
		return attack;
	}
	
}
