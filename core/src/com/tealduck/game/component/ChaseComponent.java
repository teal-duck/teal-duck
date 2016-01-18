package com.tealduck.game.component;


import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.engine.Component;
import com.tealduck.game.world.EntityConstants;


/**
 *
 */
public class ChaseComponent extends Component {
	public int targetEntityId;
	public float forgetTime;
	public float maxTimeToForget;
	public boolean forgettable;

	public Vector2 searchDirection;
	public float searchTime = 0f;

	// TODO: Clean up rotation code duplication
	public float rotateTime = 0f;
	public float startRotation = 0f;
	public float endRotation = 0f;
	public float rotPerSecond = 0f;
	public boolean hitWorld = false;
	public float maxSearchTime = 0f;


	/**
	 *
	 */
	public ChaseComponent() {
		this(-1);
	}


	/**
	 * @param targetEntityId
	 */
	public ChaseComponent(int targetEntityId) {
		this(targetEntityId, EntityConstants.ENEMY_TIME_TO_FORGET);
	}


	/**
	 * @param targetEntityId
	 * @param maxTimeToForget
	 */
	public ChaseComponent(int targetEntityId, float maxTimeToForget) {
		this(targetEntityId, maxTimeToForget, true);
	}


	/**
	 * @param targetEntityId
	 * @param forgettable
	 */
	public ChaseComponent(int targetEntityId, boolean forgettable) {
		this(targetEntityId, 0, forgettable);
	}


	/**
	 * @param targetEntityId
	 * @param maxTimeToForget
	 * @param forgettable
	 */
	public ChaseComponent(int targetEntityId, float maxTimeToForget, boolean forgettable) {
		this.targetEntityId = targetEntityId;
		this.maxTimeToForget = maxTimeToForget;
		this.forgettable = forgettable;
		forgetTime = 0;

		searchDirection = new Vector2().setToRandomDirection();
	}


	@Override
	public String toString() {
		return "ChaseComponent(" + targetEntityId + ")";
	}
}
