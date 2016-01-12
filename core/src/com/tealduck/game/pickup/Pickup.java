package com.tealduck.game.pickup;


import com.tealduck.game.engine.EntityManager;


public abstract class Pickup {
	public abstract boolean applyToEntity(EntityManager entityManager, int entity);


	public abstract String getTextureName();
}
