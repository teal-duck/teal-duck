package com.tealduck.game.collision;


import com.badlogic.gdx.math.Vector2;


/**
 * Represents a circle where the position property is the center.
 */
public class Circle extends CollisionShape {
	private Vector2 position;
	private float radius;


	/**
	 * @param position
	 *                the center of the circle
	 * @param radius
	 */
	public Circle(Vector2 position, float radius) {
		this.position = position;
		this.radius = radius;
	}


	/**
	 * @return
	 */
	public Vector2 getBottomLeft() {
		return position.cpy().sub(radius, radius);
	}


	/**
	 * @return a vector representing the size of the bounding box
	 */
	public Vector2 getSize() {
		return new Vector2(radius * 2, radius * 2);
	}


	/**
	 * @return
	 */
	public Vector2 getCenter() {
		return position;
	}


	/**
	 * @return
	 */
	public float getCenterX() {
		return position.x;
	}


	/**
	 * @return
	 */
	public float getCenterY() {
		return position.y;
	}


	/**
	 * @return
	 */
	public float getRadius() {
		return radius;
	}


	@Override
	public Vector2 getPosition() {
		return position;
	}


	@Override
	public void setPosition(Vector2 newPosition) {
		position.set(newPosition);
	}


	@Override
	public AABB getAABB() {
		return new AABB(getBottomLeft(), getSize());
	}


	/**
	 * Returns true if a point is within (not on the edge of) the area.
	 */
	@Override
	public boolean containsPoint(Vector2 point) {
		return (point.cpy().sub(position).len2() < (radius * radius));
	}


	@Override
	public String toString() {
		return "Circle(" + position.toString() + ", " + radius + ")";
	}
}
