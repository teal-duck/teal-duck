package com.tealduck.game.engine;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Provides a ordered data structure and iterator for game systems.
 */
public class SystemManager implements Iterable<System> {
	private List<SystemWithPriority<? extends System>> systems;


	public SystemManager() {
		systems = new ArrayList<SystemWithPriority<? extends System>>();
	}


	/**
	 * Adds the system to the list of systems based on priority. If there already is a system of the same type, it
	 * is removed and returned. Priority is in ascending order (i.e. 0 comes before 1).
	 *
	 * @param <T>
	 *                T extends {@link System}
	 * @param system
	 *                system to add
	 * @param priority
	 *                priority to add system with
	 * @return system that was removed if there was one, else null
	 * @throws IllegalArgumentException
	 *                 if system is null
	 */
	public <T extends System> T addSystem(T system, int priority) {
		if (system == null) {
			throw new IllegalArgumentException("system is null");
		}

		SystemWithPriority<T> systemWithPriority = new SystemWithPriority<T>(system, priority);

		int addAtIndex = -1;
		int removeAtIndex = -1;

		for (int i = 0, l = systems.size(); i < l; i += 1) {
			SystemWithPriority<? extends System> s = systems.get(i);

			if (system.getClass().equals(s.system.getClass())) {
				removeAtIndex = i;
			}

			if ((addAtIndex < 0) && (priority < s.priority)) {
				addAtIndex = i;
			}

		}

		T removed = null;
		if (removeAtIndex >= 0) {
			@SuppressWarnings("unchecked")
			SystemWithPriority<T> removedWithPriority = (SystemWithPriority<T>) systems
					.remove(removeAtIndex);
			removed = removedWithPriority.system;
		}

		if (addAtIndex < 0) {
			addAtIndex = systems.size();
		}

		systems.add(addAtIndex, systemWithPriority);

		return removed;
	}


	/**
	 * Removes a system of the given type if there is one. Returns the system it removed.
	 *
	 * @param <T>
	 *                T extends {@link System}
	 * @param systemType
	 *                the type of system to remove
	 * @return the system that was removed or null
	 * @throws IllegalArgumentException
	 *                 if systemType is null
	 */
	@SuppressWarnings("unchecked")
	public <T extends System> T removeSystem(Class<T> systemType) {
		if (systemType == null) {
			throw new IllegalArgumentException("systemType is null");
		}
		int index = -1;

		for (int i = 0, l = systems.size(); i < l; i += 1) {
			if (systems.get(i).system.getClass().equals(systemType)) {
				index = i;
				break;
			}
		}

		if (index >= 0) {
			SystemWithPriority<T> systemWithPriority = (SystemWithPriority<T>) systems.remove(index);
			return systemWithPriority.system;
		}

		return null;
	}


	/**
	 * Clears the list of systems
	 */
	public void clear() {
		systems.clear();
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
	public Iterator<System> iterator() {
		List<System> pureSystems = new ArrayList<System>();

		for (SystemWithPriority<? extends System> pureSystem : systems) {
			pureSystems.add(pureSystem.system);
		}

		return pureSystems.iterator();
	}


	@Override
	public String toString() {
		return "SystemManager(" + systems.toString() + ")";
	}


	/**
	 * Internal class used to pair a system with its priority
	 *
	 * @param <T>
	 *                T extends {@link System}
	 */
	private class SystemWithPriority<T extends System> {
		public T system;
		public int priority;


		public SystemWithPriority(T system, int priority) {
			this.system = system;
			this.priority = priority;
		}


		@Override
		public String toString() {
			return "SystemWithPriority(" + system.toString() + ", " + priority + ")";
		}
	}
}