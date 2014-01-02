package com.ernyz.dotw.View;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Model.Player;

/**
 * Accepts input and directly modifies Model.
 * 
 * @author ernyz
 */
public class InputView implements InputProcessor {
	
	private GameWorld gameWorld;
	private WorldRenderer worldRenderer;
	private Vector3 touch = new Vector3();
	private Vector2 vec2Touch = new Vector2();
	private Player player;

	public InputView(GameWorld gameWorld, WorldRenderer worldRenderer) {
		this.gameWorld = gameWorld;
		this.worldRenderer = worldRenderer;
	}

	@Override
	public boolean keyDown(int keycode) {
		player = gameWorld.getPlayer();
		switch(keycode) {
		case Keys.W:
			player.setVelocity(new Vector2(player.getVelocity().x, 1));
			gameWorld.addMessage("W pressed");
 			break;
		case Keys.A:
			player.setVelocity(new Vector2(-1, player.getVelocity().y));
			gameWorld.addMessage("A pressed");
			break;
		case Keys.S:
			player.setVelocity(new Vector2(player.getVelocity().x, -1));
			gameWorld.addMessage("S pressed");
			break;
		case Keys.D:
			player.setVelocity(new Vector2(1, player.getVelocity().y));
			gameWorld.addMessage("D pressed");
			break;
		case Keys.P:
			gameWorld.addMessage("P pressed");
			SaveGame save = new SaveGame(gameWorld);
			save.save();
			break;
		default:
			break;
		}
		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		player = gameWorld.getPlayer();
		switch(keycode) {
		case Keys.W:
			if(player.getVelocity().y == 1)
				player.setVelocity(new Vector2(player.getVelocity().x, 0));
			break;
		case Keys.A:
			if(player.getVelocity().x == -1)
				player.setVelocity(new Vector2(0, player.getVelocity().y));
			break;
		case Keys.S:
			if(player.getVelocity().y == -1)
				player.setVelocity(new Vector2(player.getVelocity().x, 0));
			break;
		case Keys.D:
			if(player.getVelocity().x == 1)
				player.setVelocity(new Vector2(0, player.getVelocity().y));
			break;
		default:
			break;
		}
		
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		mouseMoved(screenX, screenY);  //Player must be rotated even if left-mouse is clicked
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		player = gameWorld.getPlayer();
		touch.set(screenX, screenY, 0);
		worldRenderer.getCamera().unproject(touch);
		vec2Touch.set(touch.x-player.getWidth()/2, touch.y-player.getHeight()/2);		
		player.setRotation(new Vector2(vec2Touch.sub(player.getPosition()).nor()).angle());
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

}
