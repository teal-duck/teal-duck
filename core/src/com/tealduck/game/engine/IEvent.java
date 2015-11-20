package com.tealduck.game.engine;


public interface IEvent {
	/**
	 * @param entityManager
	 * @param sender
	 * @param receiver
	 * @return false if the receiver entity should be removed, else true
	 */
	public boolean fire(EntityManager entityManager, EntityTagManager entityTagManager, int sender, int receiver);
}
