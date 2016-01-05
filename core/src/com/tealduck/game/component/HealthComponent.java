package com.tealduck.game.component;


import com.tealduck.game.engine.Component;


public class HealthComponent extends Component {
	public int maxHealth;
	public int health;


	public HealthComponent(int maxHealth) {
		this(maxHealth, maxHealth);
	}


	public HealthComponent(int maxHealth, int health) {
		this.maxHealth = maxHealth;
		this.health = health;
	}


	@Override
	public String toString() {
		return "HealthComponent(" + maxHealth + ", " + health + ")";
	}
}
