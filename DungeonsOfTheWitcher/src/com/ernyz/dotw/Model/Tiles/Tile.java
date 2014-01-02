package com.ernyz.dotw.Model.Tiles;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ernyz.dotw.Model.Entity;

/**
 * Base class for all tiles.
 * 
 * @author ernyz
 */
public class Tile extends Entity {
	protected Boolean walkable;
	protected Rectangle bounds;

	public Tile(Vector2 position, float rotation) {
		super(position, rotation);
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public Boolean getWalkable() {
		return walkable;
	}

	public void setWalkable(Boolean walkable) {
		this.walkable = walkable;
	}
}
