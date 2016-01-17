package com.tealduck.game.event;


import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.IEvent;


/**
 * 
 */
public class BulletRemove implements IEvent {
	public static final BulletRemove instance = new BulletRemove();


	private BulletRemove() {
	}


	@Override
	public boolean fire(EntityEngine entityEngine, int sender, int receiver, Object data) {
		return true;
	}

}
