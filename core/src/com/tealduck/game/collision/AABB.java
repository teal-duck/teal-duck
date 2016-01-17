package com.tealduck.game.collision;


import com.badlogic.gdx.math.Vector2;


public class AABB extends CollisionShape {
	private Vector2 position;
	private Vector2 size;


	public AABB() {
		this(new Vector2(0, 0), new Vector2(0, 0));
	}


	public AABB(Vector2 position, Vector2 size) {
		this.position = position;
		this.size = size;
	}


	@Override
	public AABB getAABB() {
		return this;
	}


	public float getWidth() {
		return size.x;
	}


	public float getHeight() {
		return size.y;
	}


	public float getLeft() {
		return position.x;
	}


	public float getTop() {
		return position.y + size.y;
	}


	public float getRight() {
		return position.x + size.x;
	}


	public float getBottom() {
		return position.y;
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
	public String toString() {
		return "AABB(" + position.toString() + ", " + size.toString() + ")";
	}


	public Vector2 getBottomLeft() {
		return position.cpy();
	}


	public Vector2 getTopLeft() {
		return new Vector2(getLeft(), getTop());
	}


	public Vector2 getCenter() {
		return new Vector2(position.x + (size.x / 2), position.y + (size.y / 2));
	}

	/**
	 * Returns true if a point is within (not on the edge of) the area.
	 */
	@Override
	public boolean containsPoint(Vector2 point) {
		return (point.x > getLeft()) && (point.x < getRight()) && (point.y > getBottom())
				&& (point.y < getTop());
	}
}
