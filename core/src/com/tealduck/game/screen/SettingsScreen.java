package com.tealduck.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tealduck.game.DuckGame;
import com.tealduck.game.engine.SystemManager;
import com.tealduck.game.gui.ButtonList;
import com.tealduck.game.input.Action;


public class SettingsScreen extends DuckScreenBase {
	private GlyphLayout titleText;
	private ButtonList backButton;


	public SettingsScreen(DuckGame game) {
		super(game);
		titleText = new GlyphLayout(getFont(), "Settings");
		backButton = new ButtonList(new String[] { "Back" }, getFont(), getGuiCamera(), getControlMap(),
				getController());
		setBackButtonPosition();
	}


	private void setBackButtonPosition() {
		backButton.setPositions(30, 32 + ButtonList.BUTTON_HEIGHT, //
				ButtonList.BUTTON_WIDTH, //
				ButtonList.BUTTON_HEIGHT, //
				ButtonList.BUTTON_DIFFERENCE, //
				ButtonList.BUTTON_TEXT_VERTICAL_OFFSET);
	}


	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		setBackButtonPosition();
	}


	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);
		backButton.updateSelected();
		if (backButton.isSelectedSelected()) {
			goBack();
		}
		if (getControlMap().getStateForAction(Action.BACK, getController()) > 0) {
			goBack();
		}
		draw();
	}


	private void goBack() {
		loadScreen(MainMenuScreen.class);
	}


	public void draw() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		SpriteBatch batch = getBatch();
		BitmapFont font = getFont();

		int windowMiddle = getWindowWidth() / 2;
		int textHalfWidth = (int) (titleText.width / 2);

		batch.begin();
		font.draw(batch, titleText, windowMiddle - textHalfWidth, getWindowHeight() - 50);
		batch.end();

		backButton.render(batch);
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
