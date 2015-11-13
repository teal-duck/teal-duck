package com.tealduck.game.components;


import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.engine.Component;


public class MovementComponent extends Component {
	public Vector2 velocity;


	public MovementComponent(Vector2 velocity) {
		this.velocity = velocity;
	}


	@Override
	public String toString() {
		return "MovementComponent(" + velocity.toString() + ")";
	}

}
