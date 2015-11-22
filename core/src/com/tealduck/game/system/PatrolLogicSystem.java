package com.tealduck.game.system;


import java.util.Set;

import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PathfindingComponent;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EntityTagManager;
import com.tealduck.game.engine.EventManager;
import com.tealduck.game.engine.GameSystem;


public class PatrolLogicSystem extends GameSystem {
	public PatrolLogicSystem(EntityManager entityManager, EntityTagManager entityTagManager,
			EventManager eventManager) {
		super(entityManager, entityTagManager, eventManager);
	}


	@Override
	public void update(float deltaTime) {
		@SuppressWarnings("unchecked")
		Set<Integer> entities = entityManager.getEntitiesWithComponents(PositionComponent.class,
				MovementComponent.class, PathfindingComponent.class);

		for (int entity : entities) {
			PathfindingComponent pathfindingComponent = entityManager.getComponent(entity,
					PathfindingComponent.class);

			int targetEntityId = pathfindingComponent.targetEntityId;
			if (entityManager.entityExists(targetEntityId)
					&& entityManager.entityHasComponent(targetEntityId, PositionComponent.class)) {

				Vector2 entityPosition = entityManager.getComponent(entity,
						PositionComponent.class).position;

				Vector2 targetPosition = entityManager.getComponent(targetEntityId,
						PositionComponent.class).position;

				// TODO: How to handle entity movement speeds
				// Value in MovementComponent?
				float entitySpeed = 20;
				Vector2 difference = targetPosition.cpy().sub(entityPosition);
				Vector2 velocity = difference.setLength(entitySpeed);

				MovementComponent movementComponent = entityManager.getComponent(entity,
						MovementComponent.class);
				movementComponent.velocity = velocity;
			}
		}
	}
}
