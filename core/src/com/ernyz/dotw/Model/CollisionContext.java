package com.ernyz.dotw.Model;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Combat.BasicAttack;
import com.ernyz.dotw.Combat.BasicAttack.StateEnum;
import com.ernyz.dotw.Model.Tiles.Tile;

public class CollisionContext {
	
	private Array<MoveableEntity> entities;
	private Array<Tile> tiles;
	private Array<BasicAttack> attacks;

	public CollisionContext(Array<MoveableEntity> entities, Array<Tile> tiles, Array<BasicAttack> attacks) {
		this.entities = entities;
		this.tiles = tiles;
		this.attacks = attacks;
	}
	
	public void checkCollisions(float delta) {  //public void checkCollisions() {
		//Loop through all entities and check if they collide with any of the following:
		//other entities, tiles and attacks. If collision occurs, call handleCollision()
		
		Array<Tile> surroundingTiles;
		Array<MoveableEntity> surroundingEntities;
		MoveableEntity e;
		
		for(int i = 0; i < entities.size; i++) {
			
			e = entities.get(i);
			surroundingTiles = entities.get(i).getSurroundingTiles(); 
			surroundingEntities = entities.get(i).getSurroundingEntities();
			
			e.moveX(delta);
			for(int j = 0; j < surroundingEntities.size; j++) {
				if(!e.equals(surroundingEntities.get(j))) {
					//Entity vs. other entities
					if(Intersector.overlapConvexPolygons(e.getBounds(), surroundingEntities.get(j).getBounds())) {
						e.getPosition().x = e.getLastPosX();
						e.getBounds().setPosition(e.getLastPosX()-e.getRadius(), e.getBounds().getY());
					}
				}
			}
			for(int j = 0; j < surroundingTiles.size; j++) {
				//X axis
				if(!surroundingTiles.get(j).getWalkable() && Intersector.overlapConvexPolygons(e.getBounds(), surroundingTiles.get(j).getBounds())) {
					e.getPosition().x = e.getLastPosX();
					e.getBounds().setPosition(e.getLastPosX()-e.getRadius(), e.getBounds().getY());
				}
			}
			e.moveY(delta);
			for(int j = 0; j < surroundingEntities.size; j++) {
				if(!e.equals(surroundingEntities.get(j))) { 
					if(Intersector.overlapConvexPolygons(e.getBounds(), surroundingEntities.get(j).getBounds())) {
						e.getPosition().y = e.getLastPosY();
						e.getBounds().setPosition(e.getBounds().getX(), e.getLastPosY()-e.getRadius());
					}
				}
			}
			for(int j = 0; j < surroundingTiles.size; j++) {
				//Y axis
				if(!surroundingTiles.get(j).getWalkable() && Intersector.overlapConvexPolygons(e.getBounds(), surroundingTiles.get(j).getBounds())) {
					e.getPosition().y = e.getLastPosY();
					e.getBounds().setPosition(e.getBounds().getX(), e.getLastPosY()-e.getRadius());
				}
			}
			
			for(int j = 0; j < attacks.size; j++) {
				if(!attacks.get(j).getAttacker().equals(e)) {
					if(attacks.get(j).getState().equals(StateEnum.ATTACKING)) {
						if(Intersector.overlapConvexPolygons(e.getBounds(), attacks.get(j).getBounds())) {
							e.onCollision(attacks.get(j));
							attacks.get(j).onCollision(e);
						}
					}
				}
			}
			for(int j = 0; j < surroundingTiles.size; j++) {
				if(!surroundingTiles.get(j).getWalkable()) {
					for(int k = 0; k < attacks.size; k++) {
						if(attacks.get(k).getState().equals(StateEnum.ATTACKING)) {
							if(Intersector.overlapConvexPolygons(attacks.get(k).getBounds(), surroundingTiles.get(j).getBounds())) {
								attacks.get(k).onCollision(surroundingTiles.get(j));
							}
						}
					}
				}
			}
		}
	}
	
}
