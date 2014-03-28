package com.ernyz.dotw.Factories;

import com.ernyz.dotw.Model.Items.Item;
import com.ernyz.dotw.Model.Items.Item.ItemType;

/**
 * This class contains all the methods which are needed
 * to create weapons of various types.
 * 
 * @author Ernyz
 */
public class WeaponFactory {
	
	public WeaponFactory() {
		
	}
	
	/**
	 * Creates a standard dagger.
	 * 
	 * @return {@link Item}
	 */
	public Item createDagger(long id) {
		Item i = new Item();
		i.setId(id);
		i.setName("Dagger");
		i.setType(ItemType.WEAPON);
		i.set("PrimaryAttack", "Stab");
		i.set("SecondaryAttack", "Cut");
		i.set("Speed", 170);
		i.set("AttackInterval", .8f);  //Interval between attacks
		i.set("TimeUntilAttack", 0f);
		i.set("IsWeapon", true);
		i.set("IsMelee", true);
		i.set("Damage", 4);
		i.set("Range", 20);
		
		return i;
	}
	
}
