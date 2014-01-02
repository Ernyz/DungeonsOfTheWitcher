package com.ernyz.dotw.View;

import box2dLight.ConeLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Model.MoveableEntity;
import com.ernyz.dotw.Model.Player;
import com.ernyz.dotw.Model.Tiles.Tile;

/**
 * Renders whole game world and HUD.
 * 
 * @author ernyz
 */
public final class WorldRenderer {
	
	private boolean debug = false;  //If true, shape renderer will draw bounding boxes.
	private ShapeRenderer sr = new ShapeRenderer();  //Useful for debugging
	
	private GameWorld gameWorld;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private float width;
	private float height;
	
	private Player player;
	
	//Stuff needed for FOV
	private Box2DDebugRenderer debugRenderer;
	private RayHandler rayHandler;
	private ConeLight playerLight;
	
	public WorldRenderer(GameWorld gameWorld) {
		this.gameWorld = gameWorld;

		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, width, height);
		camera.update();
		
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		
		/*box2d stuff for lights*/
		//Create renderer and light
		debugRenderer = new Box2DDebugRenderer(false, false, false, false, false, false);
		rayHandler = new RayHandler(gameWorld.getWorld());
		rayHandler.setCombinedMatrix(camera.combined);
		playerLight = new ConeLight(rayHandler, 1000, Color.BLACK, 600, 0, 0, 0, 65);
		//TODO good for testing playerLight = new ConeLight(rayHandler, 1000, Color.BLACK, 600, 0, 0, 0, 360);
	}
	
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		//Get player and other entities
		player = gameWorld.getPlayer();
		
		//Update camera
		camera.position.set(player.getPosition().x+player.getWidth()/2 + gameWorld.getHUD().getBgTexture().getWidth()/2, player.getPosition().y+player.getHeight()/2 - gameWorld.getHUD().getOutputBGTexture().getHeight()/2, 0);
		camera.update();
		
		//Draw stuff that is invisible unless lit
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		//Begin from tiles
		Array<Tile> tiles = player.getSurroundingTiles();
		for(int i = 0; i < tiles.size; i++) {
			batch.draw(tiles.get(i).getTexture(), tiles.get(i).getPosition().x, tiles.get(i).getPosition().y);
		}
		//Then entities
		Array<MoveableEntity> entities = player.getSurroundingEntities();
		for(int i = 0; i < entities.size; i++) {
			batch.draw(entities.get(i).getTexture(), entities.get(i).getPosition().x, entities.get(i).getPosition().y, entities.get(i).getWidth()/2, entities.get(i).getHeight()/2, entities.get(i).getWidth(), entities.get(i).getHeight(), 1, 1, entities.get(i).getRotation(), 0, 0, entities.get(i).getTexture().getWidth(), entities.get(i).getTexture().getHeight(), false, false);
		}
		batch.end();
		
		//Deal with light(FOV) stuff
		debugRenderer.render(gameWorld.getWorld(), camera.combined);
		playerLight.setDirection(player.getRotation());
		playerLight.setPosition(player.getPosition().x + player.getWidth()/2, player.getPosition().y + player.getHeight()/2);
		rayHandler.setCombinedMatrix(camera.combined);
		rayHandler.updateAndRender();
		
		//Draw stuff that is always visible
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		batch.draw(player.getTexture(), player.getPosition().x, player.getPosition().y, player.getWidth()/2, player.getHeight()/2, player.getWidth(), player.getHeight(), 1, 1, player.getRotation(), 0, 0, player.getTexture().getWidth(), player.getTexture().getHeight(), false, false);
		batch.end();
		
		//Draw HUD
		gameWorld.getHUD().updateAndRender();
		
		//ShapeRenderer, useful to see bounds
		if(debug) {
			sr.setProjectionMatrix(camera.combined);
			sr.begin(ShapeType.Line);
			sr.setColor(Color.CYAN);
			
			sr.circle(player.getBounds().x, player.getBounds().y, player.getBounds().radius);
			for(int i = 0; i < entities.size; i++) {
				sr.setColor(Color.RED);
				sr.circle(entities.get(i).getBounds().x, entities.get(i).getBounds().y, entities.get(i).getBounds().radius);
			}
			for(int i = 0; i < tiles.size; i++) {
				sr.setColor(Color.GREEN);
				if(!tiles.get(i).getWalkable())
					sr.rect(tiles.get(i).getBounds().x, tiles.get(i).getBounds().y, tiles.get(i).getWidth(), tiles.get(i).getHeight());
			}
			sr.end();
		}
	}
	
	public void dispose() {
		batch.dispose();
		debugRenderer.dispose();
		rayHandler.dispose();
		sr.dispose();
	}
	
	public OrthographicCamera getCamera() {
		return camera;
	}

}
