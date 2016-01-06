package com.tealduck.game.event;


import com.tealduck.game.collision.Intersection;
import com.tealduck.game.component.BulletComponent;
import com.tealduck.game.component.DamageComponent;
import com.tealduck.game.component.HealthComponent;
import com.tealduck.game.component.KnockbackComponent;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PickupComponent;
import com.tealduck.game.component.ScoreComponent;
import com.tealduck.game.component.WeaponComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.pickup.AmmoPickup;
import com.tealduck.game.pickup.HealthPickup;
import com.tealduck.game.pickup.Pickup;


public class CollisionEvents {
	private CollisionEvents() {
	};


	public static void handleKnockback(EntityManager entityManager, int sender, int receiver,
			Intersection intersection) {
		// TODO: Add randomness to knockback code
		// TODO: Stop enemies bouncing off enemies
		if (entityManager.entityHasComponent(sender, KnockbackComponent.class)
				&& entityManager.entityHasComponent(receiver, MovementComponent.class)) {
			KnockbackComponent knockbackComponent = entityManager.getComponent(sender,
					KnockbackComponent.class);
			MovementComponent movementComponent = entityManager.getComponent(receiver,
					MovementComponent.class);

			// TODO: Calculate knockback from mass
			float knockbackAmount = knockbackComponent.knockbackForce;
			movementComponent.acceleration.add(intersection.normal.cpy().scl(knockbackAmount));
		}
	}


	public static void handleDamage(EntityManager entityManager, int sender, int receiver) {
		// TODO: Defence component
		if (entityManager.entityHasComponent(sender, DamageComponent.class)
				&& entityManager.entityHasComponent(receiver, HealthComponent.class)) {
			DamageComponent damageComponent = entityManager.getComponent(sender, DamageComponent.class);
			HealthComponent healthComponent = entityManager.getComponent(receiver, HealthComponent.class);

			// TODO: Maybe have invulnerability for a second after taking damage
			healthComponent.health -= damageComponent.damage;
			if (healthComponent.health <= 0) {
				healthComponent.health = 0;

				CollisionEvents.handleScoreForEntityDeath(entityManager, sender, receiver);
			}
		}
	}


	public static void handleScoreForEntityDeath(EntityManager entityManager, int killerEntity, int deadEntity) {
		if (entityManager.entityHasComponent(killerEntity, ScoreComponent.class)) {
			ScoreComponent scoreComponent = entityManager.getComponent(killerEntity, ScoreComponent.class);
			scoreComponent.increaseScoreWithComboGain(10);

		} else if (entityManager.entityHasComponent(killerEntity, BulletComponent.class)) {
			int shooterEntity = entityManager.getComponent(killerEntity, BulletComponent.class).shooterId;
			CollisionEvents.handleScoreForEntityDeath(entityManager, shooterEntity, deadEntity);
		}
	}


	public static void handlePickup(EntityEngine entityEngine, int sender, int receiver) {
		// TODO: Animation on pick up
		EntityManager entityManager = entityEngine.getEntityManager();
		if (!entityManager.entityHasComponent(sender, PickupComponent.class)) {
			return;
		}
		boolean pickedUp = false;

		PickupComponent pickupComponent = entityManager.getComponent(sender, PickupComponent.class);
		Pickup contents = pickupComponent.contents;

		if (contents instanceof AmmoPickup) {
			if (entityManager.entityHasComponent(receiver, WeaponComponent.class)) {
				WeaponComponent weaponComponent = entityManager.getComponent(receiver,
						WeaponComponent.class);
				AmmoPickup ammoPickup = (AmmoPickup) contents;

				weaponComponent.addAmmo(ammoPickup.ammo);
				pickedUp = true;
			}
		} else if (contents instanceof HealthPickup) {
			if (entityManager.entityHasComponent(receiver, HealthComponent.class)) {
				HealthComponent healthComponent = entityManager.getComponent(receiver,
						HealthComponent.class);
				HealthPickup healthPickup = (HealthPickup) contents;

				if (healthComponent.health != healthComponent.maxHealth) {
					healthComponent.health += healthPickup.health;
					if (healthComponent.health > healthComponent.maxHealth) {
						healthComponent.health = healthComponent.maxHealth;
					}
					pickedUp = true;
				}
			}
		}

		if (pickedUp) {
			entityEngine.flagEntityToRemove(sender);
		}
	}
}
