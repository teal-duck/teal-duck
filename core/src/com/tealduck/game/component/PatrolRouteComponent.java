package com.tealduck.game.component;


import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.engine.Component;


public class PatrolRouteComponent extends Component {
	// TODO: Maybe separate PatrolRouteComponent into 2 objects
	private ArrayList<Vector2> route;
	private int targetVertex;
	public Vector2 previousPosition = null;

	public float pauseTime = 0f;
	public float maxPauseTime = 0f;

	public float startRotation = 0f;
	public float endRotation = 0f;
	public float rotPerSecond = 0f;


	public PatrolRouteComponent(ArrayList<Vector2> route, float maxPauseTime) {
		this.route = route;
		this.maxPauseTime = maxPauseTime;
		targetVertex = 0;
	}


	public ArrayList<Vector2> getRoute() {
		return route;
	}


	public Vector2 advanceTarget() {
		targetVertex = (targetVertex + 1) % route.size();
		return route.get(targetVertex);
	}


	public Vector2 getTarget() {
		return route.get(targetVertex);
	}


	public Vector2 getElementByIndex(int index) {
		return route.get(index);
	}


	@Override
	public String toString() {
		return route.toString();
	}
}
