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
import com.tealduck.game.world.MapNames;


/**
 *
 */
public class WinScreen extends DuckScreenBase {
	private static final int CONTINUE = 0;
	private static final int SAVE_QUIT = 1;
	private static final String[] BUTTON_TEXTS = new String[] { "Continue", "Save and Quit" };
	private GlyphLayout winText;
	private GlyphLayout scoreText;
	private ButtonList buttonList;

	private GameProgress levelOverData;
	private int levelNumber;
	private int playerScore;


	/**
	 * @param game
	 * @param data
	 */
	public WinScreen(DuckGame game, Object data) {
		super(game, data);

		if (data instanceof GameProgress) {
			levelOverData = (GameProgress) data;
		} else {
			throw new IllegalArgumentException("Win screen expects level over data");
		}

		levelNumber = levelOverData.levelNumber;
		playerScore = levelOverData.score;
	}


	@Override
	public boolean startAssetLoading(AssetManager assetManager) {
		return false;
	}


	@Override
	protected void load() {
		String winTextString = "Level #" + levelNumber + " Complete!";

		if (MapNames.isLastLevel(levelNumber)) {
			winTextString = "Game Complete!";
			WinScreen.BUTTON_TEXTS[WinScreen.CONTINUE] = "Finish!";
		}

		winText = new GlyphLayout(getTitleFont(), winTextString);
		scoreText = new GlyphLayout(getTextFont(), "Your score: " + playerScore);

		buttonList = new ButtonList(WinScreen.BUTTON_TEXTS, getTextFont(), getGuiCamera(), getControlMap(),
				getController());
		setButtonLocations();
	}


	/**
	 *
	 */
	private void setButtonLocations() {
		buttonList.setPositionDefaultSize((getWindowWidth() / 2) - (ButtonList.BUTTON_WIDTH / 2), //
				ButtonList.WINDOW_EDGE_OFFSET + ButtonList
						.getHeightForDefaultButtonList(WinScreen.BUTTON_TEXTS.length));
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
			selectOption(selected);
		}
		draw();
	}


	/**
	 * @param selected
	 */
	private void selectOption(int selected) {
		switch (selected) {
		case CONTINUE:
			continueGame();
			break;
		case SAVE_QUIT:
			saveGame();
			quitGame();
			break;
		}
	}


	/**
	 *
	 */
	private void continueGame() {
		if (MapNames.isLastLevel(levelNumber)) {
			loadScreen(MainMenuScreen.class);
		} else {
			loadScreen(GameScreen.class, new GameProgress(levelNumber + 1, playerScore));
		}
	}


	/**
	 *
	 */
	private void saveGame() {
		Gdx.app.log("Save", "Todo");
	}


	/**
	 *
	 */
	private void quitGame() {
		loadScreen(MainMenuScreen.class);
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
		titleFont.draw(batch, winText, (getWindowWidth() / 2) - (winText.width / 2), y);
		y -= 50;
		textFont.draw(batch, scoreText, (getWindowWidth() / 2) - (scoreText.width / 2), y);

		batch.end();

		buttonList.render(batch);
	}
}
