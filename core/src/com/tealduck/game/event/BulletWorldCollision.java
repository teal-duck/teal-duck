package com.tealduck.game.event;


import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.IEvent;


/**
 * 
 */
public class BulletWorldCollision implements IEvent {
	public static final BulletWorldCollision instance = new BulletWorldCollision();


	private BulletWorldCollision() {
	}


	@Override
	public boolean fire(EntityEngine entityEngine, int sender, int receiver, Object data) {
		return true;
	}

}
