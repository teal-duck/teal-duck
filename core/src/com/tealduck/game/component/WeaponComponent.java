package com.tealduck.game.component;


import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.Team;
import com.tealduck.game.engine.Component;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.weapon.Weapon;


/**
 *
 */
public class WeaponComponent extends Component {
	public Weapon weapon;

	public int ammoInClip;
	public int extraAmmo;

	public float cooldownTime = 0;

	public float reloadTime = 0;
	public float timePerReloadBullet;
	public float bulletReloadTime = 0;

	// For muzzle flash
	public boolean justFired = false;
	public Vector2 fireLocation;
	public Vector2 fireDirection;


	/**
	 * @param weapon
	 * @param ammoInClip
	 * @param extraAmmo
	 */
	public WeaponComponent(Weapon weapon, int ammoInClip, int extraAmmo) {
		this.weapon = weapon;
		this.ammoInClip = ammoInClip;
		this.extraAmmo = extraAmmo;

		timePerReloadBullet = weapon.getReloadTime() / getClipSize();

		fireLocation = new Vector2(0, 0);
		fireDirection = new Vector2(0, 0);
	}


	/**
	 * @param entityEngine
	 * @param shooterId
	 * @param position
	 * @param direction
	 * @param team
	 */
	public void fireWeapon(EntityEngine entityEngine, int shooterId, Vector2 position, Vector2 direction,
			Team team) {
		if (cooldownTime != 0) {
			return;
		}
		if (!hasEnoughAmmoInClipToFire()) {
			startReloading();
			return;
		}
		if (isReloading() && hasEnoughAmmoInClipToFire()) {
			stopReloading();
		}
		cooldownTime = weapon.getCooldownTime();
		int ammoUsed = weapon.fire(entityEngine, shooterId, position, direction, team);
		ammoInClip -= ammoUsed;

		justFired = true;
		fireLocation.set(position);
		fireDirection.set(direction);

		if (ammoInClip <= 0) {
			startReloading();
		}
	}


	/**
	 *
	 */
	public void stopReloading() {
		reloadTime = 0;
		bulletReloadTime = 0;
	}


	/**
	 *
	 */
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
		reloadTime = weapon.getReloadTime();
		bulletReloadTime = 0;
	}


	/**
	 * @param deltaTime
	 */
	public void doCooldown(float deltaTime) {
		cooldownTime -= deltaTime;
		if (cooldownTime < 0) {
			cooldownTime = 0;
		}
	}


	/**
	 * @param ammo
	 */
	public void addAmmo(int ammo) {
		boolean shouldReload = ((ammoInClip == 0) && (extraAmmo == 0));
		extraAmmo += ammo;
		if (shouldReload) {
			startReloading();
		}
	}


	/**
	 * @param deltaTime
	 */
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


	/**
	 * @return
	 */
	public boolean hasEnoughAmmoInClipToFire() {
		return (ammoInClip >= weapon.ammoRequiredToFire());
	}


	/**
	 * @return
	 */
	public int getClipSize() {
		return weapon.getClipSize();
	}


	/**
	 * @return
	 */
	public boolean isReloading() {
		return (reloadTime > 0);
	}


	/**
	 * @return
	 */
	public int getRemainingExtraClips() {
		return extraAmmo / weapon.getClipSize();
	}


	@Override
	public String toString() {
		return "WeaponComponent(" + weapon.toString() + ", " + ammoInClip + ", " + extraAmmo + ")";
	}
}
