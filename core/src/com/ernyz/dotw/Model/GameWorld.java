package com.ernyz.dotw.Model;

import java.util.HashMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.DOTW;
import com.ernyz.dotw.Combat.BasicAttack;
import com.ernyz.dotw.Combat.BasicAttack.StateEnum;
import com.ernyz.dotw.Model.Items.Item;
import com.ernyz.dotw.Model.Tiles.Tile;
import com.ernyz.dotw.Screens.LostGameScreen;
import com.ernyz.dotw.Utils.WindowManager;
import com.ernyz.dotw.View.FloatingText;
import com.ernyz.dotw.View.HeadsUpDisplay;
import com.ernyz.dotw.View.LoadGame;
import com.ernyz.dotw.Windows.CustomWindow;

/**
 * Class in which all the game objects are held.
 * 
 * @author Ernyz
 */
public final class GameWorld {
	
	private enum GameStateEnum {
		LOADING, PLAYING, PAUSED, FINISHED
	}
	private GameStateEnum gameState;
	
	private DOTW game;
	@SuppressWarnings("unused")
	private String playerName;
	private HeadsUpDisplay headsUpDisplay;
	
	//Messages
	private static Array<String> messageHistory;  //Will contain all game messages
	private static int maxMessagesSaved;  //Will denote number of maximum number of messages saved
	
	private Player player;
	private Array<Item> items;
	private Array<MoveableEntity> entities;
	private Array<Tile> tiles;
	
	private CollisionContext collisionContext;
	
	//FIXME: temporarily public
	public Array<BasicAttack> basicAttacks = new Array<BasicAttack>();
	private static Array<FloatingText> floatingText = new Array<FloatingText>();
	
	//Manager class to manage an array of windows like inventory window, character window and etc.
	public WindowManager windowManager;
	private HashMap<String, CustomWindow> windows;
	
	//Box2d variables (lighting variables are in renderer class)
	private static World world;
	//private static Body wallBody;
	
	public GameWorld(DOTW game, SpriteBatch batch, String playerName) {
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
		long timer = System.currentTimeMillis();
			LoadGame l = new LoadGame(playerName, this);
			tiles = l.laodTiles("level0");
			items = l.loadItems();
			player = l.loadPlayer();
			entities = l.loadEntities();
			entities.add(player);  //Player is an entity too, so add it
		timer = System.currentTimeMillis()-timer;
		//System.out.println("World loaded in: " + timer);
		
		collisionContext = new CollisionContext(entities, tiles, basicAttacks);

		//Initialise window related stuff
		windowManager = new WindowManager(this, batch);
		windows = new HashMap<String, CustomWindow>();
		
		//Set game state to PLAYING, because the game has finished loading
		gameState = GameStateEnum.PLAYING;
	}
	
	public void update(float delta) {
		//Update everything
 		if(gameState == GameStateEnum.PLAYING) {
 			if(player.getIsDead()) {
 				game.setScreen(new LostGameScreen(game));
 				gameState = GameStateEnum.FINISHED;
 			}
 			
 			//Update entities
			for(MoveableEntity entity : entities) {
				if(entity.getIsDead()) {
					entity.dispose();
					entities.removeValue(entity, false);
				} else {
					entity.update(delta);
				}
			}
			//Update basic attacks, dispose of finished ones
			for(BasicAttack ba : basicAttacks) {
				//if(!ba.getIsFinished()) {
				if(!ba.getState().equals(StateEnum.FINISHED)) {
					ba.update(delta);
				} else {
					basicAttacks.removeValue(ba, false);
				}
			}
			collisionContext.checkCollisions();
			//Update active windows
			for(String windowName : windows.keySet()) {
				if(windows.get(windowName).isVisible()) {
					windows.get(windowName).update(delta);
				}
			}
			//Update floating texts
			for(FloatingText t : floatingText) {
				t.update(delta);
				if(t.isFinished()) {
					floatingText.removeValue(t, false);
				}
			}
			//Update tiles
			for(Tile t : player.getSurroundingTiles()) {
				t.update(delta);
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
		def.position.set(x+25, y+25); //TODO: remove hardcoding
		Body wallBody = world.createBody(def);
		PolygonShape rectShape = new PolygonShape();
		rectShape.setAsBox(25, 25);
		FixtureDef wallFixture = new FixtureDef();
		wallFixture.shape = rectShape;
		wallBody.createFixture(wallFixture);
		
		return wallBody;
	}
	
	public Item getItemById(int itemId) {  //XXX: Same method is in ItemManager...
		Item item = null;
		for(Item i : items) {
			if(i.getId() == itemId) {
				item = i;
				break;
			}
		}
		return item;
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
	
	public static void addFloatingText(FloatingText t) {
		floatingText.add(t);
	}
	
	public static Array<FloatingText> getFloatingText() {
		return floatingText;
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

	public HashMap<String, CustomWindow> getWindows() {
		return windows;
	}

	public void setWindows(HashMap<String, CustomWindow> windows) {
		this.windows = windows;
	}
	
	public DOTW getGame() {
		return game;
	}

}
