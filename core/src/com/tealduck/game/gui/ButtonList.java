package com.tealduck.game.gui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.tealduck.game.input.Action;
import com.tealduck.game.input.ControlMap;


public class ButtonList {
	private String[] buttonTexts;
	private GlyphLayout[] buttonLayouts;
	private BitmapFont font;
	private OrthographicCamera guiCamera;
	private ShapeRenderer shapeRenderer;

	private ControlMap controlMap;
	private Controller controller;

	private int topLeftX = 0;
	private int topLeftY = 0;
	private int buttonWidth = 0;
	private int buttonHeight = 0;
	private int buttonDifference = 0;
	private int buttonTextVerticalOffset = 0;

	private int halfButtonWidth = buttonWidth / 2;

	private int selected = 0;
	private int previousDirection = 0;
	private boolean enterJustPushed = true;

	public static final int BUTTON_WIDTH = 200;
	public static final int BUTTON_HEIGHT = 40;
	public static final int BUTTON_DIFFERENCE = 10;
	public static final int BUTTON_TEXT_VERTICAL_OFFSET = 30;
	public static final int WINDOW_EDGE_OFFSET = 32;


	public ButtonList(String[] buttonTexts, BitmapFont font, OrthographicCamera guiCamera, ControlMap controlMap,
			Controller controller) {
		this.buttonTexts = buttonTexts;
		this.font = font;
		this.guiCamera = guiCamera;
		this.controlMap = controlMap;
		this.controller = controller;

		buttonLayouts = new GlyphLayout[buttonTexts.length];
		for (int i = 0; i < buttonTexts.length; i += 1) {
			buttonLayouts[i] = new GlyphLayout(font, buttonTexts[i]);
		}

		shapeRenderer = new ShapeRenderer();
	}


	public static int getHeightForDefaultButtonList(int buttonCount) {
		// return 0;
		return (buttonCount * (ButtonList.BUTTON_HEIGHT + ButtonList.BUTTON_DIFFERENCE))
				- ButtonList.BUTTON_DIFFERENCE;
	}


	public void setDimensions(int topLeftX, int topLeftY, int buttonWidth, int buttonHeight, int buttonDifference,
			int buttonTextVerticalOffset) {
		this.topLeftX = topLeftX;
		this.topLeftY = topLeftY;
		this.buttonWidth = buttonWidth;
		this.buttonHeight = buttonHeight;
		this.buttonDifference = buttonDifference;
		this.buttonTextVerticalOffset = buttonTextVerticalOffset;
		halfButtonWidth = buttonWidth / 2;
	}


	public void setPositionDefaultSize(int topLeftX, int topLeftY) {
		setDimensions(topLeftX, topLeftY, ButtonList.BUTTON_WIDTH, ButtonList.BUTTON_HEIGHT,
				ButtonList.BUTTON_DIFFERENCE, ButtonList.BUTTON_TEXT_VERTICAL_OFFSET);
	}


	public int getSelected() {
		return selected;
	}


	/**
	 * True if enter key/controller button/mouse is pressed whilst over a button
	 *
	 * @return
	 */
	public boolean isSelectedSelected() {
		boolean isSelected = false;

		float selectState = controlMap.getStateForAction(Action.ENTER, controller);
		if (selectState > 0) {
			isSelected = true;
		}

		if (getMouseOverButton() == selected) {
			if (Gdx.input.isButtonPressed(0)) {
				isSelected = true;
			}
		}

		if (enterJustPushed) {
			enterJustPushed = isSelected || Gdx.input.isButtonPressed(0);
			return false;
		} else {
			enterJustPushed = isSelected || Gdx.input.isButtonPressed(0);
			return isSelected;
		}
	}


	public void updateSelected() {
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
		int over = getMouseOverButton();
		if (over >= 0) {
			selected = over;
		}
	}


	/**
	 * Returns -1 if the mouse isn't over a button, else returns the index of the button
	 *
	 * @return
	 */
	private int getMouseOverButton() {
		float originalMouseX = Gdx.input.getX();
		float originalMouseY = Gdx.input.getY();

		Vector3 posInWorld3 = guiCamera.unproject(new Vector3(originalMouseX, originalMouseY, 0));

		float mouseX = posInWorld3.x;
		float mouseY = posInWorld3.y;

		if ((mouseY <= topLeftY) && (mouseX >= topLeftX) && (mouseX <= (topLeftX + buttonWidth))) {
			int over = 0;
			int testY = topLeftY;
			boolean overButton = false;

			while (over < buttonTexts.length) {
				testY -= buttonHeight;
				if (testY <= mouseY) {
					overButton = true;
					break;
				}
				over += 1;
				testY -= buttonDifference;
				if (testY < mouseY) {
					overButton = false;
					break;
				}
			}

			if (overButton) {
				return over;
			}
		}
		return -1;
	}


	/**
	 * Increments or decrements selected by amount, performing wrap-around if necessary
	 *
	 * @param amount
	 */
	private void moveSelected(int amount) {
		selected += amount;

		int buttons = buttonTexts.length;

		while (selected < 0) {
			selected = buttons + selected;
		}
		while (selected >= buttons) {
			selected = selected - buttons;
		}
	}


	public void render(SpriteBatch batch) {
		shapeRenderer.setProjectionMatrix(guiCamera.combined);
		shapeRenderer.begin(ShapeType.Filled);

		int y = topLeftY - buttonHeight;

		for (int i = 0; i < buttonTexts.length; i += 1) {
			if (selected == i) {
				shapeRenderer.setColor(Color.BLUE);
			} else {
				shapeRenderer.setColor(Color.RED);
			}

			shapeRenderer.rect(topLeftX, y, buttonWidth, buttonHeight);

			y -= buttonDifference;
			y -= buttonHeight;
		}

		shapeRenderer.end();

		batch.setProjectionMatrix(guiCamera.combined);
		batch.begin();
		batch.enableBlending();

		y = (topLeftY - buttonHeight) + buttonTextVerticalOffset;

		for (int i = 0; i < buttonTexts.length; i += 1) {
			String text = buttonTexts[i];
			GlyphLayout layout = buttonLayouts[i];

			if (selected == i) {
				text = "> " + text + " <";
			}
			layout.setText(font, text);

			int x = (int) ((topLeftX + halfButtonWidth) - (layout.width / 2));
			font.draw(batch, text, x, y);

			y -= buttonDifference;
			y -= buttonHeight;
		}

		batch.end();
	}
}
