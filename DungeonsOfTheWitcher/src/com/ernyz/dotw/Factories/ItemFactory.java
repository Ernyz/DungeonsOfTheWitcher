package com.ernyz.dotw.Factories;

import com.ernyz.dotw.Model.Items.Item;

/**
 * All items are created through this class.
 * 
 * @author Ernyz
 */
public class ItemFactory {
	
	public static long id = -1;  //When item is created, id is incremented and assigned to that item
	
	//Factories
	private WeaponFactory weaponFactory;
	
	public ItemFactory(long id) {
		//TODO Because id isn't stored anywhere yet, it's initialised when game starts
		this.id = id;
		//Initialise factories
		weaponFactory = new WeaponFactory();
	}
	
	/**
	 * TODO: Think over this constructor. Maybe i should send itemFactoryState everytime when constructing ItemFactory.
	 */
	public ItemFactory() {
		//Initialise factories
		weaponFactory = new WeaponFactory();
	}
	
	/**
	 * Creates weapon with unique id.
	 * TODO: Add better documentation.
	 * @param weapon - name of the desired weapon
	 * @return - weapon of type {@link Item}
	 */
	public Item createWeapon(String weapon, Float x, Float y, Boolean isInInv) {
		
		id++;
		/*if(weapon.equals("Dagger")) {
			return weaponFactory.createDagger(id, x, y, isInInv);
		} else {
			id--;  //If no weapon was created, id should not be incremented
		}
		return null;*/
		return createWeapon(id, weapon, x, y, isInInv);
		
	}
	
	/**
	 * Creates weapon with given id. Useful for generating loaded items from save file.
	 * TODO: Add better documentation.
	 * @param id
	 * @param weapon
	 * @param x
	 * @param y
	 * @param isInInv
	 * @return
	 */
	public Item createWeapon(long id, String weapon, Float x, Float y, Boolean isInInv) {
		if(weapon.equals("Dagger")) {
			return weaponFactory.createDagger(id, x, y, isInInv);
		}
		//Throw exception or something if weapon wasn't created
		return null;
	}
	
}
