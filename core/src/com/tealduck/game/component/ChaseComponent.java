package com.tealduck.game.component;


import com.tealduck.game.engine.Component;
import com.tealduck.game.world.EntityConstants;


public class ChaseComponent extends Component {
	public int targetEntityId;
	public float forgetTime;
	public float maxTimeToForget;
	public boolean forgettable;


	public ChaseComponent() {
		this(-1);
	}


	public ChaseComponent(int targetEntityId) {
		this(targetEntityId, EntityConstants.ENEMY_TIME_TO_FORGET);
	}


	public ChaseComponent(int targetEntityId, float maxTimeToForget) {
		this(targetEntityId, maxTimeToForget, true);
	}


	public ChaseComponent(int targetEntityId, boolean forgettable) {
		this(targetEntityId, 0, forgettable);
	}


	public ChaseComponent(int targetEntityId, float maxTimeToForget, boolean forgettable) {
		this.targetEntityId = targetEntityId;
		this.maxTimeToForget = maxTimeToForget;
		this.forgettable = forgettable;
		forgetTime = 0;
	}


	@Override
	public String toString() {
		return "ChaseComponent(" + targetEntityId + ")";
	}
}
