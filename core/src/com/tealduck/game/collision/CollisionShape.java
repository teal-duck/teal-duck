package com.tealduck.game.collision;


import com.badlogic.gdx.math.Vector2;


public abstract class CollisionShape {
	public abstract Vector2 getPosition();


	public abstract void setPosition(Vector2 newPosition);
}
