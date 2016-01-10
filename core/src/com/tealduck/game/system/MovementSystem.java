package com.tealduck.game.system;


import java.util.Set;

import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.GameSystem;


public class MovementSystem extends GameSystem {
	// private float worldFriction = 0.8f;

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
			PositionComponent positionComponent = entityManager.getComponent(entity,
					PositionComponent.class);
			Vector2 position = positionComponent.position;
			Vector2 velocity = movementComponent.velocity;
			Vector2 acceleration = movementComponent.acceleration;
			// TODO: Calculate friction based on surface (and mass?)
			float friction = movementComponent.friction;

			float MAX_VELOCITY = 1500f;

			velocity.mulAdd(acceleration, deltaTime);
			acceleration.setZero();
			velocity.limit(MAX_VELOCITY);
			position.mulAdd(velocity, deltaTime);
			velocity.scl(friction);
		}
	}
}
