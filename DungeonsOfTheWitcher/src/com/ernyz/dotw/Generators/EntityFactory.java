package com.ernyz.dotw.Generators;

import com.badlogic.gdx.math.Vector2;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Model.Player;
import com.ernyz.dotw.Model.Enemies.Goblin;

/**
 * Factory for entity creation.
 * 
 * @author Ernyz
 */
public class EntityFactory {
	
	public EntityFactory() {
		
	}
	
	public Player createPlayer(float x, float y, GameWorld gameWorld) {
		Player player = new Player(new Vector2(), new Vector2(), 0f, 0f, gameWorld);
		return player;
	}
	
	/**
	 * Creates "standard" goblin
	 */
	public Goblin createGoblin(float x, float y, GameWorld gameWorld) {
		Goblin goblin = new Goblin(new Vector2(x, y), new Vector2(0, 0), 0, 0, gameWorld);
		goblin.setName("Goblin");
		goblin.setDungeonLevel(1);  //TODO this should be received as a parameter
		goblin.setSpeed(70f);
		goblin.setHealth(50);
		
		return goblin;
	}

}
