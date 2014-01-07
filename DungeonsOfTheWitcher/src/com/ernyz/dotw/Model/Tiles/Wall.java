package com.ernyz.dotw.Model.Tiles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Wall extends Tile {
	
	private transient Body box2dBody;

	public Wall(Vector2 position, float rotation) {
		super(position, rotation);
		
		texture = new Texture(Gdx.files.internal("data/tiles/Wall.png"));
		width = texture.getWidth();
		height = texture.getHeight();
		bounds = new Rectangle(this.getPosition().x, this.getPosition().y, this.getWidth(), this.getHeight());
		bounds.setWidth(texture.getWidth());
		bounds.setHeight(texture.getHeight());
		
		this.setWalkable(false);
		this.setName("wall");
	}

	/**
	 * @return the box2dBody
	 */
	public Body getBox2dBody() {
		return box2dBody;
	}

	/**
	 * @param box2dBody the box2dBody to set
	 */
	public void setBox2dBody(Body box2dBody) {
		this.box2dBody = box2dBody;
	}
	
}
