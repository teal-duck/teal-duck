package com.tealduck.game.weapon;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.EventName;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.component.SpriteComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EventManager;
import com.tealduck.game.event.BulletCollision;
import com.tealduck.game.world.EntityConstants;
import com.tealduck.game.world.EntityLoader;


public class MachineGun extends Weapon {
	private Texture bulletTexture;


	public MachineGun(Texture bulletTexture) {
		this.bulletTexture = bulletTexture;
	}


	@Override
	public int ammoRequiredToFire() {
		return 1;
	}


	@Override
	public int fire(EntityEngine entityEngine, Vector2 position, Vector2 direction) {
		direction.nor();

		EntityManager entityManager = entityEngine.getEntityManager();

		int bulletId = entityManager.createEntity();

		entityManager.addComponent(bulletId, new PositionComponent(position, direction));

		float bulletSpeed = EntityConstants.BULLET_SPEED;
		entityManager.addComponent(bulletId,
				new MovementComponent(direction.cpy().scl(bulletSpeed), bulletSpeed, 0, 1));

		entityManager.addComponent(bulletId, new SpriteComponent(bulletTexture));

		int bulletRadius = EntityConstants.BULLET_RADIUS;
		EntityLoader.addEntityCircleCollisionComponent(entityManager, bulletId, position, bulletRadius);

		EventManager eventManager = entityEngine.getEventManager();
		eventManager.addEvent(bulletId, EventName.COLLISION, BulletCollision.instance);

		return 1;
	}
}
