package com.tealduck.game.collision;


import com.badlogic.gdx.math.Vector2;


public class Intersection {
	public Vector2 normal;
	public float distance;


	public Intersection(Vector2 normal, float distance) {
		this.normal = normal;
		this.distance = distance;
	}
}
