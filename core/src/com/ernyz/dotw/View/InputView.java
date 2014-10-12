package com.ernyz.dotw.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ernyz.dotw.Controller.Controller;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Model.Player;
import com.ernyz.dotw.Model.Items.ItemManager;

/**
 * Accepts input and directly modifies Model.
 * 
 * @author Ernyz
 */
public class InputView implements InputProcessor {
	private GameWorld gameWorld;
	private Controller controller;
	private WorldRenderer worldRenderer;
	private Vector3 touch = new Vector3();
	private Vector2 vec2Touch = new Vector2();
	private Player player;

	public InputView(GameWorld gameWorld, WorldRenderer worldRenderer, Controller controller) {
		this.gameWorld = gameWorld;
		this.controller = controller;
		this.worldRenderer = worldRenderer;
	}

	@Override
	public boolean keyDown(int keycode) {
		player = gameWorld.getPlayer();
		if(player.canMove()) {
			if(keycode == Keys.W) {
				controller.keyPressed(keycode);
			}
			if(keycode == Keys.A) {
				controller.keyPressed(keycode);
			}
			if(keycode == Keys.S) {
				controller.keyPressed(keycode);
			}
			if(keycode == Keys.D) {
				controller.keyPressed(keycode);
			}
		}
		if(keycode == Keys.I) {
			gameWorld.windowManager.toggleWindow("InventoryAndEquipment");
		} else if(keycode == Keys.E) {
			gameWorld.windowManager.toggleWindow("InventoryAndEquipment");
		} else if(keycode == Keys.X) {
			gameWorld.windowManager.toggleWindow("InventoryAndEquipment");
		} else if(keycode == Keys.P) {
			SaveGame.save(gameWorld.getTiles(), gameWorld.getPlayer(), gameWorld.getEntities(), gameWorld.getItems());
		} else if(keycode == Keys.G) {
			ItemManager.takeItem(gameWorld.getItems(), player);
		} else if(keycode == Keys.SPACE) {
			//controller.keySpace = true;
		} else if(keycode == Keys.ESCAPE) {
			//Close all windows. If all windows are closed, then show the escape menu.
			if(!gameWorld.windowManager.hideAllActiveWindows()) {
				gameWorld.windowManager.toggleWindow("InGameOptions");
			}
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		player = gameWorld.getPlayer();
		if(player.canMove()) {
			if(keycode == Keys.W) {
				controller.keyReleased(keycode);
			}
			if(keycode == Keys.A) {
				controller.keyReleased(keycode);
			}
			if(keycode == Keys.S) {
				controller.keyReleased(keycode);
			}
			if(keycode == Keys.D) {
				controller.keyReleased(keycode);
			}
		}
		if(keycode == Keys.SPACE) {
			//controller.keySpace = false;
		}
		
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		player.handleMouseClick(button);
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		mouseMoved(screenX, screenY);  //Player must be rotated even if mouse is clicked
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		player = gameWorld.getPlayer();
		touch.set(screenX, screenY, 0);
		worldRenderer.getCamera().unproject(touch);
		vec2Touch.set(touch.x, touch.y);
		player.setTargetRotation(new Vector2(vec2Touch.sub(player.getPosition()).nor()).angle());
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		float scrollAmount = 0.05f;
		if(Gdx.input.isKeyPressed(Keys.CONTROL_LEFT)) {
			if(amount > 0) {
				worldRenderer.setViewportMultiplier(worldRenderer.getViewportMultiplier() + scrollAmount);
			} else if(amount < 0) {
				worldRenderer.setViewportMultiplier(worldRenderer.getViewportMultiplier() - scrollAmount);
			}
		}
		return false;
	}

}
