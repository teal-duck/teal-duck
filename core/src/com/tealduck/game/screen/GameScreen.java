package com.tealduck.game.screen;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.tealduck.game.DuckGame;
import com.tealduck.game.TextureNames;
import com.tealduck.game.engine.SystemManager;
import com.tealduck.game.system.CollisionSystem;
import com.tealduck.game.system.GuiRenderSystem;
import com.tealduck.game.system.InputLogicSystem;
import com.tealduck.game.system.MovementSystem;
import com.tealduck.game.system.PatrolLogicSystem;
import com.tealduck.game.system.WorldRenderSystem;
import com.tealduck.game.world.MapNames;
import com.tealduck.game.world.World;


public class GameScreen extends DuckScreenBase {
	// TODO: Split game screen into multiple smaller classes
	// private final DuckGame game;

	private Texture duckTexture;
	private Texture enemyTexture;

	private World world;

	// private TiledMap tiledMap;

	// private WorldRenderSystem worldRenderSystem;


	public GameScreen(DuckGame game) {
		super(game);
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tealduck.game.screen.DuckScreenBase#startAssetLoading(com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public boolean startAssetLoading(AssetManager assetManager) {
		TextureParameter textureParameter = new TextureParameter();
		textureParameter.minFilter = TextureFilter.Nearest;
		textureParameter.magFilter = TextureFilter.Nearest;

		assetManager.load(TextureNames.DUCK, Texture.class, textureParameter);
		assetManager.load(TextureNames.ENEMY, Texture.class, textureParameter);

		assetManager.load(MapNames.TEST_MAP, TiledMap.class);

		return true;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tealduck.game.screen.DuckScreenBase#load()
	 */
	@Override
	protected void load() {
		AssetManager assetManager = getAssetManager();

		duckTexture = assetManager.get(TextureNames.DUCK);
		enemyTexture = assetManager.get(TextureNames.ENEMY);

		TiledMap tiledMap = assetManager.get(MapNames.TEST_MAP);
		world = new World(getEntityEngine(), tiledMap);

		world.loadEntities(duckTexture, enemyTexture);
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tealduck.game.screen.DuckScreenBase#loadSystems(com.tealduck.game.engine.SystemManager)
	 */
	@Override
	protected void loadSystems(SystemManager systemManager) {
		systemManager.addSystem(new InputLogicSystem(getEntityEngine()), 0);
		systemManager.addSystem(new PatrolLogicSystem(getEntityEngine()), 1);
		systemManager.addSystem(new CollisionSystem(getEntityEngine()), 2);
		systemManager.addSystem(new MovementSystem(getEntityEngine(), world), 3);
		systemManager.addSystem(new WorldRenderSystem(getEntityEngine(), world, getCamera()), 4);
		systemManager.addSystem(new GuiRenderSystem(getEntityEngine()), 5);
	}


	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

		WorldRenderSystem worldRenderSystem = getSystemManager().getSystemOfType(WorldRenderSystem.class);
		if (worldRenderSystem != null) {
			worldRenderSystem.resizeCamera(width, height);
		}
	}
}
