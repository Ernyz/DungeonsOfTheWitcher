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
	
	private boolean debug = true;  //If true, shape renderer will draw bounding boxes of various things.
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
	
	public WorldRenderer(GameWorld gameWorld) {
		this.gameWorld = gameWorld;

		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		viewportMultiplier = viewportMultiplierDefaultValue;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, width*viewportMultiplier, height*viewportMultiplier);
		camera.update();
		
		batch = new SpriteBatch();
		batch.setProjectionMatrix(camera.combined);
		
		skeletonRenderer = new SkeletonRenderer();
		skeletonRenderer.setPremultipliedAlpha(false);
		skeletonRendererDebug = new SkeletonRendererDebug();
		
		/*Box2d stuff for lights*/
		//Create renderer and light
		debugRenderer = new Box2DDebugRenderer(false, false, false, false, false, false);
		rayHandler = new RayHandler(gameWorld.getWorld());
		rayHandler.setCombinedMatrix(camera.combined);
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
		camera.position.set(player.getPosition().x/* + gameWorld.getHUD().getBgTexture().getWidth()/2*/, player.getPosition().y/* - gameWorld.getHUD().getOutputBGTexture().getHeight()/2*/, 0);
		camera.update();
		
		//Draw stuff that is invisible unless lit
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		//Begin from tiles
		Array<Tile> tiles = player.getSurroundingTiles();
		for(int i = 0; i < tiles.size; i++) {
			batch.draw(tiles.get(i).getTexture(), tiles.get(i).getPosition().x, tiles.get(i).getPosition().y);
		}
		
		//Then items
		Array<Item> items = gameWorld.getItems();
		for(int i = 0; i < items.size; i++) {
			if(!items.get(i).getIsInInventory()) {
				batch.draw(items.get(i).getTexture(), items.get(i).getX(), items.get(i).getY());
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
		//batch.draw(player.getTexture(), player.getPosition().x, player.getPosition().y, player.getWidth()/2, player.getHeight()/2, player.getWidth(), player.getHeight(), 1, 1, player.getRotation(), 0, 0, player.getTexture().getWidth(), player.getTexture().getHeight(), false, false);
		//######################
		player.getSkeleton().updateWorldTransform();
		player.getSkeleton().setPosition(player.getPosition().x, player.getPosition().y);  //FIXME: I dont like this being here.
		player.getSkeleton().getRootBone().setRotation(player.getRotation());
		player.getSkeleton().update(Gdx.graphics.getDeltaTime());
		skeletonRenderer.draw(batch, player.getSkeleton());
		//skeletonRendererDebug.draw(player.getSkeleton());
		//######################
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
			/*for(int i = 0; i < entities.size; i++) {
				sr.setColor(Color.RED);
				//AttackBounds
				for(int j = 0; j < entities.get(i).getAttacks().size; j++) {
					sr.polygon(entities.get(i).getAttacks().get(j).getBounds().getTransformedVertices());
				}
				//Attack lines
				for(int j = 0; j < entities.get(i).getAttacks().size; j++) {
					if(entities.get(i).getAttacks().get(j).getPath().size >= 2) {
						for(int k = 0; k < entities.get(i).getAttacks().get(j).getPath().size-1; k++) {
							Vector2 v0 = entities.get(i).getAttacks().get(j).getPath().get(k);
							Vector2 v1 = entities.get(i).getAttacks().get(j).getPath().get(k+1);
							if(v0 != null && v1 != null)
								sr.line(v0, v1);
						}
					}
				}
			}*/
			
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
