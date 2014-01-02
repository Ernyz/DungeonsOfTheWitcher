package com.ernyz.dotw;

import com.badlogic.gdx.Game;
import com.ernyz.dotw.Screens.MainMenuScreen;

public class DOTW extends Game {
	public static final String VERSION = "0.6.0";
	
	@Override
	public void create() {		
		setScreen(new MainMenuScreen(this));
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {		
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}
