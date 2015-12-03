package com.tealduck.game.collision;


import com.badlogic.gdx.math.Vector2;


public class AABB extends CollisionShape {
	private Vector2 position;
	private Vector2 size;


	public AABB(Vector2 position, Vector2 size) {
		this.position = position;
		this.size = size;
	}


	public float getLeft() {
		return position.x;
	}


	public float getTop() {
		return position.y;
	}


	public float getRight() {
		return position.x + size.x;
	}


	public float getBottom() {
		return position.y + size.y;
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
}
