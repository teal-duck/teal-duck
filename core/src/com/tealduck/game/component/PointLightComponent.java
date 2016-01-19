package com.tealduck.game.component;


import com.tealduck.game.engine.Component;


public class PointLightComponent extends Component {
	public float radius;


	public PointLightComponent(float radius) {
		this.radius = radius;
	}


	@Override
	public String toString() {
		return "PointLightComponent(" + radius + ")";
	}
}
