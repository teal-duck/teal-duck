package com.tealduck.game.engine;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Provides a ordered set for ordering systems.
 */
public class SystemManager implements Iterable<SystemAbstract> {
	// TODO: SystemManager

	private List<SystemWithPriority<? extends SystemAbstract>> systems;


	public SystemManager() {
		systems = new ArrayList<SystemWithPriority<? extends SystemAbstract>>();
	}


	public <T extends SystemAbstract> void addSystem(T system, int priority) {
		SystemWithPriority<T> systemWithPriority = new SystemWithPriority<T>(system, priority);

	}


	@Override
	public Iterator<SystemAbstract> iterator() {
		List<SystemAbstract> pureSystems = new ArrayList<SystemAbstract>();

		for (SystemWithPriority<? extends SystemAbstract> pureSystem : systems) {
			pureSystems.add(pureSystem.system);
		}

		return pureSystems.iterator();
	}


	private class SystemWithPriority<T extends SystemAbstract> {
		public T system;
		public int priority;


		public SystemWithPriority(T system, int priority) {
			this.system = system;
			this.priority = priority;
		}
	}
}