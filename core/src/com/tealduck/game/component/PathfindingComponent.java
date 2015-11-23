package com.tealduck.game.component;


import com.tealduck.game.engine.Component;


public class PathfindingComponent extends Component {
	public int targetEntityId;


	public PathfindingComponent() {
		this(-1);
	}


	public PathfindingComponent(int targetEntityId) {
		this.targetEntityId = targetEntityId;
	}


	@Override
	public String toString() {
		return "PathfindingComponent(" + targetEntityId + ")";
	}
}
