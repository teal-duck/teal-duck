package com.tealduck.game.engine;


public abstract class SystemAbstract {
	protected EntityManager entityManager;


	public SystemAbstract(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	/**
	 * @param deltaTime
	 */
	public abstract void update(float deltaTime);
}
