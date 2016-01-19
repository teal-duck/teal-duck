package com.tealduck.game.pickup;


import com.tealduck.game.AssetLocations;
import com.tealduck.game.component.WeaponComponent;
import com.tealduck.game.engine.EntityManager;


/**
 *
 */
public class AmmoPickup extends Pickup {
	public int ammo;


	/**
	 * @param ammo
	 */
	public AmmoPickup(int ammo) {
		this.ammo = ammo;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tealduck.game.pickup.Pickup#applyToEntity(com.tealduck.game.engine.EntityManager, int)
	 *
	 * Only applicable if the entity has a weapon component.
	 */
	@Override
	public boolean applyToEntity(EntityManager entityManager, int entity) {
		if (entityManager.entityHasComponent(entity, WeaponComponent.class)) {
			WeaponComponent weaponComponent = entityManager.getComponent(entity, WeaponComponent.class);
			weaponComponent.addAmmo(ammo);
			return true;
		}
		return false;
	}


	@Override
	public String getTextureName() {
		return AssetLocations.AMMO_PICKUP;
	}


	@Override
	public String toString() {
		return "AmmoPickup(" + ammo + ")";
	}
}
