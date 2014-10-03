package com.ernyz.dotw.Factories;

import com.badlogic.gdx.math.MathUtils;
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
	
	/**
	 * Creates player whose values then can be set according to save file values.
	 * @param x - x coordinate of the player
	 * @param y - y coordinate of the player
	 * @param gameWorld - {@link GameWorld}
	 * @return {@link Player} instance
	 */
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
		goblin.setSpeed(60f);
		goblin.setHealth(50);
		goblin.setRotation(MathUtils.random(360));
		
		return goblin;
	}

}
