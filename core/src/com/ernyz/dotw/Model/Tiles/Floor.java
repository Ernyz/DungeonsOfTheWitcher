package com.ernyz.dotw.Model.Tiles;

import com.badlogic.gdx.math.Vector2;

public class Floor extends Tile {

	public Floor(Vector2 position, float rotation) {
		super(position, rotation);
		
		asciiSymbol = '.';
		//texture = new Texture(Gdx.files.internal("data/tiles/CaveGround.png"));
		this.setWalkable(true);
		this.setName("floor");
	}
	
	@Override
	public void update(float delta) {
	}
}
