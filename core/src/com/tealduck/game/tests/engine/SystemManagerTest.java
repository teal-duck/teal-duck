package com.tealduck.game.tests.engine;


import org.junit.Before;
import org.junit.Test;

import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.SystemManager;
import com.tealduck.game.systems.CollisionSystem;
import com.tealduck.game.systems.InputLogicSystem;
import com.tealduck.game.systems.MovementSystem;
import com.tealduck.game.systems.PatrolLogicSystem;


public class SystemManagerTest {
	// TODO: SystemManagerTest
	private SystemManager systemManager;
	private EntityManager entityManager;


	@Before
	public void setup() {
		systemManager = new SystemManager();
		entityManager = new EntityManager();
	}


	@Test
	public void testAddSystem() {
		// TODO: Add asserts to SystemManagerTest
		
		InputLogicSystem ils = new InputLogicSystem(entityManager);
		systemManager.addSystem(ils, 0);

		System.out.println(systemManager);
		System.out.println("");

		MovementSystem ms = new MovementSystem(entityManager);
		systemManager.addSystem(ms, 4);

		System.out.println(systemManager);
		System.out.println("");

		PatrolLogicSystem pls = new PatrolLogicSystem(entityManager);
		systemManager.addSystem(pls, 2);

		System.out.println(systemManager);
		System.out.println("");

		CollisionSystem cs = new CollisionSystem(entityManager);
		systemManager.addSystem(cs, 3);

		System.out.println(systemManager);
		System.out.println("");

		// Test adding a duplicate
		CollisionSystem cs2 = new CollisionSystem(entityManager);
		systemManager.addSystem(cs2, 1);

		System.out.println(systemManager);
		System.out.println("");

		systemManager.removeSystem(PatrolLogicSystem.class);

		System.out.println(systemManager);
		System.out.println("");

	}
}
