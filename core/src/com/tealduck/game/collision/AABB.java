package com.tealduck.game.collision;


import com.badlogic.gdx.math.Vector2;


/**
 * Represent an axis-aligned bounding box.
 */
public class AABB extends CollisionShape {
	private Vector2 position;
	private Vector2 size;


	public AABB() {
		this(new Vector2(0, 0), new Vector2(0, 0));
	}


	/**
	 *
	 * @param position
	 *                Bottom-left of box
	 * @param size
	 *                Length of sides
	 */

	public AABB(Vector2 position, Vector2 size) {
		this.position = position;
		this.size = size;
	}


	/**
	 * @return
	 */
	public float getWidth() {
		return size.x;
	}


	/**
	 * @return
	 */
	public float getHeight() {
		return size.y;
	}


	/**
	 * @return
	 */
	public float getLeft() {
		return position.x;
	}


	/**
	 * @return
	 */
	public float getTop() {
		return position.y + size.y;
	}


	/**
	 * @return
	 */
	public float getRight() {
		return position.x + size.x;
	}


	/**
	 * @return
	 */
	public float getBottom() {
		return position.y;
	}


	/**
	 * @return
	 */
	public Vector2 getBottomLeft() {
		return position.cpy();
	}


	/**
	 * @return
	 */
	public Vector2 getTopLeft() {
		return new Vector2(getLeft(), getTop());
	}


	/**
	 * @return
	 */
	public Vector2 getCenter() {
		return new Vector2(position.x + (size.x / 2), position.y + (size.y / 2));
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
		return this;
	}


	@Override
	public String toString() {
		return "AABB(" + position.toString() + ", " + size.toString() + ")";
	}


	@Override
	public boolean containsPoint(Vector2 point) {
		return (point.x > getLeft()) && (point.x < getRight()) && (point.y > getBottom())
				&& (point.y < getTop());
	}
}
