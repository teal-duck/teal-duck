package com.tealduck.game.screen;


import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tealduck.game.DuckGame;


public class AssetLoadingScreen implements Screen {
	// TODO: Clean up AssetLoadingScreen

	private DuckGame game;
	private AssetManager assetManager;

	private int progressBarWidth;
	private int progressBarHeight;
	private Pixmap progressBarPixmap;
	private Texture progressBarTexture;
	private Texture loadingText;

	private Screen nextScreen;


	public AssetLoadingScreen(DuckGame game, AssetManager assetManager, Screen nextScreen) {
		this.game = game;
		this.assetManager = assetManager;

		progressBarWidth = 100;
		progressBarHeight = 16;
		progressBarPixmap = new Pixmap(progressBarWidth, progressBarHeight, Format.RGBA8888);
		progressBarTexture = new Texture(progressBarPixmap);
		loadingText = new Texture("textures/loading_text.png");

		this.nextScreen = nextScreen;
	}


	@Override
	public void show() {
	}


	private boolean loaded = false;
	private float time = 0;
	private float slowDownLoading = 0.0f;
	private float timeToStayOnLoadingScreen = 0.0f;


	@Override
	public void render(float delta) {
		time += delta;

		if (loaded) {
			if (time > timeToStayOnLoadingScreen) {
				game.setScreen(nextScreen);
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

		// progress = 1;
		for (int x = 0; x < ((int) progress * progressBarWidth); x += 1) {
			progressBarPixmap.setColor(Color.RED);
			progressBarPixmap.drawLine(x, 0, x, progressBarHeight);
		}

		progressBarTexture.draw(progressBarPixmap, 0, 0);

		// System.out.println(t);
		SpriteBatch batch = game.getBatch();
		batch.begin();
		batch.draw(progressBarTexture, 0, 0);
		batch.draw(loadingText, (640 / 2) - (256 / 2), (512 / 2) - (256 / 2));
		batch.end();
	}


	@Override
	public void resize(int width, int height) {

	}


	@Override
	public void pause() {
	}


	@Override
	public void resume() {
	}


	@Override
	public void hide() {
	}


	@Override
	public void dispose() {
	}

}
