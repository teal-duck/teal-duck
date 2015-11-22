package com.tealduck.game.input;


import java.util.EnumMap;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;


/**
 *
 */
public class ControlMap {
	// TODO: ControlMap (+ other input) javadoc, tests, exceptions, clean up, possible change return types from void

	private EnumMap<Action, Binding> controls;


	public ControlMap() {
		controls = new EnumMap<Action, Binding>(Action.class);
	}


	/**
	 * @param action
	 * @param key
	 */
	public void addKeyForAction(Action action, int key) {
		this.addKeyForAction(action, new KeyBinding(key));
	}


	/**
	 * @param action
	 * @param primary
	 * @param secondary
	 */
	public void addKeyForAction(Action action, int primary, int secondary) {
		this.addKeyForAction(action, new KeyBinding(primary, secondary));
	}


	/**
	 * @param action
	 * @param keyBinding
	 */
	public void addKeyForAction(Action action, KeyBinding keyBinding) {
		Binding binding = controls.get(action);

		if (binding == null) {
			binding = new Binding(keyBinding, null);
			controls.put(action, binding);
		} else {
			binding.setKeyBinding(keyBinding);
		}
	}


	/**
	 * @param action
	 * @param controllerBindingType
	 * @param index
	 */
	public void addControllerForAction(Action action, ControllerBindingType controllerBindingType, int index) {
		this.addControllerForAction(action, new ControllerBinding(controllerBindingType, index));
	}


	/**
	 * @param action
	 * @param controllerBindingType
	 * @param index
	 * @param deadzone
	 */
	public void addControllerForAction(Action action, ControllerBindingType controllerBindingType, int index,
			float deadzone) {
		this.addControllerForAction(action, new ControllerBinding(controllerBindingType, index, deadzone));
	}


	/**
	 * @param action
	 * @param controllerBindingType
	 * @param index
	 * @param povDirection
	 */
	public void addControllerForAction(Action action, ControllerBindingType controllerBindingType, int index,
			PovDirection povDirection) {
		this.addControllerForAction(action, new ControllerBinding(controllerBindingType, index, povDirection));
	}


	/**
	 * @param action
	 * @param controllerBinding
	 */
	public void addControllerForAction(Action action, ControllerBinding controllerBinding) {
		Binding binding = controls.get(action);
		if (binding == null) {
			binding = new Binding(null, controllerBinding);
			controls.put(action, binding);
		} else {
			binding.setControllerBinding(controllerBinding);
		}
	}


	/**
	 * @param action
	 * @return
	 */
	public KeyBinding getKeyForAction(Action action) {
		// TODO: Should getKeyForAction throw exception if there isnt a binding?
		Binding binding = controls.get(action);
		if (binding != null) {
			return binding.getKeyBinding();
		} else {
			return new KeyBinding();
		}
	}


	/**
	 * @param action
	 * @return
	 */
	public ControllerBinding getControllerBindingForAction(Action action) {
		Binding binding = controls.get(action);
		if (binding != null) {
			return binding.getControllerBinding();
		} else {
			return new ControllerBinding();
		}
	}


	/**
	 * @param action
	 */
	public void removeKeyForAction(Action action) {
		Binding binding = controls.get(action);
		if (binding != null) {
			binding.removeKeyBinding();
		}
	}


	/**
	 * @param action
	 */
	public void removeControllerForAction(Action action) {
		Binding binding = controls.get(action);
		if (binding != null) {
			binding.removeControllerBinding();
		}
	}


	/**
	 * @param action
	 * @return
	 */
	public boolean isKeyForActionPressed(Action action) {
		return getKeyForAction(action).isPressed();
	}


	/**
	 * @param action
	 * @return
	 */
	public float getKeyStateForAction(Action action) {
		return isKeyForActionPressed(action) ? 1 : 0;
	}


	/**
	 * @param action
	 * @param controller
	 * @return
	 */
	public float getControllerStateForAction(Action action, Controller controller) {
		return getControllerBindingForAction(action).getControllerStateForAction(controller);
	}


	/**
	 * Returns the larger value of either key state or controller state. If controller is null, returns key state.
	 *
	 * @param action
	 * @param controller
	 * @return
	 */
	public float getStateForAction(Action action, Controller controller) {
		float controllerState = (controller != null) ? getControllerStateForAction(action, controller) : 0;
		float keyState = getKeyStateForAction(action);

		return Math.max(controllerState, keyState);
	}
}
