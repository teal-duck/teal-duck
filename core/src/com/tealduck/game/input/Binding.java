package com.tealduck.game.input;


/**
 * Manages a key binding and controller binding. The KeyBinding and ControllerBinding instances are guaranteed to never
 * be null. If there is no mapping, then instead the instances are in a state of no mapping.
 */
public class Binding {
	private KeyBinding keyBinding;
	private ControllerBinding controllerBinding;


	/**
	 * Create a binding with default key and controller bindings.
	 *
	 * @see {@link KeyBinding#KeyBinding()}
	 * @see {@link ControllerBinding#ControllerBinding()}
	 */
	public Binding() {
		this(null, null);
	}


	/**
	 * Create a binding with a key binding and default controller binding.
	 *
	 * @param keyBinding
	 * @see {@link ControllerBinding#ControllerBinding()}
	 */
	public Binding(KeyBinding keyBinding) {
		this(keyBinding, null);
	}


	/**
	 * Create a binding with a controller binding and default key binding.
	 *
	 * @param controllerBinding
	 * @see {@link KeyBinding#KeyBinding()}
	 */
	public Binding(ControllerBinding controllerBinding) {
		this(null, controllerBinding);
	}


	/**
	 * Create a binding with a key and controller binding.
	 *
	 * @param keyBinding
	 * @param controllerBinding
	 * @see {@link Binding#setKeyBinding(KeyBinding)}
	 * @see {@link Binding#setControllerBinding(ControllerBinding)}
	 */
	public Binding(KeyBinding keyBinding, ControllerBinding controllerBinding) {
		setKeyBinding(keyBinding);
		setControllerBinding(controllerBinding);
	}


	/**
	 * Sets the key binding in this binding. If keyBinding is null, the default key binding is used.
	 *
	 * @param keyBinding
	 *                the binding to set, or null to set the default.
	 * @see {@link KeyBinding#KeyBinding()}
	 */
	public void setKeyBinding(KeyBinding keyBinding) {
		if (keyBinding == null) {
			keyBinding = new KeyBinding();
		}
		this.keyBinding = keyBinding;
	}


	/**
	 * Set the key in the key binding.
	 *
	 * @param key
	 *                the key to set in the key binding
	 * @see {@link KeyBinding#setKey(int)}
	 */
	public void setKeyBinding(int key) {
		keyBinding.setKey(key);
	}


	/**
	 * Set the keys in the key binding.
	 *
	 * @param primary
	 *                the primary key to set
	 * @param secondary
	 *                the secondary to set
	 * @see {@link KeyBinding#setBothKeys(int, int)}
	 *
	 */
	public void setKeyBinding(int primary, int secondary) {
		keyBinding.setBothKeys(primary, secondary);
	}


	/**
	 * Sets the controller binding in this binding. If controllerBinding is null, the default controller binding is
	 * used.
	 *
	 * @param controllerBinding
	 *                the binding to set, or null to set the default.
	 * @see {@link ControllerBinding#ControllerBinding()}
	 */
	public void setControllerBinding(ControllerBinding controllerBinding) {
		if (controllerBinding == null) {
			controllerBinding = new ControllerBinding();
		}
		this.controllerBinding = controllerBinding;
	}


	/**
	 * Sets the binding type and index of the controller binding.
	 *
	 * @param controllerBindingType
	 *                the type of binding
	 * @param index
	 *                the index of the binding
	 * @see {@link ControllerBinding#setBinding(ControllerBindingType, int)}
	 */
	public void setControllerBinding(ControllerBindingType controllerBindingType, int index) {
		controllerBinding.setBinding(controllerBindingType, index);
	}

	// TODO: Add more setControllerBinding overloads for deadzone and povDirection in Binding


	/**
	 * Gets the key binding.
	 *
	 * @return the key binding
	 */
	public KeyBinding getKeyBinding() {
		return keyBinding;
	}


	/**
	 * Gets the controller binding.
	 *
	 * @return the controller binding
	 */
	public ControllerBinding getControllerBinding() {
		return controllerBinding;
	}


	/**
	 * Returns true if there is at least 1 key mapped for this binding.
	 *
	 * @return true if there's a key, else force
	 * @see {@link KeyBinding#hasKey()}
	 */
	public boolean hasKeyBinding() {
		return keyBinding.hasKey();
	}


	/**
	 * Returns true if there's a controller binding.
	 *
	 * @return true if there's a controller binding
	 * @see {@link ControllerBinding#hasControllerBinding()}
	 */
	public boolean hasControllerBinding() {
		return controllerBinding.hasControllerBinding();
	}


	/**
	 * Sets the key binding back to default state of no keys.
	 *
	 * @see {@link KeyBinding#clear()}
	 */
	public void removeKeyBinding() {
		keyBinding.clear();
	}


	/**
	 * Sets the controller binding back to default state of no binding.
	 *
	 * @see {@link ControllerBinding#clear()}
	 */
	public void removeControllerBinding() {
		controllerBinding.clear();
	}


	/**
	 * Sets the state of the binding back to default of no mapping.
	 *
	 * @see {@link Binding#removeKeyBinding()}
	 * @see {@link Binding#removeControllerBinding()}
	 */
	public void clear() {
		removeKeyBinding();
		removeControllerBinding();
	}


	@Override
	public String toString() {
		return "Binding(" + keyBinding.toString() + ", " + controllerBinding.toString() + ")";
	}
}