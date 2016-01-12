package com.tealduck.game.event;


import com.badlogic.gdx.Gdx;
import com.tealduck.game.collision.Intersection;
import com.tealduck.game.component.BulletComponent;
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

		// Don't be harmed by own bullet
		if (entityManager.entityHasComponent(sender, BulletComponent.class)) {
			BulletComponent bulletComponent = entityManager.getComponent(sender, BulletComponent.class);
			if (bulletComponent.shooterId == receiver) {
				return false;
			}
		}

		CollisionEvents.handlePickup(entityEngine, sender, receiver);

		CollisionEvents.handleKnockback(entityManager, sender, receiver, intersection);

		CollisionEvents.handleDamage(entityEngine, sender, receiver);

		return false;
	}
}
