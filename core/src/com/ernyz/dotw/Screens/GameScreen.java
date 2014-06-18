package com.ernyz.dotw.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.ernyz.dotw.DOTW;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.View.InputView;
import com.ernyz.dotw.View.WorldRenderer;

/**
 * Main screen of the game. Creates GameWorld, WorldRenderer and sets InputView.
 * 
 * @author ernyz
 */
public final class GameScreen implements Screen {
	private DOTW game;
	private String playerName;
	private GameWorld gameWorld;
	private WorldRenderer worldRenderer;

	public GameScreen(DOTW game, String playerName) {
		this.game = game;
		this.playerName = playerName;
		gameWorld = new GameWorld(game, playerName);
		worldRenderer = new WorldRenderer(gameWorld);
		
		InputMultiplexer multiplexer = new InputMultiplexer();
		//Stage should be added first, so it can handle input without allowing to do it for other input processors
		multiplexer.addProcessor(gameWorld.getHUD().getStage());
		multiplexer.addProcessor(new InputView(gameWorld, worldRenderer));
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void render(float delta) {
		gameWorld.update();
		worldRenderer.render();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
		
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
		//TODO Dont know whats wrong if a call these:
		//gameWorld.dispose();
		//worldRenderer.dispose();
		//game.dispose();
	}

}
