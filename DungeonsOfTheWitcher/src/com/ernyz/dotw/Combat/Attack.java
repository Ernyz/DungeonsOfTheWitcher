package com.ernyz.dotw.Combat;

import com.badlogic.gdx.math.Polygon;

/**
 * Every melee/ranged attack, every spell or anything that damages anything in any way
 * is based on this interface.
 * 
 * @author Ernyz
 */
public interface Attack {
	
	/**
	 * Updates an attack, changes its state if necessary.
	 */
	public void update();
	
	/**
	 * Only for debug purposes - shape renderer in WorldRenderer class needs these bounds.
	 */
	public Polygon getBounds();
	
	/**
	 * Returns true if this attack is finished.
	 */
	public boolean getIsFinished();
}
