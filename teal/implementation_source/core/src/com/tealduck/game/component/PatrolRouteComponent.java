package com.tealduck.game.component;


import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.engine.Component;


/**
 *
 */
public class PatrolRouteComponent extends Component {
	public ArrayList<Vector2> route;
	public int targetVertex;
	public Vector2 previousPosition = null;
	public Vector2 previousTarget = null;

	public float pauseTime = 0f;
	public float maxPauseTime = 0f;

	public float startRotation = 0f;
	public float endRotation = 0f;
	public float rotPerSecond = 0f;


	/**
	 * @param route
	 * @param maxPauseTime
	 */
	public PatrolRouteComponent(ArrayList<Vector2> route, float maxPauseTime) {
		this.route = route;
		this.maxPauseTime = maxPauseTime;
		targetVertex = 0;
	}


	/**
	 * @return
	 */
	public ArrayList<Vector2> getRoute() {
		return route;
	}


	/**
	 * @return the location of the next target in the route
	 */
	public Vector2 peekNextTarget() {
		int index = (targetVertex + 1) % route.size();
		return route.get(index);
	}


	/**
	 * Increment the targetIndex, update previousTarget and return the new target location.
	 *
	 * @return the location of the new target
	 */
	public Vector2 advanceTarget() {
		previousTarget = route.get(targetVertex);
		targetVertex = (targetVertex + 1) % route.size();
		return route.get(targetVertex);
	}


	/**
	 * @return
	 */
	public Vector2 getTarget() {
		return route.get(targetVertex);
	}


	/**
	 * @param index
	 * @return
	 */
	public Vector2 getElementByIndex(int index) {
		return route.get(index);
	}


	@Override
	public String toString() {
		return "PatrolRouteComponent(" + route.toString() + ", " + maxPauseTime + ")";
	}
}
