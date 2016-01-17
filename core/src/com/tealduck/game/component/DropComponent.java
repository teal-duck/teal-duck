package com.tealduck.game.component;


import com.tealduck.game.engine.Component;
import com.tealduck.game.pickup.Pickup;


/**
 * 
 */
public class DropComponent extends Component {
	public Pickup pickup;


	/**
	 * @param pickup
	 */
	public DropComponent(Pickup pickup) {
		this.pickup = pickup;
	}


	@Override
	public String toString() {
		return "DropComponent(" + pickup.toString() + ")";
	}
}
