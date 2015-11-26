package com.tealduck.game.tests.engine;


import org.junit.Before;
import org.junit.Test;

import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EntityTagManager;
import com.tealduck.game.engine.EventManager;
import com.tealduck.game.engine.IEvent;

import junit.framework.Assert;


@SuppressWarnings("deprecation")
public class EventManagerTest {
	// TODO: EventManager tests
	private EntityManager entityManager;
	private EventManager eventManager;


	@Before
	public void setup() {
		entityManager = new EntityManager();
		eventManager = new EventManager(entityManager, null);
	}


	@Test
	public void testAddAndTriggerEvent() {
		int entity1 = entityManager.createEntity();
		int entity2 = entityManager.createEntity();

		String testEvent1Name = "TEST_EVENT_1";
		IEvent testEvent1 = new IEvent() {
			@Override
			public boolean fire(EntityManager entityManager, EntityTagManager entityTagManager, int sender,
					int receiver) {
				System.out.println("[Test event 1] Receiver " + receiver + " got event from sender "
						+ sender);
				return true;
			}
		};
		eventManager.addEvent(entity1, testEvent1Name, testEvent1);

		String testEvent2Name = "TEST_EVENT_2";
		IEvent testEvent2 = new IEvent() {
			@Override
			public boolean fire(EntityManager entityManager, EntityTagManager entityTagManager, int sender,
					int receiver) {
				System.out.println("[Test event 2] Receiver " + receiver + " got event from sender "
						+ sender);
				return true;

			}
		};
		eventManager.addEvent(entity2, testEvent2Name, testEvent2);

		eventManager.triggerEvent(entity2, entity1, testEvent1Name);
		eventManager.triggerEvent(entity1, entity2, testEvent2Name);

		// Try to trigger an event that doesn't exist
		try {
			eventManager.triggerEvent(entity2, entity1, testEvent2Name);
			Assert.assertTrue(false);
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(true);
		}
	}
}
