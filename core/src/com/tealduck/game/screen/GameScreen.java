package com.tealduck.game.screen;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.ControllerHelper;
import com.tealduck.game.DuckGame;
import com.tealduck.game.MapNames;
import com.tealduck.game.Tag;
import com.tealduck.game.TextureNames;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.component.SpriteComponent;
import com.tealduck.game.component.UserInputComponent;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EntityTagManager;
import com.tealduck.game.engine.SystemManager;
import com.tealduck.game.input.Action;
import com.tealduck.game.input.ControlMap;
import com.tealduck.game.input.ControllerBindingType;
import com.tealduck.game.input.controller.PS4;
import com.tealduck.game.system.CollisionSystem;
import com.tealduck.game.system.GuiRenderSystem;
import com.tealduck.game.system.InputLogicSystem;
import com.tealduck.game.system.MovementSystem;
import com.tealduck.game.system.PatrolLogicSystem;
import com.tealduck.game.system.WorldRenderSystem;


public class GameScreen extends DuckScreenBase {
	// TODO: Split game screen into multiple smaller classes
	// private final DuckGame game;

	private Texture duckTexture;
	private Texture enemyTexture;

	private TiledMap tiledMap;

	private WorldRenderSystem worldRenderSystem;


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

		tiledMap = assetManager.get(MapNames.TEST_MAP);

		EntityManager entityManager = getEntityManager();
		EntityTagManager entityTagManager = getEntityTagManager();

		int playerX = 128;
		int playerY = 128;

		int playerId = createPlayer(entityManager, entityTagManager, duckTexture,
				new Vector2(playerX, playerY));

		Vector2[] enemyLocations = new Vector2[] { new Vector2(200, 200), new Vector2(100, 300),
				new Vector2(400, 100) };
		for (Vector2 location : enemyLocations) {
			createEnemy(entityManager, enemyTexture, location);
		}

		int followingEnemyCount = 2;
		for (int i = 0; i < followingEnemyCount; i += 1) {
			createPathfindingEnemy(entityManager, enemyTexture,
					new Vector2(MathUtils.random(100, 1000), MathUtils.random(100, 1000)),
					playerId);
		}
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
		systemManager.addSystem(new MovementSystem(getEntityEngine()), 3);

		// TODO: Change this to use SystemManager.getSystemOfType()
		worldRenderSystem = new WorldRenderSystem(getEntityEngine(), tiledMap, getCamera());

		systemManager.addSystem(worldRenderSystem, 4);
		systemManager.addSystem(new GuiRenderSystem(getEntityEngine()), 5);
	}


	/**
	 * @param entityManager
	 * @param entityTagManager
	 * @param texture
	 * @param location
	 * @return
	 */
	private int createPlayer(EntityManager entityManager, EntityTagManager entityTagManager, Texture texture,
			Vector2 location) {
		int playerId = entityManager.createEntityWithTag(entityTagManager, Tag.PLAYER);

		entityManager.addComponent(playerId, new SpriteComponent(texture));
		entityManager.addComponent(playerId, new PositionComponent(location));

		float maxSpeed = 100f; // 200.0f;
		float sprintScale = 1.5f;
		float friction = 0.8f; // 0.5f;
		entityManager.addComponent(playerId,
				new MovementComponent(new Vector2(0, 0), maxSpeed, sprintScale, friction));

		ControlMap controls = new ControlMap();

		controls.addKeyForAction(Action.RIGHT, Keys.D, Keys.RIGHT);
		controls.addKeyForAction(Action.LEFT, Keys.A, Keys.LEFT);
		controls.addKeyForAction(Action.UP, Keys.W, Keys.UP);
		controls.addKeyForAction(Action.DOWN, Keys.S, Keys.DOWN);
		controls.addKeyForAction(Action.SPRINT, Keys.SHIFT_LEFT, Keys.SHIFT_RIGHT);

		float deadzone = 0.1f;
		controls.addControllerForAction(Action.RIGHT, ControllerBindingType.AXIS_POSITIVE, PS4.AXIS_LEFT_X,
				deadzone);
		controls.addControllerForAction(Action.LEFT, ControllerBindingType.AXIS_NEGATIVE, PS4.AXIS_LEFT_X,
				deadzone);
		controls.addControllerForAction(Action.UP, ControllerBindingType.AXIS_NEGATIVE, PS4.AXIS_LEFT_Y,
				deadzone);
		controls.addControllerForAction(Action.DOWN, ControllerBindingType.AXIS_POSITIVE, PS4.AXIS_LEFT_Y,
				deadzone);
		controls.addControllerForAction(Action.SPRINT, ControllerBindingType.BUTTON, PS4.BUTTON_R1);

		UserInputComponent uic = new UserInputComponent(controls, ControllerHelper.getFirstControllerOrNull());
		Gdx.app.log("Controls", uic.controls.toString());
		entityManager.addComponent(playerId, uic);

		return playerId;
	}


	/**
	 * @param entityManager
	 * @param texture
	 * @param location
	 * @return
	 */
	private int createEnemy(EntityManager entityManager, Texture texture, Vector2 location) {
		int enemyId = entityManager.createEntity();
		entityManager.addComponent(enemyId, new SpriteComponent(texture));
		entityManager.addComponent(enemyId, new PositionComponent(location));
		return enemyId;
	}


	/**
	 * @param entityManager
	 * @param texture
	 * @param location
	 * @param targetId
	 * @return
	 */
	private int createPathfindingEnemy(EntityManager entityManager, Texture texture, Vector2 location,
			int targetId) {
		int enemyId = createEnemy(entityManager, texture, location);
		entityManager.addComponent(enemyId, new MovementComponent(new Vector2(0, 0), 80));
		// entityManager.addComponent(enemyId, new PathfindingComponent(targetId));
		return enemyId;
	}


	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

		if (worldRenderSystem != null) {
			worldRenderSystem.resizeCamera(width, height);
		}
	}
}
