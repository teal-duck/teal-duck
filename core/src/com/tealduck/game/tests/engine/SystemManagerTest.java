package com.tealduck.game.tests.engine;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.GameSystem;
import com.tealduck.game.engine.SystemManager;


public class SystemManagerTest {
	private SystemManager systemManager;
	private EntityEngine entityEngine;
	
	public class TestSystem extends GameSystem {
		
		public TestSystem(EntityEngine entityEngine) {
			super(entityEngine);
		}

		@Override
		public void update(float deltaTime) {
			Assert.fail();
		}
	}


	@Before
	public void setup() {
		systemManager = new SystemManager();
		entityEngine = new EntityEngine();
	}
	
	@Test
	public void testAddSystem() {
		TestSystem testSystem = new TestSystem(entityEngine);
		String managerBeforeAdd = systemManager.toString();
		systemManager.addSystem(testSystem, 0);
		String managerAfterAdd = systemManager.toString();
		
		Assert.assertNotEquals(managerBeforeAdd, managerAfterAdd);
	}
	
	@Test
	public void testGetSystem() {
		TestSystem testSystem = new TestSystem(entityEngine);
		systemManager.addSystem(testSystem, 0);
		System.out.println(systemManager);
		Assert.assertEquals(testSystem, systemManager.getSystemOfType(TestSystem.class));
		
	}
	
	@Test
	public void testRemoveSystem() {
		TestSystem testSystem = new TestSystem(entityEngine);
		systemManager.addSystem(testSystem, 0);
		
		systemManager.removeSystem(TestSystem.class);
		
		try {
			// systemManager should throw a NullPointerException, as the 
			// system should no longer exist
			
			systemManager.getSystemOfType(TestSystem.class).update(0.1f);
		} catch (NullPointerException e) {
			
		}
	}
}
