package com.tealduck.game.pickup;


public class AmmoPickup extends Pickup {
	public int ammo;


	public AmmoPickup(int ammo) {
		this.ammo = ammo;
	}


	@Override
	public String toString() {
		return "AmmoPickup(" + ammo + ")";
	}
}
