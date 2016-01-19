package com.tealduck.game.tests.event;

import org.junit.Assert;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.Team;
import com.tealduck.game.collision.Intersection;
import com.tealduck.game.component.BulletComponent;
import com.tealduck.game.component.DamageComponent;
import com.tealduck.game.component.HealthComponent;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.component.ScoreComponent;
import com.tealduck.game.component.TeamComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.event.EnemyCollision;


public class EnemyCollisionTest {

	@Test
	public void testFire() {
		
		// first part is just returning methods that have already been tested
		
		EntityEngine entityEngine = new EntityEngine();
		EntityManager entityManager = entityEngine.getEntityManager();
		int senderEntity = entityManager.createEntity();
		int receiverEntity = entityManager.createEntity();
		int shooterEntity = entityManager.createEntity();
		Intersection intersection = new Intersection(new Vector2(0,1).nor(), 10f);
		entityManager.addComponent(senderEntity, new DamageComponent(10));
		entityManager.addComponent(receiverEntity, new HealthComponent(20));
		entityManager.addComponent(senderEntity, new TeamComponent(Team.GOOD));
		entityManager.addComponent(receiverEntity, new TeamComponent(Team.BAD));
		entityManager.addComponent(receiverEntity, new PositionComponent(new Vector2(0,0)));
		entityManager.addComponent(senderEntity, new BulletComponent(shooterEntity));
		entityManager.addComponent(shooterEntity,  new ScoreComponent());
		entityManager.addComponent(senderEntity, new MovementComponent(new Vector2(1,1), 0));
		
		EnemyCollision.instance.fire(entityEngine, senderEntity, receiverEntity, intersection);
		
		Assert.assertEquals(entityManager.getComponent(receiverEntity, PositionComponent.class).lookAt, new Vector2(-1,-1).nor());
		// assert that the recieverEntity does turn to look in the opposite direction to the bullet that hit it.
		
	}

}
