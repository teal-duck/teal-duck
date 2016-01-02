package com.tealduck.game.event;


import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.IEvent;


public class GoalCollision implements IEvent {
	public static final GoalCollision instance = new GoalCollision();


	private GoalCollision() {
	}


	@Override
	public boolean fire(EntityEngine entityEngine, int sender, int receiver, Object data) {
		System.out.println("Goal collision");
		return false;
	}
}