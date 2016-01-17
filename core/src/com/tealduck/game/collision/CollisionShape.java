package com.tealduck.game.collision;


import com.badlogic.gdx.math.Vector2;


/**
 * Base class for collision shapes.
 */
public abstract class CollisionShape {
	/**
	 * @return
	 */
	public abstract Vector2 getPosition();


	/**
	 * @param newPosition
	 */
	public abstract void setPosition(Vector2 newPosition);


	/**
	 * @return the bounding box for the shape
	 */
	public abstract AABB getAABB();


	/**
	 * Returns true if a point is within (not on the edge of) the area.
	 *
	 * @param point
	 * @return
	 */
	public abstract boolean containsPoint(Vector2 point);
}
