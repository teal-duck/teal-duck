package com.tealduck.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.tealduck.game.DuckGame;
import com.tealduck.game.engine.SystemManager;
import com.tealduck.game.gui.ButtonList;


public class AssetLoadingScreen extends DuckScreenBase {
	private ShapeRenderer shapeRenderer;
	private int progressBarFullWidth;
	private int progressBarHeight;
	private int progressBarBottomOffset;

	private Screen nextScreen;

	private GlyphLayout loadingText;

	private boolean loaded = false;
	// For testing purposes, these keep the loading screen visible for a certain time
	// So we can see what it looks like
	private float time = 0;
	private float timeToStayOnLoadingScreenAfterLoadingFinished = 1f;
	private GlyphLayout loadingExtraText;
	private String loadingExtraString;


	/**
	 * @param game
	 * @param data
	 */
	public AssetLoadingScreen(DuckGame game, Object data) {
		super(game, data);
		if (data instanceof String) {
			loadingExtraString = (String) data;
		} else {
			loadingExtraString = "";
		}
		shapeRenderer = new ShapeRenderer();
	}


	/**
	 * @param nextScreen
	 */
	public void setNextScreen(DuckScreenBase nextScreen) {
		this.nextScreen = nextScreen;
	}


	@Override
	public String startAssetLoading(AssetManager assetManager) {
		return null;
	}


	@Override
	protected void load() {
		loadingText = new GlyphLayout(getTitleFont(), "Loading");
		progressBarFullWidth = 128;
		progressBarHeight = 16;
		progressBarBottomOffset = ButtonList.WINDOW_EDGE_OFFSET;

		loadingExtraText = new GlyphLayout(getTextFont(), loadingExtraString);
	}


	@Override
	protected void loadSystems(SystemManager systemManager) {
	}


	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		AssetManager assetManager = getAssetManager();
		if (!loaded && assetManager.update()) {
			loaded = true;
		}

		if (loaded) {
			time += deltaTime;
			if (time > timeToStayOnLoadingScreenAfterLoadingFinished) {
				setScreen(nextScreen);
			}
		}

		float progress = assetManager.getProgress();

		shapeRenderer.setProjectionMatrix(getGuiCamera().combined);
		shapeRenderer.begin(ShapeType.Filled);
		// Render the progress bar as green on red background
		float x = (getWindowWidth() / 2) - (progressBarFullWidth / 2);
		float y = progressBarBottomOffset + progressBarHeight;
		float width = progress * progressBarFullWidth;
		float height = progressBarHeight;

		shapeRenderer.setColor(Color.RED);
		shapeRenderer.rect(x, y, progressBarFullWidth, progressBarHeight);

		shapeRenderer.setColor(Color.GREEN);
		shapeRenderer.rect(x, y, width, height);
		shapeRenderer.end();

		SpriteBatch batch = getBatch();
		BitmapFont titleFont = getTitleFont();
		BitmapFont textFont = getTextFont();

		batch.setProjectionMatrix(getGuiCamera().combined);
		batch.begin();
		titleFont.draw(batch, loadingText, (getWindowWidth() / 2) - (loadingText.width / 2),
				getWindowHeight() / 2);
		textFont.draw(batch, loadingExtraText, (getWindowWidth() / 2) - (loadingExtraText.width / 2),
				(getWindowHeight() / 2) - 50);

		batch.end();
	}
}
