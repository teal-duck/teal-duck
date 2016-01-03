package com.tealduck.game.component;


import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.engine.Component;


public class PositionComponent extends Component {
	public Vector2 position;
	public Vector2 lookAt;


	public PositionComponent(Vector2 position) {
		this(position, new Vector2(1, 0));
	}


	public PositionComponent(Vector2 position, Vector2 lookAt) {
		this.position = position;
		this.lookAt = lookAt;
	}


	@Override
	public String toString() {
		return "PositionComponent(" + position.toString() + ", " + lookAt.toString() + ")";
	}

}
