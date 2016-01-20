package com.tealduck.game.component;


import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.collision.CollisionShape;
import com.tealduck.game.engine.Component;


/**
 *
 */
public class CollisionComponent extends Component {
	public CollisionShape collisionShape;
	// Vector from bottom left corner of position component to bottom left of collision shape
	// This is because the hitboxes can be smaller than the sprite
	public Vector2 offsetFromPosition;


	/**
	 * @param collisionShape
	 * @param offsetFromPosition
	 */
	public CollisionComponent(CollisionShape collisionShape, Vector2 offsetFromPosition) {
		this.collisionShape = collisionShape;
		this.offsetFromPosition = offsetFromPosition;
	}


	@Override
	public String toString() {
		return "CollisionComponent(" + collisionShape.toString() + ", " + offsetFromPosition.toString() + ")";
	}
}
