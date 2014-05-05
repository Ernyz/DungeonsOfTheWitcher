package com.ernyz.dotw.Generators.LevelGenerators;

import java.util.Random;

import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Factories.TileFactory;
import com.ernyz.dotw.Model.Tiles.Tile;

/**
 * Class which is responsible of generation of a level with drunkard walk algorithm.
 * 
 * TODO: For now, this is a static class. Maybe i should later convert this to non-static
 * TODO: and send generation parameters to constructor or something...
 * @author Ernyz
 */
//TODO: Later should receive info about which branches to generate and etc.
public class DrunkardWalkLevelGenerator {

	/**
	 * Generates level by using drunkard walk algorithm.
	 * @param levelNumber - number of the level to generate, representing it's depth.
	 * @return generated array of {@link Tile}
	 */
	public static Array<Tile> generateDrunkardWalkLevel(int levelNumber) {
		
		TileFactory tileFactory = new TileFactory();
		char[][] map = new char[50][50];  //TODO: Remove hardcoding
		
		//Fill map with walls
		for(int x = 0; x < map.length; x++) {
			for(int y = 0; y < map[x].length; y++) {
				map[x][y] = '#';
			}
		}
		//Set random starting position (not random while in testing stage)
		int x = 2;
		int y = 2;
		//How many percents of walls should be carved
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
		
		//Convert from char[][] to Array<Tile> and return
		return tileFactory.createTiles(map);
	}
	
}