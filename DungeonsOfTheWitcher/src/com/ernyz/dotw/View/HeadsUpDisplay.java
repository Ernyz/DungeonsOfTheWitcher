package com.ernyz.dotw.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ernyz.dotw.Model.GameWorld;

public class HeadsUpDisplay {
	
	private GameWorld gameWorld;
	private SpriteBatch batch;
	private int width;
	private int height;
	
	private int messagesShown;  //How many messages are shown in message window at a time
	
	//scene2d variables
	private Stage stage;
	private Skin skin;
	private LabelStyle ls;
	private BitmapFont serpentis;
	private Label healthLabel;
	private Label outputLabel;
	private Texture bgTexture, outputBGTexture;
	private Texture leftBorderTexture, topBorderTexture, rightBorderTexture, bottomBorderTexture;
	private Image bgImage, outputBGImage;
	private Image leftBorderImage, topBorderImage, rightBorderImage, bottomBorderImage;
	
	//TODO temp texture for testing
	private Texture hudTexture;
	private Image hudImage;
	
	public HeadsUpDisplay(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
		
		batch = new SpriteBatch();
		
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		messagesShown = 5;
		
		//Set textures
		bgTexture = new Texture(Gdx.files.internal("data/HUD/HUD_BG.png"));
		outputBGTexture = new Texture(Gdx.files.internal("data/HUD/HUD_Output_BG.png"));
		leftBorderTexture = new Texture(Gdx.files.internal("data/HUD/HUD_Left_Border.png"));
		topBorderTexture = new Texture(Gdx.files.internal("data/HUD/HUD_Top_Border.png"));
		rightBorderTexture = new Texture(Gdx.files.internal("data/HUD/HUD_Right_Border.png"));
		bottomBorderTexture = new Texture(Gdx.files.internal("data/HUD/HUD_Bottom_Border.png"));
		hudTexture = new Texture(Gdx.files.internal("data/HUD/wholeHUD.png"));
		hudImage = new Image(hudTexture);
		
		//Initialize scene2d variables
		stage = new Stage(width, height, true);
		skin = new Skin();  //Might wanna use the other constructor with TextureAtlas
		//Set images
		bgImage = new Image(bgTexture);
		bgImage.setX(Gdx.graphics.getWidth() - bgImage.getWidth());
		outputBGImage = new Image(outputBGTexture);
		leftBorderImage = new Image(leftBorderTexture);
		leftBorderImage.setY(outputBGImage.getHeight());
		topBorderImage = new Image(topBorderTexture);
		topBorderImage.setY(height - topBorderImage.getHeight());
		rightBorderImage = new Image(rightBorderTexture);
		rightBorderImage.setY(outputBGImage.getHeight());
		rightBorderImage.setX(width-bgImage.getWidth() - rightBorderImage.getWidth());
		bottomBorderImage = new Image(bottomBorderTexture);
		bottomBorderImage.setY(outputBGImage.getHeight());
		
		serpentis = new BitmapFont(Gdx.files.internal("data/fonts/Serpentis.fnt"), false);
		ls = new LabelStyle(serpentis, Color.WHITE);
		healthLabel = new Label("Health: ", ls);  //create health label
		healthLabel.setX(bgImage.getX());
		//healthLabel.setY(bgImage.getHeight() - healthLabel.getHeight());
		healthLabel.setY(250);
		outputLabel = new Label("", ls);  //Create output label
		outputLabel.setWidth(width);
		outputLabel.setHeight(96);
		outputLabel.setX(0);
		outputLabel.setY(48);  //haaard coded!!!!!96
		
		stage.addActor(bgImage);
		stage.addActor(outputBGImage);
		stage.addActor(leftBorderImage);
		stage.addActor(topBorderImage);
		stage.addActor(rightBorderImage);
		stage.addActor(bottomBorderImage);
		stage.addActor(healthLabel);
		stage.addActor(outputLabel);
		
		//TODO temp
		stage.addActor(hudImage);
	}
	
	public void updateAndRender() {
		//Update HUD
		healthLabel.setText("Health: " + (int)gameWorld.getPlayer().getHealth());
		//Update output text
		//Set its text
		outputLabel.setText("");
		 if(gameWorld.getMessageHistory().size >= messagesShown) {
			 for(int i = gameWorld.getMessageHistory().size-messagesShown; i < gameWorld.getMessageHistory().size; i++) {
				 outputLabel.setText(outputLabel.getText()+"\n"+gameWorld.getMessageHistory().get(i));
			 }
		 }
		 else {
			 for(int i = 0; i < gameWorld.getMessageHistory().size; i++) {
				 outputLabel.setText(outputLabel.getText()+"\n"+gameWorld.getMessageHistory().get(i));
			 }
		 }
		 
		//Draw HUD
		stage.act();
		batch.begin();
		stage.draw();
		batch.end();
	}
	
	public void dispose() {
		batch.dispose();
		stage.dispose();
		skin.dispose();
		serpentis.dispose();
		bgTexture.dispose();
		outputBGTexture.dispose();
	}

	public Texture getBgTexture() {
		return bgTexture;
	}

	public Texture getOutputBGTexture() {
		return outputBGTexture;
	}
	
}
