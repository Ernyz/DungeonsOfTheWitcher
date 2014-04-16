package com.ernyz.dotw.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.View.HeadsUpDisplay;
import com.ernyz.dotw.Windows.CustomWindow;
import com.ernyz.dotw.Windows.InventoryWindow;

/**
 * Class dedicated to managing scene2d windows, for example, creating them, destroying and so on.
 * 
 * @author Ernyz
 */
public class WindowManager {
	
	private GameWorld gameWorld;
	private float stageW;
	private float stageH;
	
	private Skin skin;
	
	public WindowManager(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
		stageW = Gdx.graphics.getWidth();
		stageH = Gdx.graphics.getHeight();
		
		skin = new Skin(Gdx.files.internal("data/windows/basic/uiskin.json"));
	}
	
	/**
	 * Toggles specified window on or off. If window is not yet created, this function then creates it.
	 * @param windowName - name of the window in windows' hash map.
	 */
	public void toggleWindow(String windowName) {
		
		if(!gameWorld.getWindows().containsKey(windowName)) {
			createWindow(windowName);
		}
		else if(!gameWorld.getWindows().get(windowName).isVisible()) {
			gameWorld.getWindows().get(windowName).setVisible(true);
		}
		else {
			gameWorld.getWindows().get(windowName).setVisible(false);
		}
		
	}
	
	private void createWindow(String windowName) {
		if(windowName.equals("Inventory")) {
			createInventoryWindow();
		}
	}
	
	/**
	 * Hide all active windows. If there weren't any windows to close, returns false.
	 * @return - true if at least one window was closed, false otherwise.
	 */
	public boolean hideAllActiveWindows() {
		boolean windowsClosed = false;
		for(String windowname : gameWorld.getWindows().keySet()) {
			if(gameWorld.getWindows().get(windowname).isVisible()) {
				gameWorld.getWindows().get(windowname).setVisible(false);
				windowsClosed = true;
			}
		}
		
		return windowsClosed;
	}
	
	private void addWindow(CustomWindow w) {
		addWindowToScene2dStage((Window) w);
		addWindowToGameWorld(w);
	}
	
	private void addWindowToScene2dStage(Window w) {
		gameWorld.getHUD().getStage().addActor(w);
	}
	
	private void addWindowToGameWorld(CustomWindow w) {
		gameWorld.getWindows().put("Inventory", w);
	}
	
	/**
	 * Creates inventory window and adds it to {@link HeadsUpDisplay} stage.
	 */
	private void createInventoryWindow() {
		InventoryWindow w = new InventoryWindow("Your inventory", skin, gameWorld);
		w.setPosition(stageH/100*3f, stageH/100*3f);
		w.setWidth(stageW/100*30);  //window width is equal to 25 percent of stage width
		w.setHeight(stageH/100*94);  //window height is equal to 25 percent of stage height
		
		addWindow(w);
	}
	
}
