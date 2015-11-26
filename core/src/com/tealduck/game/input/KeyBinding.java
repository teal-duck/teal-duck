package com.tealduck.game.input;


import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;


/**
 * Allows an action to be mapped to a primary and secondary key. If there is a secondary key set, there is always a
 * primary key set as well.
 */
public class KeyBinding {
	public static final int NO_KEY = Keys.UNKNOWN;

	private int primary;
	private int secondary;


	/**
	 * Create a key binding with no keys.
	 */
	public KeyBinding() {
		this(KeyBinding.NO_KEY, KeyBinding.NO_KEY);
	}


	/**
	 * Creates a key binding with just a primary key.
	 *
	 * @param primary
	 */
	public KeyBinding(int primary) {
		this(primary, KeyBinding.NO_KEY);
	}


	/**
	 * Creates a key binding with a primary and secondary key.
	 *
	 * @param primary
	 * @param secondary
	 */
	public KeyBinding(int primary, int secondary) {
		this.primary = primary;
		this.secondary = secondary;
	}


	/**
	 * Sets the primary key.
	 *
	 * @param primary
	 */
	public void setPrimary(int primary) {
		this.primary = primary;
	}


	/**
	 * Sets the secondary key.
	 *
	 * @param secondary
	 */
	private void setSecondary(int secondary) {
		this.secondary = secondary;
	}


	/**
	 * Sets both the keys.
	 *
	 * @param primary
	 * @param secondary
	 *
	 * @see {@link KeyBinding#setPrimary(int)}
	 * @see {@link KeyBinding#setSecondary(int)}
	 */
	public void setBothKeys(int primary, int secondary) {
		setPrimary(primary);
		setSecondary(secondary);
	}


	/**
	 * Sets a key in the binding. If there already is a primary key, it sets secondary to the given parameter, else
	 * it sets the primary key.
	 *
	 * @param key
	 */
	public void setKey(int key) {
		if (hasPrimary()) {
			setSecondary(key);
		} else {
			setPrimary(key);
		}
	}


	/**
	 * Replaces the primary key with the secondary. Sets the secondary to no key.
	 */
	public void removePrimary() {
		primary = secondary;
		secondary = KeyBinding.NO_KEY;
	}


	/**
	 * Sets the secondary key to no key.
	 */
	public void removeSecondary() {
		secondary = KeyBinding.NO_KEY;
	}


	/**
	 * Moves the current primary key to the secondary and replaces the primary with newPrimary.
	 *
	 * @param newPrimary
	 */
	public void setPrimaryKeepOldAsSecondary(int newPrimary) {
		secondary = primary;
		primary = newPrimary;
	}


	/**
	 * Gets the primary key.
	 *
	 * @return
	 */
	public int getPrimary() {
		return primary;
	}


	/**
	 * Gets the secondary key.
	 *
	 * @return
	 */
	public int getSecondary() {
		return secondary;
	}


	/**
	 * Returns true if the primary key is not NO_KEY.
	 *
	 * @return
	 */
	public boolean hasPrimary() {
		return (primary != KeyBinding.NO_KEY);
	}


	/**
	 * Returns true if the secondary key is not NO_KEY.
	 *
	 * @return
	 */
	public boolean hasSecondary() {
		return (secondary != KeyBinding.NO_KEY);
	}


	/**
	 * Returns true if there is any key mapped.
	 *
	 * @return
	 * @see {@link KeyBinding#hasPrimary()}
	 * @see {@link KeyBinding#hasSecondary()}
	 */
	public boolean hasKey() {
		return (hasPrimary() || hasSecondary());
	}


	/**
	 * Returns true if the primary key is pressed.
	 *
	 * @param input
	 * @return
	 * @see {@link KeyBinding#isSecondaryPressed(Input)}
	 * @see {@link KeyBinding#isPressed(Input)}
	 */
	public boolean isPrimaryPressed(Input input) {
		return hasPrimary() ? input.isKeyPressed(primary) : false;
	}


	/**
	 * Returns true if the secondary key is pressed.
	 *
	 * @param input
	 * @return
	 * @see {@link KeyBinding#isPrimaryPressed(Input)}
	 * @see {@link KeyBinding#isPressed(Input)}
	 */
	public boolean isSecondaryPressed(Input input) {
		return hasSecondary() ? input.isKeyPressed(secondary) : false;
	}


	/**
	 * Returns true if either the primary or secondary key is pressed.
	 *
	 * @param input
	 * @return
	 * @see {@link KeyBinding#isPrimaryPressed(Input)}
	 * @see {@link KeyBinding#isSecondaryPressed(Input)}
	 */
	public boolean isPressed(Input input) {
		return (isPrimaryPressed(input) || isSecondaryPressed(input));
	}


	/**
	 * Resets the primary and secondary key to NO_KEY.
	 */
	public void clear() {
		primary = KeyBinding.NO_KEY;
		secondary = KeyBinding.NO_KEY;
	}


	@Override
	public String toString() {
		return "KeyBinding(Keys." + Keys.toString(primary) + ", Keys." + Keys.toString(secondary) + ")";
	}
}
