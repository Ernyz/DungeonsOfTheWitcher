package com.ernyz.dotw.Windows;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Screens.MainMenuScreen;
import com.ernyz.dotw.View.SaveGame;

public class InGameOptionsWindow extends CustomWindow {
	
	private GameWorld gameWorld;
	private Skin skin;

	public InGameOptionsWindow(String title, Skin skin, GameWorld gameWorld) {
		super(title, skin);
		
		this.gameWorld = gameWorld;
		this.skin = skin;
		
		setUpTheWindow();
	}

	@Override
	protected void setUpTheWindow() {
		this.setMovable(false);
		
		//Populate window
		TextButton resumeButton = new TextButton("Resume game", skin);
		resumeButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				 gameWorld.windowManager.toggleWindow("InGameOptions");
		        return true;
		    }
		});
		add(resumeButton);

		row();
		
		TextButton saveAndQuitButton = new TextButton("Save and quit to main menu", skin);
		//TextButton saveAndQuitButton = new TextButton("aaaaaa aaaaa", skin);
		saveAndQuitButton.addListener(new InputListener() {
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
				SaveGame.save(gameWorld.getTiles(), gameWorld.getPlayer(), gameWorld.getEntities(), gameWorld.getItems());
				gameWorld.getGame().setScreen(new MainMenuScreen(gameWorld.getGame()));
		        return true;
		    }
		});
		add(saveAndQuitButton);
		
		//debug();
		
		setWidth(280);
		
		//pack();
	}
	
	@Override
	public void update(float delta) {
		
	}

}
