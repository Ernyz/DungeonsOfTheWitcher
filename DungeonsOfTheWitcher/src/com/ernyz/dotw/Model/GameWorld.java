package com.ernyz.dotw.Model;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.DOTW;
import com.ernyz.dotw.Model.Items.Item;
import com.ernyz.dotw.Model.Tiles.Tile;
import com.ernyz.dotw.View.HeadsUpDisplay;
import com.ernyz.dotw.View.LoadGame;

/**
 * Class in which all the game objects are held.
 */
public final class GameWorld {
	
	private enum GameStateEnum {
		LOADING, PLAYING, PAUSED
	}
	private GameStateEnum gameState;
	
	private DOTW game;
	private String playerName;
	private HeadsUpDisplay headsUpDisplay;
	
	//Messages
	private static Array<String> messageHistory;  //Will contain all game messages
	private static int maxMessagesSaved;  //Will denote number of maximum number of messages saved
	
	private Player player;
	
	//Item array
	//Im not sure if making this static is a good idea, but for now it seems to be the best option.
	//Might make other arrays like entities and tiles static too, if this proves to be ok.
	public static Array<Item> items;
	
	//Enemy array and enemies
	private Array<MoveableEntity> entities;
	
	//Tiles
	private Array<Tile> tiles;  //Holds all tiles
	
	//Box2d variables (lighting variables are in renderer class)
	private static World world;
	private static Body wallBody;
	
	public GameWorld(DOTW game, String playerName) {
		this.game = game;
		this.playerName = playerName;
		
		//Set game state
		gameState = GameStateEnum.LOADING;
		
		//Initialise box2d world
		world = new World(new Vector2(0, 0), false);
		
		maxMessagesSaved = 50;
		messageHistory = new Array<String>();
		
		headsUpDisplay = new HeadsUpDisplay(this);
		
		//Load the game
		LoadGame l = new LoadGame(playerName, this);
		tiles = l.laodTiles("levell1");
		items = l.loadItems();
		player = l.loadPlayer();
		entities = l.loadEntities();
		entities.add(player);  //Player is an entity too, so add it
		
		//Set game state to PLAYING, because the game has finished loading
		gameState = GameStateEnum.PLAYING;
	}
	
	public void update() {
		//Update everything
 		if(gameState == GameStateEnum.PLAYING) {
			for(MoveableEntity entity : entities) {
				if(entity.getIsDead()) {
					entity.dispose();
					entities.removeValue(entity, false);
				} else {
					entity.update();
				}
			}
		}
	}
	
	public void dispose() {
		world.dispose();
	}
	
	/**
	 * Creates and returns a box2d wall which can then be assigned to some opaque wall.
	 * @param x - x coordinate of wall.
	 * @param y - y coordinate of wall.
	 * @return box2d body.
	 */
	public static Body createBox2dWall(float x, float y) {
		//This check is needed, because this function is called when we create new map (at that point box2dworld is null)
		if(world == null) return null;
		
		BodyDef def = new BodyDef();
		def.type = BodyType.StaticBody;
		def.position.set(x+25, y+25);
		wallBody = world.createBody(def);
		PolygonShape rectShape = new PolygonShape();
		rectShape.setAsBox(25, 25);
		FixtureDef wallFixture = new FixtureDef();
		wallFixture.shape = rectShape;
		wallBody.createFixture(wallFixture);
		
		return wallBody;
	}
	
	public static void addMessage(String msg) {
		if(messageHistory.size >= maxMessagesSaved) {
			//Remove the oldest message which is at index zero, put the new message at the end of the array
			for(int i = 1; i < messageHistory.size; i++) {
				messageHistory.set(i-1, messageHistory.get(i));
			}
			messageHistory.set(maxMessagesSaved-1, msg);
		}
		else {
			messageHistory.add(msg);
		}
	}
	
	public Array<String> getMessageHistory() {
		return messageHistory;
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public World getWorld() {
		return world;
	}
	
	public HeadsUpDisplay getHUD() {
		return headsUpDisplay;
	}
	
	public Array<Tile> getTiles() {
		return tiles;
	}
	
	public Array<MoveableEntity> getEntities() {
		return entities;
	}
	
	public Array<Item> getItems() {
		return items;
	}
}
