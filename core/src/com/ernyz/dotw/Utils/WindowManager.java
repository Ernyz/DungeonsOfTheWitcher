package com.ernyz.dotw.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.View.HeadsUpDisplay;
import com.ernyz.dotw.Windows.CustomWindow;
import com.ernyz.dotw.Windows.InGameOptionsWindow;
import com.ernyz.dotw.Windows.InventoryAndEquipmentWindow;

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
		
		skin = new Skin(Gdx.files.internal("data/GUI/menu/packed/customuiskin.json"));
//		skin = new Skin(Gdx.files.internal("data/GUI/basic/uiskin.json"));
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
		if(windowName.equals("InventoryAndEquipment")) {
			createInventoryAndEquipmentWindow();
		} else if(windowName.equals("InGameOptions")) {
			createInGameOptionsWindow();
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
	
	/**
	 * Adds window specified to the game world window list and to scene2d stage at {@link HeadsUpDisplay}.
	 * @param w
	 */
	private void addWindow(CustomWindow w, String windowName) {
		addWindowToScene2dStage((Window) w);
		addWindowToGameWorld(w, windowName);
	}
	
	private void addWindowToScene2dStage(Window w) {
		gameWorld.getHUD().getStage().addActor(w);
	}
	
	private void addWindowToGameWorld(CustomWindow w, String windowName) {
		gameWorld.getWindows().put(windowName, w);
	}
	
	/**
	 * 
	 */
	private void createInventoryAndEquipmentWindow() {
		InventoryAndEquipmentWindow w = new InventoryAndEquipmentWindow("Inventory and equipped items", skin, gameWorld);
		w.setPosition(stageW/2-w.getWidth()/2, 0);
		
		addWindow(w, "InventoryAndEquipment");
	}
	
	/**
	 * Creates window which shows in game options, such as save game, settings and etc.
	 */
	private void createInGameOptionsWindow() {
		InGameOptionsWindow w = new InGameOptionsWindow("In-game options", skin, gameWorld);
		//w.setWidth(stageW/100*30);
		//w.setHeight(stageH/100*30);
		w.setPosition(stageW/2-w.getWidth()/2, stageH/2-w.getHeight()/2);
		
		addWindow(w, "InGameOptions");
	}
	
}
