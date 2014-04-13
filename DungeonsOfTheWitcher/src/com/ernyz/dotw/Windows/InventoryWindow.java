package com.ernyz.dotw.Windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Screens.CharacterSelectionScreen;

/**
 * Inventory window class.
 * 
 * @author Ernyz
 */
public class InventoryWindow extends Window {
	
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
	
}
