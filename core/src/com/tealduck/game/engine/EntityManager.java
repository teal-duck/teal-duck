package com.tealduck.game.engine;


import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * Stores the entities and the links between them and their components.
 *
 */
public class EntityManager {
	// TODO: Fix type of getAllComponentsForEntity in the submitted diagram
	// TODO: Maybe add a method that returns HashMap<Integer, ? extends Component>
	// TODO: Fix the @link tags in javadocs

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
	 * Reserves an ID for a new entity. Adds this to the set of entities. IDs start at 0 and increment each time a
	 * new entity is requested.
	 *
	 * @return the reserved ID
	 */
	public int createEntity() {
		int id = nextEntityId;
		nextEntityId += 1;
		entities.add(id);
		return id;
	}


	/**
	 * Shortcut method for creating an entity and assigning it a tag.
	 *
	 * @param entityTagManager
	 * @param tag
	 * @return the reserved ID
	 * @see {@link EntityTagManager#createEntity}
	 */
	public int createEntityWithTag(EntityTagManager entityTagManager, String tag) {
		int id = createEntity();
		entityTagManager.addTag(tag, id);
		return id;
	}


	/**
	 * Removes an entity and all its associated components.
	 *
	 * @param entity
	 *                ID of the entity to remove
	 * @return ID of entity removed
	 * @throws IllegalArgumentException
	 *                 if entity isn't in set of entities
	 */
	public int removeEntity(int entity) {
		if (!entityExists(entity)) {
			throw new IllegalArgumentException("entity " + entity + " doesn't exist");
		}
		entities.remove(entity);

		for (HashMap<Integer, ? extends Component> map : componentsMap.values()) {
			map.remove(entity);
		}

		return entity;
	}


	/**
	 * Removes an entity and its associated tags.
	 *
	 * @param entity
	 * @param entityTagManager
	 * @return
	 */
	public int removeEntityWithTag(int entity, EntityTagManager entityTagManager) {
		removeEntity(entity);
		entityTagManager.removeTagsAssociatedWithEntity(entity);

		return entity;
	}


	/**
	 * Gets the set of entities
	 *
	 * @return the set of entities
	 */
	public Set<Integer> getEntities() {
		return entities;
	}


	/**
	 * Gets the amount of entities
	 *
	 * @return the amount of entities
	 */
	public int getEntityCount() {
		return entities.size();
	}


	/**
	 * Returns true if the given entity exists.
	 *
	 * @param entity
	 *                ID of the entity to check
	 * @return whether the entity exists
	 */
	public boolean entityExists(int entity) {
		return entities.contains(entity);
	}


	/**
	 * Attaches the component to the given entity.
	 *
	 * @param <T>
	 *                T extends {@link Component}
	 * @param entity
	 *                ID of the entity
	 * @param component
	 *                component to add
	 * @throws IllegalArgumentException
	 *                 if entity isn't in set of entities
	 * @throws IllegalArgumentException
	 *                 if component is null
	 */
	@SuppressWarnings("unchecked")
	public <T extends Component> void addComponent(int entity, T component) {
		if (!entityExists(entity)) {
			throw new IllegalArgumentException("entity " + entity + " doesn't exist");
		}
		if (component == null) {
			throw new IllegalArgumentException("component is null");
		}

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
	 * Removes the instance of this component type associated with the entity.
	 *
	 * @param <T>
	 *                T extends {@link Component}
	 * @param entity
	 *                ID of entity whose component instance should be removed
	 * @param componentType
	 *                type of component to remove
	 * @return the component that was removed or null if this entity didn't have this type of component
	 * @throws IllegalArgumentException
	 *                 if entity isn't in set of entities
	 * @throws IllegalArgumentException
	 *                 if componentType is null
	 */
	@SuppressWarnings("unchecked")
	public <T extends Component> T removeComponent(int entity, Class<T> componentType) {
		if (!entityExists(entity)) {
			throw new IllegalArgumentException("entity " + entity + " doesn't exist");
		}
		if (componentType == null) {
			throw new IllegalArgumentException("componentType is null");
		}

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
	 * Returns true if the entity has an instance of this component type.
	 *
	 * @param <T>
	 *                T extends {@link Component}
	 * @param entity
	 *                ID of entity to check
	 * @param componentType
	 *                type of component to check
	 * @return true if the entity has an instance of this component
	 * @throws IllegalArgumentException
	 *                 if entity isn't in set of entities
	 * @throws IllegalArgumentException
	 *                 if componentType is null
	 */
	public <T extends Component> boolean entityHasComponent(int entity, Class<T> componentType) {
		if (!entityExists(entity)) {
			throw new IllegalArgumentException("entity " + entity + " doesn't exist");
		}
		if (componentType == null) {
			throw new IllegalArgumentException("componentType is null");
		}

		return componentsMap.containsKey(componentType) && componentsMap.get(componentType).containsKey(entity);
	}


	/**
	 * Gets the instance of a component for the entity.
	 *
	 * @param <T>
	 *                T extends {@link Component}
	 * @param entity
	 *                ID of entity
	 * @param componentType
	 *                type of component to get
	 * @return the component instance
	 * @throws IllegalArgumentException
	 *                 if entity isn't in set of entities
	 * @throws IllegalArgumentException
	 *                 if componentType is null
	 * @throws IllegalArgumentException
	 *                 if no components of type componentType exist
	 * @throws IllegalArgumentException
	 *                 if entity doesn't have a component of type componentType
	 *
	 */
	@SuppressWarnings("unchecked")
	public <T extends Component> T getComponent(int entity, Class<T> componentType)
			throws IllegalArgumentException {
		if (!entityExists(entity)) {
			throw new IllegalArgumentException("entity " + entity + " doesn't exist");
		}
		if (componentType == null) {
			throw new IllegalArgumentException("componentType is null");
		}

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
	 * Gets all the components that make up the entity.
	 *
	 * @param entity
	 *                ID of entity whose components to get
	 * @return a map from the component type to the component
	 * @throws IllegalArgumentException
	 *                 if the entity isn't in the set of entities
	 */
	public HashMap<Class<? extends Component>, ? extends Component> getEntityComponents(int entity) {
		if (!entityExists(entity)) {
			throw new IllegalArgumentException("entity " + entity + " doesn't exist");
		}

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
	 * Gets all the instances of a given component type.
	 *
	 * Call like so: getAllComponentsOfType(ExampleComponent.class);
	 *
	 * @param <T>
	 *                T extends {@link Component}
	 * @param componentType
	 *                type of components to get
	 * @return collection of component instances
	 * @throws IllegalArgumentException
	 *                 if componentType is null
	 */
	public <T extends Component> Collection<? extends Component> getAllComponentsOfType(Class<T> componentType) {
		if (componentType == null) {
			throw new IllegalArgumentException("componentType is null");
		}

		HashMap<Integer, ? extends Component> map = componentsMap.get(componentType);
		if (map == null) {
			map = new HashMap<Integer, T>();
		}

		return map.values();
	}


	/**
	 * Gets the set of entities that have an instance of the component type.
	 *
	 * @param <T>
	 *                T extends {@link Component}
	 * @param componentType
	 *                type of component
	 * @return set of entity IDs
	 * @throws IllegalArgumentException
	 *                 if componentType is null
	 * @see {@link EntityManager#getEntitiesWithComponents}
	 */
	public <T extends Component> Set<Integer> getEntitiesWithComponent(Class<T> componentType) {
		if (componentType == null) {
			throw new IllegalArgumentException("componentType is null");
		}

		HashMap<Integer, ? extends Component> map = componentsMap.get(componentType);
		if (map == null) {
			map = new HashMap<Integer, T>();
		}
		return map.keySet();
	}


	/**
	 * Gets the set of entities that have an instance of all the component types. Performs set intersection using
	 * {@link EntityManager#getEntitiesWithComponent}.
	 *
	 * @param <T>
	 *                T extends {@link Component}
	 * @param componentTypes
	 *                types of components
	 * @return set of entity IDs
	 * @see {@link EntityManager#getEntitiesWithComponent}.
	 */
	public Set<Integer> getEntitiesWithComponents(Class<? extends Component>... componentTypes) {
		Set<Integer> entities = new HashSet<Integer>(this.entities);

		for (Class<? extends Component> componentType : componentTypes) {
			entities.retainAll(getEntitiesWithComponent(componentType));
		}

		return entities;
	}


	/**
	 * Clears the set of entities and the links between entities and components, and resets the starting ID to 0.
	 * Recommended to call {@link EntityTagManager#clear()} with this method because the IDs stored there are now
	 * invalid.
	 *
	 * @see {@link EntityTagManager#clear()}
	 */
	public void clear() {
		entities.clear();
		componentsMap.clear();
		nextEntityId = 0;
		return;
	}
}
