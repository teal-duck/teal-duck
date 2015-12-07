package com.tealduck.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tealduck.game.DuckGame;
import com.tealduck.game.engine.SystemManager;
import com.tealduck.game.input.Action;
import com.tealduck.game.input.ControlMap;
import com.tealduck.game.input.ControllerBindingType;
import com.tealduck.game.input.controller.ControllerHelper;
import com.tealduck.game.input.controller.PS4;


public class MainMenuScreen extends DuckScreenBase {
	private ControlMap controls;


	public MainMenuScreen(DuckGame game) {
		super(game);
	}


	@Override
	protected void load() {
		controls = new ControlMap();

		controls.addKeyForAction(Action.ENTER, Keys.ENTER);
		controls.addControllerForAction(Action.ENTER, ControllerBindingType.BUTTON, PS4.BUTTON_X);
	}


	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);
		logic();
		draw();
	}


	private void logic() {
		if (controls.getStateForAction(Action.ENTER, ControllerHelper.getFirstControllerOrNull()) > 0) {
			this.loadScreen(GameScreen.class);
		}
	}


	private void draw() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// loadScreen(GameScreen.class);
		SpriteBatch batch = getBatch();
		BitmapFont font = getFont();

		batch.setProjectionMatrix(getGuiCamera().combined);
		batch.begin();
		batch.enableBlending();
		font.draw(batch, "Press enter to start!", (getWindowWidth() / 2) - 50, getWindowHeight() - 50);
		batch.end();
	}


	@Override
	public boolean startAssetLoading(AssetManager assetManager) {
		return false;
	}


	@Override
	protected void loadSystems(SystemManager systemManager) {
	}
}
