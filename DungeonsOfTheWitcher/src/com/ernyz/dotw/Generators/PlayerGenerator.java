package com.ernyz.dotw.Generators;

import com.badlogic.gdx.math.Vector2;
import com.ernyz.dotw.Model.Player;
import com.ernyz.dotw.View.SaveGame;

/**
 * Generates new player and uses {@link SaveGame} to output it to a save file.
 * 
 * @author Ernyz
 */
public class PlayerGenerator {
	
	private Player player;
	
	public PlayerGenerator() {
		
	}
	
	//Later this method will generate player according to race, class and etc.
	public void generatePlayer(String name) {
		//Create player
		player = new Player(new Vector2(), new Vector2(), 0, 0, null);
		player.setName(name);
		player.setDungeonLevel(1);
		player.setPosition(new Vector2(105, 105));
		player.setSpeed(100f);
		player.setRotation(0);
		player.setHealth(100);
		
		SaveGame.savePlayer(player);
	}
	
}
