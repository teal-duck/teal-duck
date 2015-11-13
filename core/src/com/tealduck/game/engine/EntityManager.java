package com.tealduck.game.engine;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.Set;


public class EntityManager {
	// TODO: fix type of getAllComponentsForEntity in the submitted diagram

	private Set<Integer> entities;
	private Map<Class<? extends Component>, HashMap<Integer, ? extends Component>> componentStore;
	private int lowestUnassignedEntityId = 0;


	public EntityManager() {
		entities = new HashSet<Integer>();
		componentStore = new HashMap<Class<? extends Component>, HashMap<Integer, ? extends Component>>();
		lowestUnassignedEntityId = 0;

		clear();
	}


	public <T extends Component> T getComponent(int entity, Class<T> componentType) {
		// TODO: getComponent
		return null;
	}


	public <T extends Component> List<T> getAllComponentsOfType(Class<T> componentType) {
		// TODO: getAllComponentsOfType
		return null;
	}


	public <T extends Component> Set<Integer> getAllEntitiesPossessingComponent(Class<T> componentType) {
		// TODO: getAllEntitiesPossessingComponent
		return null;
	}


	public <T extends Component> Set<Integer> getAllEntitiesPossessingComponents(Class<T>[] componentTypes) {
		// TODO: getAllEntitiesPossessingComponents
		return null;
	}


	public <T extends Component> HashMap<Class<T>, T> getAllComponentsForEntitiy(int entity) {
		// TODO: getAllComponentsForEntity
		return null;
	}


	public <T extends Component> boolean entityHasComponent(int entity, Class<T> componentType) {
		// TODO: entityHasComponent
		return false;
	}


	public <T extends Component> void addComponent(int entity, T component) {
		// TODO: addComponent
		return;
	}


	public <T extends Component> T removeComponent(int entity, Class<T> componentType) {
		// TODO: removeComponent
		return null;
	}


	public int createEntity() {
		int id = lowestUnassignedEntityId;
		lowestUnassignedEntityId += 1;
		entities.add(id);
		return id;
	}


	public int removeEntity(int entity) {
		// TODO: removeEntity
		return 0;
	}


	public Set<Integer> getEntities() {
		return entities;
	}


	public void clear() {
		entities.clear();
		componentStore.clear();
		lowestUnassignedEntityId = 0;
		return;
	}
}
