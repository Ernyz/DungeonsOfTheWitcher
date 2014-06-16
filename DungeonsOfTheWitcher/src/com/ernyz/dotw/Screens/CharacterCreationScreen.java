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
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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
	
	private Texture bgTexture;
	
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
	//Labels
	private Label screenTitle;
	
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
		
		table.row().colspan(2);
		screenTitle = new Label("Set up your character", skin);
		table.add(screenTitle);
		
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
				
				if(saveFileExistsWithName(nameTextField.getText())) {
					//Create dialog which asks whether to overwrite or to enter new name
					CharacterOverwritingDialog d = new CharacterOverwritingDialog("Overwrite save file?");
					d.show(stage);
				} else {
					createPlayerSaveFile();
				}
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
	
	private void createPlayerSaveFile() {
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

	@Override
	public void show() {
		batch = new SpriteBatch();
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
	
	private boolean saveFileExistsWithName(String name) {
		File file = new File("save");
		FileHandle fileHandle = new FileHandle(file);
		
		for(int i = 0; i < fileHandle.list().length; i++) {
			if(name.equals(fileHandle.list()[i].name())) {
				return true;
			}
		}
		
		return false;
	}
	
	private class CharacterOverwritingDialog extends Dialog {
		
		public CharacterOverwritingDialog(String title) {
			super(title, skin);
			
			text("Character with the name entered already exists.\nOverwrite the existing save file or modify the name entered?");
			button("Overwrite", "overwrite");
			button("Modify name", "modify");
		}
		
		@Override
		protected void result(Object object) {
			if(object.toString().equals("overwrite")) {
				File f = new File("save/"+nameTextField.getText());
				FileHandle fh = new FileHandle(f);
				fh.deleteDirectory();
				createPlayerSaveFile();
			} else if(object.toString().equals("modify")) {
				nameTextField.selectAll();
			}
		}
		
	}

}
