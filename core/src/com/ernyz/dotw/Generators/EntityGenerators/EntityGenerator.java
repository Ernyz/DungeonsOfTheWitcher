package com.ernyz.dotw.Generators.EntityGenerators;

import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Factories.EntityFactory;
import com.ernyz.dotw.Model.MoveableEntity;
import com.ernyz.dotw.Model.Enemies.Goblin;
import com.ernyz.dotw.Model.Tiles.Tile;

/**
 * This class is responsible for creating entities in a given level (array of {@link Tile}s).
 * 
 * @author Ernyz
 */
public class EntityGenerator {

	/**
	 * Loops through all tiles and decides what entities to generate on those tiles.
	 * @return Array of {@link MoveableEntity} which will reside on a given level.
	 */
	public static Array<MoveableEntity> generateEntitiesInLevel(int dungeonLevel, char[][] tiles) {
		Array<MoveableEntity> entities = new Array<MoveableEntity>();
		EntityFactory entityFactory = new EntityFactory();
		
		//for(int i = 0; i < tiles.size; i++) {
		for(int x = 0; x < tiles.length; x++) {
			for(int y = 0; y < tiles[0].length; y++) {
				//Determine type of the tile //TODO: Add more functionality later.
				//if(tiles.get(i).getAsciiSymbol() == '.') {
				if(tiles[x][y] == '.') {
					if(Math.random() < 0.009) {
						//FIXME: Bad starting coords.
						Goblin g = entityFactory.createGoblin(x*50+25, y*50+25, null);  //TODO remove hardcoding
						//Format json and return it to world generator? just for it to save the array of enemies
						entities.add(g);
					}
				}
			}
		}
		
		return entities;
	}
	
}
