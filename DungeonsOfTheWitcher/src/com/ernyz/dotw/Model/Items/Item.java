package com.ernyz.dotw.Model.Items;

/**
 * Base class for all items, like weapons, scrolls, potions and etc.
 * 
 * @author ernyz
 */
public class Item {
	
	private Long id;
	private String name;
	
	public Item() {
		
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
