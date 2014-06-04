package com.ernyz.dotw.Windows;

import java.util.HashMap;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Payload;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Source;
import com.badlogic.gdx.scenes.scene2d.utils.DragAndDrop.Target;
import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Model.Resources;
import com.ernyz.dotw.Model.Items.ItemManager;
import com.ernyz.dotw.Windows.Scene2dUserObects.ItemUserObject;

public class InventoryAndEquipmentWindow extends CustomWindow {

	private GameWorld gameWorld;
	private int gridWidth = 8;  //TODO: Make these constants, because these denote max number of items in player inv
	private int gridHeight = 5;  //TODO: Make these constants, because these denote max number of items in player inv
	private Skin skin;
	private Skin inventorySkin;
	private TextureAtlas atlas;
	
	private Table inventoryTable;
	private Table equippedItemsTable;
	
	private Array<Actor> inventorySlots;
	private HashMap<String, Actor> equippedItemSlots;
	
	private Image headSlotImage;
	private Image neckSlotImage;
	private Image shoulderSlotImage;
	private Image chestSlotImage;
	private Image leftHandSlotImage;
	private Image rightHandSlotImage;
	//private Image leftHandRingSlotImage;
	//private Image rightHandRingSlotImage;
	private Image glovesSlotImage;
	private Image beltSlotImage;
	private Image legsSlotImage;
	private Image feetSlotImage;
	
	private DragAndDrop dragAndDrop = new DragAndDrop();
	
	public InventoryAndEquipmentWindow(String title, Skin skin, GameWorld gameWorld) {
		super(title, skin);
		this.skin = skin;
		this.gameWorld = gameWorld;
		
		atlas = new TextureAtlas("data/GUI/invIcons/invIconsPacked/invIcons.atlas");
		inventorySkin = new Skin(atlas);
		
		headSlotImage = new Image(inventorySkin.getDrawable("head"));
		neckSlotImage = new Image(inventorySkin.getDrawable("neck"));
		shoulderSlotImage = new Image(inventorySkin.getDrawable("shoulder"));
		chestSlotImage = new Image(inventorySkin.getDrawable("chest"));
		leftHandSlotImage = new Image(inventorySkin.getDrawable("leftHand"));
		rightHandSlotImage = new Image(inventorySkin.getDrawable("rightHand"));
		//leftHandRingSlotImage = new Image(inventorySkin.getDrawable("---"));
		//rightHandRingSlotImage = new Image(inventorySkin.getDrawable("---"));
		glovesSlotImage = new Image(inventorySkin.getDrawable("gloves"));
		beltSlotImage = new Image(inventorySkin.getDrawable("belt"));
		legsSlotImage = new Image(inventorySkin.getDrawable("legs"));
		feetSlotImage = new Image(inventorySkin.getDrawable("feet"));
		
		inventorySlots = new Array<Actor>();
		equippedItemSlots = new HashMap<String, Actor>();
		
		setUpTheWindow();
	}

	@Override
	public void update(float delta) {
		setUpTheWindow();
	}

	@Override
	protected void setUpTheWindow() {
		setMovable(false);
		clearChildren();
		inventorySlots.clear();
		equippedItemSlots.clear();
		
		setUpInventoryGrid();
		setUpEquippedItemsGrid();
		
		pack();
		
		fillInventorySlots();
		fillEquippedItemsSlots();
	}
	
	private void fillInventorySlots() {
		//Loop through player inventory and put those items
		Array<Integer> inventory = gameWorld.getPlayer().getInventory();
		for(int i = 0; i < inventory.size; i++) {
			Texture t = gameWorld.getItems().get(inventory.get(i)).getTexture();
			final Image img = new Image(t);
			//Set up custom userObject to hold data related for this actor
			img.setUserObject(new ItemUserObject(inventory.get(i)));
			img.setX(inventorySlots.get(i).getX());
			img.setY(inventorySlots.get(i).getY());
			inventoryTable.addActor(img);
			img.addListener(new InputListener() {
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			        onItemIconClick(event.getListenerActor());
			        return true;
			    }
			});
			
			dragAndDrop.addSource(new Source(img) {
				@Override
				public Payload dragStart(InputEvent event, float x, float y, int pointer) {
					Payload payload = new Payload();
					payload.setDragActor(img);
					/*payload.setValidDragActor(someValidActor);
					payload.setInvalidDragActor(someInvalidActor);*/
					return payload;
				}
			});
			if(gameWorld.getItems().get(inventory.get(i)).getTargetBodyPart().equals(Resources.BODY_HANDS)) {
				dragAndDrop.addTarget(createTarget(Resources.BODY_LEFT_HAND));
				dragAndDrop.addTarget(createTarget(Resources.BODY_RIGHT_HAND));
			} else {
				dragAndDrop.addTarget(createTarget(gameWorld.getItems().get(inventory.get(i)).getTargetBodyPart()));
			}
			
		}
	}
	
	private void fillEquippedItemsSlots() {
		//Loop through equipped items and put those items into appropriate slots
		Set<String> equipment = gameWorld.getPlayer().getEquipmentSlots().keySet();
		for(String equipmentSlot : equipment) {
			Integer itemId = gameWorld.getPlayer().getEquipmentSlots().get(equipmentSlot);
			//If there's something in the equipment slot, render it
			if(itemId != -1) {
				//Texture t = gameWorld.getItems().get(itemId).getTexture();  //FIXME: this is a bug, get shouldnt be used
				Texture t = gameWorld.getItemById(itemId).getTexture();
				final Image img = new Image(t);
				//Set up custom userObject to hold data related for this actor
				img.setUserObject(new ItemUserObject(itemId));
				img.setX(equippedItemSlots.get(equipmentSlot).getX());
				img.setY(equippedItemSlots.get(equipmentSlot).getY());
				equippedItemsTable.addActor(img);
				img.addListener(new InputListener() {
					public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				        onItemIconClick(event.getListenerActor());
				        return true;
				    }
				});
				
				dragAndDrop.addSource(new Source(img) {
					@Override
					public Payload dragStart(InputEvent event, float x, float y, int pointer) {
						Payload payload = new Payload();
						payload.setDragActor(img);
						/*payload.setValidDragActor(someValidActor);
						payload.setInvalidDragActor(someInvalidActor);*/
						return payload;
					}
				});
				String target = gameWorld.getItemById(itemId).getTargetBodyPart();
				if(target.equals(Resources.BODY_HANDS)) {
					dragAndDrop.addTarget(createTarget(Resources.BODY_LEFT_HAND));
					dragAndDrop.addTarget(createTarget(Resources.BODY_RIGHT_HAND));
				} else {
					dragAndDrop.addTarget(createTarget(target));
				}
				dragAndDrop.addTarget(new Target(inventoryTable) {
					@Override
					public boolean drag(Source source, Payload payload, float x, float y, int pointer) {
						return true;
					}
					@Override
					public void drop(Source source, Payload payload, float x, float y, int pointer) {
						ItemUserObject userObj = (ItemUserObject) payload.getDragActor().getUserObject();
						ItemManager.equipOrUnequipItem(gameWorld.getPlayer(), gameWorld.getItems(), userObj.getItemId());
					}
				});
				
			}
		}
	}
	
	private void onItemIconClick(Actor actor) {
		ItemUserObject userObject = (ItemUserObject) actor.getUserObject();
		
		if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
			ItemManager.dropItem(gameWorld.getPlayer(), gameWorld.getItems(), userObject.getItemId());
		} else if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
			ItemManager.equipOrUnequipItem(gameWorld.getPlayer(), gameWorld.getItems(), userObject.getItemId());
		}
	}
	
	private void setUpInventoryGrid() {
		inventoryTable = new Table(inventorySkin);
		Actor a;
		for(int y = 0; y < gridHeight; y++) {
			inventoryTable.row().colspan(gridWidth);
			for(int x = 0; x < gridWidth; x++) {
				a = new Image(inventorySkin.getDrawable("inventorySlot"));
				inventorySlots.add(a);
				inventoryTable.add(a);
			}
		}
		add(inventoryTable);
	}
	
	private void setUpEquippedItemsGrid() {
		equippedItemsTable = new Table(inventorySkin);
		Actor a;
		
		Set<String> equipment = gameWorld.getPlayer().getEquipmentSlots().keySet();
		for(String equipmentSlot : equipment) {
			if(equipmentSlot.equals(Resources.BODY_HEAD)) {
				a = headSlotImage;
				equippedItemSlots.put(Resources.BODY_HEAD, a);
				equippedItemsTable.add(a);
			} else if(equipmentSlot.equals(Resources.BODY_NECK)) {
				a = neckSlotImage;
				equippedItemSlots.put(Resources.BODY_NECK, a);
				equippedItemsTable.add(a);
			} else if(equipmentSlot.equals(Resources.BODY_SHOULDERS)) {
				a = shoulderSlotImage;
				equippedItemSlots.put(Resources.BODY_SHOULDERS, a);
				equippedItemsTable.add(a);
			} else if(equipmentSlot.equals(Resources.BODY_CHEST)) {
				a = chestSlotImage;
				equippedItemSlots.put(Resources.BODY_CHEST, a);
				equippedItemsTable.add(a);
			} else if(equipmentSlot.equals(Resources.BODY_LEFT_HAND)) {
				a = leftHandSlotImage;
				equippedItemSlots.put(Resources.BODY_LEFT_HAND, a);
				equippedItemsTable.add(a);
			} else if(equipmentSlot.equals(Resources.BODY_RIGHT_HAND)) {
				a = rightHandSlotImage;
				equippedItemSlots.put(Resources.BODY_RIGHT_HAND, a);
				equippedItemsTable.add(a);
			} else if(equipmentSlot.equals(Resources.BODY_PALMS)) {
				a = glovesSlotImage;
				equippedItemSlots.put(Resources.BODY_PALMS, a);
				equippedItemsTable.add(a);
			} else if(equipmentSlot.equals(Resources.BODY_WAIST)) {
				a = beltSlotImage;
				equippedItemSlots.put(Resources.BODY_WAIST, a);
				equippedItemsTable.add(a);
			} else if(equipmentSlot.equals(Resources.BODY_LEGS)) {
				a = legsSlotImage;
				equippedItemSlots.put(Resources.BODY_LEGS, a);
				equippedItemsTable.add(a);
			} else if(equipmentSlot.equals(Resources.BODY_FEET)) {
				a = feetSlotImage;
				equippedItemSlots.put(Resources.BODY_FEET, a);
				equippedItemsTable.add(a);
			}
		}
		
		add(equippedItemsTable);
	}
	
	private Target createTarget(final String targetSlot) {
		return new Target(equippedItemSlots.get(targetSlot)) {
			@Override
			public boolean drag(Source source, Payload payload, float x, float y, int pointer) {
				return true;
			}
			@Override
			public void drop(Source source, Payload payload, float x, float y, int pointer) {
				ItemUserObject userObj = (ItemUserObject) payload.getDragActor().getUserObject();
				//ItemManager.equipOrUnequipItem(gameWorld.getPlayer(), gameWorld.getItems(), userObj.getItemId());
				ItemManager.equipItem(gameWorld.getPlayer(), userObj.getItemId(), targetSlot);
			}
		};
	}

}
