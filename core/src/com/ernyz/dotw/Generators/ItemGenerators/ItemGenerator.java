package com.ernyz.dotw.Generators.ItemGenerators;

import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Factories.ItemFactory;
import com.ernyz.dotw.Model.Enemies.Goblin;
import com.ernyz.dotw.Model.Items.Item;
import com.ernyz.dotw.Model.Tiles.Tile;

/**
 * Contains methods for generating items in the given levels.
 * 
 * @author Ernyz
 */
public class ItemGenerator {

	/**
	 * Generates item in given dungeon level.
	 * @param dungeonLevel - level in which items will be generated.
	 * @param tiles -  map in which to generate items.
	 * @return An array of {@link Item}.
	 */
	//public static Array<Item> generateItemsInLevel(int dungeonLevel, Array<Tile> tiles) {
	public static Array<Item> generateItemsInLevel(int dungeonLevel, char[][] tiles) {
		Array<Item> items = new Array<Item>();
		ItemFactory itemFactory = new ItemFactory();
		
		for(int x = 0; x < tiles.length; x++) {
			for(int y = 0; y < tiles[0].length; y++) {
				if(tiles[x][y] == '.') {
					if(Math.random() < 0.079) {
						Item item = itemFactory.createWeapon("Dagger", x*50f+5, y*50f+5, false);  //TODO remove hardcoding
						items.add(item);
					}
				}
			}
		}
		
		return items;
	}
	
}
