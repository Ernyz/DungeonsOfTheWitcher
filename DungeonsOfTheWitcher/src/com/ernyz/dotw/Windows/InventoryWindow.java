package com.ernyz.dotw.Windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Model.Items.ItemManager;
import com.ernyz.dotw.Windows.Scene2dUserObects.ItemUserObject;

/**
 * Inventory window class.
 * 
 * @author Ernyz
 */
public class InventoryWindow extends CustomWindow {
	
	private GameWorld gameWorld;

	public InventoryWindow(String title, Skin skin, GameWorld gameWorld) {
		super(title, skin);
		
		this.gameWorld = gameWorld;
		setUpTheWindow();
	}

	@Override
	protected void setUpTheWindow() {
		this.setMovable(false);
		
		//Populate window
		Array<Integer> inventory = gameWorld.getPlayer().getInventory();
		for(Integer item : inventory) {
			this.row().fill();
			Image img = new Image(GameWorld.items.get(item).getTexture());
			ImageButton itemActor = new ImageButton(img.getDrawable());
			
			//Set up custom userObject to hold data related for this button.
			itemActor.setUserObject(createItemUserObject(item));
			
			itemActor.addListener(new InputListener() {
				public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
			        onItemIconClick(event.getListenerActor());
			        return true;
			    }
			});
			this.add(itemActor);
		}
	}
	
	private void onItemIconClick(Actor actor) {
		ItemUserObject userObject = (ItemUserObject) actor.getUserObject();
		
		if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
			ItemManager.dropItem(gameWorld.getPlayer(), userObject.getItemId());
		} else if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
			ItemManager.equipItem(gameWorld.getPlayer(), userObject.getItemId());
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
		this.setUpTheWindow();
	}
	
}
