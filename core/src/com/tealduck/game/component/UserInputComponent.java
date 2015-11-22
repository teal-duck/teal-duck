package com.tealduck.game.component;


import com.badlogic.gdx.controllers.Controller;
import com.tealduck.game.component.input.ControlMap;
import com.tealduck.game.engine.Component;


public class UserInputComponent extends Component {
	public ControlMap controls;
	public Controller controller;


	public UserInputComponent(ControlMap controls, Controller controller) {
		this.controls = controls;
		this.controller = controller;
	}
}
