package com.ernyz.dotw.Combat;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Polygon;
import com.ernyz.dotw.Model.MoveableEntity;
import com.ernyz.dotw.Model.Items.Item;
import com.ernyz.dotw.Model.Tiles.Tile;

public abstract class BasicAttack {
	
	protected MoveableEntity attacker;
	protected Item weapon;
	protected Texture texture;
	protected Polygon bounds;
	
	protected float distanceTraveled = 0;
	protected boolean isFinished = false;
	
	public abstract void update(float delta);
	
	protected abstract void destroy();
	
	public void onCollision(MoveableEntity e) {  //FIXME: All this is temporary. Rework is a must!
		/*float attackDisableDuration = 0.8f;
		if(!e.isBlocking()) {
			e.setHealth(e.getHealth()-weapon.getFloat("Damage"));
			FloatingText floatingText = new FloatingText(String.valueOf(weapon.getFloat("Damage")), e.getPosition().x-e.getWidth()/2, e.getPosition().y);
			floatingText.setX(floatingText.getX()-floatingText.getWidth()/2);
			GameWorld.addFloatingText(new FloatingText(String.valueOf(weapon.getFloat("Damage")), e.getPosition().x-e.getWidth()/2, e.getPosition().y));
			e.effects.add(new Effect("RecoveringFromAttack", attackDisableDuration, attacker, e));
			//isFinished = true;
			destroy();
		} else if (e.isBlocking()) {
			boolean recoveringFromAttack = false;
			for(Effect effect : e.effects) {
				if(effect.getEffectName().equals("RecoveringFromAttack")) recoveringFromAttack = true;
			}
			if(recoveringFromAttack) {
				if(MathUtils.randomBoolean(0.75f)) {
					FloatingText floatingText = new FloatingText("Blocked", e.getPosition().x-e.getWidth()/2, e.getPosition().y);
					floatingText.setX(floatingText.getX()-floatingText.getWidth()/2);
					GameWorld.addFloatingText(floatingText);
					e.effects.add(new Effect("CanCounterAttack", 0.5f, attacker, e));
					//isFinished = true;
					destroy();
				} else {
					e.setHealth(e.getHealth()-weapon.getFloat("Damage"));
					FloatingText floatingText = new FloatingText(String.valueOf(weapon.getFloat("Damage")), e.getPosition().x-e.getWidth()/2, e.getPosition().y);
					floatingText.setX(floatingText.getX()-floatingText.getWidth()/2);
					GameWorld.addFloatingText(new FloatingText(String.valueOf(weapon.getFloat("Damage")), e.getPosition().x-e.getWidth()/2, e.getPosition().y));
					e.effects.add(new Effect("RecoveringFromAttack", attackDisableDuration, attacker, e));
					//isFinished = true;
					destroy();
				}
			} else {
				if(MathUtils.randomBoolean(0.85f)) {
					FloatingText floatingText = new FloatingText("Blocked", e.getPosition().x-e.getWidth()/2, e.getPosition().y);
					floatingText.setX(floatingText.getX()-floatingText.getWidth()/2);
					GameWorld.addFloatingText(floatingText);
					e.effects.add(new Effect("CanCounterAttack", 0.5f, attacker, e));
					//isFinished = true;
					destroy();
				} else {
					e.setHealth(e.getHealth()-weapon.getFloat("Damage"));
					FloatingText floatingText = new FloatingText(String.valueOf(weapon.getFloat("Damage")), e.getPosition().x-e.getWidth()/2, e.getPosition().y);
					floatingText.setX(floatingText.getX()-floatingText.getWidth()/2);
					GameWorld.addFloatingText(new FloatingText(String.valueOf(weapon.getFloat("Damage")), e.getPosition().x-e.getWidth()/2, e.getPosition().y));
					e.effects.add(new Effect("RecoveringFromAttack", attackDisableDuration, attacker, e));
					//isFinished = true;
					destroy();
				}
			}
		}*/
		destroy();
	}
	
	public void onCollision(Tile t) {
		//isFinished = true;
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

	public Item getWeapon() {
		return weapon;
	}
	
}
