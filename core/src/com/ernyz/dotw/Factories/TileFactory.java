package com.ernyz.dotw.Factories;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Model.Tiles.Floor;
import com.ernyz.dotw.Model.Tiles.Tile;
import com.ernyz.dotw.Model.Tiles.Torch;
import com.ernyz.dotw.Model.Tiles.Wall;

/**
 * Factory for tile creation.
 * 
 * @author Ernyz
 */
public class TileFactory {
	
	//Texture tileSheet;
	TextureAtlas atlas;
	
	public TileFactory() {
		atlas = new TextureAtlas("data/tiles/tiles.atlas");
		//tileSheet = new Texture("data/tiles/tiles.png");
	}
	
	/**
	 * Creates simple floor tile.
	 * @param x - x coordinates of tile.
	 * @param y - y coordinates of tile.
	 * @return {@link Floor} tile.
	 */
	public Floor createFloor(float x, float y) {
		Floor floor = new Floor(new Vector2(x, y), 0);
		floor.setTextureRegion(atlas.findRegion("CaveGround"));
		return floor;
	}
	
	public Torch createTorch(float x, float y) {
		Torch torch = new Torch(new Vector2(x, y), 0);
		return torch;
	}
	
	/**
	 * Creates simple wall tile.
	 * @param x - x coordinates of tile.
	 * @param y - y coordinates of tile.
	 * @return {@link Wall} tile.
	 */
	public Wall createWall(float x, float y) {
		Wall wall = new Wall(new Vector2(x, y), 0);
		wall.setTextureRegion(atlas.findRegion("Wall"));
		//Create box2d wall for this wall
		wall.setBox2dBody(GameWorld.createBox2dWall(wall.getPosition().x, wall.getPosition().y));
		return wall;
	}
	
	/**
	 * Creates array of {@link Tile} from two dimensional array of chars generated in one of the generators.
	 */
	public Array<Tile> createTiles(char[][] map) {
		Array<Tile> tiles = new Array<Tile>();
		
		for(int x = 0; x < map.length; x++) {
			for(int y = 0; y < map[x].length; y++) {
				if(map[x][y] == '.' || map[x][y] == '@') {
					tiles.add(createFloor(x*50, y*50));  //TODO remove hardcoding
				}
				else if(map[x][y] == '#') {
					tiles.add(createWall(x*50, y*50));  //TODO remove hardcoding
				}
				else if(map[x][y] == 'Y') {
					tiles.add(createFloor(x*50, y*50));  //TODO remove hardcoding
					tiles.add(createTorch(x*50, y*50));
				}
			}
		}
		
		return tiles;
	}
	
}
