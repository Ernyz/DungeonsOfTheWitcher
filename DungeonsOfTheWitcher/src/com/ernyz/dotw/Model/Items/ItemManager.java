package com.ernyz.dotw.Model.Items;

import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Model.MoveableEntity;
import com.ernyz.dotw.Model.Items.Item.ItemType;

/**
 * Has the methods needed for inventory manipulations, equipping items and so on.
 * 
 * @author Ernyz
 */
public class ItemManager {

	/**
	 * Sorts inventory items in inventory.
	 * @param inputArray - inventory array of entity.
	 * @return modified inventory array.
	 */
	public static Array<Integer> sortInventory(Array<Integer> inputArray) {
		//TODO
		return null;
	}
	
	/**
	 * Equips entity with item from it's inventory.
	 * @param e -  @{MoveableEntity} on which we want to equip the item.
	 * @param item -  @{Item} id we want to equip.
	 * @return Message whether equipping was successful or not. If not - message contains reason.
	 */
	public static String equipItem(MoveableEntity e, Integer item) {
		//TODO By default, we put weapon in right hand slot. More functionality will be needed later.
		if(e.getInventory().contains(item, true)) {
			Item i = GameWorld.items.get(item);
			if(i.getType() == ItemType.WEAPON) {
				e.getEquipmentSlots().put("RightHand", item);
				return "Equipped.";
			}
		}
		return "Equipping unsuccessful.";
	}
	
	/**
	 * Unequip item specified from the entity.
	 * @param e -  @{MoveableEntity} from which we want to unequip the item.
	 * @param item -  @{Item} id we want to unequip.
	 * @return Message whether unequipping was successful or not. If not - message contains reason.
	 */
	public static String unequipItem(MoveableEntity e, Long item) {
		//TODO
		return "Unequipped.";
	}
	
	/**
	 * Gets equipped item in specified equipment slot.
	 * @param e -  @{MoveableEntity} from which we want to unequip the item.
	 * @param slotName - name of the slot in which the item that we want to get is.
	 * @return -1 if slot is empty and item id if slot has item.
	 */
	public static Integer getEquippedItem(MoveableEntity e, String slotName) {
		return e.getEquipmentSlots().get(slotName).intValue();
	}
	
	/**
	 * Get item from inventory.
	 */
	//TODO
	
	/**
	 * Gets item from the ground
	 * TODO: Add documentation.
	 */
	public static String takeItem(Array<Item> items, MoveableEntity entity) {
		for(Item i : items) {
			//TODO: This is temporary, remake this!
			float dx = i.getX() - entity.getPosition().x;
			float dy = i.getY() - entity.getPosition().y;
			if(Math.sqrt(dx*dx + dy*dy) <= 30) {
				i.setIsInInventory(true);
				entity.getInventory().add(i.getId().intValue());  //TODO this is retarded, fix asap
			}
		}
		return "Taken.";
	}
	
	/**
	 * TODO: Add documentation.
	 * TODO: Clean all this mess.
	 */
	public static String dropItem(MoveableEntity e, Integer item) {
		if(e.getInventory().contains(item, true)) {
			Item i = GameWorld.items.get(item);
			i.setX(e.getPosition().x);
			i.setY(e.getPosition().y);
			i.setIsInInventory(false);
			e.getInventory().removeIndex(e.getInventory().indexOf(item, true));
			GameWorld.addMessage("Item dropped");
			return "Dropped.";
		}
		return "Item was not dropped.";
	}
	
}
