package com.tealduck.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EntityTagManager;
import com.tealduck.game.engine.EventManager;
import com.tealduck.game.engine.SystemManager;
import com.tealduck.game.screen.GameScreen;


public class DuckGame extends Game {
	private int width;
	private int height;

	private SpriteBatch batch;
	private EntityManager entityManager;
	private EntityTagManager entityTagManager;
	private SystemManager systemManager;
	private EventManager eventManager;


	@Override
	public void create() {
		entityManager = new EntityManager();
		entityTagManager = new EntityTagManager();
		systemManager = new SystemManager();
		eventManager = new EventManager(entityManager, entityTagManager);

		batch = new SpriteBatch(10);
		// TODO: Is batch.disableBlending() needed?
		batch.disableBlending();
		//

		setupControllers();

		setScreen(new GameScreen(this));

	}


	public void setupControllers() {
		// TODO: setupControllers and controller helper
		// ControllerHelper controllerHelper = new ControllerHelper();
		// Controllers.addListener(controllerHelper);
		ControllerHelper.printControllers();
	}


	@Override
	public void resize(int width, int height) {
		// System.out.println("Game resized to (" + width + ", " + height + ")");
		this.width = width;
		this.height = height;

		super.resize(width, height);
	}


	@Override
	public void render() {
		super.render();
	}


	@Override
	public void dispose() {
		super.dispose();
		screen.dispose();
		batch.dispose();
	}


	public SpriteBatch getBatch() {
		return batch;
	}


	public EntityManager getEntityManager() {
		return entityManager;
	}


	public EntityTagManager getEntityTagManager() {
		return entityTagManager;
	}


	public SystemManager getSystemManager() {
		return systemManager;
	}


	public EventManager getEventManager() {
		return eventManager;
	}


	public int getWidth() {
		return width;
	}


	public int getHeight() {
		return height;
	}

}
