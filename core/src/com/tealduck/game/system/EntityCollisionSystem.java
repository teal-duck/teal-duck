package com.tealduck.game.system;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.tealduck.game.EventName;
import com.tealduck.game.collision.Circle;
import com.tealduck.game.collision.Collision;
import com.tealduck.game.collision.Intersection;
import com.tealduck.game.component.CollisionComponent;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PositionComponent;
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
			if (!(cc0.collisionShape instanceof Circle)) {
				System.out.println("TODO: aabb collision");
				continue;
			}

			Circle c0 = (Circle) cc0.collisionShape;

			for (int other : entities) {
				CollisionComponent cc1 = entityManager.getComponent(other, CollisionComponent.class);

				if (!(cc1.collisionShape instanceof Circle)) {
					System.out.println("TODO: aabb collision");
					continue;
				}

				Circle c1 = (Circle) cc1.collisionShape;

				Intersection intersection = Collision.circleToCircle(c0, c1);

				if (intersection == null) {
					continue;
				}

				// System.out.println("Collide: " + intersection.toString());
				// TODO: Send entity collisions to events
				// TODO: Clean up keeping position and collision in sync
				// TODO: Move collision bouncing into events
				PositionComponent p = null;
				CollisionComponent cc = null;
				if (entityManager.entityHasComponent(entity, PositionComponent.class)) {
					p = entityManager.getComponent(entity, PositionComponent.class);
					cc = cc0;
				} else if (entityManager.entityHasComponent(other, PositionComponent.class)) {
					p = entityManager.getComponent(other, PositionComponent.class);
					cc = cc1;
					intersection.normal.scl(-1);
				}

				if (p != null) {
					cc.collisionShape.getPosition().add(intersection.getResolveVector());
					p.position.set(cc.collisionShape.getPosition().cpy()
							.sub(cc.offsetFromPosition));
				}

				entityManager.getComponent(entity, MovementComponent.class).acceleration
						.add(intersection.normal.cpy().scl(80000));

				eventManager.triggerEvent(other, entity, EventName.COLLISION);
			}
		}
	}
}
