package com.tealduck.game.engine;


import static java.lang.System.out;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Provides a ordered set for ordering systems.
 */
public class SystemManager implements Iterable<System> {
	// TODO: SystemManager

	private List<SystemWithPriority<? extends System>> systems;


	public SystemManager() {
		systems = new ArrayList<SystemWithPriority<? extends System>>();
	}


	/**
	 * @param system
	 * @param priority
	 */
	public <T extends System> void addSystem(T system, int priority) {
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

		if (removeAtIndex >= 0) {
			out.println("Removing at " + removeAtIndex);
			systems.remove(removeAtIndex);
		}

		if (addAtIndex < 0) {
			out.println("No addAtIndex found, adding to end");
			addAtIndex = systems.size();
		}

		out.println("Adding " + system.toString() + " with priority " + priority + " at " + addAtIndex);
		systems.add(addAtIndex, systemWithPriority);
	}


	/**
	 * @param systemType
	 * @return
	 */
	public <T extends System> T removeSystem(Class<T> systemType) {
		return null;
	}


	/**
	 * @return
	 */
	public List<SystemWithPriority<? extends System>> getSystems() {
		return systems;
	}


	/**
	 * 
	 */
	public void clear() {
		systems.clear();
	}


	/* (non-Javadoc)
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