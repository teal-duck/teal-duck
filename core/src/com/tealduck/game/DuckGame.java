package com.tealduck.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EntityTagManager;
import com.tealduck.game.engine.EventManager;
import com.tealduck.game.engine.SystemManager;
import com.tealduck.game.screen.AssetLoadingScreen;
import com.tealduck.game.screen.GameScreen;


public class DuckGame extends Game {
	private int windowWidth;
	private int windowHeight;

	private AssetManager assetManager;
	private SpriteBatch batch;
	private EntityManager entityManager;
	private EntityTagManager entityTagManager;
	private SystemManager systemManager;
	private EventManager eventManager;

	private float time = 0;
	private int frames = 0;


	@Override
	public void create() {
		assetManager = new AssetManager();

		batch = new SpriteBatch(100);
		batch.disableBlending();

		entityManager = new EntityManager();
		entityTagManager = new EntityTagManager();
		systemManager = new SystemManager();
		eventManager = new EventManager(entityManager, entityTagManager);
		setupControllers();

		loadGameScreen();

	}


	public void loadGameScreen() {
		// TODO: Abstract loadGameScreen so that other screens can use it
		// Maybe class LoadableScreen implements Screen
		GameScreen.startAssetLoading(assetManager);
		setScreen(new AssetLoadingScreen(this, assetManager, new GameScreen(this)));
	}


	public void setupControllers() {
		// TODO: setupControllers and controller helper
		// ControllerHelper controllerHelper = new ControllerHelper();
		// Controllers.addListener(controllerHelper);
		ControllerHelper.printControllers();
	}


	/**
	 * @return the first controller instance or null
	 */
	public Controller getFirstControllerOrNull() {
		Array<Controller> controllers = Controllers.getControllers();
		Controller controller = (controllers.size > 0) ? controllers.first() : null;
		return controller;
	}


	@Override
	public void resize(int width, int height) {
		// System.out.println("Game resized to (" + width + ", " + height + ")");
		windowWidth = width;
		windowHeight = height;

		super.resize(width, height);
	}


	@Override
	public void render() {
		calculateFPS(Gdx.graphics.getDeltaTime());
		super.render();
	}


	private void calculateFPS(float deltaTime) {
		time += deltaTime;
		frames += 1;

		while (time >= 1) {
			System.out.println("Calculated FPS: " + frames + "; Libgdx FPS: "
					+ Gdx.graphics.getFramesPerSecond());
			frames = 0;
			time -= 1;
		}
	}


	@Override
	public void dispose() {
		super.dispose();
		if (screen != null) {
			screen.dispose();
		}
		if (batch != null) {
			batch.dispose();
		}
		if (assetManager != null) {
			assetManager.dispose();
		}
	}


	public AssetManager getAssetManager() {
		return assetManager;
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


	public int getWindowWidth() {
		return windowWidth;
	}


	public int getWindowHeight() {
		return windowHeight;
	}

}
