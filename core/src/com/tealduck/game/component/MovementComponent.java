package com.tealduck.game.component;


import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.engine.Component;


public class MovementComponent extends Component {
	public Vector2 velocity;
	public float maxSpeed;


	public MovementComponent(Vector2 velocity, float maxSpeed) {
		this.velocity = velocity;
		this.maxSpeed = maxSpeed;
	}


	@Override
	public String toString() {
		return "MovementComponent(" + velocity.toString() + ")";
	}

}
