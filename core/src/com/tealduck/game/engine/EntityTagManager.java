package com.tealduck.game.engine;


import java.util.HashMap;


/**
 * Maps string tags to entity IDs.
 */
public class EntityTagManager {
	// TODO: EntityTagManager Javadoc and tests

	private HashMap<String, Integer> tags;


	public EntityTagManager() {
		tags = new HashMap<String, Integer>();
	}


	public void addTag(String tag, int entity) {
		tags.put(tag, entity);
	}


	public int getEntity(String tag) {
		return tags.get(tag);
	}


	public int removeTag(String tag) {
		return tags.remove(tag);
	}


	public void clear() {
		tags.clear();
	}
}