package com.tealduck.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tealduck.game.DuckGame;
import com.tealduck.game.GameProgress;
import com.tealduck.game.engine.SystemManager;
import com.tealduck.game.gui.ButtonList;


/**
 *
 */
public class GameOverScreen extends DuckScreenBase {
	private GlyphLayout gameOverText;
	private GlyphLayout scoreText;
	private GameProgress levelOverData;
	private ButtonList buttonList;

	private int playerScore;


	/**
	 * @param game
	 * @param data
	 */
	public GameOverScreen(DuckGame game, Object data) {
		super(game, data);

		if (data instanceof GameProgress) {
			levelOverData = (GameProgress) data;
		} else {
			throw new IllegalArgumentException("Game over screen expects level over data");
		}

		playerScore = levelOverData.score;
	}


	@Override
	public String startAssetLoading(AssetManager assetManager) {
		return null;
	}


	@Override
	protected void load() {
		gameOverText = new GlyphLayout(getTitleFont(), "Game Over");
		scoreText = new GlyphLayout(getTextFont(), "Your score: " + playerScore);

		buttonList = new ButtonList(new String[] { "Return to Menu" }, getTextFont(), getGuiCamera(),
				getControlMap(), getController());
		setButtonLocations();
	}


	/**
	 *
	 */
	private void setButtonLocations() {
		buttonList.setPositionDefaultSize((getWindowWidth() / 2) - (ButtonList.BUTTON_WIDTH / 2), //
				ButtonList.WINDOW_EDGE_OFFSET + ButtonList.BUTTON_HEIGHT);
	}


	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		setButtonLocations();
	}


	@Override
	protected void loadSystems(SystemManager systemManager) {
	}


	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);
		buttonList.updateSelected();
		if (buttonList.isSelectedSelected()) {
			int selected = buttonList.getSelected();
			if (selected == 0) {
				this.loadScreen(MainMenuScreen.class);
			}
		}
		draw();
	}


	/**
	 *
	 */
	private void draw() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		SpriteBatch batch = getBatch();
		BitmapFont textFont = getTextFont();
		BitmapFont titleFont = getTitleFont();

		batch.setProjectionMatrix(getGuiCamera().combined);
		batch.begin();
		batch.enableBlending();

		float y = getWindowHeight() - 50;
		titleFont.draw(batch, gameOverText, (getWindowWidth() / 2) - (gameOverText.width / 2), y);
		y -= 50;
		textFont.draw(batch, scoreText, (getWindowWidth() / 2) - (scoreText.width / 2), y);

		batch.end();

		buttonList.render(batch);
	}
}
