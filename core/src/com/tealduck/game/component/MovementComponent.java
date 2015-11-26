package com.tealduck.game.component;


import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.engine.Component;


public class MovementComponent extends Component {
	public Vector2 velocity;
	public Vector2 deltaVelocity;
	public float maxSpeed;
	public float sprintScale;
	public float friction;


	public MovementComponent(Vector2 velocity, float maxSpeed) {
		this(velocity, maxSpeed, 1, 0.1f);
	}


	public MovementComponent(Vector2 velocity, float maxSpeed, float sprintScale, float friction) {
		this.velocity = velocity;
		this.maxSpeed = maxSpeed;
		this.sprintScale = sprintScale;
		this.friction = friction;

		deltaVelocity = new Vector2(0, 0);
	}


	@Override
	public String toString() {
		return "MovementComponent(" + velocity.toString() + ", " + deltaVelocity + ", " + maxSpeed + ", "
				+ sprintScale + ", " + friction + ")";
	}

}
