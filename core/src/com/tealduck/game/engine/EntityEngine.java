package com.tealduck.game.engine;


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
