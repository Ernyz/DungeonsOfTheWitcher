package com.ernyz.dotw.Screens;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL10;
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
import com.ernyz.dotw.DOTW;

public class CharacterSelectionScreen implements Screen {
	
	private DOTW game;
	
	private SpriteBatch batch;
	private Texture bgTexture;
	
	private File file;
	private FileHandle fileHandle;
	
	//Scene2d variables for GUI
	private Stage stage;
	private TextureAtlas atlas;
	private Skin skin;
	private BitmapFont serpentis;
	private TextButton button;
	private TextButtonStyle textButtonStyle;
	
	public CharacterSelectionScreen(DOTW game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		
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
		if(stage == null)
			stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		//Set style for buttons
		textButtonStyle = new TextButtonStyle();
		textButtonStyle.up = skin.getDrawable("PlayBtn");
		textButtonStyle.font = serpentis;
		//Create as many buttons, as there are save games
		for(int i = 0; i < fileHandle.list().length; i++) {
			final String playerName = fileHandle.list()[i].name();
			button = new TextButton(fileHandle.list()[i].name(), textButtonStyle);
			button.setX(100);
			button.setY(i*button.getHeight() + 450 + 20*i);
			button.addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					return true;
				}
				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
					game.setScreen(new GameScreen(game, playerName));
				}
			});
			stage.addActor(button);
		}
		//Finally create "Back" button
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
		
		file = new File("save");
		fileHandle = new FileHandle(file);
		
		atlas = new TextureAtlas("data/Button.atlas");
		skin = new Skin();
		skin.addRegions(atlas);
		
		serpentis = new BitmapFont(Gdx.files.internal("data/fonts/Serpentis.fnt"), false);
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
		stage.dispose();
		skin.dispose();
		serpentis.dispose();
	}

}
