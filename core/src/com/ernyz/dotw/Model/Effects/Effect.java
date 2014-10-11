package com.ernyz.dotw.Model.Effects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.ernyz.dotw.Model.MoveableEntity;

/**
 * TODO: Probably needs factory to create them.
 * 
 * @author Ernyz
 */
public class Effect {
	
	private String effectName;
	private float duration;
	private MoveableEntity source;
	private MoveableEntity target;
	private float timeWorking = 0;
	private float interval = 1;
	private float lastTick = 0;
	private boolean finished = false;
	private Texture texture;
	private int maxStacks;
	private int stacks;
	
	public Effect(String effectName, float duration, MoveableEntity source, MoveableEntity target) {
		this.effectName = effectName;
		this.duration = duration;
		this.source = source;
		this.target = target;
		
		onStart();
	}
	
	//TODO: Decorate and make this more abstract
	
	private void onStart() {
		//Initial effect
		if(effectName.equals("RecoveringFromAttack")) {
			texture = new Texture("data/effects/icons/RecoveringFromAttack.png");
		} else if(effectName.equals("CanCounterAttack")) {
			texture = new Texture("data/effects/icons/CanCounterAttack.png");
			target.removeEffect("RecoveringFromAttack");
		} else if(effectName.equals("KnockBack")) {
			Vector2 dirVector = new Vector2((target.getPosition().x-source.getPosition().cpy().x), (target.getPosition().y-source.getPosition().cpy().y));
			//target.getVelocity().set(dirVector.x/Math.abs(dirVector.x)*2.5f, dirVector.y/Math.abs(dirVector.y)*2.5f);
			target.getVelocityEnforced().set(dirVector.x/Math.abs(dirVector.x)*2.5f, dirVector.y/Math.abs(dirVector.y)*2.5f);  //TODO: fix the case with 0 division
		} else {
			finished = true;
		}
	}
	
	public void update(float delta) {
		if(finished) return;
		
		lastTick += delta;
		timeWorking += delta;
		if(lastTick >= interval) {
			lastTick = 0;
			tick();
		}
		
		if(timeWorking >= duration) {
			onFinish();
		}
	}
	
	private void tick() {
		if(effectName.equals("RecoveringFromAttack")) {
		} else if(effectName.equals("CanCounterAttack")) {
		} else if(effectName.equals("KnockBack")) {
			//
		} else {
			finished = true;
		}
	}
	
	private void onFinish() {
		if(effectName.equals("RecoveringFromAttack")) {
		} else if(effectName.equals("CanCounterAttack")) {
		} else if(effectName.equals("KnockBack")) {
			target.getVelocityEnforced().set(0, 0);
		} else {
			finished = true;
		}
		finished = true;
	}
	
	public void destroy() {
		finished = true;
	}
	
	public String getEffectName() {
		return effectName;
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	public Texture getTexture() {
		return texture;
	}

	public int getMaxStacks() {
		return maxStacks;
	}

	public void setMaxStacks(int maxStacks) {
		this.maxStacks = maxStacks;
	}

	public int getStacks() {
		return stacks;
	}

	public void setStacks(int stacks) {
		this.stacks = stacks;
	}

	public void setTimeWorking(float timeWorking) {
		this.timeWorking = timeWorking;
	}
	
}
