package com.tealduck.game.engine;


import java.util.HashMap;


/**
 * Stores the events performable on entities and provides an API for triggering events.
 */
public class EventManager {
	// Map from entities to (map from event names to functions)
	private HashMap<Integer, HashMap<String, IEvent>> events;
	private EntityEngine entityEngine;


	/**
	 * @param entityEngine
	 */
	public EventManager(EntityEngine entityEngine) {
		events = new HashMap<Integer, HashMap<String, IEvent>>();
		this.entityEngine = entityEngine;
	}


	/**
	 * @param entity
	 * @param name
	 * @param function
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
	 * Calls {@link EventManager#triggerEvent(int, int, String, Object)} with null data.
	 *
	 * @param senderEntity
	 * @param receiverEntity
	 * @param name
	 * @return
	 */
	public boolean triggerEvent(int senderEntity, int receiverEntity, String name) {
		return triggerEvent(senderEntity, receiverEntity, name, null);
	}


	/**
	 * @param senderEntity
	 * @param receiverEntity
	 * @param name
	 * @return True if the receiver entity is to be removed, else false.
	 * @throws IllegalArgumentException
	 *                 if name is null
	 */
	public boolean triggerEvent(int senderEntity, int receiverEntity, String name, Object data) {
		if (name == null) {
			throw new IllegalArgumentException("name is null");
		}

		HashMap<String, IEvent> receiverEvents = events.get(receiverEntity);
		if (receiverEvents == null) {
			return false;
		}

		IEvent function = receiverEvents.get(name);
		if (function == null) {
			return false;
		}

		boolean killEntity = function.fire(entityEngine, senderEntity, receiverEntity, data);
		if (killEntity) {
			entityEngine.flagEntityToRemove(receiverEntity);
		}

		return killEntity;
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
