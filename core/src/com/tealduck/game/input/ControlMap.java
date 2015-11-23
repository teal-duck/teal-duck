package com.tealduck.game.input;


import java.util.EnumMap;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;


/**
 * Maps actions to bindings.
 */
public class ControlMap {
	// TODO: ControlMap (+ other input) javadoc, tests, exceptions, clean up, possible change return types from void

	private EnumMap<Action, Binding> controls;


	/**
	 * Creates a new control map. Calls clear to set all actions to have a default binding.
	 */
	public ControlMap() {
		controls = new EnumMap<Action, Binding>(Action.class);
		clear();
	}


	/**
	 * Sets the default key binding for the action.
	 *
	 * @param action
	 * @return the key binding that was inserted
	 * @throws IllegalArgumentException
	 *                 if action is null
	 * @see {@link KeyBinding#KeyBinding()}
	 */
	public KeyBinding addDefaultKeyBindingForAction(Action action) {
		if (action == null) {
			throw new IllegalArgumentException("action is null");
		}
		return addKeyForAction(action, null);
	}


	/**
	 * Sets the primary key binding for the action.
	 *
	 * @param action
	 * @param key
	 * @return the key binding that was inserted
	 * @throws IllegalArgumentException
	 *                 if action is null
	 * @see {@link KeyBinding#KeyBinding(int)}
	 */
	public KeyBinding addKeyForAction(Action action, int key) {
		if (action == null) {
			throw new IllegalArgumentException("action is null");
		}
		return addKeyForAction(action, new KeyBinding(key));
	}


	/**
	 * Sets the primary and secondary key binding for the action.
	 *
	 * @param action
	 * @param primary
	 * @param secondary
	 * @return the key binding that was inserted
	 * @throws IllegalArgumentException
	 *                 if action is null
	 * @see {@link KeyBinding#KeyBinding(int, int)}
	 */
	public KeyBinding addKeyForAction(Action action, int primary, int secondary) {
		if (action == null) {
			throw new IllegalArgumentException("action is null");
		}
		return addKeyForAction(action, new KeyBinding(primary, secondary));
	}


	/**
	 * Sets the KeyBinding instance for the action's binding. If the action doesn't have a binding, a new one is
	 * created. If keyBinding is null, the default is used.
	 *
	 * @param action
	 * @param keyBinding
	 * @return the key binding that was inserted
	 * @throws IllegalArgumentException
	 *                 if action is null
	 * @see {@link Binding#Binding(KeyBinding)}
	 * @see {@link Binding#setKeyBinding(KeyBinding)}
	 */
	public KeyBinding addKeyForAction(Action action, KeyBinding keyBinding) {
		if (action == null) {
			throw new IllegalArgumentException("action is null");
		}

		Binding binding = controls.get(action);

		if (binding == null) {
			binding = new Binding(keyBinding, null);
			controls.put(action, binding);
		} else {
			binding.setKeyBinding(keyBinding);
		}

		return binding.getKeyBinding();
	}


	/**
	 * Sets the default controller binding for the action.
	 *
	 * @param action
	 * @return the controller binding that was inserted
	 * @throws IllegalArgumentException
	 *                 if action is null
	 * @see {@link ControllerBinding#ControllerBinding()}
	 */
	public ControllerBinding addDefaultControllerBindingForAction(Action action) {
		if (action == null) {
			throw new IllegalArgumentException("action is null");
		}
		return addControllerForAction(action, null);
	}


	/**
	 * Sets the controller binding type and index for the action.
	 *
	 * @param action
	 * @param controllerBindingType
	 * @param index
	 * @return the controller binding that was inserted
	 * @throws IllegalArgumentException
	 *                 if action is null
	 * @throws IllegalArgumentException
	 *                 if controllerBindingType is null
	 * @see {@link ControllerBinding#ControllerBinding(ControllerBindingType, int)}
	 */
	public ControllerBinding addControllerForAction(Action action, ControllerBindingType controllerBindingType,
			int index) {
		if (action == null) {
			throw new IllegalArgumentException("action is null");
		}
		return addControllerForAction(action, new ControllerBinding(controllerBindingType, index));
	}


	/**
	 * Sets the controller binding type, index and deadzone for the action.
	 *
	 * @param action
	 * @param controllerBindingType
	 * @param index
	 * @param deadzone
	 * @return the controller binding that was inserted
	 * @throws IllegalArgumentException
	 *                 if action is null
	 * @throws IllegalArgumentException
	 *                 if controllerBindingType is null
	 * @see {@link ControllerBinding#ControllerBinding(ControllerBindingType, int, float)}
	 */
	public ControllerBinding addControllerForAction(Action action, ControllerBindingType controllerBindingType,
			int index, float deadzone) {
		if (action == null) {
			throw new IllegalArgumentException("action is null");
		}
		return addControllerForAction(action, new ControllerBinding(controllerBindingType, index, deadzone));
	}


	/**
	 * Sets the controller binding type, index and POV direction for the action.
	 *
	 * @param action
	 * @param controllerBindingType
	 * @param index
	 * @param povDirection
	 * @return the controller binding that was inserted
	 * @throws IllegalArgumentException
	 *                 if action is null
	 * @throws IllegalArgumentException
	 *                 if controllerBindingType is null
	 * @see {@link ControllerBinding#ControllerBinding(ControllerBindingType, int, PovDirection)}
	 */
	public ControllerBinding addControllerForAction(Action action, ControllerBindingType controllerBindingType,
			int index, PovDirection povDirection) {
		if (action == null) {
			throw new IllegalArgumentException("action is null");
		}
		return addControllerForAction(action,
				new ControllerBinding(controllerBindingType, index, povDirection));
	}


	/**
	 * Sets the ControllerBinding instance for the action's binding. If the action doesn't have a binding, a new one
	 * is created. If controllerBinding is null, the default is used.
	 *
	 * @param action
	 * @param controllerBinding
	 * @return the controller binding that was inserted
	 * @throws IllegalArgumentException
	 *                 if action is null
	 * @see {@link Binding#Binding(ControllerBinding)}
	 * @see {@link Binding#setControllerBinding(ControllerBinding)}
	 */
	public ControllerBinding addControllerForAction(Action action, ControllerBinding controllerBinding) {
		if (action == null) {
			throw new IllegalArgumentException("action is null");
		}

		Binding binding = controls.get(action);
		if (binding == null) {
			binding = new Binding(null, controllerBinding);
			controls.put(action, binding);
		} else {
			binding.setControllerBinding(controllerBinding);
		}
		return binding.getControllerBinding();
	}


	/**
	 * Gets the key binding for an action. If there is no key binding, the default is inserted into the map and
	 * returned.
	 *
	 * @param action
	 * @return the key binding for the action, or default if there is none
	 * @throws IllegalArgumentException
	 *                 if action is null
	 */
	public KeyBinding getKeyBindingForAction(Action action) {
		if (action == null) {
			throw new IllegalArgumentException("action is null");
		}

		Binding binding = controls.get(action);
		if (binding != null) {
			return binding.getKeyBinding();
		} else {
			return addDefaultKeyBindingForAction(action);
		}
	}


	/**
	 * Gets the controller binding for an action. If there is no controller binding, the default is inserted into
	 * the map and returned.
	 *
	 * @param action
	 * @return the controller binding for the action, or default if there is none
	 * @throws IllegalArgumentException
	 *                 if action is null
	 */
	public ControllerBinding getControllerBindingForAction(Action action) {
		if (action == null) {
			throw new IllegalArgumentException("action is null");
		}

		Binding binding = controls.get(action);
		if (binding != null) {
			return binding.getControllerBinding();
		} else {
			return addDefaultControllerBindingForAction(action);
		}
	}


	// TODO: Should remove key binding insert a default NO_KEY binding if there is none?
	/**
	 * Removes the key binding for the action.
	 *
	 * @param action
	 * @throws IllegalArgumentException
	 *                 if action is null
	 * @see {@link Binding#removeKeyBinding()}
	 */
	public void removeKeyForAction(Action action) {
		if (action == null) {
			throw new IllegalArgumentException("action is null");
		}

		Binding binding = controls.get(action);
		if (binding != null) {
			binding.removeKeyBinding();
		}
	}


	/**
	 * Removes the controller binding for the action.
	 *
	 * @param action
	 * @throws IllegalArgumentException
	 *                 if action is null
	 * @see {@link Binding#removeControllerBinding()}
	 */
	public void removeControllerForAction(Action action) {
		if (action == null) {
			throw new IllegalArgumentException("action is null");
		}

		Binding binding = controls.get(action);
		if (binding != null) {
			binding.removeControllerBinding();
		}
	}


	/**
	 * Sets all actions to have a default binding.
	 *
	 * @see {@link Binding#Binding()}
	 * @see {@link Binding#clear()}
	 */
	public void clear() {
		for (Action action : Action.values()) {
			Binding binding = controls.get(action);
			if (binding != null) {
				binding.clear();
			} else {
				controls.put(action, new Binding());
			}
		}
	}


	/**
	 * Returns true if a key in the action's binding is pressed.
	 *
	 * @param action
	 * @return true if the binding's key is pressed, else false
	 * @throws IllegalArgumentException
	 *                 if action is null
	 * @see {@link KeyBinding#isPressed()}
	 */
	public boolean isKeyForActionPressed(Action action) {
		if (action == null) {
			throw new IllegalArgumentException("action is null");
		}

		return getKeyBindingForAction(action).isPressed();
	}


	/**
	 * Returns 1 if a key in the action's binding is pressed.
	 *
	 * @param action
	 * @return 1 if the binding's key is pressed, else 0
	 * @throws IllegalArgumentException
	 *                 if action is null
	 * @see {@link ControlMap#isKeyForActionPressed(Action)}
	 */
	public float getKeyStateForAction(Action action) {
		if (action == null) {
			throw new IllegalArgumentException("action is null");
		}

		return isKeyForActionPressed(action) ? 1 : 0;
	}


	/**
	 * Returns the controller state for the action as a float between 0 and 1. If controller is null, returns 0.
	 *
	 * @param action
	 * @param controller
	 * @return the state for the action, or 0 if controller is null
	 * @throws IllegalArgumentException
	 *                 if action is null
	 * @see {@link ControllerBinding#getControllerStateForAction(Controller)}
	 */
	public float getControllerStateForAction(Action action, Controller controller) {
		if (action == null) {
			throw new IllegalArgumentException("action is null");
		}
		if (controller == null) {
			return 0;
		}
		return getControllerBindingForAction(action).getControllerStateForAction(controller);
	}


	/**
	 * Returns the larger value of either key state or controller state. If controller is null, returns key state.
	 *
	 * @param action
	 * @param controller
	 * @return the larger of the 2 states
	 * @throws IllegalArgumentException
	 *                 if action is null
	 * @see {@link ControlMap#getControllerStateForAction(Action, Controller)}
	 * @see {@link ControlMap#getKeyStateForAction(Action)}
	 */
	public float getStateForAction(Action action, Controller controller) {
		if (action == null) {
			throw new IllegalArgumentException("action is null");
		}
		float controllerState = (controller != null) ? getControllerStateForAction(action, controller) : 0;
		float keyState = getKeyStateForAction(action);

		return Math.max(controllerState, keyState);
	}
}
