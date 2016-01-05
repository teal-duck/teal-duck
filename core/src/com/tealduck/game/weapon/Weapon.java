package com.tealduck.game.weapon;


import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.engine.EntityEngine;


public abstract class Weapon {
	public abstract int ammoRequiredToFire();


	public abstract int fire(EntityEngine entityEngine, int shooterId, Vector2 position, Vector2 direction);


	@Override
	public String toString() {
		return "Weapon()";
	}


	public abstract int getClipSize();
}
