package com.ernyz.dotw.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ernyz.dotw.DOTW;

/**
 * Screen which is shown right after the splash screen.
 * 
 * @author Ernyz
 */
public class MainMenuScreen implements Screen {
	
	private DOTW game;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	
	/*//Stuff needed for box2d lighting
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private RayHandler rayHandler;
	private PointLight crystalLight;*/
	
	//Scene2d variables
	private Stage stage;
	private Skin skin;
	private Table table;
	
	private Texture bgTexture;
	
	private TextButton loadCharacterBtn;
	private TextButton newCharacterBtn;
	private TextButton deleteCharacterBtn;
	private TextButton exitBtn;
	private Label label;
	
	public MainMenuScreen(DOTW game, SpriteBatch batch) {
		this.game = game;
		this.batch = batch;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(bgTexture, 0, 0);
		batch.end();
		
		stage.act(delta);
		stage.draw();
		//Table.drawDebug(stage);
		
		/*//Deal with light stuff
		debugRenderer.render(world, camera.combined);
		rayHandler.setCombinedMatrix(camera.combined);
		rayHandler.updateAndRender();*/
	}

	@Override
	public void resize(int width, int height) {
		//if(stage == null) stage = new Stage(width, height, true);
		ScreenViewport viewport = new ScreenViewport();
		//viewport.setWorldWidth(width);
		//viewport.setWorldWidth(height);
		if(stage == null) stage = new Stage(viewport);
		Gdx.input.setInputProcessor(stage);
		
		table = new Table(skin);
		table.setFillParent(true);
		table.debug();
		stage.addActor(table);
		
		table.row().colspan(2);
		
		label = new Label("Dungeons of the Witcher", skin);
		table.add(label);
		
		table.row();
		
		//Load character button
		loadCharacterBtn = new TextButton("Load character", skin);
		loadCharacterBtn.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new CharacterSelectionScreen(game, batch));
			}
		});
		table.add(loadCharacterBtn);
		
		//New character button
		newCharacterBtn = new TextButton("New character", skin);
		newCharacterBtn.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new CharacterCreationScreen(game, batch));
			}
		});
		table.add(newCharacterBtn);
		
		table.row().colspan(2);
		
		deleteCharacterBtn = new TextButton("Delete character", skin);
		deleteCharacterBtn.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new CharacterDeletionScreen(game, batch));
			}
		});
		table.add(deleteCharacterBtn);
		
		table.row().colspan(2);
		
		exitBtn = new TextButton("Exit", skin);
		exitBtn.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				Gdx.app.exit();
			}
		});
		table.add(exitBtn);
	}

	@Override
	public void show() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();
		
		//batch = new SpriteBatch();
		//skin = new Skin(Gdx.files.internal("data/GUI/basic/uiskin.json"));
		skin = new Skin(Gdx.files.internal("data/GUI/menu/packed/customuiskin.json"));
		
		bgTexture = new Texture("data/GUI/MainMenuBackground.png");
		
		/*//Initialise box2d stuff
		world = new World(new Vector2(0, 0), false);
		debugRenderer = new Box2DDebugRenderer(false, false, false, false, false, false);
		rayHandler = new RayHandler(world);
		rayHandler.setCombinedMatrix(camera.combined);
		//crystalLight = new PointLight(rayHandler, 360, Color.CYAN, 1200, 0, 0);
		crystalLight = new PointLight(rayHandler, 30, Color.BLACK, 800, 500, 650);
		//crystalLight = new PointLight(rayHandler, 360, Color.CYAN, 600, 1000, 100);
		//crystalLight = new PointLight(rayHandler, 360, Color.PINK, 200, 0, 650);*/
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
		skin.dispose();
		//batch.dispose();
		stage.dispose();
	}

}
