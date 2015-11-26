package com.tealduck.game.screen;


import com.badlogic.gdx.assets.AssetManager;
import com.tealduck.game.DuckGame;
import com.tealduck.game.engine.SystemManager;


public class MainMenuScreen extends DuckScreenBase {
	public MainMenuScreen(DuckGame game) {
		super(game);
	}


	@Override
	public void render(float deltaTime) {
		loadScreen(GameScreen.class);
	}


	@Override
	public boolean startAssetLoading(AssetManager assetManager) {
		return false;
	}


	@Override
	protected void load() {
	}


	@Override
	protected void loadSystems(SystemManager systemManager) {
	}
}
