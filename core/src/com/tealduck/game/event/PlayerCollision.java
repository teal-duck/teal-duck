package com.tealduck.game.event;


import com.badlogic.gdx.Gdx;
import com.tealduck.game.collision.Intersection;
import com.tealduck.game.component.DamageComponent;
import com.tealduck.game.component.HealthComponent;
import com.tealduck.game.component.KnockbackComponent;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.IEvent;


public class PlayerCollision implements IEvent {
	public static final PlayerCollision instance = new PlayerCollision();


	private PlayerCollision() {
	}


	@Override
	public boolean fire(EntityEngine entityEngine, int sender, int receiver, Object data) {
		// TODO: Maybe change how data is passed to events
		if ((data == null) || !(data instanceof Intersection)) {
			Gdx.app.log("Event", "Player collision event expected an intersection instance");
			return false;
		}
		Intersection intersection = (Intersection) data;

		EntityManager entityManager = entityEngine.getEntityManager();

		if (entityManager.entityHasComponent(sender, KnockbackComponent.class)) {
			KnockbackComponent knockbackComponent = entityManager.getComponent(sender,
					KnockbackComponent.class);
			MovementComponent movementComponent = entityManager.getComponent(receiver,
					MovementComponent.class);

			// TODO: Calculate knockback from mass
			float knockbackAmount = knockbackComponent.knockbackForce;
			movementComponent.acceleration.add(intersection.normal.cpy().scl(knockbackAmount));
		}

		// TODO: Defence component
		if (entityManager.entityHasComponent(sender, DamageComponent.class)) {
			DamageComponent damageComponent = entityManager.getComponent(sender, DamageComponent.class);
			HealthComponent healthComponent = entityManager.getComponent(receiver, HealthComponent.class);

			// TODO: Maybe have invulnerability for a second after taking damage
			healthComponent.health -= damageComponent.damage;
			if (healthComponent.health < 0) {
				healthComponent.health = 0;
			}
		}

		return false;
	}
}
