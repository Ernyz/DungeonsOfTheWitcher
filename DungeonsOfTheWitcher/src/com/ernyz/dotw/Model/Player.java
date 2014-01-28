package com.ernyz.dotw.Model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Model.Tiles.Tile;

public final class Player extends MoveableEntity {
	
	public Player(Vector2 position, Vector2 velocity, float rotation, float speed, GameWorld gameWorld) {
		super(position, velocity, rotation, speed, gameWorld);
		texture = new Texture(Gdx.files.internal("data/player/player.png"));
		
		this.setWidth(texture.getWidth());
		this.setHeight(texture.getHeight());
		bounds.setVertices(new float[] {0, 0, getWidth(), 0, getWidth(), getHeight()-5, 0, getHeight()-5});//This hardcoding is temporary
		bounds.setOrigin(getWidth()/2, getHeight()/2);
		
		surroundingTiles = new Array<Tile>();
		surroundingEntities = new Array<MoveableEntity>();
		
		//Some stats should be set manually
		activeSurroundingsRange = 500;
		rightHand = new Vector2(-75, 19);  //Values are hard coded and found by trial and error.
		
		equipItem("RightHand", 0);  //TODO this is temporary
	}

	@Override
	public void update() {
		super.update();
		
		checkCollisions();
	}
}
