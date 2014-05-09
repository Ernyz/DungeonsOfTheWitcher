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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.ernyz.dotw.DOTW;

public class CharacterDeletionScreen implements Screen {
	
	private DOTW game;
	private SpriteBatch batch;
	
	private File file;
	private FileHandle fileHandle;
	
	private Texture bgTexture;
	
	private Stage stage;
	private Table table;
	private DeletionDialog deletionDialog;
	private Skin skin;
	
	private TextButton backButton;
	private TextButton button;

	public CharacterDeletionScreen(DOTW game) {
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
	}

	@Override
	public void resize(int width, int height) {
		if(stage == null)
			stage = new Stage();
		Gdx.input.setInputProcessor(stage);
		
		table = new Table(skin);
		table.setFillParent(true);
		stage.addActor(table);
		
		//Start of by listing all save files, adding deletion listeners to them
		for(int i = 0; i < fileHandle.list().length; i++) {
			String name = fileHandle.list()[i].name();
			button = new TextButton(name, skin);
			button.addListener(new InputListener() {
				public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
					return true;
				}
				public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
					TextButton tmp = (TextButton) event.getListenerActor();
					showDeletionDialog(tmp.getText().toString());
				}
			});
			table.add(button);
			table.row();
		}
		
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
		//If user clicks save file, dialog pops up asking to type "yes" to confirm deletion
		//If yes is typed, delete save file and close the dialog
		//If nothing or anything else is typed, just close the dialog
		//Crete new dialog with ok button to indicate whether deletion was successful
	}
	
	//TODO move this class down a little
	private class DeletionDialog extends Dialog {
		
		String saveFileName;
		TextField tf;
		
		public DeletionDialog(String title, Skin skin, String saveFileName) {
			super(title, skin);
			this.saveFileName = saveFileName;
			
			getContentTable().row().colspan(2);
			text("Really delete " + saveFileName + "?");
			getContentTable().row().colspan(2);
			text("(Type \"yes\" to confirm)");
			
			getContentTable().row().colspan(2);
			tf = new TextField("", skin);
			getButtonTable().add(tf);
			button("Delete", tf.getText().toLowerCase());
			getButtonTable().row().colspan(2);
			button("Cancel", tf.getText().toLowerCase());
		}
		
		@Override
		protected void result(Object object) {
			if(tf.getText().toLowerCase().equals("yes")) {
				//Delete char
				File f = new File("save/"+saveFileName);
				FileHandle fh = new FileHandle(f);
				fh.deleteDirectory();
				//fh.delete();
			} else {
				
			}
		}
	}
	
	private void showDeletionDialog(final String name) {
		deletionDialog = new DeletionDialog("Delete character: "+name, skin, name);
		deletionDialog.show(stage);
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		
		file = new File("save");
		fileHandle = new FileHandle(file);
		
		bgTexture = new Texture("data/GUI/MainMenuBackground.png");
		
		skin = new Skin(Gdx.files.internal("data/GUI/basic/uiskin.json"));
	}

	@Override
	public void hide() {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

}
