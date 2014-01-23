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
	}

	@Override
	public void update() {
		super.update();
		
		//TODO temp
		currentWeapon = gameWorld.getItems().get(0);
		
		checkCollisions();
	}
	
	/*@Override
	public void checkCollisions() {
		//Update last position
		lastPos.set(position);
		
		//Rotate bounds
		bounds.setRotation(this.rotation);
		
		//Move this entity in x axis
		position.x += velocity.cpy().x * Gdx.graphics.getDeltaTime() * speed;
		//bounds.x = position.x + this.getWidth()/2;
		bounds.setPosition(position.x, bounds.getY());
		//Check collisions with tiles and then with entities
		for(int i = 0; i < surroundingTiles.size; i++) {
			if(!surroundingTiles.get(i).getWalkable() && Intersector.overlaps(bounds, surroundingTiles.get(i).getBounds())) {
				position.x = lastPos.x;
				bounds.x = lastPos.x + this.getWidth()/2;
			}
		}
		for(int i = 0; i < surroundingEntities.size; i++) {
			if(Intersector.overlaps(bounds, surroundingEntities.get(i).getBounds())) {
				position.x = lastPos.x;
				bounds.x = lastPos.x + this.getWidth()/2;
				//Move player in other axis, so it wont get stuck when in contact with entity
				if(this.position.y <= surroundingEntities.get(i).getPosition().y)
					this.position.y -= 1.5f;
				else
					this.position.y += 1.5f;
			}
		}
		
		//Move this entity in y axis
		position.y += velocity.cpy().y * Gdx.graphics.getDeltaTime() * speed;
		//bounds.y = position.y+this.getHeight()/2;
		bounds.setPosition(bounds.getX(), position.y);
		//Check collisions with tiles and then with entities
		for(int i = 0; i < surroundingTiles.size; i++) {
			if(!surroundingTiles.get(i).getWalkable() && Intersector.overlaps(bounds, surroundingTiles.get(i).getBounds())) {
				position.y = lastPos.y;
				bounds.y = lastPos.y + this.getHeight()/2;
			}
		}
		for(int i = 0; i < surroundingEntities.size; i++) {
			if(Intersector.overlaps(bounds, surroundingEntities.get(i).getBounds())) {
				position.y = lastPos.y;
				bounds.y = lastPos.y + this.getHeight()/2;
				//Move player in other axis, so it wont get stuck when in contact with entity
				if(this.position.x <= surroundingEntities.get(i).getPosition().x)
					this.position.x -= 1.5f;
				else
					this.position.x += 1.5f;
			}
		}
	}*/
}
