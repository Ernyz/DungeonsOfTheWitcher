package com.ernyz.dotw.Factories;

import com.ernyz.dotw.Model.Items.Item;

/**
 * All items are created through this class.
 * 
 * @author Ernyz
 */
public class ItemFactory {
	
	public static int id = -1;  //When item is created, id is incremented and assigned to that item
	
	//Factories
	private WeaponFactory weaponFactory;
	
	/**
	 * This constructor is used when loading a game and id has to be the same as it was when saving the game.
	 * @param id
	 */
	public ItemFactory(int id) {
		ItemFactory.id = id;
		//Initialise factories
		initialiseFactories();
	}
	
	/**
	 * This constructor is used, when creating new game with new items. Then id starts at -1 - its default value.
	 */
	public ItemFactory() {
		//Initialise factories
		initialiseFactories();
	}
	
	private void initialiseFactories() {
		weaponFactory = new WeaponFactory();
	}
	
	/**
	 * Creates weapon with unique id.
	 * @param weapon - name of the desired weapon
	 * @return - weapon of type {@link Item}
	 */
	public Item createWeapon(String weapon, Float x, Float y, Boolean isInInv) {
		id++;
		return createWeapon(id, weapon, x, y, isInInv);
		
	}
	
	/**
	 * Creates weapon with given id. Useful for generating loaded items from save file.
	 * @param id - an id which will be assigned to this weapon
	 * @param weapon - name of the weapon
	 * @param x - x coordinates in the game world
	 * @param y - y coordinates in the game world
	 * @param isInInv - true if this weapon is in someone's inventory
	 * @return created {@link Item}.
	 */
	public Item createWeapon(Integer id, String weapon, Float x, Float y, Boolean isInInv) {
		if(weapon.equals("Dagger")) {
			return weaponFactory.createDagger(id, x, y, isInInv);
		}
		//TODO Throw exception or something if weapon wasn't created
		return null;
	}
	
}
