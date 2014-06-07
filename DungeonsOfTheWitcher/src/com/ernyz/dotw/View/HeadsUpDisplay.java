package com.ernyz.dotw.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.ernyz.dotw.Model.GameWorld;

/**
 * Holds all the resources/logic/etc. needed for HUD.
 * 
 * @author Ernyz
 */
public class HeadsUpDisplay {
	
	private GameWorld gameWorld;
	private int width;
	private int height;
	private final int hpBarMaxHeight = 26;  //Denotes maximum bar height
	private final int manaBarMaxHeight = 17;  //Denotes maximum bar height
	private final int staminaBarMaxHeight = 17;  //Denotes maximum bar height
	
	private int messagesShown;  //How many messages are shown in message window at a time
	
	//scene2d variables
	private Stage stage;
	private Skin skin;
	private Label outputLabel;
	//private Table table;
	
	private Image bars;  //FIXME: What a misleading name...come up with a better one.
	private Image healthBar, manaBar, staminaBar;
	
	public HeadsUpDisplay(GameWorld gameWorld) {
		this.gameWorld = gameWorld;
		
		width = Gdx.graphics.getWidth();
		height = Gdx.graphics.getHeight();
		
		messagesShown = 5;
		
		//Initialise scene2d variables
		stage = new Stage(width, height, true);
		skin = new Skin(Gdx.files.internal("data/HUD/PackOutput/hud.json"));
		
		bars = new Image(skin.getDrawable("bars"));
		healthBar = new Image(skin.getDrawable("healthBar"));
		manaBar = new Image(skin.getDrawable("manaBar"));
		staminaBar = new Image(skin.getDrawable("staminaBar"));
		
		bars.setPosition(width/2-bars.getWidth()/2, height-bars.getHeight());
		staminaBar.setPosition(bars.getX(), bars.getY()+3);
		healthBar.setPosition(staminaBar.getX()+staminaBar.getWidth(), bars.getY()+2);
		manaBar.setPosition(healthBar.getX()+healthBar.getWidth(), bars.getY()+3);
		
		System.out.println(bars.getHeight());
		System.out.println(healthBar.getHeight());
		
		/*table = new Table(skin);
		stage.addActor(table);
		table.setFillParent(true);
		table.add(staminaBar);
		table.add(healthBar);
		table.add(manaBar);
		bars.setPosition(table.getX(), table.getY());
		stage.addActor(bars);
		//table.add(healthBar);
		//table.add(bars);
		table.debugTable();*/
		
		outputLabel = new Label("", skin);  //Create output label
		outputLabel.setWidth(width);
		outputLabel.setHeight(96);
		outputLabel.setX(0);
		outputLabel.setY(0);  //haaard coded!!!!!96
		
		stage.addActor(outputLabel);

		stage.addActor(healthBar);
		stage.addActor(manaBar);
		stage.addActor(staminaBar);
		stage.addActor(bars);
	}
	
	public void updateAndRender(SpriteBatch batch) {
		//Update HUD
		healthBar.setHeight(hpBarMaxHeight * gameWorld.getPlayer().getHealth() / gameWorld.getPlayer().getMaxHealth());
		manaBar.setHeight(manaBarMaxHeight * gameWorld.getPlayer().getMana() / gameWorld.getPlayer().getMaxMana());
		staminaBar.setHeight(staminaBarMaxHeight * gameWorld.getPlayer().getStamina() / gameWorld.getPlayer().getMaxStamina());
		//Update output text
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
		
		//Table.drawDebug(stage);
		
		batch.end();
	}
	
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}

	public Stage getStage() {
		return stage;
	}
	
}
