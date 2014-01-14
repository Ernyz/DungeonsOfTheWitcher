package com.ernyz.dotw.Combat;

/**
 * Every melee attack, every spell or anything that damages anything in any way
 * is based on this interface.
 * 
 * @author ernyz
 */
public interface Attack {
	
	/**
	 * Updates an attack, changes its state if necessary.
	 */
	public void update();
	
}
