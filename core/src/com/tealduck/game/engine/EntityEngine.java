package com.tealduck.game.engine;


/**
 * Used to group entity manager, entity tag manager and event manager together as these classes are often passed around
 * together.
 */
public class EntityEngine {
	private EntityManager entityManager;
	private EntityTagManager entityTagManager;
	private EventManager eventManager;


	public EntityEngine() {
		entityManager = new EntityManager();
		entityTagManager = new EntityTagManager();
		eventManager = new EventManager(entityManager, entityTagManager);
	}


	public EntityEngine(EntityManager entityManager, EntityTagManager entityTagManager, EventManager eventManager) {
		this.entityManager = entityManager;
		this.entityTagManager = entityTagManager;
		this.eventManager = eventManager;
	}


	public void clear() {
		entityManager.clear();
		entityTagManager.clear();
		eventManager.clear();
	}


	public EntityManager getEntityManager() {
		return entityManager;
	}


	public EntityTagManager getEntityTagManager() {
		return entityTagManager;
	}


	public EventManager getEventManager() {
		return eventManager;
	}
}
