package com.ernyz.dotw.View;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public class FloatingText {
	
	private String text;
	private float x;
	private float y;
	private float alpha = 1;
	private Color color;
	private float speed = 32;
	private float distance = 25;
	private float distanceCovered = 0;
	private boolean finished = false;
	private BitmapFont font = new BitmapFont(Gdx.files.internal("data/fonts/courier.fnt"));
	
	public FloatingText(String text, float x, float y) {
		this.text = text;
		this.x = x;
		this.y = y;
		//font.setColor(Color.RED);
		color = Color.RED;
	}
	
	public void update(float delta) {
		y += delta*speed;
		distanceCovered += delta*speed;
		
		alpha = (100-(100/distance*distanceCovered))/100;
		font.setColor(color.r, color.g, color.b, alpha);
		
		if(distanceCovered >= distance)
			finished = true;
	}
	
	public float getX() {
		return x;
	}
	
	public float getY() {
		return y;
	}
	
	public BitmapFont getFont() {
		return font;
	}
	
	public String getText() {
		return text;
	}
	
	public boolean isFinished() {
		return finished;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
}
