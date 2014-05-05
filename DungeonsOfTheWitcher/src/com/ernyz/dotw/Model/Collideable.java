package com.ernyz.dotw.Model;

import com.ernyz.dotw.Model.Items.Item;
import com.ernyz.dotw.Model.Tiles.Tile;

/**
 * Interface for entities which can collide.
 * 
 * @author Ernyz
 */
public interface Collideable {

	public void handleCollisions(MoveableEntity e);
	
	public void handleCollisions(Tile e);
	
	public void handleCollisions(Item e);
	
}
