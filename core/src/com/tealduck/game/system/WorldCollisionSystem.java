package com.tealduck.game.system;


import java.util.Set;

import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.collision.AABB;
import com.tealduck.game.collision.Circle;
import com.tealduck.game.collision.Collision;
import com.tealduck.game.collision.CollisionShape;
import com.tealduck.game.collision.Intersection;
import com.tealduck.game.component.CollisionComponent;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.GameSystem;
import com.tealduck.game.world.World;


public class WorldCollisionSystem extends GameSystem {
	private final World world;


	public WorldCollisionSystem(EntityEngine entityEngine, World world) {
		super(entityEngine);
		this.world = world;
	}


	@Override
	public void update(float deltaTime) {
		EntityManager entityManager = getEntityManager();

		@SuppressWarnings("unchecked")
		Set<Integer> entities = entityManager.getEntitiesWithComponents(PositionComponent.class,
				CollisionComponent.class);

		for (int entity : entities) {
			PositionComponent positionComponent = entityManager.getComponent(entity,
					PositionComponent.class);
			CollisionComponent collisionComponent = entityManager.getComponent(entity,
					CollisionComponent.class);

			CollisionShape shape = collisionComponent.collisionShape;
			Vector2 offsetFromPosition = collisionComponent.offsetFromPosition;
			Vector2 position = positionComponent.position;

			shape.setPosition(position.cpy().add(offsetFromPosition));

			int collisionAttempts = 10;
			for (int i = 0; i < collisionAttempts; i += 1) {
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
					if (world.isTileCollidable((int) bottomLeftTile.x, (int) bottomLeftTile.y)) {
						collisionAABB.setPosition(world.tileToPixel(bottomLeftTile));
						bottomLeft = Collision.aabbToCircle(collisionAABB, circle);
					}

					Vector2 bottomRightTile = bottomLeftTile.add(1, 0);
					if (world.isTileCollidable((int) bottomRightTile.x, (int) bottomRightTile.y)) {
						collisionAABB.setPosition(world.tileToPixel(bottomRightTile));
						bottomRight = Collision.aabbToCircle(collisionAABB, circle);
					}

					Vector2 topRightTile = bottomRightTile.add(0, 1);
					if (world.isTileCollidable((int) topRightTile.x, (int) topRightTile.y)) {
						collisionAABB.setPosition(world.tileToPixel(topRightTile));
						topRight = Collision.aabbToCircle(collisionAABB, circle);
					}
					Vector2 topLeftTile = topRightTile.add(-1, 0);
					if (world.isTileCollidable((int) topLeftTile.x, (int) topLeftTile.y)) {
						collisionAABB.setPosition(world.tileToPixel(topLeftTile));
						topLeft = Collision.aabbToCircle(collisionAABB, circle);
					}

					Intersection fixIntersection = null;
					float smallestDistance = 64;

					if ((bottomLeft != null) && (bottomLeft.distance < smallestDistance)) {
						fixIntersection = bottomLeft;
						smallestDistance = bottomLeft.distance;
					}
					if ((bottomRight != null) && (bottomRight.distance < smallestDistance)) {
						fixIntersection = bottomRight;
						smallestDistance = bottomRight.distance;
					}
					if ((topLeft != null) && (topLeft.distance < smallestDistance)) {
						fixIntersection = topLeft;
						smallestDistance = topLeft.distance;
					}
					if ((topRight != null) && (topRight.distance < smallestDistance)) {
						fixIntersection = topRight;
						smallestDistance = topRight.distance;
					}

					if (fixIntersection != null) {
						// Vector2 normal = fixIntersection.normal;
						// float distance = fixIntersection.distance;

						Vector2 fixVector = fixIntersection.getResolveVector();

						shape.getPosition().add(fixVector);
						// normal.cpy().setLength(distance));
					} else {
						break;
					}
				} else {
					// TODO: Other shape collision
					System.out.println("TODO: Other shape collision");
				}
			}

			position.set(shape.getPosition().cpy().sub(offsetFromPosition));
		}
	}
}
