package com.tealduck.game;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tealduck.game.screen.GameScreen;


public class DuckGame extends Game {
	private SpriteBatch batch;


	@Override
	public void create() {
		batch = new SpriteBatch();

		setScreen(new GameScreen(this));
	}


	@Override
	public void render() {
		super.render();
	}


	@Override
	public void dispose() {
		batch.dispose();
	}


	public SpriteBatch getBatch() {
		return batch;
	}

}
