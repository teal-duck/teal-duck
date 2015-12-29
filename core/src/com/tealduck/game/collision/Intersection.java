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


	public Intersection copy() {
		return new Intersection(normal.cpy(), distance);
	}


	public Intersection flippedCopy() {
		return new Intersection(normal.cpy().scl(-1), distance);
	}


	@Override
	public String toString() {
		return "Intersection(" + normal.toString() + ", " + distance + ")";
	}
}
