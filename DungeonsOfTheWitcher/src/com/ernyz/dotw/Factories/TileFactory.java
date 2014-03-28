package com.ernyz.dotw.Factories;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Model.Tiles.Floor;
import com.ernyz.dotw.Model.Tiles.Tile;
import com.ernyz.dotw.Model.Tiles.Wall;

/**
 * Factory for tile creation.
 * 
 * @author Ernyz
 */
public class TileFactory {
	
	public TileFactory() {
		
	}
	
	/**
	 * Creates simple floor tile.
	 * @param x - x coordinates of tile.
	 * @param y - y coordinates of tile.
	 * @return {@link Floor} tile.
	 */
	public Floor createFloor(float x, float y) {
		Floor floor = new Floor(new Vector2(x, y), 0);
		return floor;
	}
	
	/**
	 * Creates simple wall tile.
	 * @param x - x coordinates of tile.
	 * @param y - y coordinates of tile.
	 * @return {@link Wall} tile.
	 */
	public Wall createWall(float x, float y) {
		Wall wall = new Wall(new Vector2(x, y), 0);
		//Create box2d wall for this wall
		wall.setBox2dBody(GameWorld.createBox2dWall(wall.getPosition().x, wall.getPosition().y));
		return wall;
	}
	
	/**
	 * Creates array of {@link Tile} from two dimensional array of integers generated in one of the generators.
	 */
	public Array<Tile> createTiles(char[][] map) {
		Array<Tile> tiles = new Array<Tile>();
		
		for(int x = 0; x < map.length; x++) {
			for(int y = 0; y < map[x].length; y++) {
				if(map[x][y] == '.') {
					tiles.add(createFloor(x*50, y*50));  //TODO remove hardcoding
				}
				else if(map[x][y] == '#') {
					tiles.add(createWall(x*50, y*50));  //TODO remove hardcoding
				}
			}
		}
		
		return tiles;
	}
	
}
