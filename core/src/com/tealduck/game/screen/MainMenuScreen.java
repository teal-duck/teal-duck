package com.tealduck.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.tealduck.game.DuckGame;
import com.tealduck.game.engine.SystemManager;
import com.tealduck.game.input.Action;
import com.tealduck.game.input.ControlMap;
import com.tealduck.game.input.controller.ControllerHelper;


public class MainMenuScreen extends DuckScreenBase {
	private static final int NEW_GAME = 0;
	private static final int LOAD_GAME = 1;
	private static final int SETTINGS = 2;
	private static final int QUIT = 3;
	private static final String[] BUTTONS = { "New Game", "Load Game", "Settings", "Quit" };
	private GlyphLayout[] BUTTONS_LAYOUT;
	private GlyphLayout titleText;

	private int selected = 0;
	private int previousDirection = 0;
	private boolean enterJustPushed = true;

	private int buttonsTextStartY = 200;
	private int buttonsBackgroundStartY = buttonsTextStartY - 30;
	private int buttonsDifference = 50;
	private int buttonWidth = 200;
	private int buttonHeight = 40;
	private int buttonBackgroundDifference = buttonsDifference - buttonHeight;

	private ShapeRenderer shapeRenderer;


	public MainMenuScreen(DuckGame game) {
		super(game);

		BUTTONS_LAYOUT = new GlyphLayout[MainMenuScreen.BUTTONS.length];
		for (int i = 0; i < MainMenuScreen.BUTTONS.length; i += 1) {
			BUTTONS_LAYOUT[i] = new GlyphLayout(getFont(), MainMenuScreen.BUTTONS[i]);
		}

		titleText = new GlyphLayout(getFont(), "Game Name Goes Here!");

		shapeRenderer = new ShapeRenderer();
	}


	@Override
	protected void load() {
	}


	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);
		logic();
		draw();
	}


	private void logic() {
		Controller controller = ControllerHelper.getFirstControllerOrNull();
		ControlMap controlMap = getControlMap();

		float upState = controlMap.getStateForAction(Action.UP, controller);
		float downState = controlMap.getStateForAction(Action.DOWN, controller);

		int directionToMove = 0;
		if (upState > 0) {
			directionToMove -= 1;
		}
		if (downState > 0) {
			directionToMove += 1;
		}

		if (previousDirection == 0) {
			moveSelected(directionToMove);
		}

		previousDirection = directionToMove;

		float selectState = controlMap.getStateForAction(Action.ENTER, controller);
		if ((selectState > 0) && !enterJustPushed) {
			selectOption();
		}
		enterJustPushed = (selectState > 0);

		float originalMouseX = Gdx.input.getX();
		float originalMouseY = Gdx.input.getY();

		Vector3 posInWorld3 = getGuiCamera().unproject(new Vector3(originalMouseX, originalMouseY, 0));

		float mouseX = posInWorld3.x;
		float mouseY = posInWorld3.y;

		int windowMiddle = getWindowWidth() / 2;
		if ((mouseY < (buttonsBackgroundStartY + buttonHeight)) && (mouseX > (windowMiddle - (buttonWidth / 2)))
				&& (mouseX < (windowMiddle + (buttonWidth / 2)))) {
			// System.out.println("Over button");

			int over = 0;
			int testY = buttonsBackgroundStartY + buttonHeight;
			boolean overButton = false;

			while (over < MainMenuScreen.BUTTONS.length) {
				testY -= buttonHeight;
				if (testY < mouseY) {
					overButton = true;
					break;
				}
				over += 1;
				testY -= buttonBackgroundDifference;
				if (testY < mouseY) {
					overButton = false;
					break;
				}
			}

			if (overButton) {
				selected = over;

				if (Gdx.input.isButtonPressed(0)) {
					selectOption();
				}
			}
		}
	}


	private void moveSelected(int amount) {
		selected += amount;

		int buttons = MainMenuScreen.BUTTONS.length;

		if (selected < 0) {
			selected = buttons + selected;
		}
		if (selected >= buttons) {
			selected = selected - buttons;
		}

	}


	private void selectOption() {
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

		int windowMiddle = getWindowWidth() / 2;

		shapeRenderer.setProjectionMatrix(getGuiCamera().combined);
		shapeRenderer.begin(ShapeType.Filled);

		int y = buttonsBackgroundStartY;

		int buttonX = windowMiddle - (buttonWidth / 2);

		for (int i = 0; i < MainMenuScreen.BUTTONS.length; i += 1) {
			if (selected == i) {
				shapeRenderer.setColor(Color.BLUE);
			} else {
				shapeRenderer.setColor(Color.RED);
			}

			shapeRenderer.rect(buttonX, y, buttonWidth, buttonHeight);

			y -= buttonsDifference;

		}

		shapeRenderer.end();

		batch.setProjectionMatrix(getGuiCamera().combined);
		batch.begin();
		batch.enableBlending();

		y = getWindowHeight() - 50;

		font.draw(batch, titleText, windowMiddle - (titleText.width / 2), y);

		y -= titleText.height;
		y = buttonsTextStartY;

		for (int i = 0; i < MainMenuScreen.BUTTONS.length; i += 1) {
			String text = MainMenuScreen.BUTTONS[i];
			GlyphLayout layout = BUTTONS_LAYOUT[i];

			if (selected == i) {
				text = "> " + text + " <";
			}
			layout.setText(font, text);

			int x = (int) (windowMiddle - (layout.width / 2));
			font.draw(batch, text, x, y);

			y -= buttonsDifference;
		}

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
