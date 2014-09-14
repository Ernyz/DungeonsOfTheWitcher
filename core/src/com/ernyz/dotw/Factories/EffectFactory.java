package com.ernyz.dotw.Factories;

import com.ernyz.dotw.Model.MoveableEntity;
import com.ernyz.dotw.Model.Effects.Effect;

public class EffectFactory {

	public static Effect recoveringFromAttack(MoveableEntity source, MoveableEntity target) {
		Effect e = new Effect("RecoveringFromAttack", 0.65f, source, target);
		e.setMaxStacks(1);
		return e;
	}
	
	public static Effect canCounterAttack(MoveableEntity source, MoveableEntity target) {
		Effect e = new Effect("CanCounterAttack", 0.5f, source, target);
		e.setMaxStacks(1);
		return e;
	}
	
}
