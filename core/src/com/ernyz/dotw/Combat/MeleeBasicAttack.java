package com.ernyz.dotw.Combat;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.ernyz.dotw.Model.MoveableEntity;
import com.ernyz.dotw.Model.Items.Item;

public class MeleeBasicAttack extends BasicAttack {
	
	private String hand;
	
	public MeleeBasicAttack(MoveableEntity attacker, Item weapon, String hand) {
		this.attacker = attacker;
		this.texture = weapon.getTexture();
		this.weapon = weapon;
		this.hand = hand;
		
		bounds = new Polygon();
		bounds.setVertices(new float[] {
			0, 0,
			0, weapon.getTexture().getHeight(),
			weapon.getTexture().getWidth(), weapon.getTexture().getHeight(),
			weapon.getTexture().getWidth(), 0
		});
		bounds.setOrigin(0, 0);
		
		weapon.set("CanAttack", false);
		attacker.getSkeleton().findSlot(hand).getColor().a = 0;
	}

	@Override
	public void update(float delta) {
		if(!getState().equals(StateEnum.RETURNING)) {
			distanceTraveled += delta * weapon.getFloat("Speed");
			float boneWorldX = attacker.getSkeleton().findBone(hand).getWorldX();
			float boneWorldY = attacker.getSkeleton().findBone(hand).getWorldY();
			float centerPointX = weapon.getTexture().getHeight()/2 * MathUtils.cosDeg(attacker.getRotation()-90);
			float centerPointY = weapon.getTexture().getHeight()/2 * MathUtils.sinDeg(attacker.getRotation()-90);
			bounds.setPosition(
					boneWorldX+attacker.getPosition().x+centerPointX+distanceTraveled*MathUtils.cosDeg(attacker.getRotation()),
					boneWorldY+attacker.getPosition().y+centerPointY+distanceTraveled*MathUtils.sinDeg(attacker.getRotation()));
			bounds.setRotation(attacker.getRotation());
			if(distanceTraveled >= weapon.getFloat("Range")) {
				setState(StateEnum.RETURNING);
			}
		} else if(getState().equals(StateEnum.RETURNING)) {
			distanceTraveled -= delta * weapon.getFloat("Speed");
			float boneWorldX = attacker.getSkeleton().findBone(hand).getWorldX();
			float boneWorldY = attacker.getSkeleton().findBone(hand).getWorldY();
			float centerPointX = weapon.getTexture().getHeight()/2 * MathUtils.cosDeg(attacker.getRotation()-90);
			float centerPointY = weapon.getTexture().getHeight()/2 * MathUtils.sinDeg(attacker.getRotation()-90);
			bounds.setPosition(
					boneWorldX+attacker.getPosition().x+centerPointX+distanceTraveled*MathUtils.cosDeg(attacker.getRotation()),
					boneWorldY+attacker.getPosition().y+centerPointY+distanceTraveled*MathUtils.sinDeg(attacker.getRotation()));
			bounds.setRotation(attacker.getRotation());
			if(distanceTraveled <= 0) {
				destroy();
			}
		}
	}
	
	@Override
	protected void destroy() {
		setState(StateEnum.FINISHED);
		weapon.set("CanAttack", true);
		attacker.getSkeleton().findSlot(hand).getColor().a = 1;
	}

}
