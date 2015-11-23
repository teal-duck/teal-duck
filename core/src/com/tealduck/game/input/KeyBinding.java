package com.tealduck.game.input;


import com.badlogic.gdx.Gdx;
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


	// TODO: should setSecondary be kept? It goes against always enforcing there is always a primary key if there's
	// a secondary
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
		// TODO: Should remove key methods return previous state
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
		// TODO: Change name of setPrimaryKeepOldAsSecondary
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


	// TODO: Should KeyBinding isPressed methods take the input object as a parameter?
	/**
	 * Returns true if the primary key is pressed.
	 *
	 * @return
	 */
	public boolean isPrimaryPressed() {
		return hasPrimary() ? Gdx.input.isKeyPressed(primary) : false;
	}


	/**
	 * Returns true if the secondary key is pressed.
	 *
	 * @return
	 */
	public boolean isSecondaryPressed() {
		return hasSecondary() ? Gdx.input.isKeyPressed(secondary) : false;
	}


	/**
	 * Returns true if either the primary or secondary key is pressed.
	 *
	 * @return
	 */
	public boolean isPressed() {
		return (isPrimaryPressed() || isSecondaryPressed());
	}


	/**
	 * Resets the primary and secondary key to NO_KEY.
	 */
	public void clear() {
		primary = KeyBinding.NO_KEY;
		secondary = KeyBinding.NO_KEY;
	}
}
