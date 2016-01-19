package com.tealduck.game.tests.engine;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EntityTagManager;


public class EntityTagManagerTest {
	private EntityManager entityManager;
	private EntityTagManager entityTagManager;


	@Before
	public void setup() {
		entityManager = new EntityManager();
		entityTagManager = new EntityTagManager();
	}


	@Test
	public void testAddTag() {
		int entity = entityManager.createEntity();
		String tag = "TAG1";
		entityTagManager.addTag(tag, entity);

		Assert.assertTrue(entityTagManager.getTags().contains(tag));
	}


	@Test
	public void testGetEntity() {
		int entity = entityManager.createEntity();
		String tag = "TAG1";
		entityTagManager.addTag(tag, entity);
		Assert.assertTrue(entityTagManager.getEntity(tag) == entity);
	}


	@Test
	public void testRemoveTag() {
		int entity = entityManager.createEntity();
		String tag = "TAG1";
		entityTagManager.addTag(tag, entity);

		entityTagManager.removeTag(tag);

		Assert.assertFalse(entityTagManager.getTags().contains(tag));

		try {
			entityTagManager.removeTag(tag);
		} catch (NullPointerException e) {
		}
	}


	@Test
	public void testRemoveTagsAssociatedWithEntity() {
		int entity1 = entityManager.createEntity();
		String tag1 = "TAG1";
		entityTagManager.addTag(tag1, entity1);

		int entity2 = entityManager.createEntity();
		String tag2 = "TAG2";
		String tag3 = "TAG3";
		entityTagManager.addTag(tag2, entity2);
		entityTagManager.addTag(tag3, entity2);

		int entity3 = entityManager.createEntity();

		entityTagManager.removeTagsAssociatedWithEntity(entity1);
		Assert.assertFalse(entityTagManager.getTags().contains(tag1));

		entityTagManager.removeTagsAssociatedWithEntity(entity2);
		Assert.assertFalse(entityTagManager.getTags().contains(tag2));
		Assert.assertFalse(entityTagManager.getTags().contains(tag3));

		// Test that an entity with no tags does not cause an exception
		entityTagManager.removeTagsAssociatedWithEntity(entity3);
	}


	@Test
	public void testDoesEntityIdHaveTag() {
		int entity1 = entityManager.createEntity();
		int entity2 = entityManager.createEntity();
		String tag1 = "TAG1";
		entityTagManager.addTag(tag1, entity1);

		Assert.assertTrue(entityTagManager.doesEntityIdHaveTag(entity1, tag1));
		Assert.assertFalse(entityTagManager.doesEntityIdHaveTag(entity2, tag1));
	}


	@Test
	public void testIsTagAssignedToEntity() {
		int entity1 = entityManager.createEntity();
		String tag1 = "TAG1";
		String tag2 = "TAG2";
		entityTagManager.addTag(tag1, entity1);

		Assert.assertTrue(entityTagManager.isTagAssignedToEntity(tag1));
		Assert.assertFalse(entityTagManager.isTagAssignedToEntity(tag2));
	}

}