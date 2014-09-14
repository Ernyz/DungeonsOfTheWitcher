package com.ernyz.dotw.Model;

import com.badlogic.gdx.math.Vector2;

/**
 * Base class for all entities, that includes monsters, player, NPC's, walls and etc.
 * 
 * @author ernyz
 */
public abstract class Entity {
	
	protected Vector2 position;
	protected float rotation;
	protected float width, height;
	//protected Texture texture;  //TODO: Move this down to MoveableEntity
	
	protected String name;
	
	public Entity(Vector2 position, float rotation) {
		this.position = position;
		this.rotation = rotation;
		//Width and height are set in constructors of each subclass
	}
	
	public abstract void update(float delta);
	
	/**
	 * Properly disposes entities resources. TODO: rethink if this method is needed
	 */
	public void dispose() {
		//texture.dispose();
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
