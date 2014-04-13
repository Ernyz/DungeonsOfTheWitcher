package com.ernyz.dotw.Generators.ItemGenerators;

import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Factories.ItemFactory;
import com.ernyz.dotw.Model.Enemies.Goblin;
import com.ernyz.dotw.Model.Items.Item;
import com.ernyz.dotw.Model.Tiles.Tile;

public class ItemGenerator {

	public static Array<Item> generateItemsInLevel(int dungeonLevel, Array<Tile> tiles) {
		Array<Item> items = new Array<Item>();
		ItemFactory itemFactory = new ItemFactory();
		
		for(int i = 0; i < tiles.size; i++) {
			if(tiles.get(i).getAsciiSymbol() == '.') {
				if(Math.random() < 0.079) {
					//TODO: Document this.
					Item item = itemFactory.createWeapon("Dagger", tiles.get(i).getPosition().x, tiles.get(i).getPosition().y, false);
					items.add(item);
				}
			}
		}
		
		return items;
	}
	
}
