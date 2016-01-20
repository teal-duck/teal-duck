package com.tealduck.game.system;


import java.util.Set;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.Tag;
import com.tealduck.game.collision.Ray;
import com.tealduck.game.component.ChaseComponent;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.component.ViewconeComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EntityTagManager;
import com.tealduck.game.engine.GameSystem;
import com.tealduck.game.world.World;


/**
 *
 */
public class ChaseSystem extends GameSystem {
	private World world;


	/**
	 * @param entityEngine
	 * @param world
	 */
	public ChaseSystem(EntityEngine entityEngine, World world) {
		super(entityEngine);
		this.world = world;
	}


	@Override
	public void update(float deltaTime) {
		EntityManager entityManager = getEntityManager();
		EntityTagManager entityTagManager = getEntityTagManager();

		@SuppressWarnings("unchecked")
		Set<Integer> entities = getEntityManager().getEntitiesWithComponents(PositionComponent.class,
				MovementComponent.class);

		for (int entity : entities) {
			// If the entity has a viewcone, but not already chasing
			if (entityManager.entityHasComponent(entity, ViewconeComponent.class)
					&& !entityManager.entityHasComponent(entity, ChaseComponent.class)) {
				// If the player exists
				if (entityTagManager.isTagAssignedToEntity(Tag.PLAYER)) {
					int target = entityTagManager.getEntity(Tag.PLAYER);

					// If the enemy can see the player, start chasing
					if (canEntitySeeEntity(entityManager, entity, target)) {
						entityManager.addComponent(entity, new ChaseComponent(target));
					}
				}
			}

			// If the enemy is chasing
			if (entityManager.entityHasComponent(entity, ChaseComponent.class)) {
				ChaseComponent chaseComponent = entityManager.getComponent(entity,
						ChaseComponent.class);
				int target = chaseComponent.targetEntityId;

				// If the target doesn't exist (e.g. already killed)
				// Stop chasing
				if (!entityManager.entityExists(target)
						|| !entityManager.entityHasComponent(target, PositionComponent.class)) {
					if (chaseComponent.forgettable) {
						entityManager.removeComponent(entity, ChaseComponent.class);
					}
					continue;
				}

				// If the enemy doesn't have a viewcone, they should always chase
				// Otherwise the viewcone limits what they can see, so might not chase
				boolean shouldChase = true;

				if (entityManager.entityHasComponent(entity, ViewconeComponent.class)) {
					// If enemy can see their target, then chase
					if (canEntitySeeEntity(entityManager, entity, target)) {
						shouldChase = true;
						chaseComponent.forgetTime = 0f;
					} else {
						// Can't see target, start forgetting
						shouldChase = false;

						if (chaseComponent.forgettable) {
							chaseComponent.forgetTime += deltaTime;

							if (chaseComponent.forgetTime > chaseComponent.maxTimeToForget) {
								entityManager.removeComponent(entity,
										ChaseComponent.class);
								continue;
							}
						}
					}
				}

				if (shouldChase) {
					chaseTarget(entityManager, entity, target);
				} else {
					searchForTarget(entityManager, entity, target, deltaTime);
				}
			}
		}
	}


	/**
	 * Returns true if the target entity is in the chaser entity's viewcone. Requires chaser to have a position and
	 * viewcone, and target to have a position.
	 *
	 * @param entityManager
	 * @param chaser
	 * @param target
	 * @return
	 */
	private boolean canEntitySeeEntity(EntityManager entityManager, int chaser, int target) {
		if (entityManager.entityHasComponent(chaser, PositionComponent.class)
				&& entityManager.entityHasComponent(chaser, ViewconeComponent.class)
				&& entityManager.entityHasComponent(target, PositionComponent.class)) {

			PositionComponent chaserPositionComponent = entityManager.getComponent(chaser,
					PositionComponent.class);
			ViewconeComponent chaserViewconeComponent = entityManager.getComponent(chaser,
					ViewconeComponent.class);

			PositionComponent targetPositionComponent = entityManager.getComponent(target,
					PositionComponent.class);

			Vector2 chaserPosition = chaserPositionComponent.getCenter();
			Vector2 targetPosition = targetPositionComponent.getCenter();
			Vector2 chaserDirection = chaserPositionComponent.lookAt;

			float coneFov = chaserViewconeComponent.halfFov;
			float coneLength = chaserViewconeComponent.length;
			float coneLengthSquared = coneLength * coneLength;

			Vector2 vecToTarget = targetPosition.cpy().sub(chaserPosition);

			// Target not too far away
			if (vecToTarget.len2() > coneLengthSquared) {
				return false;
			}

			Vector2 dirToTarget = vecToTarget.cpy().nor();

			// Target is in the field of view
			float dot = chaserDirection.dot(dirToTarget);
			if (dot < coneFov) {
				return false;
			}

			// Hacky
			// If player is less than a tile away, don't perform raycast
			if (vecToTarget.len2() < (64 * 64)) {
				return true;
			}

			// Check if there is a wall between the chaser and target
			// I.e. is there a line of sight between the 2 entities
			Ray ray = new Ray(chaserPosition.cpy(), vecToTarget.cpy().nor());
			Vector2 intersectTile = ray.worldIntersection(world, vecToTarget.len() / 64f);

			if (intersectTile == null) {
				return true;
			}
		}

		return false;
	}


	/**
	 * Moves the chaser entity towards the target entity.
	 *
	 * @param entityManager
	 * @param chaser
	 * @param target
	 */
	private void chaseTarget(EntityManager entityManager, int chaser, int target) {
		if (entityManager.entityHasComponent(chaser, PositionComponent.class)
				&& entityManager.entityHasComponent(chaser, MovementComponent.class)
				&& entityManager.entityHasComponent(target, PositionComponent.class)) {
			PositionComponent chaserPositionComponent = entityManager.getComponent(chaser,
					PositionComponent.class);
			MovementComponent chaserMovementComponent = entityManager.getComponent(chaser,
					MovementComponent.class);

			PositionComponent targetPositionComponent = entityManager.getComponent(target,
					PositionComponent.class);

			Vector2 entityPosition = chaserPositionComponent.getCenter();
			Vector2 targetPosition = targetPositionComponent.getCenter();

			Vector2 acceleration = chaserMovementComponent.acceleration;

			Vector2 accToAdd = targetPosition.cpy().sub(entityPosition);

			if (accToAdd.len2() < 8) {
				accToAdd.setZero();
			}

			accToAdd.setLength(chaserMovementComponent.maxSpeed);
			acceleration.add(accToAdd);

			chaserPositionComponent.lookAt.set(accToAdd).nor();

			// Remember the last direction to the target
			// If in the next frame, the chaser can't see the target
			// They can continue along the last known path
			if (entityManager.entityHasComponent(chaser, ChaseComponent.class)) {
				ChaseComponent chaseComponent = entityManager.getComponent(chaser,
						ChaseComponent.class);
				chaseComponent.searchTime = randomSearchTime();
				chaseComponent.searchDirection.set(accToAdd.cpy().nor());
			}
		}
	}


	/**
	 * @return
	 */
	private float randomSearchTime() {
		return MathUtils.random(2f, 6f);
	}


	/**
	 * @param firstAngle
	 * @param secondAngle
	 * @return
	 */
	private float calculateDifferenceBetweenAngles(float firstAngle, float secondAngle) {
		// TODO: Remove duplicate rotation code
		return (((secondAngle - firstAngle) + 180) % 360) - 180;
	}


	/**
	 * @param entityManager
	 * @param chaser
	 * @param target
	 * @param deltaTime
	 */
	private void searchForTarget(EntityManager entityManager, int chaser, int target, float deltaTime) {
		ChaseComponent chaseComponent = entityManager.getComponent(chaser, ChaseComponent.class);
		PositionComponent positionComponent = entityManager.getComponent(chaser, PositionComponent.class);

		// Look in a different direction if the chaser hit the world
		if (chaseComponent.hitWorld && (chaseComponent.rotateTime <= 0f)) {
			chaseComponent.searchTime = 0;
		}

		// End of search in a direction, pick a new direction
		if ((chaseComponent.searchTime <= 0) && (chaseComponent.rotateTime <= 0f)) {
			chaseComponent.searchTime = randomSearchTime();
			chaseComponent.maxSearchTime = chaseComponent.searchTime;

			chaseComponent.startRotation = chaseComponent.searchDirection.angle();

			// If they hit a wall, rotate them to face away from the wall
			if (chaseComponent.hitWorld) {
				chaseComponent.searchDirection.rotate(MathUtils.random(100, 260));
				chaseComponent.hitWorld = false;
			} else {
				chaseComponent.searchDirection.setToRandomDirection();
			}

			chaseComponent.endRotation = chaseComponent.searchDirection.angle();

			float rotChange = calculateDifferenceBetweenAngles(chaseComponent.startRotation,
					chaseComponent.endRotation);

			float r = 90;
			chaseComponent.rotateTime = Math.abs(rotChange) / r;
			chaseComponent.rotPerSecond = r * Math.signum(rotChange);
		}

		// Rotate towards the new direction
		if (chaseComponent.rotateTime > 0f) {
			chaseComponent.rotateTime -= deltaTime;
			positionComponent.lookAt.rotate(chaseComponent.rotPerSecond * deltaTime);
		} else {
			// Walk in the search direction
			chaseComponent.searchTime -= deltaTime;
			positionComponent.lookAt.set(chaseComponent.searchDirection);

			MovementComponent movementComponent = entityManager.getComponent(chaser,
					MovementComponent.class);
			movementComponent.acceleration
					.add(chaseComponent.searchDirection.cpy().scl(movementComponent.maxSpeed));

		}
	}
}
