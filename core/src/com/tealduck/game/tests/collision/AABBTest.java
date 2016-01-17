package com.tealduck.game.tests.collision;

import org.junit.Assert;
import org.junit.Test;

import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.collision.AABB;

public class AABBTest {
	private static final double DELTA = 1e-15;

	
	@Test
	public void testCoordinatePositions() {
		AABB aabb = new AABB(new Vector2(0, 1), new Vector2(5, 6));
		Assert.assertEquals(5, aabb.getWidth(), DELTA);
		Assert.assertEquals(6, aabb.getHeight(), DELTA);
		Assert.assertEquals(0, aabb.getLeft(), DELTA);
		Assert.assertEquals(7, aabb.getTop(), DELTA);
		Assert.assertEquals(5, aabb.getRight(), DELTA);
		Assert.assertEquals(1, aabb.getBottom(), DELTA);
		
		Assert.assertEquals(0f, aabb.getBottomLeft().x, DELTA);
		Assert.assertEquals(1f, aabb.getBottomLeft().y, DELTA);
		
		Assert.assertEquals(0f, aabb.getTopLeft().x, DELTA);
		Assert.assertEquals(7f, aabb.getTopLeft().y, DELTA);
		
		Assert.assertEquals(2.5f, aabb.getCenter().x, DELTA);
		Assert.assertEquals(4f, aabb.getCenter().y, DELTA);
		
		Assert.assertEquals(0f, aabb.getPosition().x, DELTA);
		Assert.assertEquals(1f, aabb.getPosition().y, DELTA);
	}
	
	@Test
	public void testSetPosition() {
		AABB aabb = new AABB(new Vector2(0, 1), new Vector2(5, 6));
		aabb.setPosition(new Vector2(2, 3));
		Assert.assertEquals(3, aabb.getBottom(), DELTA);
		Assert.assertEquals(2, aabb.getLeft(), DELTA);
	}
	
	@Test
	public void testContainsPoint() {
		AABB aabb = new AABB(new Vector2(0,1), new Vector2(5, 6));
		
		Assert.assertTrue(aabb.containsPoint(new Vector2(1,2)));
		// returns false for point on the edge of AABB 
		Assert.assertFalse(aabb.containsPoint(new Vector2(0, 1)));
		
		Assert.assertFalse(aabb.containsPoint(new Vector2(1, 8)));
		Assert.assertFalse(aabb.containsPoint(new Vector2(1, -3)));
		
		Assert.assertFalse(aabb.containsPoint(new Vector2(7, 3)));
		Assert.assertFalse(aabb.containsPoint(new Vector2(-2, 3)));
	}
}
