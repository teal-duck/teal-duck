package com.tealduck.game.system;


import java.util.Set;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
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
			Vector2 acceleration = movementComponent.acceleration;
			float friction = movementComponent.friction;

			velocity.mulAdd(acceleration, deltaTime);
			acceleration.setZero();
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

			position.set(endX, endY);
			velocity.scl(friction);

			// Handle X first
			// Keep moving the entity to the left/right until they hit a tile or endX
			// Adjust endX based on collision and size
			// Then repeat for Y
			// Move entity to new location

			float x = position.x;
			float y = position.y;
			float w = width;
			float h = height;

			int left = world.xPixelToTile(x);
			int right = world.xPixelToTile(x + w);
			int bottom = world.yPixelToTile(y);
			int top = world.yPixelToTile(y + h);

			Rectangle test = new Rectangle(0, 0, 0, 0);

			float xIntercept = 0;
			float yIntercept = 0;

			if (deltaX > 0) {
				left = world.xPixelToTile(currentX);
				int end = world.xPixelToTile(endX + width) + 1;

				for (; left <= end; left += 1) {
					if (world.isTileCollidable(left, top) || world.isTileCollidable(left, bottom)) {
						// horizontalCollision = true;
						// horizontalCollisionLocation = left;
						break;
					}
				}

			} else if (deltaX < 0) {
				right = world.xPixelToTile(currentX + width) + 1;
				int end = world.xPixelToTile(endX);

				for (; right >= end; right -= 1) {
					if (world.isTileCollidable(right, top)
							|| world.isTileCollidable(right, bottom)) {
						// horizontalCollision = true;
						// horizontalCollisionLocation = right;
						break;
					}
				}
			}

			if (xIntercept < yIntercept) {

			} else {

			}

			// int horizontalCollisionLocation = -1;
			// boolean horizontalCollision = false;
			// int verticalCollisionLocation = -1;
			// boolean verticalCollision = false;
			//

			//
			// }
			//
			// int left = world.xPixelToTile(endX);
			// int right = world.xPixelToTile(endX + width);
			//
			// if (deltaY > 0) {
			// bottom = world.yPixelToTile(currentY);
			// int end = world.yPixelToTile(endY + height) + 1;
			//
			// for (; bottom <= end; bottom += 1) {
			// if (world.isTileCollidable(left, bottom)
			// || world.isTileCollidable(right, bottom)) {
			// verticalCollision = true;
			// verticalCollisionLocation = bottom;
			// break;
			// }
			// }
			//
			// // if (collision) {
			// // float newY = world.yTileToPixel(collisionLocation - 1);
			// // if (newY < endY) {
			// // // endY = newY;
			// // }
			// // }
			// }
			//
			// if (horizontalCollision) {
			// if (deltaX > 0) {
			// float newX = world.xTileToPixel(horizontalCollisionLocation - 1);
			// if (newX < endX) {
			// endX = newX;
			// }
			// } else {
			// float newX = world.xTileToPixel(horizontalCollisionLocation + 1);
			// if (newX > endX) {
			// endX = newX;
			// }
			// }
			// }

			// int extra = 2;
			//
			// int left = world.xPixelToTile(endX - extra);
			// int bottom = world.yPixelToTile(endY - extra);
			// int right = world.xPixelToTile(endX + width + extra);
			// int top = world.yPixelToTile(endY + height + extra);
			//
			// if (deltaX > 0) {
			// // Going right
			// if (world.isTileCollidable(right, top) || world.isTileCollidable(right, bottom)) {
			// endX = world.xTileToPixel(right) - width;
			// }
			// } else {
			// if (world.isTileCollidable(left, top) || world.isTileCollidable(left, bottom)) {
			// endX = world.xTileToPixel(left + 1);
			// }
			// }

			// left = world.xPixelToTile(endX - extra);
			// bottom = world.yPixelToTile(endY - extra);
			// right = world.xPixelToTile(endX + width + extra);
			// top = world.yPixelToTile(endY + height + extra);
			//
			// if (deltaY > 0) {
			// // Going up
			// if (world.isTileCollidable(left, top) || world.isTileCollidable(right, top)) {
			// endY = world.yTileToPixel(top) - height;
			// }
			// } else {
			// if (world.isTileCollidable(left, bottom) || world.isTileCollidable(right, bottom)) {
			// endY = world.yTileToPixel(bottom + 1);
			// }
			// }

		}

	}


	/**
	 * For each entity that has a position and sprite, updates the position in the sprite to be the same as the
	 * position.
	 */
	@SuppressWarnings("unchecked")
	private void updateSpriteLocations() {
		EntityManager entityManager = getEntityManager();

		Set<Integer> entities = entityManager.getEntitiesWithComponents(PositionComponent.class,
				SpriteComponent.class);

		for (int entity : entities) {
			Sprite sprite = entityManager.getComponent(entity, SpriteComponent.class).sprite;
			Vector2 position = entityManager.getComponent(entity, PositionComponent.class).position;

			sprite.setPosition(position.x, position.y);
		}

		entities = entityManager.getEntitiesWithComponents(SpriteComponent.class, MovementComponent.class);

		for (int entity : entities) {
			Sprite sprite = entityManager.getComponent(entity, SpriteComponent.class).sprite;
			Vector2 velocity = entityManager.getComponent(entity, MovementComponent.class).velocity;

			sprite.setRotation(velocity.angle());
		}
	}
}
