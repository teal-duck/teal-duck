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

	// TODO: Improve muzzle flash handling
	public boolean justFired = false;
	public Vector2 fireLocation;
	public Vector2 fireDirection;


	public WeaponComponent(Weapon weapon, int ammo, float maxCooldownTime) {
		this.weapon = weapon;
		this.ammo = ammo;
		this.maxCooldownTime = maxCooldownTime;

		fireLocation = new Vector2(0, 0);
		fireDirection = new Vector2(0, 0);
	}


	// TODO: Maybe move weapon logic out of the component
	public void fireWeapon(EntityEngine entityEngine, int shooterId, Vector2 position, Vector2 direction) {
		if (cooldownTime != 0) {
			return;
		}
		if (ammo < weapon.ammoRequiredToFire()) {
			return;
		}
		cooldownTime = maxCooldownTime;
		int ammoUsed = weapon.fire(entityEngine, shooterId, position, direction);
		ammo -= ammoUsed;

		justFired = true;
		fireLocation.set(position);
		fireDirection.set(direction);
	}


	@Override
	public String toString() {
		return "WeaponComponent(" + weapon.toString() + ", " + ammo + ", " + maxCooldownTime + ")";
	}
}
