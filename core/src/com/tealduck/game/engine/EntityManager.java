package com.tealduck.game.engine;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


public class EntityManager {
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
		// TODO: remove entity
		entities.remove(entity);

		return 0;
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
		// TODO: getAllComponentsForEntity
		// TODO: fix type of getAllComponentsForEntity in the submitted
		// diagram

		HashMap<Class<? extends Component>, Component> components = new HashMap<Class<? extends Component>, Component>();

		Iterator<Entry<Class<? extends Component>, HashMap<Integer, ? extends Component>>> it = componentsMap
				.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<Class<? extends Component>, HashMap<Integer, ? extends Component>> pair = (Map.Entry<Class<? extends Component>, HashMap<Integer, ? extends Component>>) it.next();

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
	public <T extends Component> List<T> getAllComponentsOfType(Class<T> componentType) {
		// TODO: getAllComponentsOfType
		return null;
	}


	/**
	 * @param componentType
	 * @return
	 */
	public <T extends Component> Set<Integer> getAllEntitiesPossessingComponent(Class<T> componentType) {
		// TODO: getAllEntitiesPossessingComponent
		return null;
	}


	/**
	 * @param componentTypes
	 * @return
	 */
	public <T extends Component> Set<Integer> getAllEntitiesPossessingComponents(Class<T>[] componentTypes) {
		// TODO: getAllEntitiesPossessingComponents
		return null;
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
