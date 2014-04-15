package com.ernyz.dotw.Windows;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

/**
 * All windows must subclass this class. Contains all custom methods or their declarations.
 * 
 * @author Ernyz
 */
public abstract class CustomWindow extends Window {

	public CustomWindow(String title, Skin skin) {
		super(title, skin);
	}
	
	public abstract void update(float delta);

}
