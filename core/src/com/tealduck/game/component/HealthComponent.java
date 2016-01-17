package com.tealduck.game.component;


import com.tealduck.game.engine.Component;


/**
 * 
 */
public class HealthComponent extends Component {
	public int maxHealth;
	public int health;


	/**
	 * Sets starting health equal to maxHealth.
	 * 
	 * @param maxHealth
	 */
	public HealthComponent(int maxHealth) {
		this(maxHealth, maxHealth);
	}


	/**
	 * @param maxHealth
	 * @param health
	 */
	public HealthComponent(int maxHealth, int health) {
		this.maxHealth = maxHealth;
		this.health = health;
	}


	@Override
	public String toString() {
		return "HealthComponent(" + maxHealth + ", " + health + ")";
	}
}
