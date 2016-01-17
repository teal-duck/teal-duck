package com.tealduck.game.component;


import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.engine.Component;
import com.tealduck.game.world.EntityConstants;


/**
 * 
 */
public class PositionComponent extends Component {
	public Vector2 position;
	public Vector2 lookAt;

	public Vector2 size;


	/**
	 * @param position
	 */
	public PositionComponent(Vector2 position) {
		this(position, new Vector2(1, 0));
	}


	/**
	 * Sets the size to be (64, 64) which is the default size for entities.
	 * 
	 * @param position
	 * @param lookAt
	 */
	public PositionComponent(Vector2 position, Vector2 lookAt) {
		this(position, lookAt, new Vector2(EntityConstants.TILE_SIZE, EntityConstants.TILE_SIZE));
	}


	/**
	 * @param position
	 * @param lookAt
	 * @param size
	 */
	public PositionComponent(Vector2 position, Vector2 lookAt, Vector2 size) {
		this.position = position;
		this.lookAt = lookAt;
		this.size = size;
	}


	/**
	 * @return
	 */
	public Vector2 getCenter() {
		return position.cpy().add(size.cpy().scl((float) 0.5));
	}


	@Override
	public String toString() {
		return "PositionComponent(" + position.toString() + ", " + lookAt.toString() + ", " + size.toString()
				+ ")";
	}

}
