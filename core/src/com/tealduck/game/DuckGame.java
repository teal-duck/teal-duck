package com.tealduck.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
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
import com.tealduck.game.input.Action;
import com.tealduck.game.input.ControlMap;
import com.tealduck.game.input.ControllerBindingType;
import com.tealduck.game.input.controller.ControllerHelper;
import com.tealduck.game.input.controller.PS4;
import com.tealduck.game.screen.AssetLoadingScreen;
import com.tealduck.game.screen.DuckScreenBase;
import com.tealduck.game.screen.MainMenuScreen;


public class DuckGame extends Game {
	private SpriteBatch batch;
	private AssetManager assetManager;
	private SystemManager systemManager;
	private EntityEngine entityEngine;
	private OrthographicCamera guiCamera;

	private ControlMap controlMap;

	private BitmapFont font;

	private int windowWidth;
	private int windowHeight;

	private float time = 0;
	private int frames = 0;
	private boolean logFPS = false;
	private float gameTime = 0;


	@Override
	public void create() {
		Gdx.app.log("Game", "Starting game");

		batch = new SpriteBatch(100);
		// batch.disableBlending();

		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		Texture.setAssetManager(assetManager);

		systemManager = new SystemManager();
		entityEngine = new EntityEngine();

		guiCamera = new OrthographicCamera();

		// TODO: Font size
		font = new BitmapFont();
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		font.getData().setScale(1.5f);

		setupControllers();

		controlMap = loadPlayerControls();

		loadScreen(MainMenuScreen.class);
		// loadScreen(GameScreen.class);
	}


	@SuppressWarnings("unchecked")
	public <T extends DuckScreenBase> T loadScreen(Class<T> screenClass) {
		Gdx.app.log("Screen", "Changing screen to " + screenClass.getSimpleName());

		getEntityEngine().clear();
		getSystemManager().clear();
		getAssetManager().clear();

		DuckScreenBase screen = null;
		try {
			screen = screenClass.getConstructor(DuckGame.class).newInstance(this);
		} catch (Exception e) {
			e.printStackTrace();
		}

		boolean requiresAssets = screen.startAssetLoading(assetManager);

		if (requiresAssets) {
			AssetLoadingScreen loadingScreen = new AssetLoadingScreen(this);
			loadingScreen.setNextScreen(screen);
			setScreen(loadingScreen);
		} else {
			setScreen(screen);
		}

		return (T) screen;
	}


	public void setupControllers() {
		// TODO: setupControllers and controller helper
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


	public float getGameTime() {
		return gameTime;
	}


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


	private ControlMap loadPlayerControls() {
		ControlMap controls = new ControlMap();

		controls.addKeyForAction(Action.RIGHT, Keys.D, Keys.RIGHT);
		controls.addKeyForAction(Action.LEFT, Keys.A, Keys.LEFT);
		controls.addKeyForAction(Action.UP, Keys.W, Keys.UP);
		controls.addKeyForAction(Action.DOWN, Keys.S, Keys.DOWN);
		controls.addKeyForAction(Action.SPRINT, Keys.SHIFT_LEFT, Keys.SHIFT_RIGHT);
		controls.addKeyForAction(Action.FIRE, Keys.SPACE);
		// TODO: Add mouse click for firing to control map

		controls.addKeyForAction(Action.RELOAD, Keys.R);

		controls.addKeyForAction(Action.ENTER, Keys.ENTER);

		float deadzone = 0.2f;
		controls.addControllerForAction(Action.RIGHT, ControllerBindingType.AXIS_POSITIVE, PS4.AXIS_LEFT_X,
				deadzone);
		controls.addControllerForAction(Action.LEFT, ControllerBindingType.AXIS_NEGATIVE, PS4.AXIS_LEFT_X,
				deadzone);
		controls.addControllerForAction(Action.UP, ControllerBindingType.AXIS_NEGATIVE, PS4.AXIS_LEFT_Y,
				deadzone);
		controls.addControllerForAction(Action.DOWN, ControllerBindingType.AXIS_POSITIVE, PS4.AXIS_LEFT_Y,
				deadzone);
		controls.addControllerForAction(Action.SPRINT, ControllerBindingType.BUTTON, PS4.BUTTON_R1);

		controls.addControllerForAction(Action.LOOK_RIGHT, ControllerBindingType.AXIS_POSITIVE,
				PS4.AXIS_RIGHT_X, deadzone);
		controls.addControllerForAction(Action.LOOK_LEFT, ControllerBindingType.AXIS_NEGATIVE, PS4.AXIS_RIGHT_X,
				deadzone);
		controls.addControllerForAction(Action.LOOK_UP, ControllerBindingType.AXIS_NEGATIVE, PS4.AXIS_RIGHT_Y,
				deadzone);
		controls.addControllerForAction(Action.LOOK_DOWN, ControllerBindingType.AXIS_POSITIVE, PS4.AXIS_RIGHT_Y,
				deadzone);
		controls.addControllerForAction(Action.FIRE, ControllerBindingType.AXIS_POSITIVE, PS4.AXIS_R2,
				deadzone);
		controls.addControllerForAction(Action.RELOAD, ControllerBindingType.BUTTON, PS4.BUTTON_L1);

		controls.addControllerForAction(Action.ENTER, ControllerBindingType.BUTTON, PS4.BUTTON_CROSS);

		return controls;
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


	public SpriteBatch getBatch() {
		return batch;
	}


	public OrthographicCamera getGuiCamera() {
		return guiCamera;
	}


	public AssetManager getAssetManager() {
		return assetManager;
	}


	public SystemManager getSystemManager() {
		return systemManager;
	}


	public EntityEngine getEntityEngine() {
		return entityEngine;
	}


	public BitmapFont getFont() {
		return font;
	}


	public int getWindowWidth() {
		return windowWidth;
	}


	public ControlMap getControlMap() {
		return controlMap;
	}


	public int getWindowHeight() {
		return windowHeight;
	}
}
