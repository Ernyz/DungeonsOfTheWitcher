package com.ernyz.dotw.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.View.HeadsUpDisplay;

/**
 * Class dedicated to managing scene2d windows, for example, creating them, destroying and so on.
 * 
 * @author Ernyz
 */
public class WindowManager {
	
	private GameWorld gameWorld;
	
	private Skin skin;
	
	public WindowManager(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
		
		skin = new Skin(Gdx.files.internal("data/windows/basic/uiskin.json"));
	}
	
	/**
	 * Creates inventory window and adds it to {@link HeadsUpDisplay} stage.
	 */
	public void createInventoryWindow() {
		Window w = new Window("Your inventory", skin);
		w.setPosition(200, 200);
		
		addToScene2dStage(w);
		addToGameWorld(w);
	}
	
	private void addToScene2dStage(Window w) {
		gameWorld.getHUD().getStage().addActor(w);
	}
	
	private void addToGameWorld(Window w) {
		gameWorld.getWindows().put("Inventory", w);
	}
	
}
