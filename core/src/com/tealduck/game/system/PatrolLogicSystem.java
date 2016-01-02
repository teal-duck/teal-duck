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


	private float calculateDifferenceBetweenAngles(float firstAngle, float secondAngle) {
		// TODO: Stop geese spinning 360
		// float difference = secondAngle - firstAngle;
		// while (difference < 0) {
		// difference += 360;
		// }
		// while (difference > 360) {
		// difference -= 360;
		// }
		// return difference;
		// return ((((secondAngle - firstAngle) % 360) + 540) % 360) - 180;
		return (((secondAngle - firstAngle) + 180) % 360) - 180;
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

			if (patrolRouteComponent.pauseTime > 0) {
				patrolRouteComponent.pauseTime -= deltaTime;
				positionComponent.lookAt.rotate(patrolRouteComponent.rotPerSecond * deltaTime);

			} else {
				float maxSpeed = movementComponent.maxSpeed;

				Vector2 entityPosition = positionComponent.position;
				Vector2 targetPosition = patrolRouteComponent.getTarget();

				if (patrolRouteComponent.previousPosition == null) {
					patrolRouteComponent.previousPosition = entityPosition.cpy();
				}

				Vector2 previousPosition = patrolRouteComponent.previousPosition;

				Vector2 previousVecToTarget = targetPosition.cpy().sub(previousPosition);
				Vector2 vecToTarget = targetPosition.cpy().sub(entityPosition);

				Vector2 vecToPreviousTarget;
				if (patrolRouteComponent.previousTarget != null) {
					vecToPreviousTarget = patrolRouteComponent.previousTarget.cpy()
							.sub(entityPosition);
				} else {
					vecToPreviousTarget = new Vector2(1000, 1000);
				}

				float distanceToTarget = vecToTarget.len();
				float distanceToPreviousTarget = vecToPreviousTarget.len();

				previousVecToTarget.nor();
				vecToTarget.nor();
				vecToPreviousTarget.nor();

				float dot = vecToTarget.dot(previousVecToTarget);
				if ((dot <= 0) && (distanceToTarget < (maxSpeed * deltaTime * 0.5f))
						&& (distanceToPreviousTarget > (maxSpeed * deltaTime))) {
					targetPosition = patrolRouteComponent.advanceTarget();
					patrolRouteComponent.pauseTime = patrolRouteComponent.maxPauseTime;
					patrolRouteComponent.startRotation = positionComponent.lookAt.angle();

					vecToTarget = targetPosition.cpy().sub(entityPosition);
					patrolRouteComponent.endRotation = vecToTarget.angle();

					float rotChange = calculateDifferenceBetweenAngles(
							patrolRouteComponent.startRotation,
							patrolRouteComponent.endRotation);

					patrolRouteComponent.rotPerSecond = rotChange / patrolRouteComponent.pauseTime;

				} else {
					if (distanceToTarget < (maxSpeed * deltaTime)) {
						maxSpeed = distanceToTarget / deltaTime;
					}

					Vector2 v = vecToTarget.setLength(maxSpeed);
					movementComponent.acceleration.add(v);
					positionComponent.lookAt = patrolRouteComponent.getTarget().cpy()
							.sub(entityPosition).nor();
				}
			}
		}
	}
}
