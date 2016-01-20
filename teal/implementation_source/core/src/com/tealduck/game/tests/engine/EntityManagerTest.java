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
import com.tealduck.game.engine.EntityTagManager;


public class EntityManagerTest {
	private EntityManager entityManager;
	private EntityTagManager entityTagManager;


	@Before
	public void setup() {
		entityManager = new EntityManager();
		entityTagManager = new EntityTagManager();
	}


	@Test
	public void testCreateEntity() {
		int entity1 = entityManager.createEntity();
		int entity2 = entityManager.createEntity();

		// Entity IDs start at 0, assigned sequentially
		Assert.assertEquals(0, entity1);
		Assert.assertEquals(1, entity2);

		// Test added to set of entities
		Assert.assertEquals(2, entityManager.getEntityCount());
		Assert.assertTrue(entityManager.getEntities().contains(entity1));
		Assert.assertTrue(entityManager.getEntities().contains(entity2));
	}


	@Test
	public void testCreateEntityWithTag() {
		int entity1 = entityManager.createEntityWithTag(entityTagManager, "TAG1");
		int entity2 = entityManager.createEntityWithTag(entityTagManager, "TAG2");

		Assert.assertEquals(0, entity1);
		Assert.assertEquals(1, entity2);

		Assert.assertTrue(entityTagManager.doesEntityIdHaveTag(entity1, "TAG1"));
		Assert.assertTrue(entityTagManager.doesEntityIdHaveTag(entity2, "TAG2"));

		Assert.assertEquals(2, entityManager.getEntityCount());
	}


	@Test
	public void testRemoveEntityWithTag() {
		int entity1 = entityManager.createEntityWithTag(entityTagManager, "TAG1");
		@SuppressWarnings("unused")
		int entity2 = entityManager.createEntityWithTag(entityTagManager, "TAG2");
		int removedEntity = entityManager.removeEntityWithTag(entity1, entityTagManager);

		Assert.assertEquals(entity1, removedEntity);

		Assert.assertEquals(1, entityManager.getEntityCount());
		Assert.assertFalse(entityTagManager.getTags().contains("TAG1"));
		Assert.assertTrue(entityTagManager.getTags().contains("TAG2"));
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

		entityManager.removeEntityWithTag(entity1, entityTagManager);

		Collection<PositionComponent> positionComponents = entityManager
				.getAllComponentsOfType(PositionComponent.class);
		Collection<? extends Component> movementComponents = entityManager
				.getAllComponentsOfType(MovementComponent.class);

		Assert.assertEquals(1, positionComponents.size());
		Assert.assertEquals(0, movementComponents.size());
	}


	@Test
	public void testGetEntities() {
		int entity1 = entityManager.createEntity();
		int entity2 = entityManager.createEntity();
		entityManager.removeEntityWithTag(entity2, entityTagManager);
		Set<Integer> entities = entityManager.getEntities();

		Assert.assertTrue(entities.contains(entity1));
		Assert.assertFalse(entities.contains(entity2));
	}


	@Test
	public void testGetEntityCount() {
		Assert.assertEquals(0, entityManager.getEntityCount());

		entityManager.createEntity();
		Assert.assertEquals(1, entityManager.getEntityCount());

		int entityToRemove = entityManager.createEntity();
		Assert.assertEquals(2, entityManager.getEntityCount());
		entityManager.removeEntityWithTag(entityToRemove, entityTagManager);
		Assert.assertEquals(1, entityManager.getEntityCount());
	}


	@Test
	public void testEntityExists() {
		int entity1 = entityManager.createEntity();
		Assert.assertTrue(entityManager.entityExists(entity1));

		entityManager.removeEntityWithTag(entity1, entityTagManager);
		Assert.assertFalse(entityManager.entityExists(entity1));
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
	public void testEntityHasComponent() {
		int entity = entityManager.createEntity();
		PositionComponent positionComponent = new PositionComponent(new Vector2(0, 0));

		Assert.assertFalse(entityManager.entityHasComponent(entity, PositionComponent.class));

		entityManager.addComponent(entity, positionComponent);
		Assert.assertTrue(entityManager.entityHasComponent(entity, PositionComponent.class));
	}


	@Test
	public void testGetComponent() {
		int entity = entityManager.createEntity();
		PositionComponent positionComponent = new PositionComponent(new Vector2(0, 0));
		entityManager.addComponent(entity, positionComponent);

		Assert.assertEquals(positionComponent, entityManager.getComponent(entity, PositionComponent.class));
	}


	@Test
	public void testGetEntityComponents() {
		int entity = entityManager.createEntity();

		PositionComponent positionComponent = new PositionComponent(new Vector2(0, 0));
		MovementComponent movementComponent = new MovementComponent(new Vector2(1, 0), 1.0f);

		entityManager.addComponent(entity, positionComponent);
		entityManager.addComponent(entity, movementComponent);

		HashMap<Class<? extends Component>, ? extends Component> components = entityManager
				.getEntityComponents(entity);

		Assert.assertEquals(positionComponent, components.get(PositionComponent.class));
		Assert.assertEquals(movementComponent, components.get(MovementComponent.class));
	}


	@Test
	public void testGetEntitiesWithComponent() {
		int entity1 = entityManager.createEntity();
		int entity2 = entityManager.createEntity();
		int entity3 = entityManager.createEntity();

		PositionComponent positionComponent = new PositionComponent(new Vector2(0, 0));

		entityManager.addComponent(entity1, positionComponent);
		entityManager.addComponent(entity2, positionComponent);

		Set<Integer> components = entityManager.getEntitiesWithComponent(PositionComponent.class);

		Assert.assertTrue(components.contains(entity1));
		Assert.assertTrue(components.contains(entity2));
		Assert.assertFalse(components.contains(entity3));
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
