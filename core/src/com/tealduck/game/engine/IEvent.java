package com.tealduck.game.engine;


public interface IEvent {
	// TODO: Probably add another parameter for extra data to pass to the function
	/**
	 * @param entityEngine
	 * @param sender
	 * @param receiver
	 * @return true if the receiver entity should be removed, else false
	 */
	public boolean fire(EntityEngine entityEngine, int sender, int receiver);
}
