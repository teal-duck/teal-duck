package com.tealduck.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tealduck.game.DuckGame;
import com.tealduck.game.LevelOverData;
import com.tealduck.game.engine.SystemManager;
import com.tealduck.game.gui.ButtonList;


public class WinScreen extends DuckScreenBase {
	private static final int CONTINUE = 0;
	private static final int SAVE_QUIT = 1;
	private static final String[] BUTTON_TEXTS = new String[] { "Continue", "Save and Quit" };
	private GlyphLayout winText;
	private GlyphLayout scoreText;
	private ButtonList buttonList;

	private LevelOverData levelOverData;
	private int levelNumber;
	private int playerScore;


	public WinScreen(DuckGame game, Object data) {
		super(game, data);

		if (data instanceof LevelOverData) {
			levelOverData = (LevelOverData) data;
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
		winText = new GlyphLayout(getFont(), "Level #" + levelNumber + " Complete!");
		scoreText = new GlyphLayout(getFont(), "Your score: " + playerScore);

		buttonList = new ButtonList(WinScreen.BUTTON_TEXTS, getFont(), getGuiCamera(), getControlMap(),
				getController());
		setButtonLocations();
	}


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


	private void continueGame() {
		loadScreen(GameScreen.class, levelNumber + 1);
	}


	private void saveGame() {
		Gdx.app.log("Save", "Todo");
	}


	private void quitGame() {
		loadScreen(MainMenuScreen.class);
	}


	private void draw() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		SpriteBatch batch = getBatch();
		BitmapFont font = getFont();

		batch.setProjectionMatrix(getGuiCamera().combined);
		batch.begin();
		batch.enableBlending();

		float y = getWindowHeight() - 50;
		font.draw(batch, winText, (getWindowWidth() / 2) - (winText.width / 2), y);
		y -= 50;
		font.draw(batch, scoreText, (getWindowWidth() / 2) - (scoreText.width / 2), y);

		batch.end();

		buttonList.render(batch);
	}
}
