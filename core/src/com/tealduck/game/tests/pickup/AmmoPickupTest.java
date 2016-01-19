package com.tealduck.game.tests.pickup;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.tealduck.game.component.WeaponComponent;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.pickup.AmmoPickup;
import com.tealduck.game.weapon.MachineGun;

public class AmmoPickupTest {
	private EntityManager entityManager;
	private int entityWithWeapon;
	private int entityWithoutWeapon;
	private AmmoPickup ammoPickup;

	@Before
	public void setup() {
		entityManager = new EntityManager();
		entityWithWeapon = entityManager.createEntity();
		entityWithoutWeapon = entityManager.createEntity();
		entityManager.addComponent(entityWithWeapon, new WeaponComponent(new MachineGun(null), 10, 0));
		ammoPickup = new AmmoPickup(5);
		// sould add 5 ammo, 5 ammo in pickup, words.
	}

	@Test
	public void testAmmoPickup() {
		Assert.assertFalse(ammoPickup.applyToEntity(entityManager, entityWithoutWeapon));

		Assert.assertTrue(ammoPickup.applyToEntity(entityManager, entityWithWeapon));
		Assert.assertEquals(entityManager.getComponent(entityWithWeapon, WeaponComponent.class).extraAmmo, 5);
	}
}
