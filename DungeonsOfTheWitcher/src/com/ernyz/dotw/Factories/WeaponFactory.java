package com.ernyz.dotw.Factories;

import com.badlogic.gdx.graphics.Texture;
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
	public Item createDagger(Integer id, Float x, Float y, Boolean isInInv) {
		Item i = new Item();
		i.setId(id);
		i.setName("Dagger");
		i.setType(ItemType.WEAPON);
		i.setIsInInventory(isInInv);
		i.setX(x);
		i.setY(y);
		i.set("PrimaryAttack", "Stab");
		i.set("SecondaryAttack", "Cut");
		i.set("Speed", 170);
		i.set("AttackInterval", .8f);  //Interval between attacks
		i.set("TimeUntilAttack", 0f);
		i.set("IsWeapon", true);
		i.set("IsMelee", true);
		i.set("Damage", 8);
		i.set("Range", 20);
		
		i.setTexture(new Texture("data/dagger.png"));
		
		return i;
	}
	
}
