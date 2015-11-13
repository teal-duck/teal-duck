package com.tealduck.game.tests.engine;


import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.components.MovementComponent;
import com.tealduck.game.components.PositionComponent;
import com.tealduck.game.engine.EntityManager;


public class EntityManagerTest {
	private EntityManager entityManager;


	@Before
	public void setup() {
		entityManager = new EntityManager();
	}


	@Test
	public void testAddEntity() {
		int entity1 = entityManager.createEntity();
		int entity2 = entityManager.createEntity();

		Assert.assertEquals(entity1, 0);
		Assert.assertEquals(entity2, 1);
		Assert.assertEquals(entityManager.getEntityCount(), 2);
	}


	@Test
	public void testRemoveEntity() {
		int entity1 = entityManager.createEntity();
		int entity2 = entityManager.createEntity();
		int removedEntity = entityManager.removeEntity(entity1);

		Assert.assertEquals(entity1, removedEntity);
		Assert.assertEquals(entityManager.getEntityCount(), 1);
		Assert.assertFalse(entityManager.getEntities().contains(entity1));
		Assert.assertTrue(entityManager.getEntities().contains(entity2));
	}


	@Test
	public void testRemoveEntityWithComponents() {
		// TODO: testRemoveEntityWithComponents
	}


	@Test
	public void testAddComponent() {
		int entity = entityManager.createEntity();
		Vector2 position = new Vector2(10, 20);
		PositionComponent positionComponent = new PositionComponent(position);
		entityManager.addComponent(entity, positionComponent);

		Assert.assertTrue(entityManager.entityHasComponent(entity, PositionComponent.class));
		Assert.assertEquals(entityManager.getComponent(entity, PositionComponent.class), positionComponent);
	}


	@Test
	public void testRemoveComponent() {
		// TODO: testRemoveComponent
	}
	
	
	@Test
	public void testGetAllComponentsForEntity() {
		// TODO: add asserts to testGetAllComponentsForEntity
		int entity = entityManager.createEntity();
		PositionComponent positionComponent = new PositionComponent(new Vector2(0, 0));
		MovementComponent movementComponent = new MovementComponent(new Vector2(0, 0));
		

		System.out.println(entityManager.getAllComponentsForEntity(entity));
		entityManager.addComponent(entity, positionComponent);
		System.out.println(entityManager.getAllComponentsForEntity(entity));
		entityManager.addComponent(entity, movementComponent);
		System.out.println(entityManager.getAllComponentsForEntity(entity));
		
	}
}
