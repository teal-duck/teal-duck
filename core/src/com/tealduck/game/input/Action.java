package com.tealduck.game.input;


/**
 * The possible actions that require binding to some input event.
 */
public enum Action {
	// TODO: Possibly let action say whether it can be assigned to a button or axis on controller
	// And maybe add to it a displayable string so that we can generate a settings screen by iterating through
	// Action.values()
	UP, DOWN, LEFT, RIGHT;
}
