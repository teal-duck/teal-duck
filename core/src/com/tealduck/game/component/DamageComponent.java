package com.tealduck.game.component;


import com.tealduck.game.engine.Component;


/**
 *
 */
public class DamageComponent extends Component {
	public int damage;


	/**
	 * @param damage
	 */
	public DamageComponent(int damage) {
		this.damage = damage;
	}


	@Override
	public String toString() {
		return "DamageComponent(" + damage + ")";
	}
}
