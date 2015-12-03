package com.tealduck.game.collision;


import com.badlogic.gdx.math.Vector2;


public class Collision {
	/**
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
	 * @param lineDirection
	 * @param b0p0
	 * @param b0p1
	 * @param b1p0
	 * @param b1p1
	 * @return
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
	 * Closest and furthers vectors get mutated
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

		if (y < top) {
			closer.y = top;
			further.y = bottom;
		} else if (y > bottom) {
			closer.y = bottom;
			further.y = top;
		} else {
			closer.y = y;
			further.y = y;
		}
	}


	/**
	 * 0 if no collision, else the smallest overlap
	 *
	 * @param aabb
	 * @param circle
	 * @return
	 */
	public static float aabbToCircle(AABB aabb, Circle circle, Vector2 normal) {
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
			normal.set(projection);
		} else {
			normal.setZero();
		}

		return overlap;
	}
}
