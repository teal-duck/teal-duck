package com.tealduck.game.tests.event;


import org.junit.Assert;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.Team;
import com.tealduck.game.collision.Intersection;
import com.tealduck.game.component.BulletComponent;
import com.tealduck.game.component.ChaseComponent;
import com.tealduck.game.component.DamageComponent;
import com.tealduck.game.component.HealthComponent;
import com.tealduck.game.component.KnockbackComponent;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PickupComponent;
import com.tealduck.game.component.ScoreComponent;
import com.tealduck.game.component.TeamComponent;
import com.tealduck.game.component.WeaponComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.event.CollisionEvents;
import com.tealduck.game.pickup.AmmoPickup;
import com.tealduck.game.weapon.MachineGun;


public class CollisionEventsTest {
	@Test
	public void testAreEntitiesOnSameTeam() {
		EntityManager entityManager = new EntityManager();
		int e1 = entityManager.createEntity();
		int e2 = entityManager.createEntity();
		int e3 = entityManager.createEntity();
		int e4 = entityManager.createEntity();
		int e5 = entityManager.createEntity();
		int e6 = entityManager.createEntity();

		entityManager.addComponent(e1, new TeamComponent(Team.GOOD));
		entityManager.addComponent(e2, new TeamComponent(Team.GOOD));
		entityManager.addComponent(e3, new TeamComponent(Team.BAD));
		entityManager.addComponent(e4, new TeamComponent(Team.BAD));
		Assert.assertTrue(CollisionEvents.areEntitiesOnSameTeam(entityManager, e1, e2));// Good + Good
		Assert.assertTrue(CollisionEvents.areEntitiesOnSameTeam(entityManager, e3, e4));// Bad+bad
		Assert.assertFalse(CollisionEvents.areEntitiesOnSameTeam(entityManager, e1, e3));// Good+Bad
		Assert.assertFalse(CollisionEvents.areEntitiesOnSameTeam(entityManager, e4, e2));// Bad+Good
		Assert.assertTrue(CollisionEvents.areEntitiesOnSameTeam(entityManager, e1, e5));// Good+None
		Assert.assertTrue(CollisionEvents.areEntitiesOnSameTeam(entityManager, e3, e5));// bad+none
		Assert.assertTrue(CollisionEvents.areEntitiesOnSameTeam(entityManager, e5, e6));// none+none
	}


	@Test
	public void testHandleKnockback() {
		EntityManager entityManager = new EntityManager();
		int e1 = entityManager.createEntity();
		int e2 = entityManager.createEntity();
		int e3 = entityManager.createEntity();

		entityManager.addComponent(e1, new TeamComponent(Team.GOOD));
		entityManager.addComponent(e2, new TeamComponent(Team.GOOD));
		entityManager.addComponent(e3, new TeamComponent(Team.BAD));
		entityManager.addComponent(e1, new KnockbackComponent(60000f));
		entityManager.addComponent(e2, new KnockbackComponent(60000f));
		entityManager.addComponent(e3, new KnockbackComponent(60000f));
		entityManager.addComponent(e1, new MovementComponent(new Vector2(0, 0), 3f));
		entityManager.addComponent(e2, new MovementComponent(new Vector2(0, 0), 3f));
		entityManager.addComponent(e3, new MovementComponent(new Vector2(0, 0), 3f));
		Intersection intersection = new Intersection(new Vector2(0, 1), 15f);

		CollisionEvents.handleKnockback(entityManager, e1, e2, intersection);
		// System.out.println(entityManager.getComponent(e2, MovementComponent.class).velocity);
		// test same team
		Assert.assertEquals(entityManager.getComponent(e2, MovementComponent.class).velocity,
				new Vector2(0.0f, 22.5f));
		Assert.assertEquals(entityManager.getComponent(e1, MovementComponent.class).velocity,
				new Vector2(0.0f, 0.0f));

		// test opposite team
		entityManager.removeComponent(e2, MovementComponent.class);
		entityManager.addComponent(e2, new MovementComponent(new Vector2(0, 0), 3f));

		Intersection intersection2 = new Intersection(new Vector2(0, 1), 15f);

		entityManager.addComponent(e3, new ChaseComponent());
		CollisionEvents.handleKnockback(entityManager, e2, e3, intersection2);
		// System.out.println(entityManager.getComponent(e2, MovementComponent.class).acceleration);
		// System.out.println(entityManager.getComponent(e3,MovementComponent.class).acceleration);

		Assert.assertEquals(((entityManager.getComponent(e3, MovementComponent.class).acceleration)),
				new Vector2(0, 60000));
		// System.out.println(entityManager.getComponent(e3, ChaseComponent.class).searchDirection);
		Assert.assertEquals((entityManager.getComponent(e3, ChaseComponent.class).searchDirection),
				new Vector2(0, 1)); // Test gets chase component

	}


	@Test
	public void testPushEntitiesApart() {

		EntityManager entityManager = new EntityManager();
		int e1 = entityManager.createEntity();
		int e2 = entityManager.createEntity();
		Intersection intersection = new Intersection(new Vector2(0, 1), 15f);
		entityManager.addComponent(e1, new MovementComponent(new Vector2(0, 0), 3f));
		entityManager.addComponent(e2, new MovementComponent(new Vector2(0, 0), 3f));
		CollisionEvents.pushEntitiesApart(entityManager, e1, e2, intersection);

		Assert.assertEquals(entityManager.getComponent(e2, MovementComponent.class).velocity,
				new Vector2(0.0f, 22.5f));
		Assert.assertEquals(entityManager.getComponent(e1, MovementComponent.class).velocity,
				new Vector2(0.0f, 0.0f));

	}


	@Test
	public void testHandleDamage() {

		EntityEngine entityEngine = new EntityEngine();
		EntityManager entityManager = entityEngine.getEntityManager();
		int e1 = entityManager.createEntity();
		int e2 = entityManager.createEntity();
		int e3 = entityManager.createEntity();
		entityManager.addComponent(e1, new TeamComponent(Team.BAD));
		entityManager.addComponent(e2, new TeamComponent(Team.GOOD));
		entityManager.addComponent(e3, new TeamComponent(Team.GOOD));
		entityManager.addComponent(e1, new DamageComponent(3));
		entityManager.addComponent(e2, new HealthComponent(5));
		entityManager.addComponent(e3, new DamageComponent(3));
		CollisionEvents.handleDamage(entityEngine, e1, e2);

		Assert.assertEquals(entityManager.getComponent(e2, HealthComponent.class).health, 2); // testing took
													// damage

		CollisionEvents.handleDamage(entityEngine, e3, e2);

		Assert.assertEquals(entityManager.getComponent(e2, HealthComponent.class).health, 2); // testing no
													// further
													// damage from
													// ally

		CollisionEvents.handleDamage(entityEngine, e1, e2);

		Assert.assertEquals(entityManager.getComponent(e2, HealthComponent.class).health, 0); // Testing minimum
													// health of 0
													// with no score
													// component

		entityManager.addComponent(e2, new HealthComponent(2)); // returning health to 2

		entityManager.addComponent(e1, new ScoreComponent()); // adding Score Component to attacker

		CollisionEvents.handleDamage(entityEngine, e1, e2); // killing again

		Assert.assertEquals(entityManager.getComponent(e1, ScoreComponent.class).workingScore, 100); // assert
														// that
														// handleScoreForEntityDeath
														// was
														// called

	}


	@Test
	public void testHandleScoreForEntityDeath() {

		// first part already tested in call from testHandleDamage

		EntityManager entityManager = new EntityManager();
		int bulletEntity = entityManager.createEntity();
		int shooterEntity = entityManager.createEntity();
		int deadEntity = entityManager.createEntity();

		entityManager.addComponent(bulletEntity, new BulletComponent(shooterEntity));
		entityManager.addComponent(shooterEntity, new ScoreComponent());
		CollisionEvents.handleScoreForEntityDeath(entityManager, bulletEntity, deadEntity);
		Assert.assertEquals(entityManager.getComponent(shooterEntity, ScoreComponent.class).workingScore, 100); // assert
															// that
															// the
															// score
															// component
															// of
															// the
															// shooter
															// entity
															// that
															// fired
															// the
															// bullet
															// is
															// awarded
															// the
															// score
															// for
															// the
															// kill
	}


	@Test
	public void testHandlePickup() {

		EntityEngine entityEngine = new EntityEngine();
		EntityManager entityManager = entityEngine.getEntityManager();
		MachineGun machineGun = new MachineGun(null);
		int senderEntity = entityManager.createEntity();
		int receiverEntity = entityManager.createEntity();

		entityManager.addComponent(senderEntity, new PickupComponent(new AmmoPickup(5)));
		entityManager.addComponent(receiverEntity, new WeaponComponent(machineGun, 10, 10));
		CollisionEvents.handlePickup(entityEngine, senderEntity, receiverEntity);

		Assert.assertEquals(entityManager.getComponent(receiverEntity, WeaponComponent.class).extraAmmo, 15); // assert
															// pickup
															// function
															// works

		Assert.assertTrue(entityEngine.isEntityFlaggedToRemove(senderEntity)); // assert pickup flagged for
											// removal

	}

}
