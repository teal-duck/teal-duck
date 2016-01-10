package com.tealduck.game.component;


import com.tealduck.game.engine.Component;


public class ViewconeComponent extends Component {
	public float halfFov;
	public float length;


	public ViewconeComponent(float halfFov, float length) {
		this.halfFov = halfFov;
		this.length = length;
	}


	public float getHalfFovRadians() {
		return (float) Math.acos(halfFov);
	}


	@Override
	public String toString() {
		return "ViewconeComponent(" + halfFov + ", " + length + ")";
	}
}