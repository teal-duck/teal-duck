package com.tealduck.game.engine;


import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;


/**
 *
 * Maps string tags to entity IDs. Wrapper around built-in {@link HashMap}
 */
public class EntityTagManager {
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
	 * @return Set containing Tags
	 */
	public Set<String> getTags() {
		return tags.keySet();
	}


	/**
	 *
	 */
	public void clear() {
		tags.clear();
	}
}