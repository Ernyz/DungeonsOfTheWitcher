package com.ernyz.dotw.View;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.ernyz.dotw.Generators.EntityFactory;
import com.ernyz.dotw.Generators.ItemFactory;
import com.ernyz.dotw.Generators.TileFactory;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Model.MoveableEntity;
import com.ernyz.dotw.Model.Player;
import com.ernyz.dotw.Model.Items.Item;
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
		Array<Tile> tiles;// = new Array<Tile>();
		char[][] tmp = new char[50][50];  //TODO Fix hardcoding!
		int y = 0;  //TODO Fix hardcoding!
		//Scan the save file and put it into an array of char
		try {
			scanner = new Scanner(new File("save/" + playerName + "/" + mapFile + ".txt"));
			while(scanner.hasNext()) {
				scanResult = scanner.nextLine();
				for(int x = 0; x < scanResult.length(); x++) {
					tmp[x][y] = scanResult.charAt(x);
				}
				y++;
			}
		}
		catch(IOException ex) {}
		finally {
			if(scanner != null)
				scanner.close();
		}
		tiles = tileFactory.createTiles(tmp);
		
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
				entities.add(e);
			}
			/*else if(name.equals("Player")) {
				e = entityFactory.createGoblin(entity.getFloat("x"), entity.getFloat("y"), gameWorld);
				e.setRotation(entity.getFloat("rotation"));
				e.setSpeed(entity.getFloat("speed"));
				e.setHealth(entity.getFloat("health"));
			}*/
			//entities.add(e);
		}
		
		return entities;
	}
	
	public Array<Item> loadItems() {
		//Create item factory
		ItemFactory itemFactory = new ItemFactory();
		
		Array<Item> items = new Array<Item>();
		//Scan the save file
		try {
			scanner = new Scanner(new File("save/" + playerName + "/items.txt"));
			scanResult = scanner.nextLine();
		}
		catch(IOException ex) {}
		finally {
			if(scanner != null)
				scanner.close();
		}
		
		//Create few items. Just because we can.
		items.add(itemFactory.createWeapon("Dagger"));
		items.add(itemFactory.createWeapon("Dagger"));
		items.add(itemFactory.createWeapon("Dagger"));
		items.add(itemFactory.createWeapon("Dagger"));
		
		return items;
	}
}
