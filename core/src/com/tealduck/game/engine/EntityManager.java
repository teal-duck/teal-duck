package com.tealduck.game.engine;


import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class EntityManager {
	// TODO: Javadoc
	// TODO: Fix type of getAllComponentsForEntity in the submitted diagram
	// TODO: Maybe add a method that returns HashMap<Integer, ? extends Component>

	private Set<Integer> entities;
	private HashMap<Class<? extends Component>, HashMap<Integer, ? extends Component>> componentsMap;
	private int nextEntityId = 0;


	public EntityManager() {
		entities = new HashSet<Integer>();
		componentsMap = new HashMap<Class<? extends Component>, HashMap<Integer, ? extends Component>>();
		nextEntityId = 0;

		clear();
	}


	/**
	 * @return
	 */
	public int createEntity() {
		int id = nextEntityId;
		nextEntityId += 1;
		entities.add(id);
		return id;
	}


	/**
	 * @param entity
	 * @return
	 */
	public int removeEntity(int entity) {
		entities.remove(entity);

		for (HashMap<Integer, ? extends Component> map : componentsMap.values()) {
			map.remove(entity);
		}

		return entity;
	}


	/**
	 * @param entity
	 * @param component
	 */
	@SuppressWarnings("unchecked")
	public <T extends Component> void addComponent(int entity, T component) {
		Class<? extends Component> componentType = component.getClass();

		if (componentsMap.containsKey(componentType)) {
			((HashMap<Integer, T>) componentsMap.get(componentType)).put(entity, component);
		} else {
			HashMap<Integer, T> map = new HashMap<Integer, T>();
			map.put(entity, component);
			componentsMap.put(componentType, map);
		}
	}


	/**
	 * @param entity
	 * @param componentType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Component> T removeComponent(int entity, Class<T> componentType) {
		HashMap<Integer, ? extends Component> map = componentsMap.get(componentType);

		if (map != null) {
			Component component = map.remove(entity);
			if (component != null) {
				return (T) component;
			}
		}

		return null;
	}


	/**
	 * @return
	 */
	public Set<Integer> getEntities() {
		return entities;
	}


	/**
	 * @return
	 */
	public int getEntityCount() {
		return entities.size();
	}


	/**
	 * @param entity
	 * @param componentType
	 * @return
	 */
	public <T extends Component> boolean entityHasComponent(int entity, Class<T> componentType) {
		return componentsMap.containsKey(componentType) && componentsMap.get(componentType).containsKey(entity);
	}


	/**
	 * @param entity
	 * @param componentType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(int entity, Class<T> componentType)
			throws IllegalArgumentException {
		HashMap<Integer, ? extends Component> map = componentsMap.get(componentType);

		if (map == null) {
			throw new IllegalArgumentException(
					"No components of type " + componentType.toString() + " exist");
		}

		Component component = map.get(entity);
		if (component == null) {
			throw new IllegalArgumentException("Entity " + entity + " doesn't have component of type "
					+ componentType.toString());
		}

		return (T) component;
	}


	/**
	 * @param entity
	 * @return
	 */
	public HashMap<Class<? extends Component>, ? extends Component> getAllComponentsForEntity(int entity) {
		HashMap<Class<? extends Component>, Component> components = new HashMap<Class<? extends Component>, Component>();

		Iterator<Entry<Class<? extends Component>, HashMap<Integer, ? extends Component>>> it = componentsMap
				.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Class<? extends Component>, HashMap<Integer, ? extends Component>> pair = it.next();

			Class<? extends Component> key = pair.getKey();
			HashMap<Integer, ? extends Component> value = pair.getValue();

			if (value.containsKey(entity)) {
				components.put(key, value.get(entity));
			}
		}

		return components;
	}


	/**
	 * @param componentType
	 * @return
	 */
	public <T extends Component> Collection<? extends Component> getAllComponentsOfType(Class<T> componentType) {
		HashMap<Integer, ? extends Component> map = componentsMap.get(componentType);
		if (map != null) {
			return map.values();
		}

		return null;
	}


	/**
	 * @param componentType
	 * @return
	 */
	public <T extends Component> Set<Integer> getAllEntitiesPossessingComponent(Class<T> componentType) {
		HashMap<Integer, ? extends Component> map = componentsMap.get(componentType);
		if (map != null) {
			return map.keySet();
		}

		return null;
	}


	/**
	 * @param componentTypes
	 * @return
	 */
	public Set<Integer> getAllEntitiesPossessingComponents(Class<? extends Component>... componentTypes) {
		Set<Integer> entities = new HashSet<Integer>();

		for (Class<? extends Component> componentType : componentTypes) {
			entities.addAll(getAllEntitiesPossessingComponent(componentType));
		}

		return entities;
	}


	/**
	 *
	 */
	public void clear() {
		entities.clear();
		componentsMap.clear();
		nextEntityId = 0;
		return;
	}
}
