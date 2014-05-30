package com.ernyz.dotw.Windows;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Screens.MainMenuScreen;
import com.ernyz.dotw.View.SaveGame;

public class InGameOptionsWindow extends CustomWindow {
	
	private GameWorld gameWorld;
	private Skin skin;
	private Table table;
	private TextButton resumeButton;
	private TextButton saveAndQuitButton;

	public InGameOptionsWindow(String title, Skin skin, GameWorld gameWorld) {
		super(title, skin);
		
		this.skin = skin;
		this.gameWorld = gameWorld;
		
		setUpTheWindow();
	}

	@Override
	public void update(float delta) {
		setUpTheWindow();
	}
	
	@Override
	protected void setUpTheWindow() {
		setMovable(false);
		clearChildren();
		
		resumeButton = new TextButton("Resume game", skin);
		resumeButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				 gameWorld.windowManager.toggleWindow("InGameOptions");
		        return true;
		    }
		});
		add(resumeButton);
		
		row();
		
		saveAndQuitButton = new TextButton("Save and quit to main menu", skin);
		saveAndQuitButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				SaveGame.save(gameWorld.getTiles(), gameWorld.getPlayer(), gameWorld.getEntities(), gameWorld.getItems());
				gameWorld.getGame().setScreen(new MainMenuScreen(gameWorld.getGame()));
		        return true;
		    }
		});
		add(saveAndQuitButton);
		
		pack();
		
		/*table = new Table(skin);
		add(table);
		
		table.row();
		
		resumeButton = new TextButton("Resume game", skin);
		resumeButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				 gameWorld.windowManager.toggleWindow("InGameOptions");
		        return true;
		    }
		});
		table.add(resumeButton);
		
		table.row();
		
		saveAndQuitButton = new TextButton("Save and quit to main menu", skin);
		saveAndQuitButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				SaveGame.save(gameWorld.getTiles(), gameWorld.getPlayer(), gameWorld.getEntities(), gameWorld.getItems());
				gameWorld.getGame().setScreen(new MainMenuScreen(gameWorld.getGame()));
		        return true;
		    }
		});
		table.add(saveAndQuitButton);
		*/
	}
}