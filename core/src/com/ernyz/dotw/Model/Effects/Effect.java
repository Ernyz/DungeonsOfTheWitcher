package com.ernyz.dotw.Model.Effects;

import com.badlogic.gdx.graphics.Texture;
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
		
	}
	
	private void onFinish() {
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
	
}
