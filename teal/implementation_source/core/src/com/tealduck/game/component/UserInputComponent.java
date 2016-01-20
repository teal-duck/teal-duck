package com.tealduck.game.component;


import com.badlogic.gdx.controllers.Controller;
import com.tealduck.game.engine.Component;
import com.tealduck.game.input.ControlMap;


/**
 *
 */
public class UserInputComponent extends Component {
	public ControlMap controls;
	public Controller controller;


	/**
	 * @param controls
	 * @param controller
	 */
	public UserInputComponent(ControlMap controls, Controller controller) {
		this.controls = controls;
		this.controller = controller;
	}


	@Override
	public String toString() {
		return "UserInputComponent(" + controls.toString() + ", "
				+ ((controller != null) ? controller.toString() : "null") + ")";
	}
}
