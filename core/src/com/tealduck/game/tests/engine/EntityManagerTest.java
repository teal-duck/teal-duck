package com.tealduck.game.tests.engine;


import static org.junit.Assert.assertEquals;

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
		int id = entityManager.createEntity();
		assertEquals(id, 0);
		int id2 = entityManager.createEntity();
		assertEquals(id2, 1);
	}
}
