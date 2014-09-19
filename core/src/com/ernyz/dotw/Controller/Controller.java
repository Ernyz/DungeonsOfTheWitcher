package com.ernyz.dotw.Controller;

import com.badlogic.gdx.math.Vector2;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Model.Player;

public class Controller {
	
	public boolean keyW = false;
	public boolean keyA = false;
	public boolean keyS = false;
	public boolean keyD = false;
	public boolean keyI = false;
	public boolean keyE = false;
	public boolean keyX = false;
	public boolean keyP = false;
	public boolean keyG = false;
	public boolean keySpace = false;
	public boolean keyEscape = false;

	private GameWorld gameWorld;
	private Player player;
	
	public Controller(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
		this.player = gameWorld.getPlayer();
	}
	
	public void update(float delta) {
		if(keyW) {
			player.getVelocity().y = 1;
		} else if(keyS) {
			player.getVelocity().y = -1;
		} else {
			player.getVelocity().y = 0;
		}
		if(keyA) {
			player.getVelocity().x = -1;
		} else if(keyD) {
			player.getVelocity().x = 1;
		} else {
			player.getVelocity().x = 0;
		}
		
		if(keySpace) {
			if(player.canBlock() && !player.isBlocking()) {
				player.setBlocking(true);
			}
		} else if(player.isBlocking()) {
			player.setBlocking(false);
		}
	}
	
}
