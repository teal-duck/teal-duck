package com.tealduck.game.weapon;


import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.engine.EntityEngine;


public abstract class Weapon {
	public abstract int ammoRequiredToFire();


	public abstract int fire(EntityEngine entityEngine, Vector2 position, Vector2 direction);
}
