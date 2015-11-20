package com.tealduck.game.engine;


public interface IEvent {
	public void fire(int sender, int receiver);
}
