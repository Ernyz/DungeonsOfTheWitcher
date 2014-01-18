package com.ernyz.dotw.Model.Enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Model.MoveableEntity;
import com.ernyz.dotw.Model.Tiles.Tile;

public class Goblin extends Enemy {
	
	private Vector2 dirVector = new Vector2();  //Direction that entity faces
	
	public Goblin(Vector2 position, Vector2 velocity, float rotation, float speed, GameWorld gameWorld) {
		super(position, velocity, rotation, speed, gameWorld);
		texture = new Texture(Gdx.files.internal("data/enemies/Enemy.png"));
		
		this.setWidth(texture.getWidth());
		this.setHeight(texture.getHeight());
		bounds.setVertices(new float[] {0, 0, getWidth(), 0, getWidth(), getHeight(), 0, getHeight()});
		bounds.setOrigin(getWidth()/2, getHeight()/2);
		
		surroundingTiles = new Array<Tile>();
		surroundingEntities = new Array<MoveableEntity>();
		
		//Some stats should be set manually
		activeSurroundingsRange = 500;
		
		state = StateEnum.WANDER;
	}

	@Override
	public void update() {
		super.update();
		
		examineSurroundings();
		checkCollisions();
	}

	@Override
	public void examineSurroundings() {
		//Set the state of the monster
		if(this.getPosition().dst(gameWorld.getPlayer().getPosition()) <= 200) {
			state = StateEnum.ATTACK;
		}
		else {
			state = StateEnum.WANDER;
		}
		
		//Now that we know the state, execute actions
		if(state == StateEnum.ATTACK) {
			//Turn to target
			dirVector.set(gameWorld.getPlayer().getPosition().x, gameWorld.getPlayer().getPosition().y);		
			this.setRotation(new Vector2(dirVector.sub(this.getPosition()).nor()).angle());
			//Decide how to attack player (with spell, melee or ranged weapon)
			//See if target is in attack range
			//Attack or move closer if needed
			velocity.set(dirVector.x/Math.abs(dirVector.x), dirVector.y/Math.abs(dirVector.y));  //Velocity only needs values of +/- 0 or 1 to indicate direction
		}
		else if(state == StateEnum.WANDER) {
			velocity.set(0, 0);
		}
	}

	/*@Override
	public void checkCollisions() {
		//Update last position
		lastPos.set(position);
		
		//Rotate bounds
		bounds.setRotation(this.rotation);
		
		//Move this entity in x axis
		position.x += velocity.cpy().x * Gdx.graphics.getDeltaTime() * speed;
		bounds.setPosition(position.x, bounds.getY());
		//Check collisions with tiles, player and then with entities
		for(int i = 0; i < surroundingTiles.size; i++) {
			if(!surroundingTiles.get(i).getWalkable() && Intersector.overlaps(bounds, surroundingTiles.get(i).getBounds())) {
				position.x = lastPos.x;
				bounds.x = lastPos.x + this.getWidth()/2;
			}
		}
		if(Intersector.overlaps(bounds, gameWorld.getPlayer().getBounds())) {
			position.x = lastPos.x;
			bounds.x = lastPos.x + this.getWidth()/2;
		}
		for(int i = 0; i < surroundingEntities.size; i++) {
			if(Intersector.overlaps(bounds, surroundingEntities.get(i).getBounds())) {
				if(!this.equals(surroundingEntities.get(i))) {
					position.x = lastPos.x;
					bounds.x = lastPos.x + this.getWidth()/2;
				}
			}
		}
				
		//Move this entity in y axis
		position.y += velocity.cpy().y * Gdx.graphics.getDeltaTime() * speed;
		//bounds.y = position.y+this.getHeight()/2;
		bounds.setPosition(bounds.getX(), position.y);
		//Check collisions with tiles, player and then with entities
		for(int i = 0; i < surroundingTiles.size; i++) {
			if(!surroundingTiles.get(i).getWalkable() && Intersector.overlaps(bounds, surroundingTiles.get(i).getBounds())) {
				position.y = lastPos.y;
				bounds.y = lastPos.y + this.getHeight()/2;
			}
		}
		if(Intersector.overlaps(bounds, gameWorld.getPlayer().getBounds())) {
			position.y = lastPos.y;
			bounds.y = lastPos.y + this.getHeight()/2;
		}
		for(int i = 0; i < surroundingEntities.size; i++) {
			if(Intersector.overlaps(bounds, surroundingEntities.get(i).getBounds())) {
				if(!this.equals(surroundingEntities.get(i))) {
					position.y = lastPos.y;
					bounds.y = lastPos.y + this.getHeight()/2;
				}
			}
		}
	}*/
}
