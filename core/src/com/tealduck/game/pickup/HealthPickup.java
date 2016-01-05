package com.tealduck.game.pickup;


public class HealthPickup extends Pickup {
	public int health;


	public HealthPickup(int health) {
		this.health = health;
	}


	@Override
	public String toString() {
		return "HealthPickup(" + health + ")";
	}
}
