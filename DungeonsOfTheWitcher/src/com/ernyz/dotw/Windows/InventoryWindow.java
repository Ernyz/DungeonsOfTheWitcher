package com.ernyz.dotw.Windows;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Model.Items.ItemManager;

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
			//TODO: Clean/rework this.
			Image img = new Image(new Texture("data/dagger.png"));
			ImageButton itemActor = new ImageButton(img.getDrawable());
			itemActor.setName(gameWorld.getItems().get(item).getId().toString());
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
		if(Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
			ItemManager.dropItem(gameWorld.getPlayer(), Integer.valueOf(actor.getName()));
		}
	}
	
	@Override
	public void update(float delta) {
		//TODO: Later update this to update only objects that change. Some stuff will persist and will not be needed to update.
		this.clear();
		this.setUpTheWindow();
	}
	
}
