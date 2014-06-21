package com.ernyz.dotw.View;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonWriter;
import com.ernyz.dotw.Factories.ItemFactory;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Model.MoveableEntity;
import com.ernyz.dotw.Model.Player;
import com.ernyz.dotw.Model.Items.Item;
import com.ernyz.dotw.Model.Tiles.Tile;

/**
 * Saves game to the location which depends on current player.
 * 
 * @author ernyz
 */
public class SaveGame {
	
	//Save file creation variables
	private static Writer writer;
	private static final String dir = "save/";  //Directory for player save games
	
	public SaveGame() {
		
	}
	
	/**
	 * Saves all the current game data (Map, player, enemies, items).
	 */
	public static void save(Array<Tile> tiles, Player player, Array<MoveableEntity> entities, Array<Item> items) {
		GameWorld.addMessage("Saving game...");
		
		long timer = System.currentTimeMillis();
		
		saveMap(player.getName(), tiles, String.valueOf(player.getDungeonLevel()));
		savePlayer(player);
		saveEntities(player.getName(), entities);
		saveItems(player.getName(), items);
		
		timer = System.currentTimeMillis() - timer;
		System.out.println("Game saved in: "+timer);
		
		GameWorld.addMessage("Game saved!");
	}
	
	public static void saveMap(String playerName, Array<Tile> tiles, String level) {
		//Create new save dir, if for some reason there is none
		String saveDir = dir + playerName + "/";
		(new File(dir)).mkdirs();
		
		//Save map in compact format
		String res = "";
		char tmp[][] = new char[50][50];  //TODO Remove this hardcoding
		for(int i = 0; i < tiles.size; i++) {
			tmp[(int) (tiles.get(i).getPosition().x/50)]
					[(int) (tiles.get(i).getPosition().y/50)]
							 = tiles.get(i).getAsciiSymbol();
		}
		for(int y = 0; y < tmp[0].length; y++) {
			for(int x = 0; x < tmp.length; x++) {
				res = res.concat(String.valueOf(tmp[x][y]));
				//System.out.print(tmp[x][y]);
			}
			res = res.concat("\n");
			//System.out.println();
		}
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(saveDir + "level" + level + ".txt"), "utf-8"));
			writer.write(res);
		}
		catch(IOException e) {e.printStackTrace();}
		finally {
			try {writer.close();} catch(IOException e) {e.printStackTrace();}
		}
	}
	
	public static void savePlayer(Player player) {
		StringWriter result = new StringWriter();
		JsonWriter jsonWriter = new JsonWriter(result);
		try {
			jsonWriter.object()
				.set("name", player.getName())
				.set("dungeonLevel", player.getDungeonLevel())
				.array("inventory");
					for(int i = 0; i < player.getInventory().size; i++) {
						jsonWriter.value(player.getInventory().get(i));
					}
				jsonWriter.pop()
				.object("equippedItems");
					for(String slotName : player.getEquipmentSlots().keySet()) {
						jsonWriter.set(slotName, player.getEquipmentSlots().get(slotName));
					}
				jsonWriter.pop()
				.set("x", player.getPosition().x)
				.set("y", player.getPosition().y)
				.set("speed", player.getSpeed())
				.set("rotation", player.getRotation())
				.set("health", player.getHealth())
			.pop();
			jsonWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + player.getName() + "/" + player.getName() + ".txt"), "utf-8"));
			writer.write(result.toString());
		}
		catch(IOException e) {e.printStackTrace();}
		finally {
			try {writer.close();} catch(IOException e) {e.printStackTrace();}
		}
	}
	
	public static void saveEntities(String playerName, Array<MoveableEntity> entities) {
		//TODO DONT SAVE PLAYER IN HERE
		StringWriter result = new StringWriter();
		JsonWriter jsonWriter = new JsonWriter(result);
		try {
			jsonWriter.array();
			for(int i = 0; i < entities.size; i++) {
				jsonWriter.object()
					.set("name", entities.get(i).getName())
					.set("dungeonLevel", entities.get(i).getDungeonLevel())
					.array("inventory");
						for(int j = 0; i < entities.get(i).getInventory().size; j++) {
							jsonWriter.value(entities.get(i).getInventory().get(j));
						}
					jsonWriter.pop()
					.object("equippedItems");
						for(String slotName : entities.get(i).getEquipmentSlots().keySet()) {
							jsonWriter.set(slotName, entities.get(i).getEquipmentSlots().get(slotName));
						}
					jsonWriter.pop()
					.set("x", entities.get(i).getPosition().x)
					.set("y", entities.get(i).getPosition().y)
					.set("speed", entities.get(i).getSpeed())
					.set("rotation", entities.get(i).getRotation())
					.set("health", entities.get(i).getHealth())
				.pop();
			}
			jsonWriter.pop();
			jsonWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + playerName + "/entities" + ".txt"), "utf-8"));
			writer.write(result.toString());
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {writer.close();} catch(IOException e) {e.printStackTrace();}
		}
	}
	
	public static void saveItems(String playerName, Array<Item> items) {
		StringWriter result = new StringWriter();
		JsonWriter jsonWriter = new JsonWriter(result);
		try {
			jsonWriter.array();
			jsonWriter.object().name("itemGeneretorState").value(ItemFactory.id)
			.pop();
			for(int i = 0; i < items.size; i++) {
				jsonWriter.object()
					.set("id", items.get(i).getId())
					.set("name", items.get(i).getName())
					.set("x", items.get(i).getX())
					.set("y", items.get(i).getY())
					.set("isInInventory", items.get(i).getIsInInventory())
				.pop();
			}
			jsonWriter.pop();
			jsonWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + playerName + "/items" + ".txt"), "utf-8"));
			writer.write(result.toString());
		} catch(IOException e) {
			e.printStackTrace();
		} finally {
			try {writer.close();} catch(IOException e) {e.printStackTrace();}
		}
	}

}
