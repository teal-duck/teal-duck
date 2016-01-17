package com.tealduck.game.component;


import com.tealduck.game.engine.Component;


/**
 * 
 */
public class ViewconeComponent extends Component {
	public float halfFov;
	public float length;


	/**
	 * Half fov is cosine of half of the field of view. This is used in dot product calculations where a . b =
	 * cos(t) (i.e. the dot product is the cosine of the angle between 2 vectors, scaled by their length).
	 * 
	 * @param halfFov
	 * @param length
	 */
	public ViewconeComponent(float halfFov, float length) {
		this.halfFov = halfFov;
		this.length = length;
	}


	/**
	 * @return
	 */
	public float getHalfFovRadians() {
		return (float) Math.acos(halfFov);
	}


	@Override
	public String toString() {
		return "ViewconeComponent(" + halfFov + ", " + length + ")";
	}
}