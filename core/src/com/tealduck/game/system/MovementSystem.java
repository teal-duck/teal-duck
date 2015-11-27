package com.tealduck.game.system;


import java.util.Set;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.component.SpriteComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.GameSystem;
import com.tealduck.game.world.World;


public class MovementSystem extends GameSystem {
	private final World world;


	public MovementSystem(EntityEngine entityEngine, World world) {
		super(entityEngine);
		this.world = world;
	}


	@Override
	public void update(float deltaTime) {
		moveEntities(deltaTime);
		updateSpriteLocations();
	}


	@SuppressWarnings("unused")
	private void moveEntities(float deltaTime) {
		EntityManager entityManager = getEntityManager();

		@SuppressWarnings("unchecked")
		Set<Integer> entities = entityManager.getEntitiesWithComponents(PositionComponent.class,
				MovementComponent.class);

		for (int entity : entities) {
			MovementComponent movementComponent = entityManager.getComponent(entity,
					MovementComponent.class);
			Vector2 position = entityManager.getComponent(entity, PositionComponent.class).position;
			Vector2 velocity = movementComponent.velocity;
			Vector2 deltaVelocity = movementComponent.acceleration;
			float friction = movementComponent.friction;

			velocity.mulAdd(deltaVelocity, deltaTime);
			deltaVelocity.setZero();
			// position.mulAdd(velocity, deltaTime);
			//

			float currentX = position.x;
			float currentY = position.y;
			float width = 64;
			float height = 64;
			float endX = position.x + (velocity.x * deltaTime);
			float endY = position.y + (velocity.y * deltaTime);

			float deltaX = endX - currentX;
			float deltaY = endY - currentY;

			// Handle X first
			// Keep moving the entity to the left/right until they hit a tile or endX
			// Adjust endX based on collision and size
			// Then repeat for Y
			// Move entity to new location

			if (deltaX > 0) {
				// Going right
				int left = world.xPixelToTile(currentX);
				int bottom = world.yPixelToTile(currentY);
				int right = world.xPixelToTile(currentX + width);
				int top = world.yPixelToTile(currentY + height);

			} else {

			}

			position.set(endX, endY);

			velocity.scl(friction);
		}

	}


	/**
	 * For each entity that has a position and sprite, updates the position in the sprite to be the same as the
	 * position.
	 */
	private void updateSpriteLocations() {
		EntityManager entityManager = getEntityManager();

		@SuppressWarnings("unchecked")
		Set<Integer> entities = entityManager.getEntitiesWithComponents(PositionComponent.class,
				SpriteComponent.class);

		for (int entity : entities) {
			Sprite sprite = entityManager.getComponent(entity, SpriteComponent.class).sprite;
			Vector2 position = entityManager.getComponent(entity, PositionComponent.class).position;

			sprite.setPosition(position.x, position.y);
		}

	}
}
