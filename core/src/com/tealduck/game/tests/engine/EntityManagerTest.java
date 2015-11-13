package com.tealduck.game.tests.engine;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.tealduck.game.engine.EntityManager;


public class EntityManagerTest {
	private EntityManager entityManager;


	@Before
	public void setup() {
		entityManager = new EntityManager();
	}


	@Test
	public void testAddEntity() {
		int entity1 = entityManager.createEntity();
		Assert.assertEquals(entity1, 0);
		int entity2 = entityManager.createEntity();
		Assert.assertEquals(entity2, 1);
		Assert.assertEquals(entityManager.getEntityCount(), 2);
	}


	@Test
	public void testRemoveEntity() {
		int entity1 = entityManager.createEntity();
		int entity2 = entityManager.createEntity();
		int removedEntity = entityManager.removeEntity(entity1);
		Assert.assertEquals(entity1, removedEntity);
		Assert.assertEquals(entityManager.getEntityCount(), 1);
		Assert.assertFalse(entityManager.getEntities().contains(entity1));
		Assert.assertTrue(entityManager.getEntities().contains(entity2));
	}


	@Test
	public void testRemoveEntityWithComponents() {
		// TODO: later
	}
}
