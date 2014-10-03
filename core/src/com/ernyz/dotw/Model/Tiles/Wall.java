package com.ernyz.dotw.Model.Tiles;

import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Wall extends Tile {
	
	private Body box2dBody;

	public Wall(Vector2 position, float rotation) {
		super(position, rotation);
		
		asciiSymbol = '#';
		//texture = new Texture(Gdx.files.internal("data/tiles/Wall.png"));
//		width = texture.getWidth();
//		height = texture.getHeight();
		width = 50;  //TODO: Remove this hardcoding
		height = 50;
		bounds = new Polygon();
		bounds.setVertices(new float[] {0, 0, getWidth(), 0, getWidth(), getHeight(), 0, getHeight()});
		bounds.setOrigin(0, 0);
		bounds.setPosition(getPosition().x, getPosition().y);
		
		this.setWalkable(false);
		this.setName("wall");
	}
	
	@Override
	public void switchToDebugTexture() {
		textureRegion = debugTextureRegion;
	}
	
	@Override
	public void update(float delta) {
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
