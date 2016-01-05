package com.tealduck.game.component;


import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.engine.Component;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.weapon.Weapon;


public class WeaponComponent extends Component {
	// TODO: Allow multiple weapons
	public Weapon weapon;
	public int ammo;
	// TODO: Should maxCooldownTime be part of the weapon?
	public float maxCooldownTime;
	public float cooldownTime = 0;


	public WeaponComponent(Weapon weapon, int ammo, float maxCooldownTime) {
		this.weapon = weapon;
		this.ammo = ammo;
		this.maxCooldownTime = maxCooldownTime;
	}


	// TODO: Maybe move weapon logic out of the component
	public void fireWeapon(EntityEngine entityEngine, Vector2 position, Vector2 direction) {
		if (cooldownTime != 0) {
			return;
		}
		if (ammo < weapon.ammoRequiredToFire()) {
			return;
		}
		cooldownTime = maxCooldownTime;
		int ammoUsed = weapon.fire(entityEngine, position, direction);
		ammo -= ammoUsed;
	}
}
