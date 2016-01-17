package com.tealduck.game.component;


import com.tealduck.game.engine.Component;
import com.tealduck.game.pickup.Pickup;


/**
 * 
 */
public class PickupComponent extends Component {
	public Pickup contents;


	/**
	 * @param contents
	 */
	public PickupComponent(Pickup contents) {
		this.contents = contents;
	}


	@Override
	public String toString() {
		return "PickupComponent(" + contents.toString() + ")";
	}
}
