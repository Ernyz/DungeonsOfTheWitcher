package com.ernyz.dotw.Model;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

/**
 * Player class which contains player related code, which can not be moved to {@link MoveableEntity}.
 * 
 * @author Ernyz
 */
public final class Player extends MoveableEntity {
	
	public Player(Vector2 position, Vector2 velocity, float rotation, float speed, GameWorld gameWorld) {
		super(position, velocity, rotation, speed, gameWorld);
		//texture = new Texture(Gdx.files.internal("data/player/player.png"));
		texture = new Texture(Gdx.files.internal("data/player/PlayerLarge.png"));
		//texture = new Texture(Gdx.files.internal("data/player/PlayerMedium.png"));
		//texture = new Texture(Gdx.files.internal("data/player/PlayerSmall.png"));
		
		this.setWidth(texture.getWidth());
		this.setHeight(texture.getHeight());
		
		//bounds.setVertices(new float[] {0, 0, texture.getWidth(), 0, texture.getWidth(), texture.getHeight()-5, 0, texture.getHeight()-5});//This hardcoding is temporary
		//bounds.setOrigin(texture.getWidth()/2, texture.getHeight()/2);
		
		int radius = texture.getHeight()/2;
		float[] tmp = new float[]{0, radius,
				radius/2, 2*radius,
				radius+radius/2, 2*radius,
				2*radius, radius,
				radius+radius/2, 0,
				radius/2, 0};
		bounds.setVertices(tmp);
		bounds.setOrigin(radius, radius);
		
		//Create equipment slots
		equipmentSlots = new HashMap<String, Integer>();
		equipmentSlots.put(Resources.BODY_HEAD, -1);
		equipmentSlots.put(Resources.BODY_NECK, -1);
		equipmentSlots.put(Resources.BODY_SHOULDERS, -1);
		equipmentSlots.put(Resources.BODY_CHEST, -1);
		equipmentSlots.put(Resources.BODY_LEFT_HAND, -1);
		equipmentSlots.put(Resources.BODY_RIGHT_HAND, -1);
		equipmentSlots.put(Resources.BODY_PALMS, -1);
		//equipmentSlots.put("LeftHandRing", -1);
		//equipmentSlots.put("RightHandRing", -1);
		equipmentSlots.put(Resources.BODY_WAIST, -1);
		equipmentSlots.put(Resources.BODY_LEGS, -1);
		equipmentSlots.put(Resources.BODY_FEET, -1);
		
		//Some stats should be set manually
		activeSurroundingsRange = 500;
		rightHand = new Vector2(-75, 19);  //Values are hard coded for each different entity and found by trial and error.
	}

	@Override
	public void update() {
		super.update();
		
		checkCollisions();
	}
}
