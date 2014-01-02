/**
 * 
 */
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
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Model.MoveableEntity;
import com.ernyz.dotw.Model.Player;
import com.ernyz.dotw.Model.Tiles.Tile;

/**
 * Saves game to the location which depends on current player.
 * 
 * @author ernyz
 */
public class SaveGame {
	
	private GameWorld gameWorld;
	
	//save file creation variables
	private Writer writer;
	private String dir = "save";  //directory for player save games
	
	public SaveGame(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
	}
	
	/**
	 * Saves all the current game data (Map, player, enemies, items).
	 */
	public void save() {
		gameWorld.addMessage("Saving game...");
		//Create new save dir, if for some reason there is none
		dir = dir + "/" + gameWorld.getPlayer().getName() + "/";
		(new File(dir)).mkdirs();
		
		//save the map
		Array<Tile> tiles = gameWorld.getTiles();
		StringWriter result = new StringWriter();
		JsonWriter jsonWriter = new JsonWriter(result);
		try {
			jsonWriter.array();
			for(int i = 0; i < tiles.size; i++) {
				jsonWriter.object()
					.set("id", i)
					.set("name", tiles.get(i).getName())
					.set("x", tiles.get(i).getPosition().x)
					.set("y", tiles.get(i).getPosition().y)
				.pop();
			}
			jsonWriter.pop();
			jsonWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + "level" + gameWorld.getPlayer().getDungeonLevel() + ".txt"), "utf-8"));
			writer.write(result.toString());
		}
		catch(IOException ex) {}
		finally {
			try {writer.close();} catch(IOException ex) {}
		}
		
		//Then save player
		Player player = gameWorld.getPlayer();
		result = new StringWriter();
		jsonWriter = new JsonWriter(result);
		try {
			jsonWriter.object()
				.set("name", player.getName())
				.set("dungeonLevel", player.getDungeonLevel())
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
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + gameWorld.getPlayer().getName() + ".txt"), "utf-8"));
			writer.write(result.toString());
		}
		catch(IOException ex) {}
		finally {
			try {writer.close();} catch(IOException ex) {}
		}
		
		//Then entities
		Array<MoveableEntity> entities = gameWorld.getEntities();
		result = new StringWriter();
		jsonWriter = new JsonWriter(result);
		try {
			jsonWriter.array();
			for(int i = 0; i < entities.size; i++) {
				jsonWriter.object()
					.set("name", entities.get(i).getName())
					.set("dungeonLevel", entities.get(i).getDungeonLevel())
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
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + "entities" + ".txt"), "utf-8"));
			writer.write(result.toString());
		}
		catch(IOException ex) {}
		finally {
			try {writer.close();} catch(IOException ex) {}
		}
		/*JSONArray entities = new JSONArray();
		for(int i = 0; i < gameWorld.getEntities().size; i++) {
			JSONObject temp = new JSONObject();
			temp.put("name", gameWorld.getEntities().get(i).getName());
			temp.put("x", gameWorld.getEntities().get(i).getPosition().x);
			temp.put("y", gameWorld.getEntities().get(i).getPosition().y);
			entities.add(temp);
		}
		obj.put("entities", entities);
		try {
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(dir + "entities" + ".txt"), "utf-8"));
			writer.write(obj.toString());
		}
		catch(IOException ex) {}
		finally {
			try {writer.close();} catch(IOException ex) {}
		}
		entities.clear();
		obj.clear();*/
		gameWorld.addMessage("Game saved!");
	}

}
