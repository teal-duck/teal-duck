package com.tealduck.game.component;


import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.engine.Component;


/**
 * 
 */
public class MovementComponent extends Component {
	public Vector2 velocity;
	public Vector2 acceleration;
	public float maxSpeed;
	public float friction;

	public float sprintScale;
	public float sprintTime;
	public float maxSprintTime;
	/// public float sprintCooldownTime;
	public boolean sprinting = false;
	public boolean usedAllSprint = false;


	/**
	 * @param velocity
	 * @param maxSpeed
	 */
	public MovementComponent(Vector2 velocity, float maxSpeed) {
		this(velocity, maxSpeed, 1, 0);
	}


	/**
	 * @param velocity
	 * @param maxSpeed
	 * @param friction
	 */
	public MovementComponent(Vector2 velocity, float maxSpeed, float friction) {
		this(velocity, maxSpeed, 1, 0, friction);
	}


	/**
	 * @param velocity
	 * @param maxSpeed
	 * @param sprintScale
	 * @param maxSprintTime
	 */
	public MovementComponent(Vector2 velocity, float maxSpeed, float sprintScale, float maxSprintTime) {
		this(velocity, maxSpeed, sprintScale, maxSprintTime, 0.8f);
	}


	/**
	 * @param velocity
	 * @param maxSpeed
	 * @param sprintScale
	 * @param maxSprintTime
	 * @param friction
	 */
	public MovementComponent(Vector2 velocity, float maxSpeed, float sprintScale, float maxSprintTime,
			float friction) {
		this.velocity = velocity;
		this.maxSpeed = maxSpeed;
		this.friction = friction;

		this.sprintScale = sprintScale;
		this.maxSprintTime = maxSprintTime;
		sprintTime = maxSprintTime;

		acceleration = new Vector2(0, 0);
	}


	@Override
	public String toString() {
		return "MovementComponent(" + velocity.toString() + ", " + maxSpeed + ", " + sprintScale + ", "
				+ friction + ")";
	}

}
