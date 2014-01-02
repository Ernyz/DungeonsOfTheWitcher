package com.ernyz.dotw.Generators;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.JsonWriter;
import com.ernyz.dotw.Model.Player;

/**
 * Generates new player and outputs it to a save file.
 * 
 * @author ernyz
 */
public class PlayerGenerator {
	
	private Writer playerWriter;
	private Player player;
	
	public PlayerGenerator() {
		
	}
	
	//Later this method will generate player according to race, class and etc.
	public void generatePlayer(String name) {
		//Create player
		player = new Player(new Vector2(), new Vector2(), 0, 0, null);
		player.setName(name);
		player.setDungeonLevel(1);
		player.setPosition(new Vector2(100, 100));
		player.setSpeed(100f);
		player.setRotation(0);
		player.setHealth(100);
		
		writeToTextFile(name);
	}
	
	private void writeToTextFile(String name) {
		try {
			playerWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("save/"+name+"/"+name+".txt"), "utf-8"));
			StringWriter result = new StringWriter();
			JsonWriter writer = new JsonWriter(result);
			writer.object()
				.set("name", player.getName())
				.set("dungeonLevel", player.getDungeonLevel())
				.set("x", player.getPosition().x)
				.set("y", player.getPosition().y)
				.set("speed", player.getSpeed())
				.set("rotation", player.getRotation())
				.set("health", player.getHealth())
			.pop();
			writer.close();
			playerWriter.write(result.toString());
			playerWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
