package com.tealduck.game.event;


import com.tealduck.game.component.BulletComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.IEvent;


/**
 * 
 */
public class BulletCollision implements IEvent {
	public static final BulletCollision instance = new BulletCollision();


	private BulletCollision() {
	}


	@Override
	public boolean fire(EntityEngine entityEngine, int sender, int receiver, Object data) {
		EntityManager entityManager = entityEngine.getEntityManager();

		BulletComponent bulletComponent = entityManager.getComponent(receiver, BulletComponent.class);
		int shooterId = bulletComponent.shooterId;

		// Don't harm the shooter
		if (sender == shooterId) {
			return false;
		}

		// Collided with another bullet
		if (entityManager.entityHasComponent(sender, BulletComponent.class)) {
			BulletComponent otherBulletComponent = entityManager.getComponent(sender,
					BulletComponent.class);
			int otherShooterId = otherBulletComponent.shooterId;

			// Remove bullet if collided with an enemy's bullet
			return (shooterId != otherShooterId);
		}
		
		return false;
	}
}
