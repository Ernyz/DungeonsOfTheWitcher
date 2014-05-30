package com.ernyz.dotw.Screens;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.ernyz.dotw.DOTW;

/**
 * Screen in which player can choose a save file of earlier created character.
 * 
 * @author Ernyz
 */
public class CharacterSelectionScreen implements Screen {
	
	private DOTW game;
	
	private SpriteBatch batch;
	
	private Texture bgTexture;
	
	private File file;
	private FileHandle fileHandle;
	
	//Scene2d variables for GUI
	private Stage stage;
	private Table table;
	private Skin skin;
	private Label screenTitle;
	private TextButton button;
	
	public CharacterSelectionScreen(DOTW game) {
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
		stage.draw();
		//Table.drawDebug(stage);
	}

	@Override
	public void resize(int width, int height) {
		if(stage == null)
			stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		table = new Table(skin);
		table.setFillParent(true);
		table.debug();
		stage.addActor(table);
		
		table.row();
		screenTitle = new Label("Choose a save file to load", skin);
		table.add(screenTitle);
		
		table.row();
		//Create as many buttons, as there are save games
		for(int i = 0; i < fileHandle.list().length; i++) {
			final String playerName = fileHandle.list()[i].name();
			button = new TextButton(fileHandle.list()[i].name(), skin);
			button.addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					return true;
				}
				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
					game.setScreen(new GameScreen(game, playerName));
				}
			});
			table.add(button);
			table.row();
		}
		
		//Finally create "Back" button
		button = new TextButton("Back", skin);
		button.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new MainMenuScreen(game));
			}
		});
		table.add(button);
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		
		file = new File("save");  //TODO: Make this static constant somewhere?..
		fileHandle = new FileHandle(file);
		
		skin = new Skin(Gdx.files.internal("data/GUI/menu/packed/customuiskin.json"));
		
		bgTexture = new Texture("data/GUI/MainMenuBackground.png");
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
		batch.dispose();
		stage.dispose();
		skin.dispose();
	}

}
