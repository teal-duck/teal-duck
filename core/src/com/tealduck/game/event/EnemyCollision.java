package com.tealduck.game.event;


import com.badlogic.gdx.Gdx;
import com.tealduck.game.collision.Intersection;
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

		CollisionEvents.handleDamage(entityManager, sender, receiver);

		return false;
	}
}
