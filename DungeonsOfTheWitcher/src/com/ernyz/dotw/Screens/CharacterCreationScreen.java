package com.ernyz.dotw.Screens;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.ernyz.dotw.DOTW;
import com.ernyz.dotw.Generators.DungeonGenerator;
import com.ernyz.dotw.Generators.PlayerGenerator;

public class CharacterCreationScreen implements Screen {
	
	private DOTW game;
	private SpriteBatch batch;
	
	//Generators
	private PlayerGenerator playerGenerator;
	private DungeonGenerator dungeonGenerator;
	//Fonts
	private BitmapFont ringbearerFont;
	//Directory for player save games
	private String dir = "save";
	//GUI variables
	private Stage stage;
	private Skin skin;
	private TextureAtlas atlas;
	//Text fields
	private TextFieldStyle textFieldStyle;
	private TextField nameTextField;
	//Buttons
	private TextButtonStyle textButtonStyle;
	private TextButton confirmBtn;
	
	public CharacterCreationScreen(DOTW game) {
		this.game = game;
		playerGenerator = new PlayerGenerator();
		dungeonGenerator = new DungeonGenerator();
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
		stage.act();
		batch.begin();
		stage.draw();
		batch.end();
	}

	@Override
	public void resize(int width, int height) {
		if(stage == null) stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		Gdx.input.setInputProcessor(stage);
		
		textFieldStyle = new TextFieldStyle();
		textFieldStyle.font = ringbearerFont;
		textFieldStyle.fontColor = Color.WHITE;
		nameTextField = new TextField("Ernyz", textFieldStyle);
		nameTextField.setX(0);
		nameTextField.setY(600);
		stage.addActor(nameTextField);
		
		textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.getDrawable("PlayBtn");
		textButtonStyle.font = ringbearerFont;
		confirmBtn = new TextButton("Confirm", textButtonStyle);
		confirmBtn.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				//Create player folder and data
				dir = dir + "/" + nameTextField.getText() + "/";
				boolean success = (new File(dir)).mkdirs();
				if(success) {
					playerGenerator.generatePlayer(nameTextField.getText());
					dungeonGenerator.generateDungeon(nameTextField.getText());
				}
				//Change screen to character selection screen
				game.setScreen(new CharacterSelectionScreen(game));
			}
		});
		stage.addActor(confirmBtn);
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		atlas = new TextureAtlas("data/Button.atlas");
		skin = new Skin();
		skin.addRegions(atlas);
		
		ringbearerFont = new BitmapFont(Gdx.files.internal("data/fonts/Serpentis.fnt"), false);
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
		batch.dispose();
		ringbearerFont.dispose();
		stage.dispose();
		skin.dispose();
	}

}
