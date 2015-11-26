package com.tealduck.game.screen;


import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tealduck.game.DuckGame;
import com.tealduck.game.engine.SystemManager;


public class AssetLoadingScreen extends DuckScreenBase {
	// TODO: Clean up AssetLoadingScreen

	private int progressBarWidth;
	private int progressBarHeight;
	private Pixmap progressBarPixmap;
	private Texture progressBarTexture;
	private Texture loadingText;

	private Screen nextScreen;

	private boolean loaded = false;
	private float time = 0;
	private float slowDownLoading = 0.0f;
	private float timeToStayOnLoadingScreen = 0.0f;


	public AssetLoadingScreen(DuckGame game) {
		super(game);
	}


	public void setNextScreen(DuckScreenBase nextScreen) {
		this.nextScreen = nextScreen;
	}


	@Override
	public boolean startAssetLoading(AssetManager assetManager) {
		return false;
	}


	@Override
	protected void load() {
		progressBarWidth = 100;
		progressBarHeight = 16;
		progressBarPixmap = new Pixmap(progressBarWidth, progressBarHeight, Format.RGBA8888);
		progressBarTexture = new Texture(progressBarPixmap);
		loadingText = new Texture("textures/loading_text.png");
	}


	@Override
	protected void loadSystems(SystemManager systemManager) {
	}


	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);

		AssetManager assetManager = getAssetManager();

		time += deltaTime;

		if (loaded) {
			if (time > timeToStayOnLoadingScreen) {
				setScreen(nextScreen);
			}
		} else {
			if (time > slowDownLoading) {
				if (assetManager.update()) {
					loaded = true;
				}

				time -= slowDownLoading;
			}
		}

		float progress = assetManager.getProgress();

		for (int x = 0; x < (int) (progress * progressBarWidth); x += 1) {
			progressBarPixmap.setColor(Color.RED);
			progressBarPixmap.drawLine(x, 0, x, progressBarHeight);
		}

		progressBarTexture.draw(progressBarPixmap, 0, 0);
		SpriteBatch batch = getBatch();
		batch.begin();
		batch.draw(progressBarTexture, 0, 0);
		batch.draw(loadingText, (640 / 2) - (256 / 2), (512 / 2) - (256 / 2));
		batch.end();
	}
}
