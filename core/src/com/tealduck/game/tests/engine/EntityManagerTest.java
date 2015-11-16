package com.tealduck.game.tests.engine;


import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.components.MovementComponent;
import com.tealduck.game.components.PositionComponent;
import com.tealduck.game.engine.Component;
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
		int entity1 = entityManager.createEntity();
		int entity2 = entityManager.createEntity();

		PositionComponent positionComponent = new PositionComponent(new Vector2(0, 0));
		MovementComponent movementComponent = new MovementComponent(new Vector2(0, 0));
		PositionComponent positionComponent2 = new PositionComponent(new Vector2(1, 1));

		entityManager.addComponent(entity1, positionComponent);
		entityManager.addComponent(entity1, movementComponent);
		entityManager.addComponent(entity2, positionComponent2);

		HashMap<Class<? extends Component>, ? extends Component> components = entityManager
				.getAllComponentsForEntity(entity1);
		Assert.assertEquals(components.size(), 2);

		entityManager.removeEntity(entity1);

		components = entityManager.getAllComponentsForEntity(entity1);
		Assert.assertEquals(components.size(), 0);
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
		int entity = entityManager.createEntity();

		PositionComponent positionComponent = new PositionComponent(new Vector2(0, 0));
		MovementComponent movementComponent = new MovementComponent(new Vector2(0, 0));

		entityManager.addComponent(entity, positionComponent);
		entityManager.addComponent(entity, movementComponent);

		HashMap<Class<? extends Component>, ? extends Component> components = entityManager
				.getAllComponentsForEntity(entity);
		Assert.assertTrue(components.containsKey(PositionComponent.class));
		Assert.assertTrue(components.containsKey(MovementComponent.class));

		entityManager.removeComponent(entity, PositionComponent.class);
		components = entityManager.getAllComponentsForEntity(entity);

		Assert.assertFalse(components.containsKey(PositionComponent.class));
		Assert.assertTrue(components.containsKey(MovementComponent.class));
	}


	@Test
	public void testGetAllComponentsForEntity() {
		int entity = entityManager.createEntity();
		PositionComponent positionComponent = new PositionComponent(new Vector2(0, 0));
		MovementComponent movementComponent = new MovementComponent(new Vector2(0, 0));

		HashMap<Class<? extends Component>, ? extends Component> components = entityManager
				.getAllComponentsForEntity(entity);
		Assert.assertEquals(components.size(), 0);

		entityManager.addComponent(entity, positionComponent);

		components = entityManager.getAllComponentsForEntity(entity);
		Assert.assertEquals(components.size(), 1);
		Assert.assertTrue(components.containsKey(PositionComponent.class));

		entityManager.addComponent(entity, movementComponent);

		components = entityManager.getAllComponentsForEntity(entity);
		Assert.assertEquals(components.size(), 2);
		Assert.assertTrue(components.containsKey(PositionComponent.class));
		Assert.assertTrue(components.containsKey(MovementComponent.class));
	}


	@Test
	public void testGetAllComponentsOfType() {
		int entity1 = entityManager.createEntity();
		int entity2 = entityManager.createEntity();

		PositionComponent positionComponent1 = new PositionComponent(new Vector2(0, 0));
		entityManager.addComponent(entity1, positionComponent1);
		MovementComponent movementComponent1 = new MovementComponent(new Vector2(0, 0));
		entityManager.addComponent(entity1, movementComponent1);
		PositionComponent positionComponent2 = new PositionComponent(new Vector2(1, 1));
		entityManager.addComponent(entity2, positionComponent2);

		Collection<? extends Component> allPositions = entityManager
				.getAllComponentsOfType(PositionComponent.class);
		Assert.assertEquals(allPositions.size(), 2);
		Collection<? extends Component> allMovements = entityManager
				.getAllComponentsOfType(MovementComponent.class);
		Assert.assertEquals(allMovements.size(), 1);
	}


	@Test
	public void getAllEntitiesPossessingComponent() {
		int entity1 = entityManager.createEntity();
		int entity2 = entityManager.createEntity();
		int entity3 = entityManager.createEntity();

		PositionComponent positionComponent1 = new PositionComponent(new Vector2(0, 0));
		entityManager.addComponent(entity1, positionComponent1);
		PositionComponent positionComponent2 = new PositionComponent(new Vector2(0, 0));
		entityManager.addComponent(entity2, positionComponent2);
		MovementComponent movementComponent3 = new MovementComponent(new Vector2(0, 0));
		entityManager.addComponent(entity3, movementComponent3);

		Set<Integer> positionEntities = entityManager
				.getAllEntitiesPossessingComponent(PositionComponent.class);

		Assert.assertEquals(positionEntities.size(), 2);
		Assert.assertTrue(positionEntities.contains(entity1));
		Assert.assertTrue(positionEntities.contains(entity2));
		Assert.assertFalse(positionEntities.contains(entity3));
	}


	@SuppressWarnings("unchecked")
	@Test
	public void testGetAllEntitiesPossessingComponents() {
		int entity1 = entityManager.createEntity();
		int entity2 = entityManager.createEntity();
		int entity3 = entityManager.createEntity();

		PositionComponent positionComponent1 = new PositionComponent(new Vector2(0, 0));
		entityManager.addComponent(entity1, positionComponent1);
		MovementComponent movementComponent1 = new MovementComponent(new Vector2(0, 0));
		entityManager.addComponent(entity1, movementComponent1);
		PositionComponent positionComponent2 = new PositionComponent(new Vector2(0, 0));
		entityManager.addComponent(entity2, positionComponent2);
		MovementComponent movementComponent3 = new MovementComponent(new Vector2(0, 0));
		entityManager.addComponent(entity3, movementComponent3);

		Set<Integer> positionAndMovementEntities = entityManager
				.getAllEntitiesPossessingComponents(PositionComponent.class, MovementComponent.class);

		Assert.assertEquals(positionAndMovementEntities.size(), 3);
		Assert.assertTrue(positionAndMovementEntities.contains(entity1));
		Assert.assertTrue(positionAndMovementEntities.contains(entity2));
		Assert.assertTrue(positionAndMovementEntities.contains(entity3));
	}
}
