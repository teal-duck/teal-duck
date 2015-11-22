package com.tealduck.game.component;


import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.engine.Component;


public class PositionComponent extends Component {
	public Vector2 position;


	public PositionComponent(Vector2 position) {
		this.position = position;
	}


	@Override
	public String toString() {
		return "PositionComponent(" + position.toString() + ")";
	}

}
