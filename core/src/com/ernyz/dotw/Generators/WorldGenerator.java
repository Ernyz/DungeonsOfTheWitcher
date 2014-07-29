package com.ernyz.dotw.Generators;

import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Generators.EntityGenerators.EntityGenerator;
import com.ernyz.dotw.Generators.ItemGenerators.ItemGenerator;
import com.ernyz.dotw.Generators.LevelGenerators.LevelGenerator;
import com.ernyz.dotw.Model.MoveableEntity;
import com.ernyz.dotw.Model.Player;
import com.ernyz.dotw.Model.Items.Item;
import com.ernyz.dotw.View.SaveGame;

/**
 * Responsible for player and whole game world generation with it's items, monsters and etc.
 * 
 * @author Ernyz
 */
public class WorldGenerator {
	
	/*
	 * Constants which determine world parameters, e.g. size, depth.
	 * TODO: Move these to some config or config class or something.
	 */
	private static final int NUMBER_OF_LEVELS = 1;
	public static final int MAP_WIDTH = 50;
	public static final int MAP_HEIGHT = 50;

	private String playerName;
	private Player player;
	
	//private Array<Array<Tile>> levels;
	private Array<char[][]> levels;
	private Array<MoveableEntity> entities;
	private Array<Item> items;
	
	//Later on, this String playerName will be replaced by some special Object or HashMap
	//which will contain all the data needed for creation.
	public WorldGenerator(String playerName) {
		this.playerName = playerName;
		
		long timer = System.currentTimeMillis();
		generateWorld();
		timer = System.currentTimeMillis()-timer;
		System.out.println("World generated in: " + timer);
		//Dump generated content to save files
		timer = System.currentTimeMillis();
		saveWorld();
		timer = System.currentTimeMillis()-timer;
		System.out.println("World saved in: " + timer);
	}
	
	/**
	 * Generates all world of the game.
	 */
	private void generateWorld() {
		//Initialise values
		levels = new Array<char[][]>();
		items = new Array<Item>();
		
		/*
		 * Generation sequence:
		 * 1. World
		 *   a. Levels
		 *   b. Branches (go through levels, and check for entrances to branches,
		 *      if one is found - generate; continue looking)
		 * 2. Items (on ground, in chests, etc.)
		 * 3. Entities and items they possess
		 * 4. Player and items it possesses
		 */
		
		//Generate world
		for(int i = 0; i < NUMBER_OF_LEVELS; i++) {
			levels.add(LevelGenerator.generateLevel(i));
		}
		
		//Generate entities
		for(int i = 0; i < levels.size; i++) {
			//TODO: Change i to level name?.. Would be good for branches and stuff
			entities = EntityGenerator.generateEntitiesInLevel(i, levels.get(i));  //add items to arguments
		}
		
		//Generate items (in the world, not in entities inventories)
		for(int i = 0; i < levels.size; i++) {
			items.addAll(ItemGenerator.generateItemsInLevel(i, levels.get(i)));
		}
		
		//Generate player
		//TODO: Generate it in some good starting position. Maybe there should be one generated in the map on purpose.
		player = PlayerGenerator.generatePlayer(playerName, levels.get(0), items);
	}
	
	private void saveWorld() {
		//String tmp;
		//Save levels
		for(int i = 0; i < levels.size; i++) {
			SaveGame.saveMap(playerName, levels.get(i), String.valueOf(i));
		}
		//Save entities
		SaveGame.saveEntities(playerName, entities);
		//Save items
		SaveGame.saveItems(playerName, items);
		//Save player
		SaveGame.savePlayer(player);
	}
	
	/*private String convertMapToString(char[][] map) {
		String result = "";
		
		for(int x = 0; x < map.length; x++) {
			for(int y = 0; y < map[x].length; y++) {
				//result = result.concat(String.valueOf(map[x][y]));
				result += String.valueOf(map[x][y]);
			}
			//result.concat(String.valueOf("\n"));
			result += "\n";
		}
		
		return result;
	}*/
	
}
