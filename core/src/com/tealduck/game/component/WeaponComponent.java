package com.tealduck.game.component;


import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.engine.Component;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.weapon.Weapon;


public class WeaponComponent extends Component {
	// TODO: Allow multiple weapons
	public Weapon weapon;

	public int ammoInClip;
	public int extraAmmo;

	// TODO: Should maxCooldownTime be part of the weapon?
	public float cooldownTime = 0;
	public float maxCooldownTime;

	public float reloadTime = 0;
	public float maxReloadTime;
	public float timePerReloadBullet;
	public float bulletReloadTime = 0;

	// TODO: Improve muzzle flash handling
	public boolean justFired = false;
	public Vector2 fireLocation;
	public Vector2 fireDirection;


	public WeaponComponent(Weapon weapon, int ammoInClip, int extraAmmo, float maxCooldownTime,
			float maxReloadTime) {
		this.weapon = weapon;
		this.ammoInClip = ammoInClip;
		this.extraAmmo = extraAmmo;
		this.maxCooldownTime = maxCooldownTime;
		this.maxReloadTime = maxReloadTime;

		timePerReloadBullet = maxReloadTime / getClipSize();

		System.out.println(ammoInClip);
		System.out.println(extraAmmo);

		fireLocation = new Vector2(0, 0);
		fireDirection = new Vector2(0, 0);
	}


	// TODO: Maybe move weapon logic out of the component
	public void fireWeapon(EntityEngine entityEngine, int shooterId, Vector2 position, Vector2 direction) {
		if (cooldownTime != 0) {
			return;
		}
		if (ammoInClip < weapon.ammoRequiredToFire()) {
			startReloading();
			return;
		}
		if (isReloading() && (ammoInClip >= weapon.ammoRequiredToFire())) {
			System.out.println("Wat");
			stopReloading();
		}
		cooldownTime = maxCooldownTime;
		int ammoUsed = weapon.fire(entityEngine, shooterId, position, direction);
		ammoInClip -= ammoUsed;

		justFired = true;
		fireLocation.set(position);
		fireDirection.set(direction);

		if (ammoInClip <= 0) {
			startReloading();
		}
	}


	public int getClipSize() {
		return weapon.getClipSize();
	}


	public boolean isReloading() {
		return (reloadTime > 0);
	}


	public void stopReloading() {
		reloadTime = 0;
		bulletReloadTime = 0;
	}


	public void startReloading() {
		if (isReloading()) {
			return;
		}
		if (extraAmmo <= 0) {
			return;
		}
		if (ammoInClip == getClipSize()) {
			return;
		}
		reloadTime = maxReloadTime;
		bulletReloadTime = 0;
	}


	public void doCooldown(float deltaTime) {
		cooldownTime -= deltaTime;
		if (cooldownTime < 0) {
			cooldownTime = 0;
		}
	}


	public void doReload(float deltaTime) {
		if (isReloading()) {
			bulletReloadTime += deltaTime;
			while (bulletReloadTime > timePerReloadBullet) {
				ammoInClip += 1;
				extraAmmo -= 1;
				bulletReloadTime -= timePerReloadBullet;

				if (extraAmmo <= 0) {
					stopReloading();
					return;
				}

				if (ammoInClip >= getClipSize()) {
					stopReloading();
					return;
				}
			}

			reloadTime -= deltaTime;
			if (reloadTime < 0) {
				stopReloading();
			}
		}
	}


	public int getRemainingExtraClips() {
		return extraAmmo / weapon.getClipSize();
	}


	@Override
	public String toString() {
		return "WeaponComponent(" + weapon.toString() + ", " + ammoInClip + ", " + extraAmmo + ", "
				+ maxCooldownTime + ", " + maxReloadTime + ")";
	}
}
