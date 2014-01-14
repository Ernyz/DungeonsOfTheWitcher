package com.ernyz.dotw.Generators;

import com.ernyz.dotw.Model.Items.Item;

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
		i.set("IsWeapon", true);
		i.set("Damage", 20);
		i.set("Range", 20);
		
		return i;
	}
	
}
