package com.tealduck.game.tests.component;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.Team;
import com.tealduck.game.component.WeaponComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.weapon.MachineGun;


public class WeaponComponentTest {
	private static final double DELTA = 1e-15;


	@Before
	public void setup() {
		// MachineGun machineGun = new MachineGun(null);
		// WeaponComponent weaponComponent = new WeaponComponent(machineGun, 10, 10);
		// EntityEngine entityEngine = new EntityEngine();
	}


	@Test
	public void testFireWeapon() {
		MachineGun machineGun = new MachineGun(null);
		WeaponComponent weaponComponent = new WeaponComponent(machineGun, 10, 10);
		EntityEngine entityEngine = new EntityEngine();
		EntityManager entityManager = entityEngine.getEntityManager();
		int shooter = entityManager.createEntity();
		weaponComponent.cooldownTime = 0;
		Team team = Team.GOOD;
		Vector2 position = new Vector2(0, 0);
		Vector2 direction = new Vector2(0, 9);

		weaponComponent.fireWeapon(entityEngine, shooter, position, direction, team); // breaks here. Too tired
												// to figure out. Ask
												// Ben in morning.

		Assert.assertEquals(weaponComponent.cooldownTime, 0.15f, WeaponComponentTest.DELTA);
		Assert.assertEquals(weaponComponent.ammoInClip, 9);
		Assert.assertEquals(weaponComponent.fireLocation, new Vector2(0, 0));
		Assert.assertEquals(weaponComponent.fireDirection, new Vector2(0, 9));

		// int shooterId = entityManager.createEntity();
		// position etc here
		// weapon.fire(entityEngine, ... );

		// Assert.assertTrue(entityManager.getEntitiesWithComponent(BulletComponent.class).size() > 0);

	}


	@Test
	public void testStopReloading() {
		MachineGun machineGun = new MachineGun(null);
		WeaponComponent weaponComponent = new WeaponComponent(machineGun, 10, 10);
		weaponComponent.reloadTime = 10.0f;
		weaponComponent.bulletReloadTime = 10.0f;
		weaponComponent.stopReloading();
		Assert.assertEquals(weaponComponent.reloadTime, 0.0f, WeaponComponentTest.DELTA);
		Assert.assertEquals(weaponComponent.bulletReloadTime, 0.0f, WeaponComponentTest.DELTA);

	}


	@Test
	public void testStartReloading() {

		MachineGun machineGun = new MachineGun(null);
		WeaponComponent weaponComponent = new WeaponComponent(machineGun, 8, 10);
		weaponComponent.reloadTime = 0f;
		weaponComponent.bulletReloadTime = 5f;
		weaponComponent.startReloading();
		Assert.assertEquals(weaponComponent.reloadTime, 4f, WeaponComponentTest.DELTA);
		Assert.assertEquals(weaponComponent.bulletReloadTime, 0f, WeaponComponentTest.DELTA);
		weaponComponent.extraAmmo = 0;
		weaponComponent.bulletReloadTime = 5f;
		weaponComponent.reloadTime = 3f;
		weaponComponent.ammoInClip = 8;
		weaponComponent.startReloading();
		Assert.assertEquals(weaponComponent.reloadTime, 3f, WeaponComponentTest.DELTA);
		Assert.assertEquals(weaponComponent.bulletReloadTime, 5f, WeaponComponentTest.DELTA);
		weaponComponent.extraAmmo = 10;
		weaponComponent.bulletReloadTime = 5f;
		weaponComponent.reloadTime = 3f;
		weaponComponent.ammoInClip = 10;
		weaponComponent.startReloading();
		Assert.assertEquals(weaponComponent.reloadTime, 3f, WeaponComponentTest.DELTA);
		Assert.assertEquals(weaponComponent.bulletReloadTime, 5f, WeaponComponentTest.DELTA);
	}


	@Test
	public void testDoCooldown() {
		MachineGun machineGun = new MachineGun(null);
		WeaponComponent weaponComponent = new WeaponComponent(machineGun, 10, 10);
		weaponComponent.cooldownTime = 10f;
		weaponComponent.doCooldown(5f);
		Assert.assertEquals(weaponComponent.cooldownTime, 5f, WeaponComponentTest.DELTA);
		weaponComponent.doCooldown(5f);
		Assert.assertEquals(weaponComponent.cooldownTime, 0f, WeaponComponentTest.DELTA);
		weaponComponent.doCooldown(5f);
		Assert.assertEquals(weaponComponent.cooldownTime, 0f, WeaponComponentTest.DELTA);

	}


	@Test
	public void testAddAmmo() {
		MachineGun machineGun = new MachineGun(null);
		WeaponComponent weaponComponent = new WeaponComponent(machineGun, 10, 10);
		weaponComponent.addAmmo(10);
		Assert.assertEquals(weaponComponent.extraAmmo, 20);
		weaponComponent.ammoInClip = 0;
		weaponComponent.extraAmmo = 0;
		weaponComponent.addAmmo(10);
		Assert.assertEquals(weaponComponent.reloadTime, 4f, WeaponComponentTest.DELTA);
		Assert.assertEquals(weaponComponent.bulletReloadTime, 0f, WeaponComponentTest.DELTA);

	}


	@Test
	public void testDoReload() {
		MachineGun machineGun = new MachineGun(null);
		WeaponComponent weaponComponent = new WeaponComponent(machineGun, 0, 10);
		weaponComponent.bulletReloadTime = 0;
		weaponComponent.reloadTime = 4;
		weaponComponent.doReload(5f);
		// System.out.println(weaponComponent.timePerReloadBullet);
		// System.out.println(weaponComponent.ammoInClip);
		Assert.assertEquals(weaponComponent.ammoInClip, 10);
		Assert.assertEquals(weaponComponent.extraAmmo, 0);
		weaponComponent.reloadTime = 0;
		weaponComponent.ammoInClip = 0;
		weaponComponent.extraAmmo = 10;
		weaponComponent.doReload(5f);
		Assert.assertEquals(weaponComponent.ammoInClip, 0);
		Assert.assertEquals(weaponComponent.extraAmmo, 10);
	}

}
