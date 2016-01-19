package com.tealduck.game.event;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.collision.Intersection;
import com.tealduck.game.component.BulletComponent;
import com.tealduck.game.component.ChaseComponent;
import com.tealduck.game.component.KnockbackComponent;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.IEvent;


/**
 *
 */
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

		Vector2 lookAt = null;
		int lookAtId = sender;
		// If the enemy was hit by a bullet, look in the direction the bullet came from
		if (entityManager.entityHasComponent(sender, BulletComponent.class)) {
			BulletComponent bulletComponent = entityManager.getComponent(sender, BulletComponent.class);
			MovementComponent movementComponent = entityManager.getComponent(sender,
					MovementComponent.class);

			// Direction to look is the reverse of the direction the bullet was travelling
			lookAt = movementComponent.velocity.cpy().scl(-1);
			lookAtId = bulletComponent.shooterId;

		} else if (!CollisionEvents.areEntitiesOnSameTeam(entityManager, sender, receiver)
				&& entityManager.entityHasComponent(sender, KnockbackComponent.class)) {
			// Enemy hit by the player, should look at him
			Vector2 senderPosition = entityManager.getComponent(sender, PositionComponent.class)
					.getCenter();
			Vector2 receiverPosition = entityManager.getComponent(receiver, PositionComponent.class)
					.getCenter();

			lookAt = senderPosition.cpy().sub(receiverPosition);
			lookAtId = sender;
		}

		if (lookAt != null) {
			lookAt.nor();
			if (!entityManager.entityHasComponent(receiver, ChaseComponent.class)) {
				entityManager.addComponent(receiver, new ChaseComponent(lookAtId));
			}

			PositionComponent positionComponent = entityManager.getComponent(receiver,
					PositionComponent.class);
			ChaseComponent chaseComponent = entityManager.getComponent(receiver, ChaseComponent.class);
			chaseComponent.searchDirection.set(lookAt);
			positionComponent.lookAt.set(lookAt);
		}

		return false;
	}
}
