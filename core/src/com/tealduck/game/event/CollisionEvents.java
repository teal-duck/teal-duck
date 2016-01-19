package com.tealduck.game.event;


import com.tealduck.game.Team;
import com.tealduck.game.collision.Intersection;
import com.tealduck.game.component.BulletComponent;
import com.tealduck.game.component.ChaseComponent;
import com.tealduck.game.component.DamageComponent;
import com.tealduck.game.component.HealthComponent;
import com.tealduck.game.component.KnockbackComponent;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PickupComponent;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.component.ScoreComponent;
import com.tealduck.game.component.TeamComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EventManager;
import com.tealduck.game.pickup.Pickup;
import com.tealduck.game.world.EntityConstants;


/**
 * Static methods for functions that are common between collision components.
 */
public class CollisionEvents {
	private CollisionEvents() {
	};


	/**
	 * Returns true if one or both entities doesn't have a team.
	 *
	 * @param entityManager
	 * @param e0
	 * @param e1
	 * @return
	 */
	public static boolean areEntitiesOnSameTeam(EntityManager entityManager, int e0, int e1) {
		Team t0 = null;
		if (entityManager.entityHasComponent(e0, TeamComponent.class)) {
			t0 = entityManager.getComponent(e0, TeamComponent.class).team;
		}
		Team t1 = null;
		if (entityManager.entityHasComponent(e1, TeamComponent.class)) {
			t1 = entityManager.getComponent(e1, TeamComponent.class).team;
		}

		return ((t0 == null) || (t1 == null) || (t0 == t1));
	}


	/**
	 * @param entityManager
	 * @param sender
	 * @param receiver
	 * @param intersection
	 */
	public static void handleKnockback(EntityManager entityManager, int sender, int receiver,
			Intersection intersection) {
		if (CollisionEvents.areEntitiesOnSameTeam(entityManager, sender, receiver)) {
			CollisionEvents.pushEntitiesApart(entityManager, sender, receiver, intersection);
		} else if (entityManager.entityHasComponent(sender, KnockbackComponent.class)
				&& entityManager.entityHasComponent(receiver, MovementComponent.class)) {
			KnockbackComponent knockbackComponent = entityManager.getComponent(sender,
					KnockbackComponent.class);
			MovementComponent movementComponent = entityManager.getComponent(receiver,
					MovementComponent.class);

			// TODO: Calculate knockback from mass
			// TODO: Add randomness to knockback code
			float knockbackAmount = knockbackComponent.knockbackForce;
			movementComponent.acceleration.add(intersection.normal.cpy().scl(knockbackAmount));

			if (entityManager.entityHasComponent(receiver, PositionComponent.class)) {
				entityManager.getComponent(receiver, PositionComponent.class).lookAt
						.set(intersection.normal).nor();
			}
			if (entityManager.entityHasComponent(receiver, ChaseComponent.class)) {
				entityManager.getComponent(receiver, ChaseComponent.class).searchDirection
						.set(intersection.normal).nor();
			}
		}
	}


	/**
	 * @param entityManager
	 * @param sender
	 * @param receiver
	 * @param intersection
	 */
	private static void pushEntitiesApart(EntityManager entityManager, int sender, int receiver,
			Intersection intersection) {
		if (entityManager.entityHasComponent(receiver, MovementComponent.class)
				&& entityManager.entityHasComponent(sender, MovementComponent.class)) {
			MovementComponent movementComponent = entityManager.getComponent(receiver,
					MovementComponent.class);
			movementComponent.velocity.add(intersection.getResolveVector().scl(1.5f));
		}
	}


	/**
	 * @param entityEngine
	 * @param sender
	 * @param receiver
	 */
	public static void handleDamage(EntityEngine entityEngine, int sender, int receiver) {
		EntityManager entityManager = entityEngine.getEntityManager();
		if (!CollisionEvents.areEntitiesOnSameTeam(entityManager, sender, receiver)) {
			if (entityManager.entityHasComponent(sender, DamageComponent.class)
					&& entityManager.entityHasComponent(receiver, HealthComponent.class)) {
				DamageComponent damageComponent = entityManager.getComponent(sender,
						DamageComponent.class);
				HealthComponent healthComponent = entityManager.getComponent(receiver,
						HealthComponent.class);

				// TODO: Defence component
				// TODO: Maybe have invulnerability for a second after taking damage
				healthComponent.health -= damageComponent.damage;

				if (healthComponent.health <= 0) {
					healthComponent.health = 0;

					CollisionEvents.handleScoreForEntityDeath(entityManager, sender, receiver);
				}

				if (entityManager.entityHasComponent(sender, BulletComponent.class)) {
					EventManager eventManager = entityEngine.getEventManager();
					eventManager.triggerEvent(receiver, sender, EventName.REMOVE);
				}
			}
		}
	}


	/**
	 * @param entityManager
	 * @param killerEntity
	 * @param deadEntity
	 */
	public static void handleScoreForEntityDeath(EntityManager entityManager, int killerEntity, int deadEntity) {
		if (entityManager.entityHasComponent(killerEntity, ScoreComponent.class)) {
			ScoreComponent scoreComponent = entityManager.getComponent(killerEntity, ScoreComponent.class);
			scoreComponent.increaseScoreWithComboGain(EntityConstants.SCORE_FOR_KILL);

		} else if (entityManager.entityHasComponent(killerEntity, BulletComponent.class)) {
			int shooterEntity = entityManager.getComponent(killerEntity, BulletComponent.class).shooterId;
			CollisionEvents.handleScoreForEntityDeath(entityManager, shooterEntity, deadEntity);
		}
	}


	/**
	 * @param entityEngine
	 * @param sender
	 * @param receiver
	 */
	public static void handlePickup(EntityEngine entityEngine, int sender, int receiver) {
		EntityManager entityManager = entityEngine.getEntityManager();

		if (!entityManager.entityHasComponent(sender, PickupComponent.class)) {
			return;
		}
		PickupComponent pickupComponent = entityManager.getComponent(sender, PickupComponent.class);

		Pickup contents = pickupComponent.contents;
		boolean pickedUp = contents.applyToEntity(entityManager, receiver);

		if (pickedUp) {
			entityEngine.flagEntityToRemove(sender);
		}
	}
}
