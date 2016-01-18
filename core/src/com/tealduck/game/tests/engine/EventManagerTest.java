package com.tealduck.game.tests.engine;


import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EventManager;
import com.tealduck.game.engine.IEvent;


public class EventManagerTest {
	// TODO: EventManager tests
	private EntityEngine entityEngine;


	@Before
	public void setup() {
		entityEngine = new EntityEngine();
	}


	@Test
	public void testAddAndTriggerEvent() {
		EntityManager entityManager = entityEngine.getEntityManager();
		EventManager eventManager = entityEngine.getEventManager();

		int entity1 = entityManager.createEntity();
		int entity2 = entityManager.createEntity();

		String testEvent1Name = "TEST_EVENT_1";
		IEvent testEvent1 = new IEvent() {
			@Override
			public boolean fire(EntityEngine entityEngine, int sender, int receiver, Object data) {
				System.out.println("[Test event 1] Receiver " + receiver + " got event from sender "
						+ sender);
				return false;
			}
		};
		eventManager.addEvent(entity1, testEvent1Name, testEvent1);

		String testEvent2Name = "TEST_EVENT_2";
		IEvent testEvent2 = new IEvent() {
			@Override
			public boolean fire(EntityEngine entityEngine, int sender, int receiver, Object data) {
				System.out.println("[Test event 2] Receiver " + receiver + " got event from sender "
						+ sender);
				
				// This event should never be triggered
				Assert.fail();
				return false;

			}
		};
		eventManager.addEvent(entity2, testEvent2Name, testEvent2);

		eventManager.triggerEvent(entity2, entity1, testEvent1Name);
		
		// Nothing should happen, as entity1 does not have an event of testEvent2Name
		// Triggering a non-existent event is allowed
		eventManager.triggerEvent(entity2, entity1, testEvent2Name);
	}
	
	@Test
	public void testRemoveEvent() {
		EntityManager entityManager = entityEngine.getEntityManager();
		EventManager eventManager = entityEngine.getEventManager();

		int entity1 = entityManager.createEntity();
		int entity2 = entityManager.createEntity();

		String testEvent1Name = "TEST_EVENT_1";
		IEvent testEvent1 = new IEvent() {
			@Override
			public boolean fire(EntityEngine entityEngine, int sender, int receiver, Object data) {
				System.out.println("[Test event 1] Receiver " + receiver + " got event from sender "
						+ sender);
				
				// Event should not be called, as it should be deleted
				Assert.fail();
				return false;
			}
		};
		eventManager.addEvent(entity1, testEvent1Name, testEvent1);
		eventManager.removeEvent(entity1, testEvent1Name);
		
		// Event is removed, so test should not fail
		eventManager.triggerEvent(entity2, entity1, testEvent1Name);
		
	}
}
