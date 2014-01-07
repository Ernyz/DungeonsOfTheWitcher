package com.ernyz.dotw.Model.Tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Floor extends Tile {

	public Floor(Vector2 position, float rotation) {
		super(position, rotation);
		
		texture = new Texture(Gdx.files.internal("data/tiles/CaveGround.png"));
		this.setWalkable(true);
		this.setName("floor");
	}
}
