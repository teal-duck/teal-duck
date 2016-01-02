package com.tealduck.game.component;


import com.tealduck.game.engine.Component;


public class ChaseComponent extends Component {
	public int targetEntityId;


	public ChaseComponent() {
		this(-1);
	}


	public ChaseComponent(int targetEntityId) {
		this.targetEntityId = targetEntityId;
	}


	@Override
	public String toString() {
		return "PathfindingComponent(" + targetEntityId + ")";
	}
}
