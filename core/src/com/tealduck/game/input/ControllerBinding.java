package com.tealduck.game.input;


import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;


/**
 * Describes a binding to some action on a gamepad.
 */
public class ControllerBinding {
	// TODO: Test what happens if libgdx controller API receives a wrong value
	// I.e. what happens if index is out of the range of indices for the action (such as negative)
	private ControllerBindingType controllerBindingType;
	private int index;
	private float deadzone;
	private PovDirection povDirection;


	/**
	 * Creates a controller binding to no input.
	 */
	public ControllerBinding() {
		this(ControllerBindingType.NONE, 0);
	}


	/**
	 * Creates a controller binding with a controller binding type and index, and default deadzone and POV
	 * direction.
	 *
	 * @param controllerBindingType
	 * @param index
	 * @throws IllegalArgumentException
	 *                 if controllerBindingType is null
	 */
	public ControllerBinding(ControllerBindingType controllerBindingType, int index) {
		this(controllerBindingType, index, 0);
	}


	/**
	 * Creates a controller binding with a controller binding type, index and deadzone, and default POV direction.
	 *
	 * @param controllerBindingType
	 * @param index
	 * @param deadzone
	 * @throws IllegalArgumentException
	 *                 if controllerBindingType is null
	 */
	public ControllerBinding(ControllerBindingType controllerBindingType, int index, float deadzone) {
		this(controllerBindingType, index, 0, null);
	}


	/**
	 * Creates a controller binding with a controller binding type, index and POV direction, and default deadzone.
	 *
	 * @param controllerBindingType
	 * @param index
	 * @param povDirection
	 * @throws IllegalArgumentException
	 *                 if controllerBindingType is null
	 */
	public ControllerBinding(ControllerBindingType controllerBindingType, int index, PovDirection povDirection) {
		this(controllerBindingType, index, 0, povDirection);
	}


	/**
	 * @param controllerBindingType
	 * @param index
	 * @param deadzone
	 * @param povDirection
	 * @throws IllegalArgumentException
	 *                 if controllerBindingType is null
	 * @throws IllegalArgumentException
	 *                 if povDirection is null and controllerBindingType is POV
	 * @see {@link ControllerBinding#setControllerBindingType(ControllerBindingType)}
	 * @see {@link ControllerBinding#setPovDirection(PovDirection)}
	 */
	public ControllerBinding(ControllerBindingType controllerBindingType, int index, float deadzone,
			PovDirection povDirection) {
		setControllerBindingType(controllerBindingType);
		this.controllerBindingType = controllerBindingType;
		this.index = index;
		this.deadzone = deadzone;
		setPovDirection(povDirection);
	}


	/**
	 * Sets the controller binding type. Throws IllegalArgumentException if controllerBindingType is null.
	 *
	 * @param controllerBindingType
	 * @throws IllegalArgumentException
	 *                 if controllerBindingType is null
	 */
	public void setControllerBindingType(ControllerBindingType controllerBindingType) {
		if (controllerBindingType == null) {
			throw new IllegalArgumentException("controllerBindingType is null");
		}
		this.controllerBindingType = controllerBindingType;
	}


	/**
	 * Sets the controller binding type and index. Throws IllegalArgumentException if controllerBindingType is null.
	 *
	 * @param controllerBindingType
	 * @param index
	 * @throws IllegalArgumentException
	 *                 if controllerBindingType is null
	 * @see {@link ControllerBinding#setControllerBindingType(ControllerBindingType)}
	 */
	public void setBinding(ControllerBindingType controllerBindingType, int index) {
		setControllerBindingType(controllerBindingType);
		this.index = index;
	}


	/**
	 * Sets the deadzone for the binding.
	 *
	 * @param deadzone
	 *                the deadzone
	 */
	public void setDeadzone(float deadzone) {
		this.deadzone = deadzone;
	}


	/**
	 * Sets the POV direction for the binding. If povDirection is null and the binding type is POV, throws
	 * IllegalArgumentException as the direction is required.
	 *
	 * @param povDirection
	 *                the direction to set
	 * @throws IllegalArgumentException
	 *                 if povDirection is null and controllerBindingType is POV
	 */
	public void setPovDirection(PovDirection povDirection) {
		if ((povDirection == null) && (controllerBindingType == ControllerBindingType.POV)) {
			throw new IllegalArgumentException("PovDirection can't be null in a POV binding");
		}
		this.povDirection = povDirection;
	}


	/**
	 * Gets the controller binding type.
	 *
	 * @return
	 */
	public ControllerBindingType getControllerBindingType() {
		return controllerBindingType;
	}


	/**
	 * Gets the index.
	 *
	 * @return
	 */
	public int getIndex() {
		return index;
	}


	/**
	 * Gets the deadzone.
	 *
	 * @return
	 */
	public float getDeadzone() {
		return deadzone;
	}


	/**
	 * Gets the pov direction.
	 *
	 * @return
	 */
	public PovDirection getPovDirection() {
		return povDirection;
	}


	/**
	 * Returns true if this binding represents an action, false if it's none.
	 *
	 * @return
	 */
	public boolean hasControllerBinding() {
		return (controllerBindingType != ControllerBindingType.NONE);
	}


	/**
	 * Resets the binding to none.
	 */
	public void clear() {
		controllerBindingType = ControllerBindingType.NONE;
		index = 0;
		deadzone = 0;
		povDirection = null;
	}


	/**
	 * Returns the state on the given controller for this binding. For button and POV, returns 0 for false and 1 for
	 * true. For axis positive and axis negative, returns value between 0 and +1 to represent axis position.
	 *
	 * @param controller
	 *                the controller to query
	 * @return float between 0 and 1
	 * @throws IllegalArgumentException
	 *                 if controller is null
	 */
	public float getControllerStateForAction(Controller controller) {
		// TODO: Should the return value from getControllerStateForAction be scaled depending on deadzone?
		// E.g. if deadzone is 0.2 and the state returned is 0.3
		// Should the return value be 0.3, or mapped into the range 0.2 to 1.0
		// So 0.125?

		if (controller == null) {
			throw new IllegalArgumentException("controller is null");
		}

		float state = 0;

		switch (controllerBindingType) {
		case AXIS_POSITIVE:
			state = controller.getAxis(index);
			if (state < 0) {
				state = 0;
			}
			if (state < deadzone) {
				state = 0;
			}
			break;

		case AXIS_NEGATIVE:
			state = controller.getAxis(index);
			if (state > 0) {
				state = 0;
			}
			state = Math.abs(state);
			if (state < deadzone) {
				state = 0;
			}
			break;

		case BUTTON:
			state = controller.getButton(index) ? 1 : 0;
			break;

		case POV:
			state = (controller.getPov(index) == povDirection) ? 1 : 0;
			break;

		case NONE:
		default:
			state = 0;
		}

		return state;
	}


	@Override
	public String toString() {
		String string = "";

		switch (controllerBindingType) {
		case AXIS_NEGATIVE:
		case AXIS_POSITIVE:
			string = "ControllerBinding(" + controllerBindingType + ", " + index + ", " + deadzone + ")";
			break;
		case BUTTON:
			string = "ControllerBinding(" + controllerBindingType + ", " + index + ")";
			break;
		case POV:
			string = "ControllerBinding(" + controllerBindingType + ", " + index + ", " + povDirection
					+ ")";
			break;
		case NONE:
		default:
			string = "ControllerBinding(" + controllerBindingType + ")";
			break;

		}

		return string;
	}
}
