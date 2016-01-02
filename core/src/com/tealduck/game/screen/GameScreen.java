package com.tealduck.game.screen;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.tealduck.game.AssetLocations;
import com.tealduck.game.DuckGame;
import com.tealduck.game.Tag;
import com.tealduck.game.TextureMap;
import com.tealduck.game.collision.Collision;
import com.tealduck.game.component.CollisionComponent;
import com.tealduck.game.component.HealthComponent;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.SystemManager;
import com.tealduck.game.system.EntityCollisionSystem;
import com.tealduck.game.system.GuiRenderSystem;
import com.tealduck.game.system.InputLogicSystem;
import com.tealduck.game.system.MovementSystem;
import com.tealduck.game.system.PathfindingSystem;
import com.tealduck.game.system.PatrolLogicSystem;
import com.tealduck.game.system.WorldCollisionSystem;
import com.tealduck.game.system.WorldRenderSystem;
import com.tealduck.game.world.EntityLoader;
import com.tealduck.game.world.MapNames;
import com.tealduck.game.world.World;


public class GameScreen extends DuckScreenBase {
	// TODO: Move loaded textures into a map from name to texture instance
	// private Texture duckTexture;
	// private Texture enemyTexture;
	// private Texture goalTexture;

	private TextureMap textureMap;
	private World world;


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

		assetManager.load(AssetLocations.DUCK, Texture.class, textureParameter);
		assetManager.load(AssetLocations.ENEMY, Texture.class, textureParameter);
		assetManager.load(AssetLocations.GOAL, Texture.class, textureParameter);

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
		textureMap = new TextureMap();

		textureMap.putTextureFromAssetManager(AssetLocations.DUCK, assetManager);
		textureMap.putTextureFromAssetManager(AssetLocations.ENEMY, assetManager);
		textureMap.putTextureFromAssetManager(AssetLocations.GOAL, assetManager);

		// duckTexture = assetManager.get(AssetLocations.DUCK);
		// enemyTexture = assetManager.get(AssetLocations.ENEMY);
		// goalTexture = assetManager.get(AssetLocations.GOAL);

		TiledMap tiledMap = assetManager.get(MapNames.TEST_MAP);
		world = new World(getEntityEngine(), tiledMap);
		world.addPatrolRoutes(EntityLoader.loadPatrolRoutes(tiledMap));

		EntityLoader.loadEntities(world, textureMap);

		// world.loadEntities(duckTexture, enemyTexture, goalTexture);
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tealduck.game.screen.DuckScreenBase#loadSystems(com.tealduck.game.engine.SystemManager)
	 */
	@Override
	protected void loadSystems(SystemManager systemManager) {
		systemManager.addSystem(new InputLogicSystem(getEntityEngine()), 0);
		systemManager.addSystem(new PathfindingSystem(getEntityEngine()), 1);
		systemManager.addSystem(new PatrolLogicSystem(getEntityEngine()), 2);
		systemManager.addSystem(new MovementSystem(getEntityEngine()), 4);
		systemManager.addSystem(new WorldCollisionSystem(getEntityEngine(), world), 5);
		systemManager.addSystem(new EntityCollisionSystem(getEntityEngine()), 6);
		systemManager.addSystem(new WorldRenderSystem(getEntityEngine(), world), 7); // , getGuiCamera()), 7);
		systemManager.addSystem(new GuiRenderSystem(getEntityEngine(), getBatch(), getGuiCamera(), getFont()),
				8);
	}


	@Override
	public void resize(int width, int height) {
		super.resize(width, height);

		WorldRenderSystem worldRenderSystem = getSystemManager().getSystemOfType(WorldRenderSystem.class);
		if (worldRenderSystem != null) {
			worldRenderSystem.resizeCamera(width, height);
		}
	}


	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);

		EntityManager entityManager = getEntityManager();
		try {
			int playerId = getEntityTagManager().getEntity(Tag.PLAYER);
			if (entityManager.entityHasComponent(playerId, HealthComponent.class)) {
				HealthComponent healthComponent = entityManager.getComponent(playerId,
						HealthComponent.class);
				int health = healthComponent.health;

				if (health <= 0) {
					this.loadScreen(GameOverScreen.class);
				}
			}

			int goalId = getEntityTagManager().getEntity(Tag.GOAL);
			if (entityManager.entityHasComponent(playerId, CollisionComponent.class)
					&& entityManager.entityHasComponent(goalId, CollisionComponent.class)) {
				CollisionComponent cc0 = entityManager.getComponent(playerId, CollisionComponent.class);
				CollisionComponent cc1 = entityManager.getComponent(goalId, CollisionComponent.class);

				if (Collision.shapeToShape(cc0.collisionShape, cc1.collisionShape) != null) {
					this.loadScreen(WinScreen.class);
				}

			}

		} catch (NullPointerException e) {

		}
	}
}
