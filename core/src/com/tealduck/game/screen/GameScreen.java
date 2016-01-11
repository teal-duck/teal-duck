package com.tealduck.game.screen;


import java.util.Set;

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
import com.tealduck.game.component.ScoreComponent;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.SystemManager;
import com.tealduck.game.system.ChaseSystem;
import com.tealduck.game.system.EntityCollisionSystem;
import com.tealduck.game.system.GuiRenderSystem;
import com.tealduck.game.system.InputLogicSystem;
import com.tealduck.game.system.MovementSystem;
import com.tealduck.game.system.PatrolLogicSystem;
import com.tealduck.game.system.WorldCollisionSystem;
import com.tealduck.game.system.WorldRenderSystem;
import com.tealduck.game.world.EntityLoader;
import com.tealduck.game.world.MapNames;
import com.tealduck.game.world.World;


public class GameScreen extends DuckScreenBase {
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
		textureParameter.minFilter = TextureFilter.Linear;
		textureParameter.magFilter = TextureFilter.Linear;

		assetManager.load(AssetLocations.DUCK, Texture.class, textureParameter);
		assetManager.load(AssetLocations.ENEMY, Texture.class, textureParameter);
		assetManager.load(AssetLocations.GOAL, Texture.class, textureParameter);
		assetManager.load(AssetLocations.BULLET, Texture.class, textureParameter);
		assetManager.load(AssetLocations.AMMO_PICKUP, Texture.class, textureParameter);
		assetManager.load(AssetLocations.HEALTH_PICKUP, Texture.class, textureParameter);
		assetManager.load(AssetLocations.POINT_LIGHT, Texture.class, textureParameter);
		assetManager.load(AssetLocations.CONE_LIGHT, Texture.class, textureParameter);
		// assetManager.load(AssetLocations.MUZZLE_FLASH, Texture.class, textureParameter);

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
		textureMap.putTextureFromAssetManager(AssetLocations.BULLET, assetManager);
		textureMap.putTextureFromAssetManager(AssetLocations.AMMO_PICKUP, assetManager);
		textureMap.putTextureFromAssetManager(AssetLocations.HEALTH_PICKUP, assetManager);
		textureMap.putTextureFromAssetManager(AssetLocations.POINT_LIGHT, assetManager);
		textureMap.putTextureFromAssetManager(AssetLocations.CONE_LIGHT, assetManager);

		TiledMap tiledMap = assetManager.get(MapNames.TEST_MAP);
		world = new World(getEntityEngine(), tiledMap);
		world.addPatrolRoutes(EntityLoader.loadPatrolRoutes(tiledMap));

		EntityLoader.loadEntities(world, textureMap, getControlMap());
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tealduck.game.screen.DuckScreenBase#loadSystems(com.tealduck.game.engine.SystemManager)
	 */
	@Override
	protected void loadSystems(SystemManager systemManager) {
		systemManager.addSystem(new ChaseSystem(getEntityEngine(), world), 1);
		systemManager.addSystem(new PatrolLogicSystem(getEntityEngine()), 2);
		systemManager.addSystem(new MovementSystem(getEntityEngine()), 4);
		systemManager.addSystem(new WorldCollisionSystem(getEntityEngine(), world), 5);
		systemManager.addSystem(new EntityCollisionSystem(getEntityEngine()), 6);

		WorldRenderSystem worldRenderSystem = new WorldRenderSystem(getEntityEngine(), world, textureMap);
		systemManager.addSystem(worldRenderSystem, 7);
		systemManager.addSystem(new InputLogicSystem(getEntityEngine(), worldRenderSystem.getCamera()), 0);

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

		GuiRenderSystem guiRenderSystem = getSystemManager().getSystemOfType(GuiRenderSystem.class);
		if (guiRenderSystem != null) {
			guiRenderSystem.resizeCamera(width, height);
		}
	}


	private boolean hasPlayerWon() {
		EntityManager entityManager = getEntityManager();
		try {
			int playerId = getEntityTagManager().getEntity(Tag.PLAYER);
			int goalId = getEntityTagManager().getEntity(Tag.GOAL);
			if (entityManager.entityHasComponent(playerId, CollisionComponent.class)
					&& entityManager.entityHasComponent(goalId, CollisionComponent.class)) {
				CollisionComponent cc0 = entityManager.getComponent(playerId, CollisionComponent.class);
				CollisionComponent cc1 = entityManager.getComponent(goalId, CollisionComponent.class);

				if (Collision.shapeToShape(cc0.collisionShape, cc1.collisionShape) != null) {
					return true;
				}

			}
		} catch (NullPointerException e) {
		}
		return false;
	}


	private boolean isEntityAlive(int entityId) {
		EntityManager entityManager = getEntityManager();
		if (entityManager.entityHasComponent(entityId, HealthComponent.class)) {
			HealthComponent healthComponent = entityManager.getComponent(entityId, HealthComponent.class);
			return (healthComponent.health > 0);
		}
		return true;
	}


	private boolean hasPlayerDied() {
		try {
			int playerId = getEntityTagManager().getEntity(Tag.PLAYER);
			return !isEntityAlive(playerId);
		} catch (NullPointerException e) {
		}
		return true;
	}


	private void removeDeadEntities() {
		EntityManager entityManager = getEntityManager();
		Set<Integer> entities = entityManager.getEntitiesWithComponent(HealthComponent.class);

		for (int entity : entities) {
			if (!isEntityAlive(entity)) {
				getEntityEngine().flagEntityToRemove(entity);
			}
		}
	}


	private void handleScores(float deltaTime) {
		EntityManager entityManager = getEntityManager();
		Set<Integer> entities = entityManager.getEntitiesWithComponent(ScoreComponent.class);

		for (int entity : entities) {
			entityManager.getComponent(entity, ScoreComponent.class).comboCountdown(deltaTime);
		}
	}


	@Override
	public void render(float deltaTime) {
		super.render(deltaTime);

		// TODO: Move score combo countdown handling
		handleScores(deltaTime);

		if (hasPlayerWon()) {
			this.loadScreen(WinScreen.class);
			return;
		}

		if (hasPlayerDied()) {
			this.loadScreen(GameOverScreen.class);
			return;
		}

		removeDeadEntities();
	}
}
