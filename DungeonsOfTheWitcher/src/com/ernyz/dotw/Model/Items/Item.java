package com.ernyz.dotw.Model.Items;

import java.util.HashMap;

/**
 * Base class for all items, like weapons, scrolls, potions and etc.
 * 
 * @author ernyz
 */
public class Item {
	
	private Long id;
	private String name;
	/*
	 * Attributes of an item will be here, for example:
	 * quantity, type, various booleans and so on...
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
