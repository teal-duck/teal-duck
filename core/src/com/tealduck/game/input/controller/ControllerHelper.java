package com.tealduck.game.input.controller;


import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerAdapter;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;


public class ControllerHelper extends ControllerAdapter {
	public int indexOf(Controller controller) {
		return Controllers.getControllers().indexOf(controller, true);
	}


	@Override
	public void connected(Controller controller) {
		System.out.println("Connected " + controller.getName());
		ControllerHelper.printControllers();
	}


	@Override
	public void disconnected(Controller controller) {
		System.out.println("Disconnected " + controller.getName());
		ControllerHelper.printControllers();
	}


	@Override
	public boolean buttonDown(Controller controller, int buttonIndex) {
		// System.out.println("Controller #" + indexOf(controller) + ", button " + buttonIndex + " down");
		return false;
	}


	@Override
	public boolean buttonUp(Controller controller, int buttonIndex) {
		// System.out.println("Controller #" + indexOf(controller) + ", button " + buttonIndex + " up");
		return false;
	}


	@Override
	public boolean axisMoved(Controller controller, int axisIndex, float value) {
		// if (Math.abs(value) >= 0.5) {
		// System.out.println("Controller #" + indexOf(controller) + ", axis " + axisIndex + ": " + value);
		// }
		return false;
	}


	@Override
	public boolean povMoved(Controller controller, int povIndex, PovDirection value) {
		System.out.println("Controller #" + indexOf(controller) + ", pov " + povIndex + ": " + value);
		return false;
	}


	@Override
	public boolean xSliderMoved(Controller controller, int sliderIndex, boolean value) {
		System.out.println("Controller #" + indexOf(controller) + ", X slider " + sliderIndex + ": " + value);
		return false;
	}


	@Override
	public boolean ySliderMoved(Controller controller, int sliderIndex, boolean value) {
		System.out.println("Controller #" + indexOf(controller) + ", Y slider " + sliderIndex + ": " + value);
		return false;
	}


	@Override
	public boolean accelerometerMoved(Controller controller, int accelerometerIndex, Vector3 value) {
		System.out.println("Controller #" + indexOf(controller) + ", accelerometer " + accelerometerIndex + ": "
				+ value);
		return false;
	}


	public static void printControllers() {
		int i = 0;
		for (Controller c : Controllers.getControllers()) {
			System.out.println("Controller #" + i + ": " + c.getName());
			i += 1;
		}

		if (Controllers.getControllers().size == 0) {
			System.out.println("Controllers: No controllers attached");
		}
	}


	public static Controller getFirstControllerOrNull() {
		Array<Controller> controllers = Controllers.getControllers();
		Controller controller = (controllers.size > 0) ? controllers.first() : null;
		return controller;
	}

}
