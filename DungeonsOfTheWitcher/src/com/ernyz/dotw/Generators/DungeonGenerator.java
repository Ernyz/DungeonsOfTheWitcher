package com.ernyz.dotw.Generators;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Random;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonWriter;
import com.ernyz.dotw.Model.MoveableEntity;
import com.ernyz.dotw.Model.Enemies.Goblin;
import com.ernyz.dotw.Model.Tiles.Tile;

/**
 * Generates whole dungeon of the game.
 * 
 * @author ernyz
 */

public class DungeonGenerator {
	private Writer tileWriter;
	private Writer entityWriter;
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
		
		writeMapToTextFile(name);  //TODO fix this to be suitable for multiple dungeon levels
		writeEntitiesToTextFile(name);  //TODO fix this to be suitable for multiple dungeon levels
	}
	
	private void generateDrunkardWalk(char[][] map) {
		//Fill map with walls
		for(int i = 0; i < map.length; i++) {
			for(int j = 0; j < map[0].length; j++) {
				map[i][j] = '#';
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
				if(y+1 < map.length-1) {
					map[x][y+1] = '.';
					y++;
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
				if(y-1 > 0) {
					map[x][y-1] = '.';
					y--;
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
				if(Math.random() < 0.001) {  //x percent chance for goblin to spawn on a walkable tile
					Goblin g = entityFactory.createGoblin(t.getPosition().x, t.getPosition().y, null);
					entities.add(g);
				}
			}
		}
	}
	
	private void writeMapToTextFile(String name) {
		try {
			tileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("save/"+name+"/"+"level1.txt"), "utf-8"));
			StringWriter result = new StringWriter();
			JsonWriter writer = new JsonWriter(result);
			writer.array();
			for(int i = 0; i < tiles.size; i++) {
				writer.object()
					.set("id", i)
					.set("name", tiles.get(i).getName())
					.set("x", tiles.get(i).getPosition().x)
					.set("y", tiles.get(i).getPosition().y)
					.pop();
			}
			writer.pop();
			writer.close();
			tileWriter.write(result.toString());
			tileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void writeEntitiesToTextFile(String name) {
		try {
			entityWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("save/"+name+"/"+"entities.txt"), "utf-8"));
			StringWriter result = new StringWriter();
			JsonWriter writer = new JsonWriter(result);
			writer.array();
			for(int i = 0; i < entities.size; i++) {
				writer.object()
					.set("id", i)
					.set("name", entities.get(i).getName())
					.set("dungeonLevel", entities.get(i).getDungeonLevel())
					.set("x", entities.get(i).getPosition().x)
					.set("y", entities.get(i).getPosition().y)
					.set("speed", entities.get(i).getSpeed())
					.set("rotation", entities.get(i).getRotation())
					.set("health", entities.get(i).getHealth())
					.pop();
			}
			writer.pop();
			writer.close();
			entityWriter.write(result.toString());
			entityWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
