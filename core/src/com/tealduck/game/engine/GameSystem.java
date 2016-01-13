package com.tealduck.game.engine;


/**
 * Abstract base class for all systems. Requires implementing the {@link GameSystem#update} method. Has reference to
 * {@link EntityManager} so update method can access entities.
 */
public abstract class GameSystem {
	private EntityEngine entityEngine;


	public GameSystem(EntityManager entityManager, EntityTagManager entityTagManager, EventManager eventManager) {
		this(new EntityEngine(entityManager, entityTagManager, eventManager));
	}


	public GameSystem(EntityEngine entityEngine) {
		this.entityEngine = entityEngine;
	}


	/**
	 * Called each frame to update some part of the game.
	 *
	 * @param deltaTime
	 *                the time elapsed since the previous frame in (seconds/millisecond)
	 */
	public abstract void update(float deltaTime);


	public EntityEngine getEntityEngine() {
		return entityEngine;
	}


	public EntityManager getEntityManager() {
		return entityEngine.getEntityManager();
	}


	public EntityTagManager getEntityTagManager() {
		return entityEngine.getEntityTagManager();
	}


	public EventManager getEventManager() {
		return entityEngine.getEventManager();
	}
}
