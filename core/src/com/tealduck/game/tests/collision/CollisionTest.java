package com.tealduck.game.tests.collision;


import org.junit.Assert;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.collision.AABB;
import com.tealduck.game.collision.Circle;
import com.tealduck.game.collision.Collision;


/*
 * This test class fails, due to known bugs.
 * We were unfortunately unable to fix bugs in time for ship date,
 * but from visual tests, these bugs do not seem to impact game in current state.
 */
public class CollisionTest {
	private static final double DELTA = 1e-4;


	@Test
	public void testOverlap() {

		Assert.assertEquals(1, Collision.overlap(0, 2, 1, 3), CollisionTest.DELTA);
		Assert.assertEquals(1, Collision.overlap(1, 3, 0, 2), CollisionTest.DELTA);

		// Check that passing parameters incorrectly gives 0
		Assert.assertEquals(0, Collision.overlap(2, 0, 1, 3), CollisionTest.DELTA);

		// Check that non-overlapping lines are handled correctly
		Assert.assertEquals(0, Collision.overlap(0, 1, 2, 3), CollisionTest.DELTA);
	}


	@Test
	public void testGetOverlapForPointsOnLine() {
		Vector2 lineDirection = new Vector2(1, 1);
		Vector2 point1 = new Vector2(2, 0);
		Vector2 point2 = new Vector2(4, 3);
		Vector2 point3 = new Vector2(3, 0);
		Vector2 point4 = new Vector2(7, 4);

		// Check that order of point parameters has no effect
		Assert.assertEquals(4,
				Collision.getOverlapForPointsOnLine(lineDirection, point1, point2, point3, point4),
				CollisionTest.DELTA);
		Assert.assertEquals(4,
				Collision.getOverlapForPointsOnLine(lineDirection, point2, point1, point4, point3),
				CollisionTest.DELTA);
		Assert.assertEquals(4,
				Collision.getOverlapForPointsOnLine(lineDirection, point3, point4, point1, point2),
				CollisionTest.DELTA);
		Assert.assertEquals(4,
				Collision.getOverlapForPointsOnLine(lineDirection, point4, point3, point2, point1),
				CollisionTest.DELTA);

		// non overlapping points
		Assert.assertEquals(0,
				Collision.getOverlapForPointsOnLine(lineDirection, point1, point3, point2, point4),
				CollisionTest.DELTA);

		// negative line direction
		Assert.assertEquals(4, Collision.getOverlapForPointsOnLine(new Vector2(-1, -1), point1, point2, point3,
				point4), CollisionTest.DELTA);
	}


	@Test
	public void testGetCloserAndFurtherPoints() {
		AABB aabb = new AABB(new Vector2(), new Vector2(1, 1));
		Circle circle = new Circle(new Vector2(4, 2), 1);
		Vector2 closer = new Vector2();
		Vector2 further = new Vector2();

		Collision.getCloserAndFurtherPoints(aabb, circle, closer, further);

		Assert.assertEquals(0, further.x, CollisionTest.DELTA);
		Assert.assertEquals(0, further.y, CollisionTest.DELTA);
		Assert.assertEquals(1, closer.x, CollisionTest.DELTA);
		Assert.assertEquals(1, closer.y, CollisionTest.DELTA);

		// Test fails due to known bug in Collision.getCloserAndFurtherPoints().
		// See comment in Collision.getCloserAndFurtherPoints().
		circle = new Circle(new Vector2(), 1);
		closer = new Vector2();
		further = new Vector2();
		Collision.getCloserAndFurtherPoints(aabb, circle, closer, further);
		Assert.assertFalse(closer.equals(further));
	}


	@Test
	public void testVectorFromCenterOfAABBToEdge() {
		AABB aabb = new AABB(new Vector2(), new Vector2(1, 1));

		Vector2 up = new Vector2(0.5f, 0.6f);
		Assert.assertEquals(0, Collision.vectorFromCenterOfAABBToEdge(aabb, up).x, CollisionTest.DELTA);
		Assert.assertEquals(0.5, Collision.vectorFromCenterOfAABBToEdge(aabb, up).y, CollisionTest.DELTA);

		Vector2 left = new Vector2(0.4f, 0.5f);
		Assert.assertEquals(-0.5, Collision.vectorFromCenterOfAABBToEdge(aabb, left).x, CollisionTest.DELTA);
		Assert.assertEquals(0, Collision.vectorFromCenterOfAABBToEdge(aabb, left).y, CollisionTest.DELTA);

		Vector2 diagonalUpRight = new Vector2(0.6f, 0.6f);
		Assert.assertEquals(0.5, Collision.vectorFromCenterOfAABBToEdge(aabb, diagonalUpRight).x,
				CollisionTest.DELTA);
		Assert.assertEquals(0.5, Collision.vectorFromCenterOfAABBToEdge(aabb, diagonalUpRight).y,
				CollisionTest.DELTA);

		Vector2 center = new Vector2(0.5f, 0.5f);
		Assert.assertEquals(0.5, Collision.vectorFromCenterOfAABBToEdge(aabb, center).x, CollisionTest.DELTA);
		Assert.assertEquals(0, Collision.vectorFromCenterOfAABBToEdge(aabb, center).y, CollisionTest.DELTA);

		aabb = new AABB(new Vector2(), new Vector2(2, 2));
		left = new Vector2(0.5f, 1f);

		Assert.assertEquals(-1, Collision.vectorFromCenterOfAABBToEdge(aabb, left).x, CollisionTest.DELTA);
		Assert.assertEquals(0, Collision.vectorFromCenterOfAABBToEdge(aabb, left).y, CollisionTest.DELTA);
	}


	@Test
	public void testCircleToAabb() {

		AABB aabb = new AABB(new Vector2(), new Vector2(1, 1));
		Circle circle = new Circle(new Vector2(1.1f, 0.5f), 0.5f);

		Assert.assertEquals(1, Collision.circleToAabb(circle, aabb).normal.x, CollisionTest.DELTA);
		Assert.assertEquals(0, Collision.circleToAabb(circle, aabb).normal.y, CollisionTest.DELTA);
		Assert.assertEquals(0.4, Collision.circleToAabb(circle, aabb).distance, CollisionTest.DELTA);

		circle = new Circle(new Vector2(0.4f, 0.4f), 0.5f);

		Assert.assertEquals(-Math.sqrt(2) / 2, Collision.circleToAabb(circle, aabb).normal.x,
				CollisionTest.DELTA);
		Assert.assertEquals(-Math.sqrt(2) / 2, Collision.circleToAabb(circle, aabb).normal.y,
				CollisionTest.DELTA);
		Assert.assertEquals(Math.sqrt(0.32) + 0.5, Collision.circleToAabb(circle, aabb).distance, 0.001);

		circle = new Circle(new Vector2(0.5f, 0.5f), 0.5f);

		Assert.assertEquals(1, Collision.circleToAabb(circle, aabb).normal.x, CollisionTest.DELTA);
		Assert.assertEquals(0, Collision.circleToAabb(circle, aabb).normal.y, CollisionTest.DELTA);

		circle = new Circle(new Vector2(), 1);

		// These tests fail. due to bug. See TODO in Collision.java at line 73
		Assert.assertEquals(1, Collision.circleToAabb(circle, aabb).normal.x, CollisionTest.DELTA);
		Assert.assertEquals(0, Collision.circleToAabb(circle, aabb).normal.y, CollisionTest.DELTA);
	}


	@Test
	public void testAabbToAabb() {
		AABB aabb1 = new AABB(new Vector2(), new Vector2(2, 2));

		AABB aabb2 = new AABB(new Vector2(1.1f, 0), new Vector2(2, 2));

		Assert.assertEquals(1, Collision.aabbToAabb(aabb2, aabb1).normal.x, CollisionTest.DELTA);
		Assert.assertEquals(0, Collision.aabbToAabb(aabb2, aabb1).normal.y, CollisionTest.DELTA);
		Assert.assertEquals(0.9, Collision.aabbToAabb(aabb2, aabb1).distance, CollisionTest.DELTA);

		aabb2 = new AABB(new Vector2(0, 1.1f), new Vector2(2, 2));
		Assert.assertEquals(0, Collision.aabbToAabb(aabb2, aabb1).normal.x, CollisionTest.DELTA);
		Assert.assertEquals(1, Collision.aabbToAabb(aabb2, aabb1).normal.y, CollisionTest.DELTA);
		Assert.assertEquals(0.9, Collision.aabbToAabb(aabb2, aabb1).distance, CollisionTest.DELTA);

		aabb2 = new AABB(new Vector2(0.9f, 0.9f), new Vector2(2, 2));
		Assert.assertEquals(1, Collision.aabbToAabb(aabb2, aabb1).normal.x, CollisionTest.DELTA);
		Assert.assertEquals(0, Collision.aabbToAabb(aabb2, aabb1).normal.y, CollisionTest.DELTA);
		Assert.assertEquals(1.1, Collision.aabbToAabb(aabb2, aabb1).distance, CollisionTest.DELTA);

		aabb2 = new AABB(new Vector2(), new Vector2(2, 2));
		Assert.assertEquals(1, Collision.aabbToAabb(aabb2, aabb1).normal.x, CollisionTest.DELTA);
		Assert.assertEquals(0, Collision.aabbToAabb(aabb2, aabb1).normal.y, CollisionTest.DELTA);
		Assert.assertEquals(2, Collision.aabbToAabb(aabb2, aabb1).distance, CollisionTest.DELTA);

	}


	@Test
	public void testCircleToCircle() {
		Circle c0 = new Circle(new Vector2(1.5f, 1), 1);
		Circle c1 = new Circle(new Vector2(1, 1), 1);

		Assert.assertEquals(1, Collision.circleToCircle(c0, c1).normal.x, CollisionTest.DELTA);
		Assert.assertEquals(0, Collision.circleToCircle(c0, c1).normal.y, CollisionTest.DELTA);
		Assert.assertEquals(1.5, Collision.circleToCircle(c0, c1).distance, CollisionTest.DELTA);

		c0 = new Circle(new Vector2(1, 1.5f), 1);
		Assert.assertEquals(0, Collision.circleToCircle(c0, c1).normal.x, CollisionTest.DELTA);
		Assert.assertEquals(1, Collision.circleToCircle(c0, c1).normal.y, CollisionTest.DELTA);
		Assert.assertEquals(1.5, Collision.circleToCircle(c0, c1).distance, CollisionTest.DELTA);

		c0 = new Circle(new Vector2(1.5f, 1.5f), 1);
		Assert.assertEquals(Math.sqrt(2) / 2, Collision.circleToCircle(c0, c1).normal.x, CollisionTest.DELTA);
		Assert.assertEquals(Math.sqrt(2) / 2, Collision.circleToCircle(c0, c1).normal.x, CollisionTest.DELTA);
		Assert.assertEquals(2 - Math.sqrt(0.5), Collision.circleToCircle(c0, c1).distance, CollisionTest.DELTA);
	}
}
