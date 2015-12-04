package com.tealduck.game.component;


import java.util.ArrayList;

import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.engine.Component;


public class PatrolRouteComponent extends Component {
	private ArrayList<Vector2> route;
	private int lastVertex;


	public PatrolRouteComponent(ArrayList<Vector2> route) {
		this.route = route;
		lastVertex = 0;
	}


	public ArrayList<Vector2> getRoute() {
		return route;
	}


	public Vector2 getNextVertex() {
		lastVertex = (lastVertex + 1) % route.size();
		return route.get(lastVertex);
	}


	public Vector2 getTarget() {
		return route.get(lastVertex);
	}


	public Vector2 getElementByIndex(int index) {
		return route.get(index);
	}


	@Override
	public String toString() {
		return route.toString();
	}
}
