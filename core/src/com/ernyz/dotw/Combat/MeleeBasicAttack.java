package com.ernyz.dotw.Combat;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polygon;
import com.ernyz.dotw.Model.MoveableEntity;
import com.ernyz.dotw.Model.Items.Item;

public class MeleeBasicAttack extends Attack {
	
	public MeleeBasicAttack(MoveableEntity attacker, Item weapon) {
		this.attacker = attacker;
		this.texture = weapon.getTexture();
		this.weapon = weapon;
		
		bounds = new Polygon();
		bounds.setVertices(new float[] {
			0, 0,
			0, weapon.getTexture().getHeight(),
			weapon.getTexture().getWidth(), weapon.getTexture().getHeight(),
			weapon.getTexture().getWidth(), 0
		});
		bounds.setOrigin(0, 0);
		
		attacker.getSkeleton().findSlot("RightHand").getColor().a = 0;
	}

	@Override
	public void update(float delta) {
		distanceTraveled += delta * weapon.getFloat("Speed");
		
		bounds.setPosition(attacker.getSkeleton().findBone("RightHand").getWorldX()+attacker.getPosition().x + distanceTraveled*MathUtils.cosDeg(attacker.getRotation()),
				attacker.getSkeleton().findBone("RightHand").getWorldY()+attacker.getPosition().y+distanceTraveled*MathUtils.sinDeg(attacker.getRotation()));
		bounds.setRotation(attacker.getRotation());
		
		if(distanceTraveled >= weapon.getFloat("Range")) {
			isFinished = true;
			destroy();
		}
	}
	
	@Override
	protected void destroy() {
		attacker.getSkeleton().findSlot("RightHand").getColor().a = 1;
	}

}
