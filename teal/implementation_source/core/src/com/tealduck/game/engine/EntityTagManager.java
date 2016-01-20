package com.tealduck.game.engine;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;


/**
 *
 * Maps string tags to entity IDs. Wrapper around built-in {@link HashMap}
 */
public class EntityTagManager {
	private HashMap<String, Integer> tags;


	/**
	 *
	 */
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
	public int removeTag(String tag) throws NullPointerException {
		return tags.remove(tag);
	}


	/**
	 * @param entity
	 */
	public void removeTagsAssociatedWithEntity(int entity) {
		Set<String> tagsToRemove = new HashSet<String>();
		for (Entry<String, Integer> tag : tags.entrySet()) {
			if (tag.getValue() == entity) {
				tagsToRemove.add(tag.getKey());
			}
		}

		for (String key : tagsToRemove) {
			tags.remove(key);
		}
	}


	@Deprecated
	/**
	 * Null if no tag.
	 *
	 * This method should not be used, as an entity may have more than one tag, but this method can only return one.
	 *
	 * @deprecated
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


	public boolean doesEntityIdHaveTag(int entity, String tag) {
		if (tags.containsKey(tag)) {
			return (tags.get(tag) == entity);
		} else {
			return false;
		}
	}


	public boolean isTagAssignedToEntity(String tag) {
		return tags.containsKey(tag);
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