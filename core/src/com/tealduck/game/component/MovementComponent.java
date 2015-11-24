package com.tealduck.game.component;


import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.engine.Component;


public class MovementComponent extends Component {
	public Vector2 velocity;
	public float maxSpeed;
	public float sprintScale;


	public MovementComponent(Vector2 velocity, float maxSpeed) {
		this(velocity, maxSpeed, 1);
	}


	public MovementComponent(Vector2 velocity, float maxSpeed, float sprintScale) {
		this.velocity = velocity;
		this.maxSpeed = maxSpeed;
		this.sprintScale = sprintScale;
	}


	@Override
	public String toString() {
		return "MovementComponent(" + velocity.toString() + ")";
	}

}
