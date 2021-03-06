package com.ernyz.dotw.View;

import box2dLight.ConeLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Combat.BasicAttack;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Model.MoveableEntity;
import com.ernyz.dotw.Model.Player;
import com.ernyz.dotw.Model.Items.Item;
import com.ernyz.dotw.Model.Tiles.Tile;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.SkeletonRendererDebug;

/**
 * Renders whole game world and HUD.
 * 
 * @author ernyz
 */
public final class WorldRenderer {
	
	private boolean debug = false;  //If true, shape renderer will draw bounding boxes of various things.
	private ShapeRenderer sr = new ShapeRenderer();  //Useful for debugging.
	
	SkeletonRenderer skeletonRenderer;
	SkeletonRendererDebug skeletonRendererDebug;
	
	private GameWorld gameWorld;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private float width;
	private float height;
	//Stuff used for zooming in/out
	private final Float viewportMultiplierDefaultValue = 1f;
	private final Float viewportMultiplierMinValue = 0.1f;
	private final Float viewportMultiplierMaxValue = 2.0f;
	private Float viewportMultiplier;
	
	private Player player;
	
	//Stuff needed for FOV
	private Box2DDebugRenderer debugRenderer;
	private RayHandler rayHandler;
	private ConeLight playerLight;
	
	public WorldRenderer(GameWorld gameWorld, SpriteBatch batch) {
		this.gameWorld = gameWorld;
		this.batch = batch;

		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		viewportMultiplier = viewportMultiplierDefaultValue;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, width*viewportMultiplier, height*viewportMultiplier);
		camera.update();
		
		//batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		
		skeletonRenderer = new SkeletonRenderer();
		skeletonRenderer.setPremultipliedAlpha(false);
		skeletonRendererDebug = new SkeletonRendererDebug();
		
		/*Box2d stuff for lights*/
		//Create renderer and light
		debugRenderer = new Box2DDebugRenderer(false, false, false, false, false, false);
		rayHandler = new RayHandler(gameWorld.getWorld());
		rayHandler.setCombinedMatrix(camera.combined);
		rayHandler.setBlurNum(0);
		playerLight = new ConeLight(rayHandler, 1000, Color.BLACK, 600, 0, 0, 0, 65);
		//Good for testing: 
		if(debug)
			playerLight = new ConeLight(rayHandler, 1000, Color.BLACK, 600, 0, 0, 0, 360);
	}
	
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//Get player and other entities
		player = gameWorld.getPlayer();
		
		//Update camera
		camera.setToOrtho(false, width*viewportMultiplier, height*viewportMultiplier);
		camera.position.set(player.getPosition().x, player.getPosition().y, 0);
		camera.update();
		
		//Draw stuff that is invisible unless lit
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		//Begin from tiles
		Array<Tile> tiles = player.getSurroundingTiles();
//		for(int i = 0; i < tiles.size; i++) {
//			batch.draw(tiles.get(i).getTexture(), tiles.get(i).getPosition().x, tiles.get(i).getPosition().y);
//		}
		for(int i = 0; i < tiles.size; i++) {
			batch.draw(tiles.get(i).getTextureRegion(), tiles.get(i).getPosition().x, tiles.get(i).getPosition().y);
		}
		
		//Then items
		Array<Item> items = gameWorld.getItems();
		for(int i = 0; i < items.size; i++) {
			if(!items.get(i).getIsInInventory()) {
				batch.draw(items.get(i).getIconTexture(), items.get(i).getX(), items.get(i).getY());
			}
		}
		
		//Then entities
		Array<MoveableEntity> entities = player.getSurroundingEntities();
		for(int i = 0; i < entities.size; i++) {
			entities.get(i).getSkeleton().updateWorldTransform();
			entities.get(i).getSkeleton().setPosition(entities.get(i).getPosition().x, entities.get(i).getPosition().y);  //FIXME: I dont like this being here.
			entities.get(i).getSkeleton().getRootBone().setRotation(entities.get(i).getRotation());
			entities.get(i).getSkeleton().update(Gdx.graphics.getDeltaTime());
			skeletonRenderer.draw(batch, entities.get(i).getSkeleton());
		}
		
		//Then attacks
		BasicAttack ba;  //FIXME: Improve.
		for(int i = 0; i < gameWorld.basicAttacks.size; i++) {
			ba = gameWorld.basicAttacks.get(i);
			batch.draw(ba.getTexture(), ba.getBounds().getX(), ba.getBounds().getY(), 0, 0, ba.getTextureWidth(), ba.getTextureHeight(), 1, 1, ba.getBounds().getRotation(), 0, 0, (int) ba.getTextureWidth(), (int) ba.getTextureHeight(), false, false);
		}
		batch.end();
		
		//Deal with light(FOV) stuff
		debugRenderer.render(gameWorld.getWorld(), camera.combined);
		playerLight.setDirection(player.getRotation());
		playerLight.setPosition(player.getPosition().x, player.getPosition().y);
		rayHandler.setCombinedMatrix(camera.combined);
		rayHandler.updateAndRender();
		
		//Draw stuff that is always visible
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
			player.getSkeleton().updateWorldTransform();
			player.getSkeleton().setPosition(player.getPosition().x, player.getPosition().y);  //FIXME: I dont like this being here.
			player.getSkeleton().getRootBone().setRotation(player.getRotation());
			player.getSkeleton().update(Gdx.graphics.getDeltaTime());
			skeletonRenderer.draw(batch, player.getSkeleton());
			//skeletonRendererDebug.draw(player.getSkeleton());
			
			for(FloatingText t : GameWorld.getFloatingText()) {
				t.getFont().draw(batch, t.getText(), t.getX(), t.getY());
			}
		batch.end();
		
		//Draw HUD
		gameWorld.getHUD().updateAndRender(batch);
		
		//ShapeRenderer, useful to see bounds
		if(debug) {
			sr.setProjectionMatrix(camera.combined);
			sr.begin(ShapeType.Line);
			
			//Entities
			for(int i = 0; i < entities.size; i++) {
				sr.setColor(Color.RED);
				sr.polygon(entities.get(i).getBounds().getTransformedVertices());
			}
			//Player
			sr.setColor(Color.CYAN);
			sr.polygon(player.getBounds().getTransformedVertices());
			//Walls
			for(int i = 0; i < tiles.size; i++) {
				sr.setColor(Color.GREEN);
				if(!tiles.get(i).getWalkable())
					sr.polygon(tiles.get(i).getBounds().getTransformedVertices());
			}
			//Attacks
			for(int i = 0; i < gameWorld.basicAttacks.size; i++) {
				sr.setColor(Color.RED);
				//AttackBounds
				sr.polygon(gameWorld.basicAttacks.get(i).getBounds().getTransformedVertices());
			}
			
			sr.end();
		}
	}
	
	public void dispose() {
		//batch.dispose();
		debugRenderer.dispose();
		rayHandler.dispose();
		sr.dispose();
	}
	
	public OrthographicCamera getCamera() {
		return camera;
	}

	public Float getViewportMultiplier() {
		return viewportMultiplier;
	}

	public void setViewportMultiplier(Float viewportMultiplier) {
		this.viewportMultiplier = viewportMultiplier;
		if(this.viewportMultiplier < viewportMultiplierMinValue) {
			this.viewportMultiplier = viewportMultiplierMinValue;
		} else if(this.viewportMultiplier > viewportMultiplierMaxValue) {
			this.viewportMultiplier = viewportMultiplierMaxValue;
		}
	}

}
