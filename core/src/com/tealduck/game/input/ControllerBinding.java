package com.tealduck.game.input;


import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.PovDirection;


/**
 *
 */
public class ControllerBinding {
	private ControllerBindingType controllerBindingType;
	private int index;
	private float deadzone;
	private PovDirection povDirection;


	/**
	 *
	 */
	public ControllerBinding() {
		this(ControllerBindingType.NONE, 0);
	}


	/**
	 * @param controllerBindingType
	 * @param index
	 */
	public ControllerBinding(ControllerBindingType controllerBindingType, int index) {
		this(controllerBindingType, index, 0);
	}


	/**
	 * @param controllerBindingType
	 * @param index
	 * @param deadzone
	 */
	public ControllerBinding(ControllerBindingType controllerBindingType, int index, float deadzone) {
		this(controllerBindingType, index, 0, null);
	}


	/**
	 * @param controllerBindingType
	 * @param index
	 * @param povDirection
	 */
	public ControllerBinding(ControllerBindingType controllerBindingType, int index, PovDirection povDirection) {
		this(controllerBindingType, index, 0, povDirection);
	}


	/**
	 * @param controllerBindingType
	 * @param index
	 * @param deadzone
	 * @param povDirection
	 */
	public ControllerBinding(ControllerBindingType controllerBindingType, int index, float deadzone,
			PovDirection povDirection) {
		this.controllerBindingType = controllerBindingType;
		this.index = index;
		this.deadzone = deadzone;
		setPovDirection(povDirection);
	}


	/**
	 * @param controllerBindingType
	 * @param index
	 */
	public void setBinding(ControllerBindingType controllerBindingType, int index) {
		this.controllerBindingType = controllerBindingType;
		this.index = index;
	}


	/**
	 * @param deadzone
	 */
	public void setDeadzone(float deadzone) {
		this.deadzone = deadzone;
	}


	/**
	 * @param povDirection
	 */
	public void setPovDirection(PovDirection povDirection) {
		if ((povDirection == null) && (controllerBindingType == ControllerBindingType.POV)) {
			throw new IllegalArgumentException("PovDirection can't be null in a POV binding");
		}
		this.povDirection = povDirection;
	}


	/**
	 * @return
	 */
	public ControllerBindingType getControllerBindingType() {
		return controllerBindingType;
	}


	/**
	 * @return
	 */
	public int getIndex() {
		return index;
	}


	/**
	 * @return
	 */
	public float getDeadzone() {
		return deadzone;
	}


	/**
	 * @return
	 */
	public PovDirection getPovDirection() {
		return povDirection;
	}


	/**
	 * @return
	 */
	public boolean hasControllerBinding() {
		return (controllerBindingType != ControllerBindingType.NONE);
	}


	/**
	 *
	 */
	public void clear() {
		controllerBindingType = ControllerBindingType.NONE;
		index = 0;
	}


	/**
	 * Note: for button and POV, returns 0 for false and 1 for true. For axis, returns value between 0 and +1 to
	 * represent axis position.
	 *
	 * @param controller
	 * @return
	 */
	public float getControllerStateForAction(Controller controller) {
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
}
