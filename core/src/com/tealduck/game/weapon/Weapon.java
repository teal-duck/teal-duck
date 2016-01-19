package com.tealduck.game.weapon;


import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.Team;
import com.tealduck.game.engine.EntityEngine;


/**
 * Base class for a weapon. The getter functions are for the WeaponComponent's logic.
 */
public abstract class Weapon {
	/**
	 * @return
	 */
	public abstract int getAmmoRequiredToFire();


	/**
	 * @param entityEngine
	 * @param shooterId
	 * @param position
	 * @param direction
	 * @param team
	 * @return
	 */
	public abstract int fire(EntityEngine entityEngine, int shooterId, Vector2 position, Vector2 direction,
			Team team);


	/**
	 * @return
	 */
	public abstract int getClipSize();


	/**
	 * @return
	 */
	public abstract float getCooldownTime();


	/**
	 * @return
	 */
	public abstract float getReloadTime();


	@Override
	public String toString() {
		return "Weapon()";
	}
}
