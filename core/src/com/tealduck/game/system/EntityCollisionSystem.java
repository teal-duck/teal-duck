package com.tealduck.game.system;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.tealduck.game.EventName;
import com.tealduck.game.collision.Collision;
import com.tealduck.game.collision.CollisionShape;
import com.tealduck.game.collision.Intersection;
import com.tealduck.game.component.CollisionComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EventManager;
import com.tealduck.game.engine.GameSystem;


public class EntityCollisionSystem extends GameSystem {
	public EntityCollisionSystem(EntityEngine entityEngine) {
		super(entityEngine);
	}


	@Override
	public void update(float deltaTime) {
		EntityManager entityManager = getEntityManager();
		EventManager eventManager = getEventManager();

		// @SuppressWarnings("unchecked")
		Set<Integer> entitiesSet = entityManager.getEntitiesWithComponent(CollisionComponent.class);
		List<Integer> entities = new ArrayList<Integer>(entitiesSet);

		// Only test entity N with entity K when K > N
		while (!entities.isEmpty()) {
			int entity = entities.get(0);
			entities.remove(0);

			CollisionComponent cc0 = entityManager.getComponent(entity, CollisionComponent.class);
			CollisionShape s0 = cc0.collisionShape;

			for (int other : entities) {
				CollisionComponent cc1 = entityManager.getComponent(other, CollisionComponent.class);
				CollisionShape s1 = cc1.collisionShape;

				Intersection intersection = Collision.shapeToShape(s0, s1);

				if (intersection == null) {
					continue;
				}

				eventManager.triggerEvent(other, entity, EventName.COLLISION, intersection);
				eventManager.triggerEvent(entity, other, EventName.COLLISION,
						intersection.flippedCopy());
			}
		}
	}
}
