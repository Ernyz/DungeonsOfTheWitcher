package com.ernyz.dotw.Combat;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Polygon;
import com.ernyz.dotw.Model.GameWorld;
import com.ernyz.dotw.Model.MoveableEntity;
import com.ernyz.dotw.Model.Resources;
import com.ernyz.dotw.Model.Items.Item;
import com.esotericsoftware.spine.Bone;

/**
 * An attack which represents an attack made by items held in entitie's hands.
 * Can be melee, ranged/thrown or even magical device evocation, like wand or rod.
 * 
 * @author Ernyz
 */

/**
 * This should be deleted.
 * 
 * @author Ernyz
 */
public class BasicAttack {
	
	private Item rightHandItem = null;
	private Bone rightHandBone;
	private Item leftHandItem = null;
	private Bone leftHandBone;
	
	private Polygon rightHandBounds;
	private MoveableEntity attacker;
	private float startX;
	private float startY;
	private float distanceCovered = 0f;
	private boolean isFinished = false;
	
	public BasicAttack(MoveableEntity attacker, boolean primary, GameWorld gameWorld) {
		this.attacker = attacker;
		
		if(attacker.getEquipmentSlots().get(Resources.BODY_RIGHT_HAND) != -1) {
			rightHandItem = gameWorld.getItemById(attacker.getEquipmentSlots().get(Resources.BODY_RIGHT_HAND));
			rightHandBone = attacker.getSkeleton().findBone(Resources.BODY_RIGHT_HAND);
			//System.out.println(rightHandItem.getName());
		}
		if(attacker.getEquipmentSlots().get(Resources.BODY_LEFT_HAND) != -1) {
			leftHandItem = gameWorld.getItemById(attacker.getEquipmentSlots().get(Resources.BODY_LEFT_HAND));
			leftHandBone = attacker.getSkeleton().findBone(Resources.BODY_LEFT_HAND);
			//System.out.println(leftHandItem.getName());
		}
		
		startX = attacker.getSkeleton().findBone(Resources.BODY_RIGHT_HAND).getX();
		startY = attacker.getSkeleton().findBone(Resources.BODY_RIGHT_HAND).getY();
		//set up bounds
		//Sprite tmp = new Sprite(attacker.getAtlas().findRegion("Dagger"));
		Sprite tmp = new Sprite();
		rightHandBounds = new Polygon();
		rightHandBounds.setVertices(new float[] {
				0, 0, 
				0, tmp.getHeight(),
				tmp.getWidth(), tmp.getHeight(),
				tmp.getWidth(), 0
		});
		//rightHandBounds.setOrigin(tmp.getWidth()/2, tmp.getHeight()/2);
		rightHandBounds.setOrigin(0, tmp.getHeight()/2);
	}
	
	public void update(float delta) {
		if(isFinished) return;
		
		rightHandBone.setX(rightHandBone.getX() + delta * rightHandItem.getFloat("Speed"));
		rightHandBone.setY(rightHandBone.getY());
		
		rightHandBounds.setPosition(rightHandBone.getWorldX()+attacker.getPosition().x, rightHandBone.getWorldY()+attacker.getPosition().y);
		rightHandBounds.setRotation(rightHandBone.getWorldRotation());
		
		distanceCovered += delta * rightHandItem.getFloat("Speed");
		if(distanceCovered >= rightHandItem.getFloat("Range")) {
			isFinished = true;
			finish();
		}
	}
	
	private void finish() {
		System.out.println("Finished");
		rightHandBone.setPosition(startX, startY);
		//destroy bounds
	}
	
	public boolean getIsFinished() {
		return isFinished;
	}
	
	/**
	 * 
	 */
	public Polygon getBounds() {
		return rightHandBounds;
	}
	
}
