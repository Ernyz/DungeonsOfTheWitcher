package com.ernyz.dotw.Screens;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
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
import com.ernyz.dotw.Generators.WorldGenerator;

/**
 * Screen in which player can create new character.
 * 
 * @author Ernyz
 */
public class CharacterCreationScreen implements Screen {
	
	private DOTW game;
	private SpriteBatch batch;
	private Texture bgTexture;
	
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
	private TextButton button;
	
	public CharacterCreationScreen(DOTW game) {
		this.game = game;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(bgTexture, 0, 0);
		batch.end();
		
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
		nameTextField = new TextField("Player", textFieldStyle);
		nameTextField.setX(0);
		nameTextField.setY(600);
		stage.addActor(nameTextField);
		
		textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.getDrawable("PlayBtn");
		textButtonStyle.font = ringbearerFont;
		button = new TextButton("Confirm", textButtonStyle);
		button.setX(30);
		button.setY(30);
		button.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				//Create player folder and data
				dir = dir + "/" + nameTextField.getText() + "/";
				boolean success = (new File(dir)).mkdirs();
				if(success) {
					//All data in the text fields should be put into some object here, and only then sent to the generator.
					//Main generator should be called here instead of these two.
					@SuppressWarnings("unused")
					WorldGenerator wg = new WorldGenerator(nameTextField.getText());
					
				}
				//Change screen to character selection screen
				game.setScreen(new CharacterSelectionScreen(game));
			}
		});
		stage.addActor(button);
		
		//Create "Back" button
		button = new TextButton("Back", textButtonStyle);
		button.setX(700);
		button.setY(30);
		button.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new MainMenuScreen(game));
			}
		});
		stage.addActor(button);
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		bgTexture = new Texture("data/GUI/StoneTexture.png");
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
		
	}

	@Override
	public void resume() {
		
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
