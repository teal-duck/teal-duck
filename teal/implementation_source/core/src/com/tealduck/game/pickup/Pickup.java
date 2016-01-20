package com.tealduck.game.pickup;


import com.tealduck.game.engine.EntityManager;


/**
 *
 */
public abstract class Pickup {
	/**
	 * If this pickup is pick-up-able by the entity, apply the effects to the entity and return true. If not,
	 * returns false.
	 *
	 * @param entityManager
	 * @param entity
	 * @return
	 */
	public abstract boolean applyToEntity(EntityManager entityManager, int entity);


	/**
	 * @return the name of the texture in the file system
	 */
	public abstract String getTextureName();
}
