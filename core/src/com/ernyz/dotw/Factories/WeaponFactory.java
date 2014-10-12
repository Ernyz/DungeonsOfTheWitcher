package com.ernyz.dotw.Factories;

import com.badlogic.gdx.graphics.Texture;
import com.ernyz.dotw.Model.Resources;
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
		i.setTargetBodyPart(Resources.BODY_HANDS);
		i.setIsInInventory(isInInv);
		i.setX(x);
		i.setY(y);
		i.setWeight(0.5f);
		i.set("PrimaryAttack", "Stab");
		i.set("SecondaryAttack", "Cut");
		i.set("Speed", 170f);  //170
		//i.set("AttackInterval", .5f);  //Interval between attacks
		//i.set("TimeUntilAttack", 0f);
		i.set("IsWeapon", true);
		i.set("IsMelee", true);
		i.set("CanAttack", true);
		i.set("Damage", 12f);//10f
		i.set("Range", 24f);
		
		i.setIconTexture(new Texture("data/items/icons/dagger.png"));
		i.setTexture(new Texture("data/items/Dagger.png"));
		
		return i;
	}
	
}
