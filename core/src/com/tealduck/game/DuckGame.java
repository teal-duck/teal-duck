package com.tealduck.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.SystemManager;
import com.tealduck.game.input.controller.ControllerHelper;
import com.tealduck.game.screen.AssetLoadingScreen;
import com.tealduck.game.screen.DuckScreenBase;
import com.tealduck.game.screen.GameScreen;


public class DuckGame extends Game {
	private SpriteBatch batch;
	private AssetManager assetManager;
	private SystemManager systemManager;
	private EntityEngine entityEngine;
	private OrthographicCamera guiCamera;

	private BitmapFont font;

	private int windowWidth;
	private int windowHeight;

	private float time = 0;
	private int frames = 0;
	private boolean logFPS = false;


	@Override
	public void create() {
		Gdx.app.log("Game", "Starting game");

		batch = new SpriteBatch(100);
		batch.disableBlending();

		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		Texture.setAssetManager(assetManager);

		systemManager = new SystemManager();
		entityEngine = new EntityEngine();

		guiCamera = new OrthographicCamera();

		// TODO: Font size
		font = new BitmapFont();
		// font.getData().setScale(1.5f);

		setupControllers();

		// loadScreen(MainMenuScreen.class);
		loadScreen(GameScreen.class);
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
		calculateFPS(Gdx.graphics.getDeltaTime());
		super.render();
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


	public int getWindowHeight() {
		return windowHeight;
	}
}
