package com.tealduck.game.component;


import com.tealduck.game.engine.Component;


public class KnockbackComponent extends Component {
	public float knockbackForce;


	public KnockbackComponent(float knockbackForce) {
		this.knockbackForce = knockbackForce;
	}


	@Override
	public String toString() {
		return "KnockbackComponent(" + knockbackForce + ")";
	}
}
