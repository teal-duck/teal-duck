package com.tealduck.game.component.input;


/**
 *
 */
public class Binding {
	private KeyBinding keyBinding;
	private ControllerBinding controllerBinding;


	/**
	 *
	 */
	public Binding() {
		this(null, null);
	}


	/**
	 * @param keyBinding
	 */
	public Binding(KeyBinding keyBinding) {
		this(keyBinding, null);
	}


	/**
	 * @param controllerBinding
	 */
	public Binding(ControllerBinding controllerBinding) {
		this(null, controllerBinding);
	}


	/**
	 * @param keyBinding
	 * @param controllerBinding
	 */
	public Binding(KeyBinding keyBinding, ControllerBinding controllerBinding) {
		setKeyBinding(keyBinding);
		setControllerBinding(controllerBinding);
	}


	/**
	 * @param keyBinding
	 */
	public void setKeyBinding(KeyBinding keyBinding) {
		if (keyBinding == null) {
			keyBinding = new KeyBinding();
		}
		this.keyBinding = keyBinding;
	}


	/**
	 * @param key
	 */
	public void setKeyBinding(int key) {
		keyBinding.setKey(key);
	}


	/**
	 * @param primary
	 * @param secondary
	 */
	public void setKeyBinding(int primary, int secondary) {
		keyBinding.setPrimary(primary);
		keyBinding.setSecondary(secondary);
	}


	/**
	 * @param controllerBinding
	 */
	public void setControllerBinding(ControllerBinding controllerBinding) {
		if (controllerBinding == null) {
			controllerBinding = new ControllerBinding(ControllerBindingType.NONE, 0);
		}
		this.controllerBinding = controllerBinding;
	}


	/**
	 * @param controllerBindingType
	 * @param index
	 */
	public void setControllerBinding(ControllerBindingType controllerBindingType, int index) {
		controllerBinding.setBinding(controllerBindingType, index);
	}


	/**
	 * @return
	 */
	public KeyBinding getKeyBinding() {
		return keyBinding;
	}


	/**
	 * @return
	 */
	public ControllerBinding getControllerBinding() {
		return controllerBinding;
	}


	/**
	 * @return
	 */
	public boolean hasKeyBinding() {
		return keyBinding.hasKey();
	}


	/**
	 * @return
	 */
	public boolean hasControllerBinding() {
		return controllerBinding.hasControllerBinding();
	}


	/**
	 *
	 */
	public void removeKeyBinding() {
		keyBinding.clear();
	}


	/**
	 *
	 */
	public void removeControllerBinding() {
		controllerBinding.clear();
	}
}