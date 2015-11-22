package com.tealduck.game.tests.engine;


import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PositionComponent;
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

		// Entity IDs start at 0, assigned sequentially
		Assert.assertEquals(0, entity1);
		Assert.assertEquals(1, entity2);

		// Test added to set of entities
		Assert.assertEquals(2, entityManager.getEntityCount());
	}


	@Test
	public void testRemoveEntity() {
		int entity1 = entityManager.createEntity();
		int entity2 = entityManager.createEntity();
		int removedEntity = entityManager.removeEntity(entity1);

		// Removing entity should return the ID of the entity it removed
		Assert.assertEquals(removedEntity, entity1);
		// Test removed from set
		Assert.assertEquals(1, entityManager.getEntityCount());
		Assert.assertFalse(entityManager.getEntities().contains(entity1));
		Assert.assertTrue(entityManager.getEntities().contains(entity2));
	}


	@Test
	public void testRemoveEntityWithComponents() {
		int entity1 = entityManager.createEntity();
		int entity2 = entityManager.createEntity();

		PositionComponent positionComponent = new PositionComponent(new Vector2(0, 0));
		MovementComponent movementComponent = new MovementComponent(new Vector2(0, 0), 0);
		PositionComponent positionComponent2 = new PositionComponent(new Vector2(1, 1));

		entityManager.addComponent(entity1, positionComponent);
		entityManager.addComponent(entity1, movementComponent);
		entityManager.addComponent(entity2, positionComponent2);

		entityManager.removeEntity(entity1);

		@SuppressWarnings("unchecked")
		Collection<PositionComponent> positionComponents = (Collection<PositionComponent>) entityManager
				.getAllComponentsOfType(PositionComponent.class);
		Collection<? extends Component> movementComponents = entityManager
				.getAllComponentsOfType(MovementComponent.class);

		Assert.assertEquals(1, positionComponents.size());
		Assert.assertEquals(0, movementComponents.size());

	}


	@Test
	public void testAddComponent() {
		int entity = entityManager.createEntity();
		PositionComponent positionComponent = new PositionComponent(new Vector2(10, 20));
		entityManager.addComponent(entity, positionComponent);

		Assert.assertEquals(1, entityManager.getAllComponentsOfType(PositionComponent.class).size());
		Assert.assertTrue(entityManager.entityHasComponent(entity, PositionComponent.class));
		Assert.assertEquals(positionComponent, entityManager.getComponent(entity, PositionComponent.class));
	}


	@Test
	public void testRemoveComponent() {
		int entity = entityManager.createEntity();

		PositionComponent positionComponent = new PositionComponent(new Vector2(0, 0));
		MovementComponent movementComponent = new MovementComponent(new Vector2(0, 0), 0);

		entityManager.addComponent(entity, positionComponent);
		entityManager.addComponent(entity, movementComponent);

		HashMap<Class<? extends Component>, ? extends Component> components = entityManager
				.getEntityComponents(entity);
		Assert.assertTrue(components.containsKey(PositionComponent.class));
		Assert.assertTrue(components.containsKey(MovementComponent.class));

		entityManager.removeComponent(entity, PositionComponent.class);
		components = entityManager.getEntityComponents(entity);

		Assert.assertFalse(components.containsKey(PositionComponent.class));
		Assert.assertTrue(components.containsKey(MovementComponent.class));
	}


	@Test
	public void testGetAllComponentsForEntity() {
		int entity = entityManager.createEntity();
		PositionComponent positionComponent = new PositionComponent(new Vector2(0, 0));
		MovementComponent movementComponent = new MovementComponent(new Vector2(0, 0), 0);

		HashMap<Class<? extends Component>, ? extends Component> components = entityManager
				.getEntityComponents(entity);
		Assert.assertEquals(0, components.size());

		entityManager.addComponent(entity, positionComponent);

		components = entityManager.getEntityComponents(entity);
		Assert.assertEquals(1, components.size());
		Assert.assertTrue(components.containsKey(PositionComponent.class));

		entityManager.addComponent(entity, movementComponent);

		components = entityManager.getEntityComponents(entity);
		Assert.assertEquals(2, components.size());
		Assert.assertTrue(components.containsKey(PositionComponent.class));
		Assert.assertTrue(components.containsKey(MovementComponent.class));
	}


	@Test
	public void testGetAllComponentsOfType() {
		int entity1 = entityManager.createEntity();
		int entity2 = entityManager.createEntity();

		PositionComponent positionComponent1 = new PositionComponent(new Vector2(0, 0));
		MovementComponent movementComponent1 = new MovementComponent(new Vector2(0, 0), 0);
		PositionComponent positionComponent2 = new PositionComponent(new Vector2(1, 1));

		entityManager.addComponent(entity1, positionComponent1);
		entityManager.addComponent(entity1, movementComponent1);
		entityManager.addComponent(entity2, positionComponent2);

		Collection<? extends Component> allPositions = entityManager
				.getAllComponentsOfType(PositionComponent.class);
		Assert.assertEquals(2, allPositions.size());

		Collection<? extends Component> allMovements = entityManager
				.getAllComponentsOfType(MovementComponent.class);
		Assert.assertEquals(1, allMovements.size());
	}


	@Test
	public void getAllEntitiesPossessingComponent() {
		int entity1 = entityManager.createEntity();
		int entity2 = entityManager.createEntity();
		int entity3 = entityManager.createEntity();

		PositionComponent positionComponent1 = new PositionComponent(new Vector2(0, 0));
		PositionComponent positionComponent2 = new PositionComponent(new Vector2(0, 0));
		MovementComponent movementComponent3 = new MovementComponent(new Vector2(0, 0), 0);

		entityManager.addComponent(entity1, positionComponent1);
		entityManager.addComponent(entity2, positionComponent2);
		entityManager.addComponent(entity3, movementComponent3);

		Set<Integer> positionEntities = entityManager.getEntitiesWithComponent(PositionComponent.class);

		Assert.assertEquals(2, positionEntities.size());
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
		MovementComponent movementComponent1 = new MovementComponent(new Vector2(0, 0), 0);
		PositionComponent positionComponent2 = new PositionComponent(new Vector2(0, 0));
		MovementComponent movementComponent3 = new MovementComponent(new Vector2(0, 0), 0);

		entityManager.addComponent(entity1, positionComponent1);
		entityManager.addComponent(entity1, movementComponent1);
		entityManager.addComponent(entity2, positionComponent2);
		entityManager.addComponent(entity3, movementComponent3);

		Set<Integer> positionAndMovementEntities = entityManager
				.getEntitiesWithComponents(PositionComponent.class, MovementComponent.class);

		Assert.assertEquals(1, positionAndMovementEntities.size());
		Assert.assertTrue(positionAndMovementEntities.contains(entity1));
		Assert.assertFalse(positionAndMovementEntities.contains(entity2));
		Assert.assertFalse(positionAndMovementEntities.contains(entity3));
	}
}
