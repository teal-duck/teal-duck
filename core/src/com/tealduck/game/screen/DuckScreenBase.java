package com.tealduck.game.screen;


import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tealduck.game.DuckGame;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EntityTagManager;
import com.tealduck.game.engine.EventManager;
import com.tealduck.game.engine.GameSystem;
import com.tealduck.game.engine.SystemManager;
import com.tealduck.game.input.ControlMap;


/**
 * Base class for screens.
 */
public abstract class DuckScreenBase implements Screen {
	private final DuckGame game;


	/**
	 * @param game
	 * @param data
	 */
	public DuckScreenBase(DuckGame game, Object data) {
		this.game = game;
	}


	/**
	 * Starts the asset manager loading the assets needed for this screen.
	 *
	 * @param assetManager
	 * @return true if there are assets to load, else false
	 */
	public abstract boolean startAssetLoading(AssetManager assetManager);


	/**
	 *
	 */
	protected abstract void load();


	/**
	 * @param systemManager
	 */
	protected abstract void loadSystems(SystemManager systemManager);


	@Override
	public void show() {
		load();
		loadSystems(getSystemManager());
		resize(getWindowWidth(), getWindowHeight());
	}


	@Override
	public void render(float deltaTime) {
		for (GameSystem system : getSystemManager()) {
			system.update(deltaTime);
		}
	}


	@Override
	public void dispose() {
		getSystemManager().clear();
		getEntityEngine().clear();
		getAssetManager().clear();
	}


	@Override
	public void resize(int width, int height) {
		getGuiCamera().setToOrtho(false, width, height);
		getGuiCamera().update();
	}


	@Override
	public void pause() {
	}


	@Override
	public void resume() {
	}


	@Override
	public void hide() {
	}


	/**
	 * @return
	 */
	public DuckGame getGame() {
		return game;
	}


	/**
	 * @return
	 */
	public SpriteBatch getBatch() {
		return game.getBatch();
	}


	/**
	 * @return
	 */
	public OrthographicCamera getGuiCamera() {
		return game.getGuiCamera();
	}


	/**
	 * @return
	 */
	public AssetManager getAssetManager() {
		return game.getAssetManager();
	}


	/**
	 * @return
	 */
	public SystemManager getSystemManager() {
		return game.getSystemManager();
	}


	/**
	 * @return
	 */
	public EntityEngine getEntityEngine() {
		return game.getEntityEngine();
	}


	/**
	 * @return
	 */
	public EntityManager getEntityManager() {
		return getEntityEngine().getEntityManager();
	}


	/**
	 * @return
	 */
	public EntityTagManager getEntityTagManager() {
		return getEntityEngine().getEntityTagManager();
	}


	/**
	 * @return
	 */
	public EventManager getEventManager() {
		return getEntityEngine().getEventManager();
	}


	/**
	 * @return
	 */
	public BitmapFont getTextFont() {
		return game.getTextFont();
	}


	/**
	 * @return
	 */
	public BitmapFont getTitleFont() {
		return game.getTitleFont();
	}


	/**
	 * @return
	 */
	public int getWindowWidth() {
		return game.getWindowWidth();
	}


	/**
	 * @return
	 */
	public int getWindowHeight() {
		return game.getWindowHeight();
	}


	/**
	 * @return
	 */
	public ControlMap getControlMap() {
		return game.getControlMap();
	}


	/**
	 * @return
	 */
	public Controller getController() {
		return game.getController();
	}


	/**
	 * @param screen
	 */
	public void setScreen(Screen screen) {
		game.setScreen(screen);
	}


	/**
	 * @param screenClass
	 * @return
	 */
	public <T extends DuckScreenBase> T loadScreen(Class<T> screenClass) {
		return game.loadScreen(screenClass);
	}


	/**
	 * @param screenClass
	 * @param data
	 * @return
	 */
	public <T extends DuckScreenBase> T loadScreen(Class<T> screenClass, Object data) {
		return game.loadScreen(screenClass, data);
	}
}
