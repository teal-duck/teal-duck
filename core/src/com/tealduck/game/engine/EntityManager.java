package com.tealduck.game.engine;


import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class EntityManager {
	private Set<Integer> entities;
	private Map<Class<? extends Component>, HashMap<Integer, ? extends Component>> componentMap;
	private int nextEntityId = 0;


	public EntityManager() {
		entities = new HashSet<Integer>();
		componentMap = new HashMap<Class<? extends Component>, HashMap<Integer, ? extends Component>>();
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
	public <T extends Component> void addComponent(int entity, T component) {
		// TODO: addComponent
		return;
	}


	/**
	 * @param entity
	 * @param componentType
	 * @return
	 */
	public <T extends Component> T removeComponent(int entity, Class<T> componentType) {
		// TODO: removeComponent
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
		// TODO: entityHasComponent
		return false;
	}


	/**
	 * @param entity
	 * @param componentType
	 * @return
	 */
	public <T extends Component> T getComponent(int entity, Class<T> componentType) {
		// TODO: getComponent
		return null;
	}


	/**
	 * @param entity
	 * @return
	 */
	public <T extends Component> HashMap<Class<T>, T> getAllComponentsForEntitiy(int entity) {
		// TODO: getAllComponentsForEntity
		// TODO: fix type of getAllComponentsForEntity in the submitted
		// diagram
		return null;
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
		componentMap.clear();
		nextEntityId = 0;
		return;
	}
}
