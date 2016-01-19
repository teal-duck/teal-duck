package com.tealduck.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.SystemManager;
import com.tealduck.game.input.ControlMap;
import com.tealduck.game.input.ControlMapCreator;
import com.tealduck.game.input.controller.ControllerHelper;
import com.tealduck.game.screen.AssetLoadingScreen;
import com.tealduck.game.screen.DuckScreenBase;
import com.tealduck.game.screen.MainMenuScreen;


// Regex for lines of code: \n[\s]*

/**
 *
 */
public class DuckGame extends Game {
	public static final String GAME_TITLE = "Teal Duck Awesome Game!"; // "Game Name Goes Here!";
	private SpriteBatch batch;
	private AssetManager assetManager;
	private SystemManager systemManager;
	private EntityEngine entityEngine;
	private OrthographicCamera guiCamera;

	private ControlMap controlMap;
	private Controller controller;

	private BitmapFont textFont;
	private BitmapFont titleFont;

	private int windowWidth;
	private int windowHeight;

	private float time = 0;
	private int frames = 0;
	private boolean logFPS = false;
	private float gameTime = 0;


	@Override
	public void create() {
		Gdx.app.log("Game", "Starting game");

		batch = new SpriteBatch(1000);

		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		Texture.setAssetManager(assetManager);

		systemManager = new SystemManager();
		entityEngine = new EntityEngine();

		guiCamera = new OrthographicCamera();

		textFont = new BitmapFont(Gdx.files.internal(AssetLocations.BERLIN_SANS),
				Gdx.files.internal(AssetLocations.BERLIN_SANS_PNG), false);
		textFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		titleFont = new BitmapFont(Gdx.files.internal(AssetLocations.BERLIN_SANS_TITLE),
				Gdx.files.internal(AssetLocations.BERLIN_SANS_TITLE_PNG), false);
		titleFont.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);

		setupControllers();

		controller = ControllerHelper.getFirstControllerOrNull();
		String controllerName = getControllerName(controller);
		controlMap = ControlMapCreator.newDefaultControlMap(controllerName);
		if (!ControlMapCreator.isControllerKnown(controllerName)) {
			controller = null;
			Gdx.app.log("Controller", "Controller not known");
		}

		loadScreen(MainMenuScreen.class);
	}


	/**
	 * Gets the name for a controller, or null if the controller is null.
	 *
	 * @param controller
	 * @return name of controller or null is null
	 */
	private String getControllerName(Controller controller) {
		if (controller == null) {
			return null;
		} else {
			return controller.getName();
		}
	}


	/**
	 * Load a screen with no extra data.
	 *
	 * @param <T>
	 *                T extends {@link DuckScreenBase}
	 * @param screenClass
	 * @return the screen that was loaded
	 * @see {@link DuckGame#loadScreen(Class, Object)}
	 */
	public <T extends DuckScreenBase> T loadScreen(Class<T> screenClass) {
		return loadScreen(screenClass, null);
	}


	/**
	 * Load a screen, pass extra data to it. If the screen requires the asset manager to load data, then a loading
	 * screen shall be shown first whilst data loads in the background.
	 *
	 * @param <T>
	 *                T extends {@link DuckScreenBase}
	 * @param screenClass
	 * @param data
	 * @return the screen that was loaded
	 */
	@SuppressWarnings("unchecked")
	public <T extends DuckScreenBase> T loadScreen(Class<T> screenClass, Object data) {
		Gdx.app.log("Screen", "Changing screen to " + screenClass.getSimpleName());

		getEntityEngine().clear();
		getSystemManager().clear();
		getAssetManager().clear();

		DuckScreenBase screen = null;
		try {
			// screen = screenClass.getConstructor(DuckGame.class).newInstance(this, data);
			screen = screenClass.getDeclaredConstructor(new Class[] { DuckGame.class, Object.class })
					.newInstance(this, data);
		} catch (Exception e) {
			e.printStackTrace();
		}

		boolean requiresAssets = screen.startAssetLoading(assetManager);

		if (requiresAssets) {
			AssetLoadingScreen loadingScreen = new AssetLoadingScreen(this, null);
			loadingScreen.setNextScreen(screen);
			setScreen(loadingScreen);
		} else {
			setScreen(screen);
		}

		return (T) screen;
	}


	/**
	 *
	 */
	public void setupControllers() {
		ControllerHelper controllerHelper = new ControllerHelper();
		Controllers.addListener(controllerHelper);
		ControllerHelper.printControllers();
	}


	@Override
	public void resize(int width, int height) {
		Gdx.app.log("Resize", "Game resized to (" + width + ", " + height + ")");
		windowWidth = width;
		windowHeight = height;

		super.resize(width, height);
	}


	@Override
	public void render() {
		float deltaTime = Gdx.graphics.getDeltaTime();
		gameTime += deltaTime;
		calculateFPS(deltaTime);
		super.render();

		entityEngine.removeAllFlaggedEntities();
	}


	/**
	 * @param deltaTime
	 */
	private void calculateFPS(float deltaTime) {
		time += deltaTime;
		frames += 1;

		while (time >= 1) {
			String fpsText = "Calculated FPS: " + frames + "; Libgdx FPS: "
					+ Gdx.graphics.getFramesPerSecond();
			if (logFPS) {
				Gdx.app.log("FPS", fpsText);
			}
			frames = 0;
			time -= 1;
		}
	}


	@Override
	public void dispose() {
		if (screen != null) {
			screen.dispose();
		}
		if (batch != null) {
			batch.dispose();
		}
		if (assetManager != null) {
			assetManager.dispose();
		}
		if (entityEngine != null) {
			entityEngine.clear();
		}

		super.dispose();
	}


	/**
	 * @return
	 */
	public SpriteBatch getBatch() {
		return batch;
	}


	/**
	 * @return
	 */
	public OrthographicCamera getGuiCamera() {
		return guiCamera;
	}


	/**
	 * @return
	 */
	public AssetManager getAssetManager() {
		return assetManager;
	}


	/**
	 * @return
	 */
	public SystemManager getSystemManager() {
		return systemManager;
	}


	/**
	 * @return
	 */
	public EntityEngine getEntityEngine() {
		return entityEngine;
	}


	/**
	 * @return
	 */
	public BitmapFont getTextFont() {
		return textFont;
	}


	/**
	 * @return
	 */
	public BitmapFont getTitleFont() {
		return titleFont;
	}


	/**
	 * @return
	 */
	public int getWindowWidth() {
		return windowWidth;
	}


	/**
	 * @return
	 */
	public int getWindowHeight() {
		return windowHeight;
	}


	/**
	 * @return
	 */
	public ControlMap getControlMap() {
		return controlMap;
	}


	/**
	 * @return
	 */
	public Controller getController() {
		return controller;
	}


	/**
	 * @return
	 */
	public float getGameTime() {
		return gameTime;
	}

}
