package com.tealduck.game.event;


import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.collision.Intersection;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.IEvent;


/**
 *
 */
public class BulletWorldCollision implements IEvent {
	public static final BulletWorldCollision instance = new BulletWorldCollision();


	private BulletWorldCollision() {
	}


	boolean reflectingBullets = false;


	@Override
	public boolean fire(EntityEngine entityEngine, int sender, int receiver, Object data) {
		if (reflectingBullets) {
			if (data instanceof Intersection) {
				// r = d − 2 * (d . n) * n
				Intersection intersection = (Intersection) data;

				MovementComponent movementComponent = entityEngine.getEntityManager()
						.getComponent(receiver, MovementComponent.class);
				PositionComponent positionComponent = entityEngine.getEntityManager()
						.getComponent(receiver, PositionComponent.class);

				Vector2 n = intersection.normal;
				Vector2 d = movementComponent.velocity.cpy().nor();
				Vector2 r = d.cpy().sub(n.cpy().scl(2 * d.dot(n)));

				float speed = movementComponent.velocity.len();
				movementComponent.velocity.set(r).scl(speed);
				positionComponent.lookAt.set(r);
				return false;
			}
		}
		return true;
	}

}
