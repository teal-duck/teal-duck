package com.tealduck.game.collision;


import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;


/**
 * Static methods for detecting and resolving collisions
 */
public class Collision {
	// TODO: Write detection functions that return bool instead of calculating resolution vectors

	/**
	 * Returns the length of the overlap between two 1D ranges of numbers.
	 *
	 * @param min1
	 * @param max1
	 * @param min2
	 * @param max2
	 * @return
	 */
	public static float overlap(float min1, float max1, float min2, float max2) {
		return Math.max(0, Math.min(max1, max2) - Math.max(min1, min2));
	}


	/**
	 * Projects the points onto the line defined by lineDirection and calculates their overlap.
	 *
	 * @param lineDirection
	 * @param b0p0
	 * @param b0p1
	 * @param b1p0
	 * @param b1p1
	 * @return
	 * @see {@link Collision#overlap(float, float, float, float)}
	 */
	public static float getOverlapForPointsOnLine(Vector2 lineDirection, Vector2 b0p0, Vector2 b0p1, Vector2 b1p0,
			Vector2 b1p1) {
		float b0p0AlongLine = b0p0.dot(lineDirection);
		float b0p1AlongLine = b0p1.dot(lineDirection);
		float b1p0AlongLine = b1p0.dot(lineDirection);
		float b1p1AlongLine = b1p1.dot(lineDirection);

		float b0Left = Math.min(b0p0AlongLine, b0p1AlongLine);
		float b0Right = Math.max(b0p0AlongLine, b0p1AlongLine);

		float b1Left = Math.min(b1p0AlongLine, b1p1AlongLine);
		float b1Right = Math.max(b1p0AlongLine, b1p1AlongLine);

		return Collision.overlap(b0Left, b0Right, b1Left, b1Right);
	}


	/**
	 * Given an AABB and Circle, this calculates the points on the AABB for projection in collision detection.
	 * Closest and furthers vector parameters get mutated.
	 *
	 * @param aabb
	 * @param circle
	 * @param closest
	 * @param furthest
	 */
	public static void getCloserAndFurtherPoints(AABB aabb, Circle circle, Vector2 closer, Vector2 further) {
		float x = circle.getCenterX();
		float y = circle.getCenterY();
		float left = aabb.getLeft();
		float right = aabb.getRight();
		float top = aabb.getTop();
		float bottom = aabb.getBottom();

		// TODO: FIX BUG

		// Bug here. If x == (left or right) or y == (top or bottom), closer and further will
		// be equal. This can make methods calling this method believe 
		// objects do not intersect, when they actually do.
		// This bug does not appear to negatively affect gameplay in current
		// build.

		if (x < left) {
			closer.x = left;
			further.x = right;
		} else if (x > right) {
			closer.x = right;
			further.x = left;
		} else {
			closer.x = x;
			further.x = x;
		}

		if (y < bottom) {
			closer.y = bottom;
			further.y = top;
		} else if (y > top) {
			closer.y = top;
			further.y = bottom;
		} else {
			closer.y = y;
			further.y = y;
		}
	}


	/**
	 * @param aabb
	 * @param pointInAABB
	 * @return
	 */
	public static Vector2 vectorFromCenterOfAABBToEdge(AABB aabb, Vector2 pointInAABB) {
		// http://stackoverflow.com/questions/3180000/calculate-a-vector-from-a-point-in-a-rectangle-to-edge-based-on-angle
		
		// TODO: Bug
		// If pointInAABB == aabb.center, then method will return (0, 0)
		// Might be correct functionality here, but results in bug in
		// circleToAabb
		Vector2 vec = pointInAABB.cpy().sub(aabb.getCenter());
		float angle = vec.angleRad();

		float cosAngle = Math.abs(MathUtils.cos(angle));
		float sinAngle = Math.abs(MathUtils.sin(angle));

		float magnitude = 0;
		if (((aabb.getWidth() / 2.0f) * sinAngle) <= ((aabb.getHeight() / 2.0f) * cosAngle)) {
			magnitude = aabb.getWidth() / 2.0f / cosAngle;
		} else {
			magnitude = aabb.getHeight() / 2.0f / sinAngle;
		}

		return vec.cpy().nor().scl(magnitude);
	}


	/**
	 * Normal returned is to push circle out of aabb. Null if no intersection.
	 *
	 * @param aabb
	 * @param circle
	 * @return
	 */
	public static Intersection circleToAabb(Circle circle, AABB aabb) {
		if (aabb.containsPoint(circle.getCenter())) {
			Vector2 vec = Collision.vectorFromCenterOfAABBToEdge(aabb, circle.getCenter());
			return new Intersection(vec.cpy().nor(), vec.len() + circle.getRadius() - (circle.getCenter().cpy().sub(aabb.getCenter()).len()));
		}

		Vector2 closerPoint = new Vector2(0, 0);
		Vector2 furtherPoint = new Vector2(0, 0);

		Collision.getCloserAndFurtherPoints(aabb, circle, closerPoint, furtherPoint);
		Vector2 projection = circle.getCenter().cpy().sub(closerPoint).nor();

		if (projection.isZero()) {
			projection = circle.getCenter().cpy().sub(furtherPoint).nor();
		}

		Vector2 circleExtents = projection.cpy().scl(circle.getRadius());
		Vector2 circleCloser = circle.getCenter().cpy().sub(circleExtents);
		Vector2 circleFurther = circle.getCenter().cpy().add(circleExtents);

		float overlap = Collision.getOverlapForPointsOnLine(projection, furtherPoint, closerPoint, circleCloser,
				circleFurther);

		if (overlap > 0) {
			return new Intersection(projection, overlap);
		} else {
			return null;
		}
	}


	/**
	 * Normal returned is for pushing b0 out of b1. Null if no intersection.
	 * Never returns a diagonal vector. If distance to push is same in both axes, prefers x.
	 *
	 * @param b0
	 * @param b1
	 * @return
	 */
	public static Intersection aabbToAabb(AABB b0, AABB b1) {
		// https://gitlab.labcomp.cl/mijara/jmoka-engine/raw/0786b26b4f9302228af8bca827369f319f58c03c/src/com/moka/physics/Collider.java
		if (!((b0.getBottom() >= b1.getTop()) || (b0.getTop() <= b1.getBottom())
				|| (b0.getLeft() >= b1.getRight()) || (b0.getRight() <= b1.getLeft()))) {
			float top = b1.getTop() - b0.getBottom();
			float bot = b0.getTop() - b1.getBottom();
			float left = b0.getRight() - b1.getLeft();
			float right = b1.getRight() - b0.getLeft();

			float y = (top < bot) ? top : 0 - bot;
			float x = (right < left) ? right : 0 - left;

			Vector2 normal = new Vector2();
			float depth = 0;
			if (Math.abs(y) < Math.abs(x)) {
				normal.set(0, Math.signum(y));
				depth = y;
			} else {
				normal.set(Math.signum(x), 0);
				depth = x;
			}

			return new Intersection(normal, depth);
		}

		return null;
	}


	/**
	 * Normal returned is for pushing c0 out of c1. Null if no intersection.
	 *
	 * @param c0
	 * @param c1
	 * @return
	 */
	public static Intersection circleToCircle(Circle c0, Circle c1) {
		float r0 = c0.getRadius();
		float r1 = c1.getRadius();
		float radiusSum = r0 + r1;
		float radiusSumSquared = radiusSum * radiusSum;

		Vector2 diff = c0.getCenter().cpy().sub(c1.getCenter());
		float distanceSquared = diff.len2();

		if (distanceSquared < radiusSumSquared) {
			float distance = diff.len();
			return new Intersection(diff.nor(), (radiusSum - distance));
		} else {
			return null;
		}
	}


	/**
	 * Calculate the intersection between 2 shapes. Intersection returned is for pushing s0 out of s1. Null if there
	 * is no collision.
	 *
	 * @param s0
	 * @param s1
	 * @return
	 */
	public static Intersection shapeToShape(CollisionShape s0, CollisionShape s1) {
		if ((s0 instanceof Circle) && (s1 instanceof Circle)) {
			return Collision.circleToCircle((Circle) s0, (Circle) s1);

		} else if ((s0 instanceof Circle) && (s1 instanceof AABB)) {
			return Collision.circleToAabb((Circle) s0, (AABB) s1);

		} else if ((s0 instanceof AABB) && (s1 instanceof Circle)) {
			Intersection intersection = Collision.circleToAabb((Circle) s1, (AABB) s0);
			// Need to flip because aabbToCircle resolution vector pushes circle out of aabb
			// But we have the aabb to be pushed out of circle
			if (intersection != null) {
				intersection = intersection.getFlippedCopy();
			}

			return intersection;

		} else if ((s0 instanceof AABB) && (s1 instanceof AABB)) {
			return Collision.aabbToAabb((AABB) s0, (AABB) s1);

		} else {
			return null;
		}
	}
}
