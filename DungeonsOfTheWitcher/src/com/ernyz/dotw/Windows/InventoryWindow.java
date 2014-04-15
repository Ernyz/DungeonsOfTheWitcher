package com.ernyz.dotw.Windows;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Model.GameWorld;

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

	private void setUpTheWindow() {
		this.setMovable(true);
		
		//Populate window
		Array<Integer> inventory = gameWorld.getPlayer().getInventory();
		for(Integer item : inventory) {
			this.row().fill();
			this.add(gameWorld.getItems().get(item).getName());
		}
	}
	
	@Override
	public void update(float delta) {
		//TODO: Later update this to update only objects that change. Some stuff will persist and will not be needed to update.
		this.clear();
		this.setUpTheWindow();
	}
	
}
