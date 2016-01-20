package com.tealduck.game.engine;


/**
 *
 */
public interface IEvent {
	/**
	 * @param entityEngine
	 * @param sender
	 * @param receiver
	 * @param data
	 * @return true if the receiver entity should be removed, else false
	 */
	public boolean fire(EntityEngine entityEngine, int sender, int receiver, Object data);
}
