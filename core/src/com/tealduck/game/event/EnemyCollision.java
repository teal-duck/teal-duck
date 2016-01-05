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
		// TODO: Maybe change how data is passed to events
		if ((data == null) || !(data instanceof Intersection)) {
			Gdx.app.log("Event", "Enemy collision event expected an intersection instance");
			return false;
		}
		Intersection intersection = (Intersection) data;

		EntityManager entityManager = entityEngine.getEntityManager();

		CollisionEvents.doKnockback(entityManager, sender, receiver, intersection);

		CollisionEvents.doDamage(entityManager, sender, receiver);

		return false;
	}
}
