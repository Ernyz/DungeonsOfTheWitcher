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
	public static Array<MoveableEntity> generateEntitiesInLevel(int dungeonLevel, Array<Tile> tiles) {
		Array<MoveableEntity> entities = new Array<MoveableEntity>();
		EntityFactory entityFactory = new EntityFactory();
		
		for(int i = 0; i < tiles.size; i++) {
			//Determine type of the tile //TODO: Add more functionality later.
			if(tiles.get(i).getAsciiSymbol() == '.') {
				if(Math.random() < 0.009) {
					Goblin g = entityFactory.createGoblin(tiles.get(i).getPosition().x, tiles.get(i).getPosition().y, null);
					entities.add(g);
				}
			}
		}
		
		return entities;
	}
	
}
