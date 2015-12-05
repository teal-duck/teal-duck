package com.tealduck.game.system;


import java.util.Set;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.collision.AABB;
import com.tealduck.game.collision.Circle;
import com.tealduck.game.collision.Collision;
import com.tealduck.game.collision.CollisionShape;
import com.tealduck.game.collision.Intersection;
import com.tealduck.game.component.CollisionComponent;
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
		updateLocations();
	}


	@SuppressWarnings("unchecked")
	private void moveEntities(float deltaTime) {
		EntityManager entityManager = getEntityManager();

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

			Vector2 end = position.cpy().mulAdd(velocity, deltaTime);
			// float endX = position.x + (velocity.x * deltaTime);
			// float endY = position.y + (velocity.y * deltaTime);

			position.set(end);
			velocity.scl(friction);
		}

		entities = entityManager.getEntitiesWithComponents(PositionComponent.class, CollisionComponent.class);

		for (int entity : entities) {
			PositionComponent positionComponent = entityManager.getComponent(entity,
					PositionComponent.class);
			CollisionComponent collisionComponent = entityManager.getComponent(entity,
					CollisionComponent.class);

			CollisionShape shape = collisionComponent.collisionShape;
			Vector2 offsetFromPosition = collisionComponent.offsetFromPosition;
			Vector2 position = positionComponent.position;

			shape.setPosition(position.cpy().add(offsetFromPosition));
			boolean solvedBottomLeft = false;
			boolean solvedBottomRight = false;
			boolean solvedTopLeft = false;
			boolean solvedTopRight = false;

			for (int i = 0; i < 4; i += 1) {
				if (shape instanceof Circle) {
					Circle circle = (Circle) shape;
					AABB aabb = shape.getAABB();

					AABB collisionAABB = new AABB(new Vector2(0, 0), new Vector2(64, 64));

					Intersection bottomLeft = null;
					Intersection bottomRight = null;
					Intersection topLeft = null;
					Intersection topRight = null;

					Vector2 bottomLeftLocation = aabb.getBottomLeft();

					Vector2 bottomLeftTile = world.pixelToTile(bottomLeftLocation);
					if (!solvedBottomLeft) {
						if (world.isTileCollidable((int) bottomLeftTile.x,
								(int) bottomLeftTile.y)) {
							collisionAABB.setPosition(world.tileToPixel(bottomLeftTile));
							bottomLeft = Collision.aabbToCircle(collisionAABB, circle);
						}
					}

					Vector2 bottomRightTile = bottomLeftTile.add(1, 0);
					if (!solvedBottomRight) {
						if (world.isTileCollidable((int) bottomRightTile.x,
								(int) bottomRightTile.y)) {
							collisionAABB.setPosition(world.tileToPixel(bottomRightTile));
							bottomRight = Collision.aabbToCircle(collisionAABB, circle);
						}
					}

					Vector2 topRightTile = bottomRightTile.add(0, 1);
					if (!solvedTopRight) {
						if (world.isTileCollidable((int) topRightTile.x,
								(int) topRightTile.y)) {
							collisionAABB.setPosition(world.tileToPixel(topRightTile));
							topRight = Collision.aabbToCircle(collisionAABB, circle);
						}
					}

					Vector2 topLeftTile = topRightTile.add(-1, 0);
					if (!solvedTopLeft) {
						if (world.isTileCollidable((int) topLeftTile.x, (int) topLeftTile.y)) {
							collisionAABB.setPosition(world.tileToPixel(topLeftTile));
							topLeft = Collision.aabbToCircle(collisionAABB, circle);
						}
					}

					Intersection fixIntersection = null;
					float smallestDistance = 64;

					if (bottomLeft != null) {
						if (bottomLeft.distance < smallestDistance) {
							fixIntersection = bottomLeft;
							smallestDistance = bottomLeft.distance;
						}
					}
					if (bottomRight != null) {
						if (bottomRight.distance < smallestDistance) {
							fixIntersection = bottomRight;
							smallestDistance = bottomRight.distance;
						}
					}
					if (topLeft != null) {
						if (topLeft.distance < smallestDistance) {
							fixIntersection = topLeft;
							smallestDistance = topLeft.distance;
						}
					}
					if (topRight != null) {
						if (topRight.distance < smallestDistance) {
							fixIntersection = topRight;
							smallestDistance = topRight.distance;
						}
					}

					if (fixIntersection != null) {
						if (fixIntersection == bottomLeft) {
							solvedBottomLeft = true;
						}
						if (fixIntersection == bottomRight) {
							solvedBottomRight = true;
						}
						if (fixIntersection == topLeft) {
							solvedTopLeft = true;
						}
						if (fixIntersection == topRight) {
							solvedTopRight = true;
						}
						Vector2 normal = fixIntersection.normal;
						float distance = fixIntersection.distance;

						position.add(normal.cpy().setLength(distance));
// TODO: shouldn't the collision shape position get updated too?
// Move the shape.setPosition etc line into the loop?
					} else {
						break;
					}
				} else {
					// TODO: Other shape collision
					System.out.println("TODO: Other shape collision");
				}
			}
		}
	}

	// TODO: Collision
	// http://gamedev.stackexchange.com/questions/61620/reusable-top-down-collision-class
	// Move entity to location
	// Calculate the intersecting rectangle
	// Find the smaller dimension of the rectangle (width on x or height on y)
	// Push entity out of tile by that amount in that direction
	// Set velocity in that direction to 0

	// Swept AABB
	// http://www.gamedev.net/page/resources/_/technical/game-programming/swept-aabb-collision-detection-and-response-r3084
	// http://www.metanetsoftware.com/technique/tutorialA.html
	// http://www.metanetsoftware.com/technique/tutorialB.html

	// Also make the entities hitbox slightly smaller than 64x64

	// float x = position.x;
	// float y = position.y;
	// float w = width;
	// float h = height;
	//
	// int left = world.xPixelToTile(x);
	// int right = world.xPixelToTile(x + w);
	// int bottom = world.yPixelToTile(y);
	// int top = world.yPixelToTile(y + h);
	//
	// float xIntercept = 0;
	// float yIntercept = 0;
	//
	// if (deltaX > 0) {
	// left = world.xPixelToTile(currentX);
	// int end = world.xPixelToTile(endX + width) + 1;
	//
	// for (; left <= end; left += 1) {
	// if (world.isTileCollidable(left, top) || world.isTileCollidable(left, bottom)) {
	// // horizontalCollision = true;
	// // horizontalCollisionLocation = left;
	// break;
	// }
	// }
	//
	// } else if (deltaX < 0) {
	// right = world.xPixelToTile(currentX + width) + 1;
	// int end = world.xPixelToTile(endX);
	//
	// for (; right >= end; right -= 1) {
	// if (world.isTileCollidable(right, top)
	// || world.isTileCollidable(right, bottom)) {
	// // horizontalCollision = true;
	// // horizontalCollisionLocation = right;
	// break;
	// }
	// }
	// }
	//
	// if (xIntercept < yIntercept) {
	//
	// } else {
	//
	// }

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


	/**
	 * For each entity that has a position and sprite, updates the position in the sprite to be the same as the
	 * position.
	 */
	@SuppressWarnings("unchecked")
	private void updateLocations() {
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

			sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);
			sprite.setRotation(velocity.angle());
		}

		entities = entityManager.getEntitiesWithComponents(PositionComponent.class, CollisionComponent.class);

		for (int entity : entities) {
			CollisionComponent collisionComponent = entityManager.getComponent(entity,
					CollisionComponent.class);
			CollisionShape shape = collisionComponent.collisionShape;
			Vector2 offsetFromPosition = collisionComponent.offsetFromPosition;
			Vector2 position = entityManager.getComponent(entity, PositionComponent.class).position;

			shape.setPosition(position.cpy().add(offsetFromPosition));
		}
	}
}
