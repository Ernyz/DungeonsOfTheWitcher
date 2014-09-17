package com.ernyz.dotw.Model.Items.ItemEnchantments;

import com.ernyz.dotw.Combat.BasicAttack;
import com.ernyz.dotw.Factories.EffectFactory;
import com.ernyz.dotw.Model.MoveableEntity;

public class Enchantment {

	private String name;
	
	public Enchantment(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void applyOn(MoveableEntity e, BasicAttack ba) {  //TODO: rework this so its done without name checking
		if(name.equals("CounterAttack")) {  //XXX: i dont like theese hardcoded names
			e.addEffect(EffectFactory.knockBack(ba.getAttacker(), e));
		}
	}
	
}
