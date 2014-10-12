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
	private boolean blocked = false;
	
	protected float distanceTraveled = 0;
	public enum StateEnum {
		ATTACKING, DAMAGE_DISABLED, FINISHED, RETURNING
	}
	private StateEnum state;
	protected enum TypeEnum {
		MELEE, RANGED
	}
	private TypeEnum type;
	
	public abstract void update(float delta);
	
	protected abstract void destroy();
	
	public void onCollision(MoveableEntity e) {
		if(!isBlocked()) {
			applyOnHitEffects(e);
		}
		if(type.equals(TypeEnum.MELEE)) {
			setState(StateEnum.DAMAGE_DISABLED);
			//setState(StateEnum.RETURNING);
		}
	}
	
	public void onCollision(Tile t) {
		if(type.equals(TypeEnum.MELEE)) {
			setState(StateEnum.RETURNING);
		}
	}
	
	private void applyOnHitEffects(MoveableEntity e) {
		for(Enchantment enchantment : enchantments) {
			enchantment.applyOn(e, this);
		}
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

	public StateEnum getState() {
		return state;
	}

	public void setState(StateEnum state) {
		this.state = state;
	}

	public TypeEnum getType() {
		return type;
	}

	public void setType(TypeEnum type) {
		this.type = type;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}
	
}
