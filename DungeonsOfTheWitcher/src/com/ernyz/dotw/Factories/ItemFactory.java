package com.ernyz.dotw.Factories;

import com.ernyz.dotw.Model.Items.Item;

/**
 * All items are created through this class.
 * 
 * @author Ernyz
 */
public class ItemFactory {
	
	private static long id;  //When item is created, id is incremented and assigned to that item
	
	//Factories
	private WeaponFactory weaponFactory;
	
	public ItemFactory() {
		//TODO Because id isn't stored anywhere yet, it's initialised when game starts
		id = -1;
		//Initialise factories
		weaponFactory = new WeaponFactory();
	}
	
	/**
	 * Creates and returns desired weapon.
	 * @param weapon - name of the desired weapon
	 * @return - weapon of type {@link Item}
	 */
	public Item createWeapon(String weapon) {
		id++;
		if(weapon.equals("Dagger"))
			return weaponFactory.createDagger(id);
		else
			id--;  //If no weapon was created, id should not be incremented
		return null;
	}

}
