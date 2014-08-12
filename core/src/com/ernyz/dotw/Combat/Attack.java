package com.ernyz.dotw.Combat;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Polygon;
import com.ernyz.dotw.Model.MoveableEntity;
import com.ernyz.dotw.Model.Items.Item;

public abstract class Attack {
	
	protected MoveableEntity attacker;
	protected Item weapon;
	protected Texture texture;
	protected Polygon bounds;
	
	protected float distanceTraveled = 0;
	protected boolean isFinished = false;
	
	public abstract void update(float delta);
	
	protected abstract void destroy();
	
	public void onCollision(MoveableEntity e) {
		e.setHealth(e.getHealth()-weapon.getFloat("Damage"));
		isFinished = true;
		destroy();
	}
	
	public boolean getIsFinished() {
		return isFinished;
	}
	
	public Polygon getBounds() {
		return bounds;
	}
	
	public Texture getTexture() {
		return texture;
	}
	
	public float getTextureWidth() {
		return texture.getWidth();
	}
	
	public float getTextureHeight() {
		return texture.getHeight();
	}
	
	public MoveableEntity getAttacker() {
		return attacker;
	}

}
