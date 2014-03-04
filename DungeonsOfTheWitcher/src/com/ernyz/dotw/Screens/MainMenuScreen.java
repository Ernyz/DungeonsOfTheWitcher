package com.ernyz.dotw.Screens;

import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.ernyz.dotw.DOTW;

public class MainMenuScreen implements Screen {
	
	private DOTW game;
	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Texture bgTexture;
	private Texture parchmentTexture;
	
	//Stuff needed for box2d lighting
	private World world;
	private Box2DDebugRenderer debugRenderer;
	private RayHandler rayHandler;
	private PointLight crystalLight;
	
	//Scene2d variables
	private Stage stage;
	private Skin skin;
	private TextureAtlas atlas;
	private BitmapFont ringbearerFont;
	private TextButton loadCharacterBtn;
	private TextButton newCharacterBtn;
	private Label label;
	
	public MainMenuScreen(DOTW game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(bgTexture, 0, 0);  //Draw wooden BG
		/*batch.draw(bgTexture, 300, 0);  //Draw wooden BG
		batch.draw(bgTexture, 600, 0);  //Draw wooden BG
		batch.draw(bgTexture, 900, 0);  //Draw wooden BG*/
		batch.draw(parchmentTexture, 290, 0);
		batch.end();
		
		stage.act(delta);
		batch.begin();
		stage.draw();
		batch.end();
		
		//Deal with light stuff
		debugRenderer.render(world, camera.combined);
		rayHandler.setCombinedMatrix(camera.combined);
		rayHandler.updateAndRender();
	}

	@Override
	public void resize(int width, int height) {
		if(stage == null)
			stage = new Stage(width, height, true);
		Gdx.input.setInputProcessor(stage);
		
		//set simple menu button style
		TextButtonStyle btnStyle = new TextButtonStyle();
		btnStyle.up = skin.getDrawable("PlayBtn");
		btnStyle.font = ringbearerFont;
		//set the play button up
		loadCharacterBtn = new TextButton("Load char", btnStyle);
		loadCharacterBtn.setWidth(120);
		loadCharacterBtn.setHeight(60);
		loadCharacterBtn.setX(50);
		loadCharacterBtn.setY(550);
		loadCharacterBtn.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new CharacterSelectionScreen(game));
			}
		});
		stage.addActor(loadCharacterBtn);
		
		//set the new character button up
		newCharacterBtn = new TextButton("New char", btnStyle);
		newCharacterBtn.setWidth(120);
		newCharacterBtn.setHeight(60);
		newCharacterBtn.setX(50);
		newCharacterBtn.setY(480);
		newCharacterBtn.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new CharacterCreationScreen(game));
			}
		});
		stage.addActor(newCharacterBtn);
		
		LabelStyle ls = new LabelStyle(ringbearerFont, Color.WHITE);
		label = new Label("Dungeons of the Witcher", ls);
		label.setX(Gdx.graphics.getWidth()/2 - label.getWidth()/2);
		label.setY(Gdx.graphics.getHeight()/2 + 200);
		label.setWidth(300);
		label.setAlignment(Align.center);
		
		stage.addActor(label);
	}

	@Override
	public void show() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera.update();
		
		batch = new SpriteBatch();
		atlas = new TextureAtlas("data/Button.atlas");
		skin = new Skin();
		skin.addRegions(atlas);
		bgTexture = new Texture(Gdx.files.internal("data/GUI/StoneTexture.png"));
		parchmentTexture = new Texture(Gdx.files.internal("data/GUI/Parchment.png"));
		ringbearerFont = new BitmapFont(Gdx.files.internal("data/fonts/ringbearerFont.fnt"), false);
		
		//init box2d stuff
		world = new World(new Vector2(0, 0), false);
		debugRenderer = new Box2DDebugRenderer(false, false, false, false, false, false);
		rayHandler = new RayHandler(world);
		rayHandler.setCombinedMatrix(camera.combined);
		//crystalLight = new PointLight(rayHandler, 360, Color.CYAN, 1200, 0, 0);
		crystalLight = new PointLight(rayHandler, 30, Color.BLACK, 1000, 500, 650);
		//crystalLight = new PointLight(rayHandler, 360, Color.CYAN, 600, 1000, 100);
		//crystalLight = new PointLight(rayHandler, 360, Color.PINK, 200, 0, 650);
	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		atlas.dispose();
		skin.dispose();
		batch.dispose();
		stage.dispose();
		ringbearerFont.dispose();
		bgTexture.dispose();
	}

}
