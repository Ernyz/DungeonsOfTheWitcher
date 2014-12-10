package com.ernyz.dotw;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ernyz.dotw.Screens.MainMenuScreen;

public class DOTW extends Game {
	public static final String VERSION = "0.0.6";
	private SpriteBatch batch;
	
	@Override
	public void create() {
//		Gdx.graphics.setDisplayMode(Gdx.graphics.getDesktopDisplayMode());
		batch = new SpriteBatch();
		setScreen(new MainMenuScreen(this, batch));
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {
		/*if(Gdx.input.isKeyPressed(Keys.ESCAPE)) {
			Gdx.app.exit();
		}*/
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
