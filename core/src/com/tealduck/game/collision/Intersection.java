package com.tealduck.game.collision;


import com.badlogic.gdx.math.Vector2;


public class Intersection {
	public Vector2 normal;
	public float distance;


	public Intersection(Vector2 normal, float distance) {
		this.normal = normal;
		this.distance = distance;
	}


	public Vector2 getResolveVector() {
		return normal.cpy().setLength(distance);
	}


	public Vector2 getFlippedResolveVector() {
		return normal.cpy().scl(-1);
	}


	public Intersection getCopy() {
		return new Intersection(normal.cpy(), distance);
	}


	public Intersection getFlippedCopy() {
		return new Intersection(getFlippedResolveVector(), distance);
	}


	@Override
	public String toString() {
		return "Intersection(" + normal.toString() + ", " + distance + ")";
	}
}
