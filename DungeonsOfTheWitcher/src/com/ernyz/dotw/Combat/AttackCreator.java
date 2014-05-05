package com.ernyz.dotw.Combat;

import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Model.MoveableEntity;
import com.ernyz.dotw.Model.Items.Item;
import com.ernyz.dotw.Model.Items.ItemManager;

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
	
	/**
	 * Creates an attack and returns it if creation was successful.
	 * @param attacker - entity which calls this function.
	 * @return {@link Attack} if attack was created, or null if attack wasn't created.
	 */
	//TODO i dunno if getting items as param is good
	public static Attack primaryAttack(MoveableEntity attacker, Array<Item> items) {
		Attack a;
		Item rightHand = null;
		Item leftHand = null;
		if(ItemManager.getEquippedItem(attacker, "RightHand") != -1)
			rightHand = items.get(ItemManager.getEquippedItem(attacker, "RightHand"));  //Item held in right hand
		if(ItemManager.getEquippedItem(attacker, "LeftHand") != -1)
			leftHand = items.get(ItemManager.getEquippedItem(attacker, "LeftHand"));  //Item held in left hand
		
		//Check if attacker has no weapon and is going to use unarmed combat
		if(rightHand == null && leftHand == null) {
			//TODO Implement unarmed combat
			return null;
		}
		
		//Check if attacker is armed
		if(rightHand != null && rightHand.getBool("IsMelee")) {  //If right hand is equipped with melee weapon
			if(rightHand.getFloat("TimeUntilAttack") <= 0) {
				if(rightHand.getName().equals("Dagger")) {
					a = new MeleeAttack(attacker, rightHand.getString("PrimaryAttack"));
					rightHand.set("TimeUntilAttack", rightHand.getFloat("AttackInterval"));
					return a;
				}
			}
		}
		else if(leftHand != null && leftHand.getBool("IsMelee")) {  //If left hand is equipped with melee weapon
			if(rightHand.getFloat("TimeUntilAttack") <= 0) {
				if(leftHand.getName().equals("Dagger")) {
					a = new MeleeAttack(attacker, leftHand.getString("PrimaryAttack"));
					leftHand.set("TimeUntilAttack", leftHand.getFloat("AttackInterval"));
					leftHand.set("TimeUntilAttack", leftHand.getFloat("AttackInterval"));
					return a;
				}
			}
		}
		return null;
	}
	
}
