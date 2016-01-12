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


public class MainMenuScreen extends DuckScreenBase {
	private static final int NEW_GAME = 0;
	private static final int LOAD_GAME = 1;
	private static final int SETTINGS = 2;
	private static final int QUIT = 3;
	private static final String[] BUTTON_TEXTS = new String[] { "New Game", "Load Game", "Settings", "Quit" };

	private GlyphLayout titleText;
	private ButtonList buttons;


	public MainMenuScreen(DuckGame game, Object data) {
		super(game, data);

		titleText = new GlyphLayout(getFont(), "Game Name Goes Here!");
		buttons = new ButtonList(MainMenuScreen.BUTTON_TEXTS, getFont(), getGuiCamera(), getControlMap(),
				getController());
		setButtonLocations();
	}


	private void setButtonLocations() {
		buttons.setPositionDefaultSize((getWindowWidth() / 2) - (ButtonList.BUTTON_WIDTH / 2), //
				ButtonList.WINDOW_EDGE_OFFSET + ButtonList
						.getHeightForDefaultButtonList(MainMenuScreen.BUTTON_TEXTS.length));
	}


	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		setButtonLocations();
	}


	@Override
	protected void load() {
	}


	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);
		buttons.updateSelected();
		if (buttons.isSelectedSelected()) {
			int selected = buttons.getSelected();
			selectOption(selected);
		}
		draw();
	}


	private void selectOption(int selected) {
		switch (selected) {
		case NEW_GAME:
			selectNewGame();
			break;
		case LOAD_GAME:
			selectLoadGame();
			break;
		case SETTINGS:
			selectSettings();
			break;
		case QUIT:
			selectQuit();
			break;
		}
	}


	private void selectNewGame() {
		this.loadScreen(GameScreen.class);
	}


	private void selectLoadGame() {
		this.loadScreen(LoadGameScreen.class);
	}


	private void selectSettings() {
		this.loadScreen(SettingsScreen.class);
	}


	private void selectQuit() {
		Gdx.app.exit();
	}


	private void draw() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		SpriteBatch batch = getBatch();
		BitmapFont font = getFont();

		buttons.render(batch);

		batch.begin();
		int windowMiddle = getWindowWidth() / 2;
		font.draw(batch, titleText, windowMiddle - (titleText.width / 2), getWindowHeight() - 50);
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
