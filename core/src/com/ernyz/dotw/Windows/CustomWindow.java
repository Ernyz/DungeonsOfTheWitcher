package com.ernyz.dotw.Windows;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

/**
 * All windows must subclass this class. Contains all custom methods or their declarations.
 * 
 * @author Ernyz
 */
public abstract class CustomWindow extends Window {
	
	protected SpriteBatch batch;

	public CustomWindow(String title, SpriteBatch batch, Skin skin) {
		super(title, skin);
	}
	
	public abstract void update(float delta);
	
	protected abstract void setUpTheWindow();

}
