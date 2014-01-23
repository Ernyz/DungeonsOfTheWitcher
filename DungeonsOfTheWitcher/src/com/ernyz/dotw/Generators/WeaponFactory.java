package com.ernyz.dotw.Generators;

import com.ernyz.dotw.Model.Items.Item;

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
	public Item createDagger() {
		Item i = new Item();
		i.setId(0L);
		i.setName("Dagger");
		i.set("PrimaryAttack", "Stab");
		i.set("SecondaryAttack", "Cut");
		i.set("IsWeapon", true);
		i.set("IsMelee", true);
		i.set("Damage", 20);
		i.set("Range", 20);
		
		return i;
	}
	
}
