package com.tealduck.game.collision;


import com.badlogic.gdx.math.Vector2;


public class Circle extends CollisionShape {
	private Vector2 position;
	private float radius;


	public Circle(Vector2 position, float radius) {
		this.position = position;
		this.radius = radius;
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
}
