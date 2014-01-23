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
		rightHand = new Vector2(-75, 19);  //Values are hard coded and found by trial and error.
		
		state = StateEnum.WANDER;
	}

	@Override
	public void update() {
		super.update();
		
		//TODO temp
		currentWeapon = gameWorld.getItems().get(0);
		
		examineSurroundings();
		checkCollisions();
	}

	@Override
	public void examineSurroundings() {  //TODO I should move this into an enemy class
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
			if(this.getPosition().dst(gameWorld.getPlayer().getPosition()) <= 50) {
				attack(0);  //Primary attack
			}
			else {
				//Attack or move closer if needed
				velocity.set(dirVector.x/Math.abs(dirVector.x), dirVector.y/Math.abs(dirVector.y));  //Velocity only needs values of +/- 0 or 1 to indicate direction
			}
		}
		else if(state == StateEnum.WANDER) {
			velocity.set(0, 0);
		}
	}
}
