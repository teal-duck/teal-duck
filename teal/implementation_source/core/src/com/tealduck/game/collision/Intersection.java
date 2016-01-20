package com.tealduck.game.collision;


import com.badlogic.gdx.math.Vector2;


/**
 * Represent an intersection between 2 objects as a normal and distance.
 */
public class Intersection {
	public Vector2 normal;
	public float distance;


	/**
	 * @param normal
	 * @param distance
	 */
	public Intersection(Vector2 normal, float distance) {
		this.normal = normal;
		this.distance = distance;
	}


	/**
	 * @return a vector in direction of normal, length of distance
	 */
	public Vector2 getResolveVector() {
		return normal.cpy().setLength(distance);
	}


	/**
	 * @return a flipped copy of the normal vector
	 */
	public Vector2 getFlippedNormalVector() {
		return normal.cpy().scl(-1);
	}


	/**
	 * @return
	 */
	public Intersection getCopy() {
		return new Intersection(normal.cpy(), distance);
	}


	/**
	 * @return
	 */
	public Intersection getFlippedCopy() {
		return new Intersection(getFlippedNormalVector(), distance);
	}


	@Override
	public String toString() {
		return "Intersection(" + normal.toString() + ", " + distance + ")";
	}
}
