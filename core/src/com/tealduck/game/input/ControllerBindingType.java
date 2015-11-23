package com.tealduck.game.input;


/**
 * Possible different actions that can be performed on a gamepad.
 * <p>
 * Axis split into positive and negative so that, for example, if you map an action for moving left to a key press and
 * the moving the stick left, the key press would return +1 but the stick would return -1. Instead, all values are in
 * range 0 to +1 so AXIS_POSITIVE only responds to moving the stick to the right - left moves would just be treated as
 * 0, and similarly AXIS_NEGATIVE returns +1 for all the way to the left and 0 for center or to the right.
 * <p>
 * POV is the d-pad on a gamepad, and none means there is no assigned action.
 */
public enum ControllerBindingType {
	AXIS_POSITIVE, AXIS_NEGATIVE, POV, BUTTON, NONE
}
