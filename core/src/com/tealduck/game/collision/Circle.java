package com.tealduck.game.collision;


import com.badlogic.gdx.math.Vector2;


public class Circle extends CollisionShape {
	private Vector2 position;
	private float radius;


	public Circle(Vector2 position, float radius) {
		this.position = position;
		this.radius = radius;
	}


	@Override
	public AABB getAABB() {
		return new AABB(getBottomLeft(), getSize());
	}


	public Vector2 getBottomLeft() {
		return position.cpy().sub(radius, radius);
	}


	public Vector2 getSize() {
		return new Vector2(radius * 2, radius * 2);
	}


	public Vector2 getCenter() {
		return position;
	}


	public float getCenterX() {
		return position.x;
	}


	public float getCenterY() {
		return position.y;
	}


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
	public String toString() {
		return "Circle(" + position.toString() + ", " + radius + ")";
	}


	@Override
	public boolean containsPoint(Vector2 point) {
		// TODO Circle contains point
		return false;
	}
}
