package com.ernyz.dotw.Model.Tiles;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ernyz.dotw.Model.Entity;

/**
 * Base class for all tiles.
 * 
 * @author ernyz
 */
public class Tile extends Entity {
	protected char asciiSymbol;
	protected Boolean walkable;
	protected Polygon bounds;

	public Tile(Vector2 position, float rotation) {
		super(position, rotation);
	}

	public Polygon getBounds() {
		return bounds;
	}

	public void setBounds(Polygon bounds) {
		this.bounds = bounds;
	}

	public Boolean getWalkable() {
		return walkable;
	}

	public void setWalkable(Boolean walkable) {
		this.walkable = walkable;
	}

	public char getAsciiSymbol() {
		return asciiSymbol;
	}

	public void setAsciiSymbol(char asciiSymbol) {
		this.asciiSymbol = asciiSymbol;
	}
	
}
