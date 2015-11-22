package com.tealduck.game.engine;


import java.util.HashMap;
import java.util.Map.Entry;


/**
 *
 * Maps string tags to entity IDs. Wrapper around built-in {@link HashMap}
 */
public class EntityTagManager {
	// TODO: EntityTagManager Javadoc and tests

	private HashMap<String, Integer> tags;


	public EntityTagManager() {
		tags = new HashMap<String, Integer>();
	}


	/**
	 * @param tag
	 * @param entity
	 */
	public void addTag(String tag, int entity) {
		tags.put(tag, entity);
	}


	/**
	 * @param tag
	 * @return
	 * @throws NullPointerException
	 *                 if tag isn't used
	 */
	public int getEntity(String tag) throws NullPointerException {
		return tags.get(tag);
	}


	/**
	 * @param tag
	 * @return
	 * @throws NullPointerException
	 *                 if tag isn't used
	 */
	public int removeTag(String tag) {
		return tags.remove(tag);
	}


	/**
	 * @param entity
	 */
	public void removeTagsAssociatedWithEntity(int entity) {
		// TODO: Test removeTagsAssociatedWithEntity
		for (Entry<String, Integer> value : tags.entrySet()) {
			if (value.getValue() == entity) {
				tags.remove(value.getKey());
			}
		}
	}


	/**
	 * @param entity
	 * @return
	 */
	public String getTagAssociatedWithEntity(int entity) {
		for (Entry<String, Integer> value : tags.entrySet()) {
			if (value.getValue() == entity) {
				return value.getKey();
			}
		}
		return null;
	}


	/**
	 *
	 */
	public void clear() {
		tags.clear();
	}
}