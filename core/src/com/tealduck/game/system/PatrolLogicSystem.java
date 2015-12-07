package com.tealduck.game.system;


import java.util.Set;

import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PatrolRouteComponent;
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
				MovementComponent.class, PatrolRouteComponent.class);

		for (int entity : entities) {
			PositionComponent positionComponent = entityManager.getComponent(entity,
					PositionComponent.class);
			MovementComponent movementComponent = entityManager.getComponent(entity,
					MovementComponent.class);
			PatrolRouteComponent patrolRouteComponent = entityManager.getComponent(entity,
					PatrolRouteComponent.class);

			float maxSpeed = movementComponent.maxSpeed;

			Vector2 entityPosition = positionComponent.position;
			Vector2 targetPosition = patrolRouteComponent.getTarget();

			// use epsilonEquals to allow for float inaccuracies
			if (entityPosition.epsilonEquals(targetPosition, 0.00001f)) {
				targetPosition = patrolRouteComponent.getNextVertex();
			}

			Vector2 v = targetPosition.cpy().sub(entityPosition).setLength(maxSpeed);

			movementComponent.acceleration.add(v);

			Vector2 entityToTarget = targetPosition.cpy().sub(entityPosition);
			Vector2 nextPosition = entityPosition.cpy().mulAdd(v, deltaTime);
			Vector2 nextToTarget = targetPosition.cpy().sub(nextPosition);

			// If we go past target in this frame, then dot product should be -1 as
			// angle between vectors == 180deg.
			if (entityToTarget.dot(nextToTarget) < 0) {
				targetPosition = patrolRouteComponent.getNextVertex();

			}
		}
	}

}
