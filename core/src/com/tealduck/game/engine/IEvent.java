package com.tealduck.game.engine;


public interface IEvent {
	// TODO: Probably add another parameter for extra data to pass to the function
	/**
	 * @param entityManager
	 * @param sender
	 * @param receiver
	 * @return false if the receiver entity should be removed, else true
	 */
	public boolean fire(EntityManager entityManager, EntityTagManager entityTagManager, int sender, int receiver);
}
