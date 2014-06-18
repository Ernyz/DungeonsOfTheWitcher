package com.ernyz.dotw.Windows.Scene2dUserObects;

/**
 * This object holds data needed for inventory/equipment slots representing game items.
 * 
 * @author Ernyz
 */
public class ItemUserObject {
	
	/**
	 * Id of an item the slot represents.
	 */
	private int itemId;
	
	public ItemUserObject() {
		
	}
	
	public ItemUserObject(Integer itemId) {
		this.itemId = itemId;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}
	
}
