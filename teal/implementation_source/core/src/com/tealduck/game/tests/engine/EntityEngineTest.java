package com.tealduck.game.tests.engine;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.tealduck.game.engine.EntityEngine;


public class EntityEngineTest {
	private EntityEngine entityEngine;


	@Before
	public void setup() {
		entityEngine = new EntityEngine();
	}


	@Test
	public void testRemoveEntities() {
		int entity1 = entityEngine.getEntityManager().createEntity();

		Assert.assertTrue(entityEngine.getEntityManager().getEntities().contains(entity1));
		entityEngine.flagEntityToRemove(entity1);
		entityEngine.removeAllFlaggedEntities();

		Assert.assertFalse(entityEngine.getEntityManager().getEntities().contains(entity1));
	}
}
