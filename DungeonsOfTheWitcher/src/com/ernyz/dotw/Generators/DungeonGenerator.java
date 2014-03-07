package com.ernyz.dotw.Generators;

import java.util.Random;

import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Model.MoveableEntity;
import com.ernyz.dotw.Model.Enemies.Goblin;
import com.ernyz.dotw.Model.Tiles.Tile;
import com.ernyz.dotw.View.SaveGame;

/**
 * Generates whole dungeon of the game.
 * 
 * @author Ernyz
 */

public class DungeonGenerator {
	private Array<Tile> tiles;
	private Array<MoveableEntity> entities;
	private TileFactory tileFactory;
	private EntityFactory entityFactory;

	public DungeonGenerator() {
		tileFactory = new TileFactory();
	}
	
	public void generateDungeon(String name) {
		//TODO remove this hardcoded stuff later
		char[][] map = new char[50][50];
		generateDrunkardWalk(map);
		
		tiles = tileFactory.createTiles(map);
		fillWithEntities(tiles);
		
		SaveGame.saveMap(name, tiles, "1");  //TODO Level shouldnt be hardcoded
		SaveGame.saveEntities(name, entities);
	}
	
	private void generateDrunkardWalk(char[][] map) {
		//Fill map with walls
		for(int x = 0; x < map.length; x++) {
			for(int y = 0; y < map[x].length; y++) {
				map[x][y] = '#';
			}
		}
		//Set random starting position (not random while in testing stage)
		int x = 2;
		int y = 2;
		//How many percents if walls should be carved
		int percentage = 50;
		//Number of walls removed
		int count = 0;
		//Directions. 1-left; 2-down; 3-right; 4-up.
		int dir;
		//Last direction. Used to prevent going backwards
		int lastDir;
		Random r = new Random();
		
		//Make current tile a floor tile
		map[x][y] = '.';
		
		while((count*100)/(map.length*map[0].length) < percentage) {
			dir = r.nextInt(4)+1;  //From 1 to 4
			
			if(dir == 1) {  //Left
				if(x-1 > 0) {
					map[x-1][y] = '.';
					x--;
					count++;
				}
			}
			else if(dir == 2) {  //Down
				if(y-1 > 0) {
					map[x][y-1] = '.';
					y--;
					count++;
				}
			}
			else if(dir == 3) {  //Right
				if(x+1 < map[0].length-1) {
					map[x+1][y] = '.';
					x++;
					count++;
				}
			}
			else if(dir == 4) {  //Up
				if(y+1 < map[0].length-1) {
					map[x][y+1] = '.';
					y++;
					count++;
				}
			}
			lastDir = dir;
		}
	}
	
	/**
	 * Fills given level with monsters.
	 */
	private void fillWithEntities(Array<Tile> tiles) {
		entities = new Array<MoveableEntity>();
		entityFactory = new EntityFactory();
		for(Tile t : tiles) {
			if(t.getWalkable()) {
				if(Math.random() < 0.009) {
					Goblin g = entityFactory.createGoblin(t.getPosition().x, t.getPosition().y, null);
					entities.add(g);
				}
			}
		}
	}
}
