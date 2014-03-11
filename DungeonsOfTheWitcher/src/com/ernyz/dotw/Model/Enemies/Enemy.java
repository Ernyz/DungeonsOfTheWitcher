package com.ernyz.dotw.Model.Enemies;

import com.badlogic.gdx.math.Vector2;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Model.MoveableEntity;

/**
 * Parent class of all enemies.
 * 
 * @author Ernyz
 */
public abstract class Enemy extends MoveableEntity {
	
	protected enum StateEnum {
		WANDER, ATTACK
	}
	protected StateEnum state;
	
	public Enemy(Vector2 position, Vector2 velocity, float rotation, float speed, GameWorld gameWorld) {
		super(position, velocity, rotation, speed, gameWorld);
	}
	
	/**
	 * Called from update() function. Observes surroundings of entity and
	 * makes decisions according those observations.
	 */
	public abstract void examineSurroundings();
	
}
