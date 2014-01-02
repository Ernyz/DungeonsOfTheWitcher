package com.ernyz.dotw.View;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.ernyz.dotw.Generators.EntityFactory;
import com.ernyz.dotw.Generators.TileFactory;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Model.MoveableEntity;
import com.ernyz.dotw.Model.Player;
import com.ernyz.dotw.Model.Tiles.Tile;

/**
 * Some kind of factory class which has the separate methods to load the map, player and everything else.
 * 
 * @author ernyz
 */
public class LoadGame {
	
	private GameWorld gameWorld;
	private String playerName;
	
	private Scanner scanner;
	private String scanResult;  //This string will hold scanned save file

	public LoadGame(String playerName, GameWorld gameWorld) {
		this.gameWorld = gameWorld;
		this.playerName = playerName;
	}
	
	/**
	 * Returns the tile array which is loaded from save file.
	 * 
	 * @param playerName - name of the player whose map we want to load.
	 * @param mapFile - name of the map we want to load, for example: level1, level25.
	 * @return  An array of {@link Tile}.
	 */
	public Array<Tile> laodTiles(String mapFile) {
		TileFactory tileFactory = new TileFactory();
		Array<Tile> tiles = new Array<Tile>();
		//Scan the save file
		try {
			scanner = new Scanner(new File("save/" + playerName + "/" + mapFile + ".txt"));
			scanResult = scanner.nextLine();
		}
		catch(IOException ex) {}
		finally {
			if(scanner != null)
				scanner.close();
		}
		//Loop through all the tiles in the save file array, and take the info from them to fill in tiles array
		JsonReader reader = new JsonReader();
		JsonValue json = reader.parse(scanResult);
		for(int i = 0; i < json.size; i++) {
			String name = json.get(i).getString("name");
			JsonValue tile = json.get(i);
			
			if(name.equals("floor"))
				tiles.add(tileFactory.createFloor(tile.getFloat("x"), tile.getFloat("y")));
			else if(name.equals("wall"))
				tiles.add(tileFactory.createWall(tile.getFloat("x"), tile.getFloat("y")));
		}
		
		return tiles;
	}
	
	/**
	 * Returns the player which is loaded from save file.
	 * 
	 * @return {@link Player} object.
	 */
	public Player loadPlayer() {
		EntityFactory entityFactory = new EntityFactory();
		Player player = entityFactory.createPlayer(0, 0, gameWorld);
		//Scan the save file
		try {
			scanner = new Scanner(new File("save/" + playerName + "/" + playerName + ".txt"));
			scanResult = scanner.nextLine();
		}
		catch(IOException ex) {}
		finally {
			if(scanner != null)
				scanner.close();
		}
		//Convert scanned data to player and return it
		JsonReader reader = new JsonReader();
		JsonValue json = reader.parse(scanResult);
		player.setName(json.getString("name"));
		player.setDungeonLevel(json.getInt("dungeonLevel"));
		player.setPosition(new Vector2(json.getFloat("x"), json.getFloat("y")));
		player.setSpeed(json.getFloat("speed"));
		player.setRotation(json.getFloat("rotation"));
		player.setHealth(json.getFloat("health"));
		
		return player;
	}
	
	/**
	 * Returns the entity array which is loaded from save file.
	 * 
	 * @return An array of {@link MoveableEntity}
	 */
	public Array<MoveableEntity> loadEntities() {
		EntityFactory entityFactory = new EntityFactory();
		Array<MoveableEntity> entities = new Array<MoveableEntity>();
		//Scan the save file
		try {
			scanner = new Scanner(new File("save/" + playerName + "/entities.txt"));
			scanResult = scanner.nextLine();
		}
		catch(IOException ex) {}
		finally {
			if(scanner != null)
				scanner.close();
		}
		//Convert scanned data to entity array and return it
		JsonReader reader = new JsonReader();
		JsonValue json = reader.parse(scanResult);
		for(int i = 0; i < json.size; i++) {
			String name = json.get(i).getString("name");
			JsonValue entity = json.get(i);
			
			MoveableEntity e = null;
			if(name.equals("Goblin")) {
				e = entityFactory.createGoblin(entity.getFloat("x"), entity.getFloat("y"), gameWorld);
				e.setRotation(entity.getFloat("rotation"));
				e.setSpeed(entity.getFloat("speed"));
				e.setHealth(entity.getFloat("health"));
			}
			entities.add(e);
		}
		
		return entities;
	}
}
