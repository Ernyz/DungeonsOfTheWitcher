package com.ernyz.dotw.Generators;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Factories.ItemFactory;
import com.ernyz.dotw.Model.Player;
import com.ernyz.dotw.Model.Items.Item;
import com.ernyz.dotw.Model.Tiles.Tile;
import com.ernyz.dotw.View.SaveGame;

/**
 * Generates new player and uses {@link SaveGame} to output it to a save file.
 * 
 * @author Ernyz
 */
public class PlayerGenerator {
	
	//Later this method will generate player according to race, class and etc.
	/**
	 * Generates player according to given parameter object (which is now only a String name).
	 * @param name
	 * @param tiles
	 * @param items
	 * @return
	 */
	public static Player generatePlayer(String name, char[][] tiles, Array<Item> items) {
		Player player;
		ItemFactory itemFactory;
		
		//Create player
		player = new Player(new Vector2(), 0, null);
		player.setName(name);
		player.setDungeonLevel(0);
		setPosition(player, tiles);
		player.setSpeed(100f);
		player.setRotation(0);
		player.setHealth(100);
		
		//Give player items. TODO Later it will depend on his race, class, etc.
		//itemFactory = new ItemFactory();
		
		return player;
	}
	
	private static void setPosition(Player p, char[][] tiles) {
		for(int x = 0; x < tiles.length; x++) {
			for(int y = 0; y < tiles[0].length; y++) {
				if(tiles[x][y] == '@') {
					p.setPosition(new Vector2(x*50+25, y*50+25));  //TODO remove hardcoding
				}
			}
		}
	}
}
