package com.tealduck.game.component;


import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.engine.Component;


public class PositionComponent extends Component {
	public Vector2 position;
	public Vector2 lookAt;

	public Vector2 size;


	public PositionComponent(Vector2 position) {
		this(position, new Vector2(1, 0));
	}


	public PositionComponent(Vector2 position, Vector2 lookAt) {
		this(position, lookAt, new Vector2(64f, 64f));
	}


	public PositionComponent(Vector2 position, Vector2 lookAt, Vector2 size) {
		this.position = position;
		this.lookAt = lookAt;
		this.size = size;
	}


	public Vector2 getCenter() {
		return position.cpy().add(size.cpy().scl((float) 0.5));
	}


	@Override
	public String toString() {
		return "PositionComponent(" + position.toString() + ", " + lookAt.toString() + ", " + size.toString()
				+ ")";
	}

}
