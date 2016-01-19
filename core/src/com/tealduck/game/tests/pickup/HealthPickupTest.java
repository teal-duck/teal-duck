package com.tealduck.game.tests.pickup;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.tealduck.game.component.HealthComponent;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.pickup.HealthPickup;

public class HealthPickupTest {

	
	private EntityManager entityManager;
	private int entityMaxHealth;
	private int entitySomeHealth;	
	private int entityNoHealthComponent;
	private HealthPickup healthPickup;

	@Before
	public void setup() {
		entityManager = new EntityManager();
		entityMaxHealth = entityManager.createEntity();
		entitySomeHealth = entityManager.createEntity();
		entityNoHealthComponent = entityManager.createEntity();
		entityManager.addComponent(entityMaxHealth, new HealthComponent(10));
		entityManager.addComponent(entitySomeHealth, new HealthComponent(10, 5));
		healthPickup = new HealthPickup(5);
		// sould add 5 health, 5 health in pickup, words.Should also do nothing to FullHealth
	}

	@Test
	public void testHealthPickup() {
		Assert.assertTrue(healthPickup.applyToEntity(entityManager, entitySomeHealth));
		Assert.assertEquals(entityManager.getComponent(entitySomeHealth, HealthComponent.class).health, 10);

		Assert.assertFalse(healthPickup.applyToEntity(entityManager, entityMaxHealth));
		Assert.assertEquals(entityManager.getComponent(entityMaxHealth, HealthComponent.class).health, 10);
		
		Assert.assertFalse(healthPickup.applyToEntity(entityManager, entityNoHealthComponent));
	}


}
