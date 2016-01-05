package com.tealduck.game.component;


import com.tealduck.game.engine.Component;


public class BulletComponent extends Component {
	public int shooterId;


	public BulletComponent(int shooterId) {
		this.shooterId = shooterId;
	}


	@Override
	public String toString() {
		return "BulletComponent(" + shooterId + ")";
	}
}
