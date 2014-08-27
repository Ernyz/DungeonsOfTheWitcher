package com.ernyz.dotw.Model.Items;

import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Model.MoveableEntity;
import com.ernyz.dotw.Model.Resources;
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
		//TODO: Implement inventory sorting.
		return null;
	}
	
	/**
	 * Equips entity with item from its' inventory.
	 * @param e -  {@link MoveableEntity} on which we want to equip the item.
	 * @param itemId -  {@link Item} id of item we want to equip.
	 * @return Message whether equipping was successful or not. If not - message contains reason.
	 */
	public static String equipOrUnequipItem(MoveableEntity e, Array<Item> items, Integer itemId) {
		//TODO By default, we put weapon in right hand slot. More functionality will be needed later.
		//Check if item is equipped, if yes - unequip it, otherwise equip it
		//TODO: calculate destination slot, in order to know where item will be placed
		if(e.getEquipmentSlots().containsValue(itemId)) {
			return unequipItem(e, itemId);
		} else if(e.getEquipmentSlots().get(Resources.BODY_RIGHT_HAND) != -1) {
			//Unequip current item and equip new one
			unequipItem(e, e.getEquipmentSlots().get(Resources.BODY_RIGHT_HAND));
			equipItem(e, itemId, items, Resources.BODY_RIGHT_HAND);
		} else {
			if(e.getInventory().contains(itemId, true)) {
				//Item i = items.get(itemId);
				Item i = getItemById(itemId, items);
				if(i.getType() == ItemType.WEAPON) {
					return equipItem(e, itemId, items, Resources.BODY_RIGHT_HAND);
				}
			}
		}
		return "Equipping unsuccessful.";
	}
	
	/**
	 * Equips entity with item from its' inventory to the specified slot.
	 * @param e -  {@link MoveableEntity} on which we want to equip the item.
	 * @param item -  {@link Item} id we want to equip.
	 * @return Message whether equipping was successful or not. If not - message contains reason.
	 */
	public static String equipItem(MoveableEntity e, Integer item, Array<Item> items, String slotName) {  //TODO: Make private?
		if(item == -1) return null;
		
		if(e.getEquipmentSlots().containsValue(item)) {
			String key = "";
			for(String str : e.getEquipmentSlots().keySet()) {
				if(e.getEquipmentSlots().get(str) == item) {
					key = str;
					break;
				}
			}
			unequipItem(e, item);
		}
		e.getEquipmentSlots().put(slotName, item);
		
		String itemName = getItemById(item, items).getName();
		e.getSkeleton().setAttachment(slotName, itemName);
		
		if(e.getInventory().contains(item, true)) {
			e.getInventory().removeIndex(e.getInventory().indexOf(item, true));
		}
		GameWorld.addMessage("Item equipped");
		return "Equipping successful.";
	}
	
	/**
	 * Unequip item specified from the entity.
	 * @param e -  {@link MoveableEntity} from which we want to unequip the item.
	 * @param item -  {@link Item} id we want to unequip.
	 * @return Message whether unequipping was successful or not. If not - message contains reason.
	 */
	private static String unequipItem(MoveableEntity e, Integer item) {
		if(e.getInventory().size+1 <= e.getBackpackCapacity()) {
			if(e.getEquipmentSlots().containsValue(item)) {
				for(String slotName : e.getEquipmentSlots().keySet()) {
					if(getEquippedItem(e, slotName).equals(item)) {
						e.getEquipmentSlots().put(slotName, -1);
						e.getInventory().add(item);
						
						//TODO: Testing
						e.getSkeleton().setAttachment(slotName, "unarmed");
						
						GameWorld.addMessage("Item unequipped");
						return "Item unequipped";
					}
				}
			}
		}
		GameWorld.addMessage("Unequipping unsuccessful.");
		return "Unequipping unsuccessful.";
	}
	
	/**
	 * Gets equipped item in specified equipment slot.
	 * @param e -  {@link MoveableEntity} from which we want to get the item.
	 * @param slotName - name of the slot in which the item that we want to get is.
	 * @return -1 if slot is empty or item id if slot has item.
	 */
	public static Integer getEquippedItem(MoveableEntity e, String slotName) {
		return e.getEquipmentSlots().get(slotName).intValue();
	}
	
	private static Item getItemById(int itemId, Array<Item> items) {
		Item item = null;
		for(Item i : items) {
			if(i.getId() == itemId) {
				item = i;
				break;
			}
		}
		return item;
	}
	
	/**
	 * Gets item from the ground
	 * @param items - Array of {@link Item}s which are in the same dungeon level as the entity.
	 * @param e - {@link MoveableEntity} which wants to take the item.
	 * @return - Message indicating whether taking item was a success.
	 */
	public static String takeItem(Array<Item> items, MoveableEntity e) {
		//for(Item i : items) {
		Item i = null;
		for(int j = 0; j < items.size; j++) {
			i = items.get(j);
			if(!i.getIsInInventory()) {
				float dx = i.getX()+i.getIconTexture().getWidth()/2 - e.getPosition().x;  //XXX: icon texture used
				float dy = i.getY()+i.getIconTexture().getHeight()/2 - e.getPosition().y;
				if(Math.sqrt(dx*dx + dy*dy) <= e.getRadius()*1) {
					if(e.canTakeItem(i)) {
						i.setIsInInventory(true);
						e.getInventory().add(i.getId());
					}
				}
			}
		}
		return "Taken.";
	}
	
	/**
	 * Drops item specified from the inventory.
	 * @param e - {@link MoveableEntity} from whose inventory we want to drop the item.
	 * @param item - id of an item we want to drop.
	 * @return Message indicating success/failure of dropping the item.
	 */
	public static String dropItem(MoveableEntity e, Array<Item> items, Integer item) {
		//if(e.getInventory().contains(item, true)) {
		//Item i = items.get(item);
		Item i = getItemById(item, items);
		//Unequip before dropping.
		if(e.getEquipmentSlots().containsValue(item)) {
			if(e.getInventory().size < e.getBackpackCapacity()) {
				unequipItem(e, item);
			} else {
				GameWorld.addMessage("Cannot unequip the item!");
				return "Item was not dropped.";
			}
		}
		i.setX(e.getPosition().x-i.getIconTexture().getWidth()/2);  //FIXME: Wrong drop coordinates.
		i.setY(e.getPosition().y-i.getIconTexture().getHeight()/2);  //XXX: Icon texture is used
		i.setIsInInventory(false);
		e.getInventory().removeIndex(e.getInventory().indexOf(item, true));
		GameWorld.addMessage("Item dropped");
		return "Dropped.";
		//}
		//return "Item was not dropped.";
	}
	
}
