package com.tealduck.game.engine;


import java.util.HashSet;


/**
 * Used to group entity manager, entity tag manager and event manager together as these classes are often passed around
 * together.
 */
public class EntityEngine {
	private EntityManager entityManager;
	private EntityTagManager entityTagManager;
	private EventManager eventManager;

	private HashSet<Integer> entitiesToRemove;


	/**
	 *
	 */
	public EntityEngine() {
		entityManager = new EntityManager();
		entityTagManager = new EntityTagManager();
		eventManager = new EventManager(this);

		entitiesToRemove = new HashSet<Integer>();
	}


	/**
	 * @param entityManager
	 * @param entityTagManager
	 * @param eventManager
	 */
	public EntityEngine(EntityManager entityManager, EntityTagManager entityTagManager, EventManager eventManager) {
		this.entityManager = entityManager;
		this.entityTagManager = entityTagManager;
		this.eventManager = eventManager;
	}


	/**
	 * Add the entity to the set of entities to remove at the end of the frame.
	 *
	 * @param entity
	 */
	public void flagEntityToRemove(int entity) {
		entitiesToRemove.add(entity);
	}


	/**
	 * @param entity
	 * @return
	 */
	public boolean isEntityFlaggedToRemove(int entity) {
		return entitiesToRemove.contains(entity);
	}


	/**
	 * Removes the entities in the entitiesToRemove set.
	 */
	public void removeAllFlaggedEntities() {
		for (int entity : entitiesToRemove) {
			removeEntity(entity);
		}
		entitiesToRemove.clear();
	}


	/**
	 * @param entity
	 */
	private void removeEntity(int entity) {
		entityManager.removeEntityWithTag(entity, entityTagManager);
		eventManager.removeEventsForEntity(entity);
	}


	/**
	 * Calls clear on the entity manager, entity tag manager and event manager.
	 */
	public void clear() {
		entityManager.clear();
		entityTagManager.clear();
		eventManager.clear();
	}


	/**
	 * @return
	 */
	public EntityManager getEntityManager() {
		return entityManager;
	}


	/**
	 * @return
	 */
	public EntityTagManager getEntityTagManager() {
		return entityTagManager;
	}


	/**
	 * @return
	 */
	public EventManager getEventManager() {
		return eventManager;
	}
}
