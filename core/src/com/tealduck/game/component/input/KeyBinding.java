package com.tealduck.game.component.input;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;


/**
 *
 */
public class KeyBinding {
	private static final int NO_KEY = Keys.UNKNOWN;

	private int primary;
	private int secondary;


	/**
	 *
	 */
	public KeyBinding() {
		this(KeyBinding.NO_KEY, KeyBinding.NO_KEY);
	}


	/**
	 * @param primary
	 */
	public KeyBinding(int primary) {
		this(primary, KeyBinding.NO_KEY);
	}


	/**
	 * @param primary
	 * @param secondary
	 */
	public KeyBinding(int primary, int secondary) {
		this.primary = primary;
		this.secondary = secondary;
	}


	/**
	 * @param primary
	 */
	public void setPrimary(int primary) {
		this.primary = primary;
	}


	/**
	 * @param secondary
	 */
	public void setSecondary(int secondary) {
		this.secondary = secondary;
	}


	/**
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
	 * @return
	 */
	public int getPrimary() {
		return primary;
	}


	/**
	 * @return
	 */
	public int getSecondary() {
		return secondary;
	}


	/**
	 * @return
	 */
	public boolean hasPrimary() {
		return (primary != KeyBinding.NO_KEY);
	}


	/**
	 * @return
	 */
	public boolean hasSecondary() {
		return (secondary != KeyBinding.NO_KEY);
	}


	/**
	 * @return
	 */
	public boolean hasKey() {
		return (hasPrimary() || hasSecondary());
	}


	/**
	 * @return
	 */
	public boolean isPrimaryPressed() {
		return hasPrimary() ? Gdx.input.isKeyPressed(primary) : false;
	}


	/**
	 * @return
	 */
	public boolean isSecondaryPressed() {
		return hasSecondary() ? Gdx.input.isKeyPressed(secondary) : false;
	}


	/**
	 * @return
	 */
	public boolean isPressed() {
		return (isPrimaryPressed() || isSecondaryPressed());
	}


	/**
	 *
	 */
	public void clear() {
		primary = KeyBinding.NO_KEY;
		secondary = KeyBinding.NO_KEY;
	}
}
