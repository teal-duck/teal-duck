package com.tealduck.game.event;


import com.tealduck.game.collision.Intersection;
import com.tealduck.game.component.DamageComponent;
import com.tealduck.game.component.HealthComponent;
import com.tealduck.game.component.KnockbackComponent;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.engine.EntityManager;


public class CollisionEvents {
	private CollisionEvents() {
	};


	public static void doKnockback(EntityManager entityManager, int sender, int receiver,
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


	public static void doDamage(EntityManager entityManager, int sender, int receiver) {
		// TODO: Defence component
		if (entityManager.entityHasComponent(sender, DamageComponent.class)
				&& entityManager.entityHasComponent(receiver, HealthComponent.class)) {
			DamageComponent damageComponent = entityManager.getComponent(sender, DamageComponent.class);
			HealthComponent healthComponent = entityManager.getComponent(receiver, HealthComponent.class);

			// TODO: Maybe have invulnerability for a second after taking damage
			healthComponent.health -= damageComponent.damage;
			if (healthComponent.health < 0) {
				healthComponent.health = 0;
			}
		}
	}
}
