package com.tealduck.game.tests.collision;

import org.junit.Assert;
import org.junit.Test;

import com.tealduck.game.collision.Circle;
import com.badlogic.gdx.math.Vector2;

public class CircleTest {
	private static final double DELTA = 1e-15;
	
	@Test
	public void testGetCoordinates() {
		Circle circle = new Circle(new Vector2(2, 4), 3);
		
		// Test getBottomLeft()
		Assert.assertEquals(-1f, circle.getBottomLeft().x, DELTA);
		Assert.assertEquals(1f, circle.getBottomLeft().y, DELTA);
		
		// Test getCenter()
		Assert.assertEquals(2f, circle.getCenter().x, DELTA);
		Assert.assertEquals(4f, circle.getCenter().y, DELTA);
		Assert.assertEquals(2f,  circle.getCenterX(), DELTA);
		Assert.assertEquals(4f, circle.getCenterY(), DELTA);
		
		// Test getPosition()
		Assert.assertEquals(2f, circle.getPosition().x, DELTA);
		Assert.assertEquals(4f, circle.getPosition().y, DELTA);
		
		// Test getRadius()
		Assert.assertEquals(3f, circle.getRadius(), DELTA);
	}
	
	@Test
	public void testSetPosition() {
		Circle circle = new Circle(new Vector2(2, 4), 3);
		
		circle.setPosition(new Vector2(6, 8));
		Assert.assertEquals(6f, circle.getCenterX(), DELTA);
		Assert.assertEquals(8f, circle.getCenterY(), DELTA);
	}
	
	@Test
	public void testContainsPoint() {
		Circle circle = new Circle(new Vector2(2, 4), 3);
		
		Assert.assertTrue(circle.containsPoint(new Vector2(2, 4)));
		
		Assert.assertFalse(circle.containsPoint(new Vector2(-1, 4)));
		
		Assert.assertTrue(circle.containsPoint(new Vector2(1, 5)));
		Assert.assertFalse(circle.containsPoint(new Vector2(-1, 5)));
	}
}
