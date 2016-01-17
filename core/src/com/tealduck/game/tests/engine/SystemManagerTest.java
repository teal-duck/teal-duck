package com.tealduck.game.tests.engine;


import org.junit.Before;
import org.junit.Test;

import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.SystemManager;
import com.tealduck.game.system.ChaseSystem;
import com.tealduck.game.system.InputLogicSystem;
import com.tealduck.game.system.MovementSystem;
import com.tealduck.game.system.WorldCollisionSystem;


public class SystemManagerTest {
	private SystemManager systemManager;
	private EntityEngine entityEngine;


	@Before
	public void setup() {
		systemManager = new SystemManager();
		entityEngine = new EntityEngine();
	}


	@Test
	public void testAddSystem() {
		InputLogicSystem ils = new InputLogicSystem(entityEngine, null);
		systemManager.addSystem(ils, 0);

		System.out.println(systemManager);
		System.out.println("");

		MovementSystem ms = new MovementSystem(entityEngine);
		systemManager.addSystem(ms, 4);

		System.out.println(systemManager);
		System.out.println("");

		ChaseSystem pls = new ChaseSystem(entityEngine, null);
		systemManager.addSystem(pls, 2);

		System.out.println(systemManager);
		System.out.println("");

		WorldCollisionSystem cs = new WorldCollisionSystem(entityEngine, null);
		systemManager.addSystem(cs, 3);

		System.out.println(systemManager);
		System.out.println("");

		// Test adding a duplicate
		WorldCollisionSystem cs2 = new WorldCollisionSystem(entityEngine, null);
		systemManager.addSystem(cs2, 1);

		System.out.println(systemManager);
		System.out.println("");

		systemManager.removeSystem(ChaseSystem.class);

		System.out.println(systemManager);
		System.out.println("");

	}
}
