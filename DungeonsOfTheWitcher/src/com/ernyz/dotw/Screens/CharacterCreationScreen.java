package com.ernyz.dotw.Screens;

import java.io.File;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
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
	
	//Directory for player save games
	private String dir = "save";
	//GUI variables
	private Stage stage;
	private Table table;
	private Skin skin;
	//Text fields
	private TextField nameTextField;
	//Buttons
	private TextButton confirmButton;
	private TextButton backButton;
	
	public CharacterCreationScreen(DOTW game) {
		this.game = game;
	}
	
	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.end();
		
		stage.act();
		stage.draw();
		//Table.drawDebug(stage);
	}

	@Override
	public void resize(int width, int height) {
		if(stage == null) stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
		Gdx.input.setInputProcessor(stage);
		
		table = new Table(skin);
		table.setFillParent(true);
		table.debug();
		stage.addActor(table);
		
		table.row();
		
		//Name text field
		nameTextField = new TextField("Player", skin);
		table.add(nameTextField);
		
		//Confirm button
		confirmButton = new TextButton("Confirm", skin);
		confirmButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				//Create player folder and data
				dir = dir + "/" + nameTextField.getText() + "/";
				boolean success = (new File(dir)).mkdirs();
				if(success) {
					//All data in the text fields should be put into some object here, and only then sent to the generator.
					@SuppressWarnings("unused")
					WorldGenerator wg = new WorldGenerator(nameTextField.getText());
					
				}
				//Change screen to character selection screen
				game.setScreen(new CharacterSelectionScreen(game));
			}
		});
		table.add(confirmButton);
		
		table.row().colspan(2);
		
		backButton = new TextButton("Back", skin);
		backButton.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new MainMenuScreen(game));
			}
		});
		table.add(backButton);
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		skin = new Skin(Gdx.files.internal("data/GUI/basic/uiskin.json"));
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
