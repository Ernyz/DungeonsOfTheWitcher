package com.ernyz.dotw.Model.Items;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

/**
 * Base class for all items, like weapons, scrolls, potions and etc.
 * 
 * @author ernyz
 */
public class Item {
	
	public static enum ItemType {
		WEAPON,
		PROJECTILE,
		ARMOUR,
		CONSUMABLE
	}
	
	private Texture texture;
	
	private Integer id;
	private String name;
	private ItemType type;
	private Boolean isInInventory;  //If true - this item is in someone's inventory, if false - it's laying on the ground.
	private Float x, y;  //Only used, if isInInventory == false
	private Boolean isCursed;
	private Boolean isStackable;
	private Boolean isContainer;
	private Array<Integer> itemsContained;
	private Float weight;
	/*
	 * Attributes (non-general ones) of an item will be here, for example:
	 * various booleans, damage, effect upon consumption and so on...
	 * Key naming is upper camel case (IsWeapon, IsConsumable and etc.)
	 */
	private HashMap attributes;
	
	public Item() {
		attributes = new HashMap();
	}
	
	public void set(String key, String value) {
		attributes.put(key, value);
	}
	public String getString(String key) {
		return attributes.get(key).toString();
	}
	
	public void set(String key, boolean value) {
		attributes.put(key, (value ? 1 : 0));
	}
	public boolean getBool(String key) {
		if((Integer)attributes.get(key) == 0)
			return false;
		else if((Integer)attributes.get(key) == 1)
			return true;
		return false;
	}
	
	public void set(String key, int value) {
		attributes.put(key, value);
	}
	public int getInt(String key) {
		return (Integer)attributes.get(key);
	}
	
	public void set(String key, float value) {
		attributes.put(key, value);
	}
	public float getFloat(String key) {
		return (Float)attributes.get(key);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ItemType getType() {
		return type;
	}

	public void setType(ItemType type) {
		this.type = type;
	}

	public Boolean getIsInInventory() {
		return isInInventory;
	}

	public void setIsInInventory(Boolean isInInventory) {
		this.isInInventory = isInInventory;
	}

	public Float getX() {
		return x;
	}

	public void setX(Float x) {
		this.x = x;
	}

	public Float getY() {
		return y;
	}

	public void setY(Float y) {
		this.y = y;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
}
