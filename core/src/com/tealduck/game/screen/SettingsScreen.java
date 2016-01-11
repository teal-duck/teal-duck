package com.tealduck.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tealduck.game.DuckGame;
import com.tealduck.game.engine.SystemManager;
import com.tealduck.game.input.Action;
import com.tealduck.game.input.ControlMap;
import com.tealduck.game.input.controller.ControllerHelper;


public class SettingsScreen extends DuckScreenBase {
	private GlyphLayout titleText;
	// Require the player to let go of enter after pressing it on main menu
	private boolean enterJustPushed = true;


	public SettingsScreen(DuckGame game) {
		super(game);
		titleText = new GlyphLayout(getFont(), "Settings");
	}


	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		SpriteBatch batch = getBatch();
		BitmapFont font = getFont();

		int windowMiddle = getWindowWidth() / 2;
		int textHalfWidth = (int) (titleText.width / 2);

		batch.begin();
		font.draw(batch, titleText, windowMiddle - textHalfWidth, getWindowHeight() - 50);
		font.draw(batch, "> Back < ", 32, 32);
		batch.end();

		Controller controller = ControllerHelper.getFirstControllerOrNull();
		ControlMap controlMap = getControlMap();
		float selectState = controlMap.getStateForAction(Action.ENTER, controller);
		if ((selectState > 0) && !enterJustPushed) {
			this.loadScreen(MainMenuScreen.class);
		}

		enterJustPushed = (selectState > 0);
	}


	@Override
	public boolean startAssetLoading(AssetManager assetManager) {
		return false;
	}


	@Override
	protected void load() {
	}


	@Override
	protected void loadSystems(SystemManager systemManager) {
	}
}
