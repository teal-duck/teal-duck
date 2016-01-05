package com.tealduck.game.event;


import com.tealduck.game.Tag;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.IEvent;


public class BulletCollision implements IEvent {
	public static final BulletCollision instance = new BulletCollision();


	private BulletCollision() {
	}


	@Override
	public boolean fire(EntityEngine entityEngine, int sender, int receiver, Object data) {
		if (entityEngine.getEntityTagManager().doesEntityIdHaveTag(sender, Tag.PLAYER)) {
			return false;
		}
		return true;
	}
}
