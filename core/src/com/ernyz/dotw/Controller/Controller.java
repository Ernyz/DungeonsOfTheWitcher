package com.ernyz.dotw.Controller;

import com.badlogic.gdx.Input.Keys;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Model.Player;

public class Controller {
	
//	private boolean keyW = false;
//	private boolean keyA = false;
//	private boolean keyS = false;
//	private boolean keyD = false;
//	private boolean keyI = false;
//	private boolean keyE = false;
//	private boolean keyX = false;
//	private boolean keyP = false;
//	private boolean keyG = false;
//	public boolean keySpace = false;
//	private boolean keyEscape = false;
	
	//private GameWorld gameWorld;
	private Player player;
	
	public Controller(GameWorld gameWorld) {
		//this.gameWorld = gameWorld;
		this.player = gameWorld.getPlayer();
	}
	
	public void update(float delta) {
//		if(keySpace) {
//			if(player.canBlock() && !player.isBlocking()) {
//				player.setBlocking(true);
//			}
//		} else if(player.isBlocking()) {
//			player.setBlocking(false);
//		}
	}
	
	public void keyPressed(Integer keycode) {
		if(keycode == Keys.W) {
			player.velocityUp = 1;
		} else if(keycode == Keys.A) {
			player.velocityLeft = -1;
		} else if(keycode == Keys.S) {
			player.velocityDown = -1;
		} else if(keycode == Keys.D) {
			player.velocityRight = 1;
		}
	}
	
	public void keyReleased(int keycode) {
		if(keycode == Keys.W) {
			player.velocityUp = 0;
		} else if(keycode == Keys.A) {
			player.velocityLeft = 0;
		} else if(keycode == Keys.S) {
			player.velocityDown = 0;
		} else if(keycode == Keys.D) {
			player.velocityRight = 0;
		}
	}
	
}
