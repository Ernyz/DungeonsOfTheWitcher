package com.ernyz.dotw.Windows;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Model.Items.ItemManager;
import com.ernyz.dotw.Windows.Scene2dUserObects.ItemUserObject;

public class EquippedItemsWindow extends CustomWindow {
	
	private GameWorld gameWorld;

	public EquippedItemsWindow(String title, Skin skin, GameWorld gameWorld) {
		super(title, skin);
		
		this.gameWorld = gameWorld;
		
		setUpTheWindow();
	}

	@Override
	protected void setUpTheWindow() {
		this.setMovable(false);
		
		//Populate window
		HashMap<String, Integer> slots = gameWorld.getPlayer().getEquipmentSlots();
		for(String slotName : slots.keySet()) {
			if(slots.get(slotName) != -1) {
				this.row().fill();
				Image img = new Image(gameWorld.getItems().get(slots.get(slotName)).getTexture());
				ImageButton itemActor = new ImageButton(img.getDrawable());
				
				//Set up custom userObject to hold data related for this button.
				itemActor.setUserObject(createItemUserObject(slots.get(slotName)));
				
				itemActor.addListener(new InputListener() {
				    public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				        onItemIconClick(event.getListenerActor());
				        return true;
				    }
				});
				this.add(itemActor);
			}
		}
	}
	
	private void onItemIconClick(Actor actor) {
		ItemUserObject userObject = (ItemUserObject) actor.getUserObject();
		
		if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
			ItemManager.dropItem(gameWorld.getPlayer(), gameWorld.getItems(), userObject.getItemId());
		} else if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
			ItemManager.unequipItem(gameWorld.getPlayer(), userObject.getItemId());
		}
	}
	
	private ItemUserObject createItemUserObject(int id) {
		ItemUserObject userObject = new ItemUserObject();
		userObject.setItemId(id);
		return userObject;
	}
	
	@Override
	public void update(float delta) {
		//Clear the window
		this.clearChildren();
		//Set it up
		setUpTheWindow();
	}

}
