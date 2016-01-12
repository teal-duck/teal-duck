package com.tealduck.game.event;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.collision.Intersection;
import com.tealduck.game.component.BulletComponent;
import com.tealduck.game.component.ChaseComponent;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.IEvent;


public class EnemyCollision implements IEvent {
	public static final EnemyCollision instance = new EnemyCollision();


	private EnemyCollision() {
	}


	@Override
	public boolean fire(EntityEngine entityEngine, int sender, int receiver, Object data) {
		if ((data == null) || !(data instanceof Intersection)) {
			Gdx.app.log("Event", "Enemy collision event expected an intersection instance");
			return false;
		}
		Intersection intersection = (Intersection) data;

		EntityManager entityManager = entityEngine.getEntityManager();

		CollisionEvents.handleKnockback(entityManager, sender, receiver, intersection);

		CollisionEvents.handleDamage(entityEngine, sender, receiver);

		if (entityManager.entityHasComponent(sender, BulletComponent.class)) {
			BulletComponent bulletComponent = entityManager.getComponent(sender, BulletComponent.class);
			if (!entityManager.entityHasComponent(receiver, ChaseComponent.class)) {
				entityManager.addComponent(receiver, new ChaseComponent(bulletComponent.shooterId));
			}

			MovementComponent movementComponent = entityManager.getComponent(sender,
					MovementComponent.class);
			PositionComponent positionComponent = entityManager.getComponent(receiver,
					PositionComponent.class);
			ChaseComponent chaseComponent = entityManager.getComponent(receiver, ChaseComponent.class);

			Vector2 direction = movementComponent.velocity.cpy().nor().scl(-1);

			chaseComponent.searchDirection.set(direction);
			positionComponent.lookAt.set(direction);
		}

		return false;
	}
}
