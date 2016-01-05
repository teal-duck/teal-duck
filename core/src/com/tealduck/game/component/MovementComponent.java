package com.tealduck.game.component;


import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.engine.Component;


public class MovementComponent extends Component {
	public Vector2 velocity;
	public Vector2 acceleration;
	public float maxSpeed;
	public float sprintScale;
	public float friction;


	public MovementComponent(Vector2 velocity, float maxSpeed) {
		this(velocity, maxSpeed, 1);
	}


	public MovementComponent(Vector2 velocity, float maxSpeed, float sprintScale) {
		this(velocity, maxSpeed, sprintScale, 0.8f);
	}


	public MovementComponent(Vector2 velocity, float maxSpeed, float sprintScale, float friction) {
		this.velocity = velocity;
		this.maxSpeed = maxSpeed;
		this.sprintScale = sprintScale;
		this.friction = friction;

		acceleration = new Vector2(0, 0);
	}


	@Override
	public String toString() {
		return "MovementComponent(" + velocity.toString() + ", " + maxSpeed + ", " + sprintScale + ", "
				+ friction + ")";
	}

}
