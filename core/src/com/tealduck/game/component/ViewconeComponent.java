package com.tealduck.game.component;


import com.tealduck.game.engine.Component;


public class ViewconeComponent extends Component {
	public float fov;
	public float length;


	public ViewconeComponent(float fov, float length) {
		this.fov = fov;
		this.length = length;
	}


	public float getFovRadians() {
		return (float) Math.acos(fov);
	}


	public float getHalfFovRadians() {
		return getFovRadians() / 2;
	}


	@Override
	public String toString() {
		return "ViewconeComponent(" + fov + ", " + length + ")";
	}
}