package com.tealduck.game.system;


import java.util.Set;

import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.GameSystem;


public class MovementSystem extends GameSystem {
	private float worldFriction = 0.8f;
	
	public MovementSystem(EntityEngine entityEngine) {
		super(entityEngine);
	}


	@Override
	public void update(float deltaTime) {
		EntityManager entityManager = getEntityManager();

		@SuppressWarnings("unchecked")
		Set<Integer> entities = entityManager.getEntitiesWithComponents(PositionComponent.class,
				MovementComponent.class);

		for (int entity : entities) {
			MovementComponent movementComponent = entityManager.getComponent(entity,
					MovementComponent.class);
			Vector2 position = entityManager.getComponent(entity, PositionComponent.class).position;
			Vector2 velocity = movementComponent.velocity;
			Vector2 acceleration = movementComponent.acceleration;
			// TODO: Calculate friction based on surface (and mass?)
			float friction = worldFriction; // movementComponent.friction;

			velocity.mulAdd(acceleration, deltaTime);
			acceleration.setZero();

			Vector2 end = position.cpy().mulAdd(velocity, deltaTime);

			position.set(end);
			velocity.scl(friction);
		}
	}
}
