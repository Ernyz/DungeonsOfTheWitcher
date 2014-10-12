package com.ernyz.dotw.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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

public class LostGameScreen implements Screen {
	
	private DOTW game;
	private SpriteBatch batch;
	private Texture bgTexture;
	
	private Stage stage;
	private Skin skin;
	
	private Table table;
	private Label label;
	private TextButton returnToMainMenuBtn;

	public LostGameScreen(DOTW game) {
		this.game = game;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		batch.begin();
		batch.draw(bgTexture, 0, 0);
		batch.end();
		
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		table = new Table(skin);
		table.setFillParent(true);
		stage.addActor(table);
		
		table.row();
		
		label = new Label("You have lost!..", skin);
		table.add(label);
		
		table.row();
		
		returnToMainMenuBtn = new TextButton("Return to main menu", skin);
		returnToMainMenuBtn.addListener(new InputListener() {
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new MainMenuScreen(game, batch));
			}
		});
		table.add(returnToMainMenuBtn);
	}

	@Override
	public void show() {
		batch = new SpriteBatch();
		bgTexture = new Texture("data/GUI/MainMenuBackground.png");
		
		stage = new Stage();
		skin = new Skin(Gdx.files.internal("data/GUI/menu/packed/customuiskin.json"));
		
		Gdx.input.setInputProcessor(stage);
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
