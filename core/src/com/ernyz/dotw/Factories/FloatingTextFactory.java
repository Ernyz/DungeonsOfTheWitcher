package com.ernyz.dotw.Factories;

import com.ernyz.dotw.View.FloatingText;

public class FloatingTextFactory {

	public static FloatingText createFloatingText(String text, float x, float y) {
		FloatingText floatingText = new FloatingText(text, x, y);
		floatingText.setX(floatingText.getX()-floatingText.getWidth()/2);
		
		return floatingText;
	}
	
}
