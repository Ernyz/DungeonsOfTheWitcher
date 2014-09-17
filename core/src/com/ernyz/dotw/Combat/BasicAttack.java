package com.ernyz.dotw.Combat;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.utils.Array;
import com.ernyz.dotw.Model.MoveableEntity;
import com.ernyz.dotw.Model.Items.Item;
import com.ernyz.dotw.Model.Items.ItemEnchantments.Enchantment;
import com.ernyz.dotw.Model.Tiles.Tile;

public abstract class BasicAttack {
	
	protected MoveableEntity attacker;
	protected Item weapon;
	private Array<Enchantment> enchantments = new Array<Enchantment>();
	protected Texture texture;
	protected Polygon bounds;
	
	protected float distanceTraveled = 0;
	protected boolean isFinished = false;
	
	public abstract void update(float delta);
	
	protected abstract void destroy();
	
	public void onCollision(MoveableEntity e) {
		applyOnHitEffects(e);
		destroy();
	}
	
	public void onCollision(Tile t) {
		destroy();
	}
	
	private void applyOnHitEffects(MoveableEntity e) {
		for(Enchantment enchantment : enchantments) {
			enchantment.applyOn(e, this);
		}
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

	public Item getWeapon() {
		return weapon;
	}
	
	public Array<Enchantment> getEnchantments() {
		return enchantments;
	}
	
}
