package com.tealduck.game.screen;


import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
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


public abstract class DuckScreenBase implements Screen {
	private final DuckGame game;


	public DuckScreenBase(DuckGame game) {
		this.game = game;
	}


	/**
	 * Starts the asset manager loading the assets needed for this screen.
	 *
	 * @param assetManager
	 * @return true if there are assets to load, else false
	 */
	public abstract boolean startAssetLoading(AssetManager assetManager);


	protected abstract void load();


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
		getCamera().setToOrtho(false, width, height);
		getCamera().update();
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


	public SpriteBatch getBatch() {
		return game.getBatch();
	}


	public OrthographicCamera getCamera() {
		return game.getCamera();
	}


	public AssetManager getAssetManager() {
		return game.getAssetManager();
	}


	public SystemManager getSystemManager() {
		return game.getSystemManager();
	}


	public EntityEngine getEntityEngine() {
		return game.getEntityEngine();
	}


	public EntityManager getEntityManager() {
		return getEntityEngine().getEntityManager();
	}


	public EntityTagManager getEntityTagManager() {
		return getEntityEngine().getEntityTagManager();
	}


	public EventManager getEventManager() {
		return getEntityEngine().getEventManager();
	}


	public BitmapFont getFont() {
		return game.getFont();
	}


	public int getWindowWidth() {
		return game.getWindowWidth();
	}


	public int getWindowHeight() {
		return game.getWindowHeight();
	}


	public void setScreen(Screen screen) {
		game.setScreen(screen);
	}


	public <T extends DuckScreenBase> T loadScreen(Class<T> screenClass) {
		return game.loadScreen(screenClass);
	}
}