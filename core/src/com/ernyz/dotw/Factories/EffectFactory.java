package com.ernyz.dotw.Factories;

import com.ernyz.dotw.Model.MoveableEntity;
import com.ernyz.dotw.Model.Effects.Effect;

public class EffectFactory {

	public static Effect recoveringFromAttack(MoveableEntity source, MoveableEntity target) {
		Effect e = new Effect("RecoveringFromAttack", 0.85f, source, target);
		e.setMaxStacks(1);
		return e;
	}
	
	public static Effect canCounterAttack(MoveableEntity source, MoveableEntity target) {
		Effect e = new Effect("CanCounterAttack", 0.5f, source, target);
		e.setMaxStacks(1);
		return e;
	}
	
	public static Effect knockBack(MoveableEntity source, MoveableEntity target) {
		Effect e = new Effect("KnockBack", 0.1f, source, target);
		e.setMaxStacks(1);
		return e;
	}
	
	public static Effect smallKnockBack(MoveableEntity source, MoveableEntity target) {
		Effect e = new Effect("KnockBack", 0.04f, source, target);//0.04f
		e.setMaxStacks(1);
		return e;
	}
	
}
