package com.tealduck.game.system;


import java.util.Set;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PathfindingComponent;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.GameSystem;


public class PatrolLogicSystem extends GameSystem {
	public PatrolLogicSystem(EntityEngine entityEngine) {
		super(entityEngine);
	}


	@Override
	public void update(float deltaTime) {
		EntityManager entityManager = getEntityManager();

		@SuppressWarnings("unchecked")
		Set<Integer> entities = getEntityManager().getEntitiesWithComponents(PositionComponent.class,
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

				int extra = 0;
				targetPosition = targetPosition.cpy().add(new Vector2(MathUtils.random(-extra, extra),
						MathUtils.random(-extra, extra)));

				MovementComponent movementComponent = entityManager.getComponent(entity,
						MovementComponent.class);
				Vector2 deltaVelocity = movementComponent.acceleration;

				deltaVelocity.set(targetPosition).sub(entityPosition);

				if (deltaVelocity.len2() < 1) {
					deltaVelocity.setZero();
				}

				deltaVelocity.setLength(movementComponent.maxSpeed);
				// movementComponent.velocity.setZero();
			}
		}
	}
}
