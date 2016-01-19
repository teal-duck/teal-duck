package com.tealduck.game.tests.event;

import org.junit.Assert;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.collision.Intersection;
import com.tealduck.game.component.BulletComponent;
import com.tealduck.game.component.DamageComponent;
import com.tealduck.game.component.HealthComponent;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.event.PlayerCollision;



public class PlayerCollisionTest {

	@Test
	public void testFire() {
		
		EntityEngine entityEngine = new EntityEngine();
		EntityManager entityManager = entityEngine.getEntityManager();
		Intersection intersection = new Intersection(new Vector2(0,1).nor(), 10f);
		int bulletEntity = entityManager.createEntity();
		int receiverEntity = entityManager.createEntity();

		entityManager.addComponent(bulletEntity, new DamageComponent(10));
		entityManager.addComponent(receiverEntity, new HealthComponent(20));
		entityManager.addComponent(receiverEntity, new PositionComponent(new Vector2(0,0)));
		entityManager.addComponent(bulletEntity, new BulletComponent(receiverEntity));
		entityManager.addComponent(bulletEntity, new MovementComponent(new Vector2(1,1), 0));
		
		PlayerCollision.instance.fire(entityEngine, bulletEntity, receiverEntity, intersection);
		
		Assert.assertEquals(entityManager.getComponent(receiverEntity, HealthComponent.class).health,20);
		//assert that own bullet does not harm self
		//others are calling methods that have already been tested
		
	}

}
