package com.tealduck.game.engine;


import java.util.HashMap;


/**
 * Stores the events performable on entities and provides an API for triggering events.
 */
public class EventManager {
	// TODO: EventManager
	// Map from entities to (map from event names to functions)
	private HashMap<Integer, HashMap<String, IEvent>> events;
	private EntityManager entityManager;
	private EntityTagManager entityTagManager;


	public EventManager(EntityManager entityManager, EntityTagManager entityTagManager) {
		events = new HashMap<Integer, HashMap<String, IEvent>>();
		this.entityManager = entityManager;
		this.entityTagManager = entityTagManager;
	}


	/**
	 * @param entity
	 * @param name
	 * @param function
	 * 
	 */
	public void addEvent(int entity, String name, IEvent function) {
		if (name == null) {
			throw new IllegalArgumentException("name is null");
		}
		if (function == null) {
			throw new IllegalArgumentException("function is null");
		}

		HashMap<String, IEvent> entityEvents = events.get(entity);
		if (entityEvents == null) {
			entityEvents = new HashMap<String, IEvent>();
			events.put(entity, entityEvents);
		}

		entityEvents.put(name, function);
	}


	/**
	 * @param senderEntity
	 * @param receiverEntity
	 * @param name
	 * @return
	 */
	public boolean triggerEvent(int senderEntity, int receiverEntity, String name) {
		// TODO: Probably add another parameter for extra data to pass to the function
		// Currently not sure how to best handle this

		if (name == null) {
			throw new IllegalArgumentException("name is null");
		}

		HashMap<String, IEvent> receiverEvents = events.get(receiverEntity);
		if (receiverEvents == null) {
			throw new IllegalArgumentException(
					"Receiver entity " + receiverEntity + " doesn't have any events");
		}

		IEvent function = receiverEvents.get(name);
		if (function == null) {
			throw new IllegalArgumentException(
					"Receiver entity " + receiverEntity + " can't respond to " + name);
		}

		boolean keepEntityAlive = function.fire(entityManager, entityTagManager, senderEntity, receiverEntity);
		if (!keepEntityAlive) {
			removeEntity(receiverEntity);
			entityManager.removeEntity(receiverEntity);
			entityTagManager.removeTagsAssociatedWithEntity(receiverEntity);
		}

		return keepEntityAlive;
	}


	/**
	 * @param entity
	 * @param name
	 * @return
	 */
	public IEvent removeEvent(int entity, String name) {
		if (name == null) {
			throw new IllegalArgumentException("name is null");
		}

		HashMap<String, IEvent> entityEvents = events.get(entity);
		if (entityEvents != null) {
			return entityEvents.remove(name);
		}
		return null;
	}


	/**
	 * @param entity
	 * @return
	 */
	public HashMap<String, IEvent> removeEntity(int entity) {
		// TODO: What type should this return (+ update class diagrams in report)
		return events.remove(entity);
	}


	/**
	 * 
	 */
	public void clear() {
		events.clear();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "EventManager(" + events.toString() + ")";
	}
}
