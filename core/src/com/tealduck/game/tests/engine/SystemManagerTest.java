package com.tealduck.game.tests.engine;


import org.junit.Before;
import org.junit.Test;

import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EntityTagManager;
import com.tealduck.game.engine.EventManager;
import com.tealduck.game.engine.SystemManager;
import com.tealduck.game.system.CollisionSystem;
import com.tealduck.game.system.InputLogicSystem;
import com.tealduck.game.system.MovementSystem;
import com.tealduck.game.system.PatrolLogicSystem;


public class SystemManagerTest {
	// TODO: SystemManager tests

	private SystemManager systemManager;
	private EntityManager entityManager;
	private EntityTagManager entityTagManager;
	private EventManager eventManager;


	@Before
	public void setup() {
		systemManager = new SystemManager();
		entityManager = new EntityManager();
		entityTagManager = new EntityTagManager();
		eventManager = new EventManager(entityManager, entityTagManager);
	}


	@Test
	public void testAddSystem() {
		EntityEngine entityEngine = new EntityEngine(entityManager, entityTagManager, eventManager);

		InputLogicSystem ils = new InputLogicSystem(entityEngine);
		systemManager.addSystem(ils, 0);

		System.out.println(systemManager);
		System.out.println("");

		MovementSystem ms = new MovementSystem(entityEngine);
		systemManager.addSystem(ms, 4);

		System.out.println(systemManager);
		System.out.println("");

		PatrolLogicSystem pls = new PatrolLogicSystem(entityEngine);
		systemManager.addSystem(pls, 2);

		System.out.println(systemManager);
		System.out.println("");

		CollisionSystem cs = new CollisionSystem(entityEngine);
		systemManager.addSystem(cs, 3);

		System.out.println(systemManager);
		System.out.println("");

		// Test adding a duplicate
		CollisionSystem cs2 = new CollisionSystem(entityEngine);
		systemManager.addSystem(cs2, 1);

		System.out.println(systemManager);
		System.out.println("");

		systemManager.removeSystem(PatrolLogicSystem.class);

		System.out.println(systemManager);
		System.out.println("");

	}
}
