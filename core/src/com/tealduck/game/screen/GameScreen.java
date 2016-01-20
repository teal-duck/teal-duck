package com.tealduck.game.screen;


import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader.TextureParameter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.AssetLocations;
import com.tealduck.game.DuckGame;
import com.tealduck.game.GameProgress;
import com.tealduck.game.Tag;
import com.tealduck.game.TextureMap;
import com.tealduck.game.collision.Collision;
import com.tealduck.game.component.CollisionComponent;
import com.tealduck.game.component.DropComponent;
import com.tealduck.game.component.HealthComponent;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.component.ScoreComponent;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.SystemManager;
import com.tealduck.game.gui.ButtonList;
import com.tealduck.game.input.Action;
import com.tealduck.game.pickup.Pickup;
import com.tealduck.game.system.ChaseSystem;
import com.tealduck.game.system.EntityCollisionSystem;
import com.tealduck.game.system.GuiRenderSystem;
import com.tealduck.game.system.InputLogicSystem;
import com.tealduck.game.system.MovementSystem;
import com.tealduck.game.system.PatrolLogicSystem;
import com.tealduck.game.system.WorldCollisionSystem;
import com.tealduck.game.system.WorldRenderSystem;
import com.tealduck.game.world.EntityConstants;
import com.tealduck.game.world.EntityLoader;
import com.tealduck.game.world.MapNames;
import com.tealduck.game.world.World;


/**
 *
 */
public class GameScreen extends DuckScreenBase {
	private static final int RESUME = 0;
	private static final int SAVE = 1;
	private static final int QUIT = 2;
	private static final String[] PAUSE_BUTTON_TEXTS = new String[] { "Resume", "Save", "Quit to Menu" };

	private ButtonList pauseButtons;

	private int pauseButtonBackgroundWidth = ButtonList.BUTTON_WIDTH + 100;
	private int pauseButtonBackgroundHeight = ButtonList.BUTTON_HEIGHT * 6;
	private int pauseButtonTextYOffset = 30;
	private int pauseButtonsBackgroundYOffset = ButtonList.BUTTON_HEIGHT;

	private GlyphLayout pauseTextLayout;

	private TextureMap textureMap;
	private World world;

	private boolean paused = false;
	private boolean previousPauseState = false;

	private ShapeRenderer shapeRenderer;

	private int levelNumber;
	private int previousScore;
	private String levelAssetName;
	private String levelName;

	private boolean pauseOnLoseFocus = false;


	/**
	 * @param game
	 * @param data
	 */
	public GameScreen(DuckGame game, Object data) {
		super(game, data);

		if (data instanceof GameProgress) {
			GameProgress gameProgress = (GameProgress) data;
			levelNumber = gameProgress.levelNumber;
			previousScore = gameProgress.score;
		} else {
			throw new IllegalArgumentException("Game screen expects an integer");
		}

		levelAssetName = MapNames.levelNumberToAssetName(levelNumber);
		if (levelAssetName == null) {
			System.out.println("Null");
		}
		levelName = MapNames.levelNumberToName(levelNumber);

		paused = false;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see com.tealduck.game.screen.DuckScreenBase#startAssetLoading(com.badlogic.gdx.assets.AssetManager)
	 */
	@Override
	public String startAssetLoading(AssetManager assetManager) {
		TextureParameter textureParameter = new TextureParameter();
		textureParameter.minFilter = TextureFilter.Linear;
		textureParameter.magFilter = TextureFilter.Linear;

		assetManager.load(AssetLocations.DUCK, Texture.class, textureParameter);
		assetManager.load(AssetLocations.ENEMY, Texture.class, textureParameter);
		assetManager.load(AssetLocations.GOAL, Texture.class, textureParameter);
		assetManager.load(AssetLocations.BULLET, Texture.class, textureParameter);
		assetManager.load(AssetLocations.AMMO_PICKUP, Texture.class, textureParameter);
		assetManager.load(AssetLocations.HEALTH_PICKUP, Texture.class, textureParameter);
		assetManager.load(AssetLocations.CONE_LIGHT, Texture.class, textureParameter);
		assetManager.load(AssetLocations.POINT_LIGHT, Texture.class, textureParameter);
		assetManager.load(AssetLocations.LIGHT_ENTITY, Texture.class, textureParameter);
		assetManager.load(AssetLocations.HEALTH_BAR, Texture.class, textureParameter);
		assetManager.load(AssetLocations.AMMO_BAR, Texture.class, textureParameter);
		assetManager.load(AssetLocations.RELOADING, Texture.class, textureParameter);
		// assetManager.load(AssetLocations.MUZZLE_FLASH, Texture.class, textureParameter);

		assetManager.load(levelAssetName, TiledMap.class);

		return levelName;
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
		textureMap.putTextureFromAssetManager(AssetLocations.CONE_LIGHT, assetManager);
		textureMap.putTextureFromAssetManager(AssetLocations.POINT_LIGHT, assetManager);
		textureMap.putTextureFromAssetManager(AssetLocations.LIGHT_ENTITY, assetManager);
		textureMap.putTextureFromAssetManager(AssetLocations.HEALTH_BAR, assetManager);
		textureMap.putTextureFromAssetManager(AssetLocations.AMMO_BAR, assetManager);
		textureMap.putTextureFromAssetManager(AssetLocations.RELOADING, assetManager);

		shapeRenderer = new ShapeRenderer();
		pauseButtons = new ButtonList(GameScreen.PAUSE_BUTTON_TEXTS, getTextFont(), getGuiCamera(),
				getControlMap(), getController());
		setButtonLocations();
		pauseTextLayout = new GlyphLayout(getTitleFont(), "Paused");

		TiledMap tiledMap = assetManager.get(levelAssetName);
		world = new World(getEntityEngine(), tiledMap);
		world.addPatrolRoutes(EntityLoader.loadPatrolRoutes(tiledMap));

		EntityLoader.loadEntities(world, textureMap, getControlMap(), getController());
	}


	/**
	 *
	 */
	private void setButtonLocations() {
		pauseButtons.setPositionDefaultSize((getWindowWidth() / 2) - (ButtonList.BUTTON_WIDTH / 2), //
				(getWindowHeight() / 2) + pauseButtonsBackgroundYOffset);
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

		systemManager.addSystem(new GuiRenderSystem(getEntityEngine(), getBatch(), getGuiCamera(),
				getTextFont(), textureMap), 8);
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

		setButtonLocations();
	}


	/**
	 * @return
	 */
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


	/**
	 * @param entityId
	 * @return
	 */
	private boolean isEntityAlive(int entityId) {
		EntityManager entityManager = getEntityManager();
		if (entityManager.entityHasComponent(entityId, HealthComponent.class)) {
			HealthComponent healthComponent = entityManager.getComponent(entityId, HealthComponent.class);
			return (healthComponent.health > 0);
		}
		return true;
	}


	/**
	 * @return
	 */
	private boolean hasPlayerDied() {
		try {
			int playerId = getEntityTagManager().getEntity(Tag.PLAYER);
			return !isEntityAlive(playerId);
		} catch (NullPointerException e) {
		}
		return true;
	}


	/**
	 *
	 */
	private void flagDeadEntitiesAndDropPickups() {
		EntityManager entityManager = getEntityManager();
		Set<Integer> entities = entityManager.getEntitiesWithComponent(HealthComponent.class);

		for (int entity : entities) {
			if (!isEntityAlive(entity)) {
				getEntityEngine().flagEntityToRemove(entity);

				if (entityManager.entityHasComponent(entity, DropComponent.class)
						&& entityManager.entityHasComponent(entity, PositionComponent.class)) {
					DropComponent dropComponent = entityManager.getComponent(entity,
							DropComponent.class);
					PositionComponent positionComponent = entityManager.getComponent(entity,
							PositionComponent.class);

					Pickup contents = dropComponent.pickup;
					Texture texture = textureMap.getTexture(contents.getTextureName());
					Vector2 position = positionComponent.position.cpy();
					EntityLoader.createPickup(getEntityEngine(), texture, position, contents);

				}
			}
		}
	}


	/**
	 * @param deltaTime
	 */
	private void handleScoreComboCountdown(float deltaTime) {
		EntityManager entityManager = getEntityManager();
		Set<Integer> entities = entityManager.getEntitiesWithComponent(ScoreComponent.class);

		for (int entity : entities) {
			entityManager.getComponent(entity, ScoreComponent.class).comboCountdown(deltaTime);
		}
	}


	@Override
	public void pause() {
		if (pauseOnLoseFocus) {
			paused = true;
		}
	}


	@Override
	public void render(float deltaTime) {
		if (paused) {
			SystemManager systemManager = getSystemManager();
			try {
				WorldRenderSystem worldRenderSystem = systemManager
						.getSystemOfType(WorldRenderSystem.class);
				worldRenderSystem.update(deltaTime);

				pauseButtons.updateSelected();
				if (pauseButtons.isSelectedSelected()) {
					int selected = pauseButtons.getSelected();
					selectPauseOption(selected);
				}
			} catch (IllegalArgumentException e) {
			} catch (NullPointerException e) {
			}

			renderPauseOverlay();
		} else {
			super.render(deltaTime);

			// TODO: Move score combo countdown handling
			handleScoreComboCountdown(deltaTime);

			if (hasPlayerWon()) {
				winGame();
				return;
			}

			if (hasPlayerDied()) {
				gameOver();
				return;
			}

			flagDeadEntitiesAndDropPickups();
		}

		float pauseState = getControlMap().getStateForAction(Action.PAUSE, getController());
		float backState = getControlMap().getStateForAction(Action.BACK, getController());
		if ((((backState > 0) && paused) || (pauseState > 0)) && !previousPauseState) {
			paused = !paused;
		}
		previousPauseState = (pauseState > 0);
	}


	/**
	 * @param finishCombo
	 * @return
	 */
	private int getPlayerScore(boolean finishCombo) {
		try {
			int playerId = getEntityTagManager().getEntity(Tag.PLAYER);
			ScoreComponent scoreComponent = getEntityManager().getComponent(playerId, ScoreComponent.class);
			if (finishCombo) {
				scoreComponent.finishCombo();
			}
			return scoreComponent.score;
		} catch (NullPointerException e) {
		} catch (IllegalArgumentException e) {
		}
		return -1;

	}


	/**
	 * @return
	 */
	private GameProgress createGameProgressData() {
		return new GameProgress(levelNumber, previousScore + getPlayerScore(true));
	}


	/**
	 *
	 */
	private void winGame() {
		int player = getEntityTagManager().getEntity(Tag.PLAYER);
		getEntityManager().getComponent(player, ScoreComponent.class).increaseScore(EntityConstants.SCORE_FOR_LEVEL_WIN);
		this.loadScreen(WinScreen.class, createGameProgressData());
	}


	/**
	 *
	 */
	private void gameOver() {
		this.loadScreen(GameOverScreen.class, createGameProgressData());
	}


	/**
	 * @param selected
	 */
	private void selectPauseOption(int selected) {
		switch (selected) {
		case RESUME:
			paused = false;
			break;
		case SAVE:
			saveGame();
			break;
		case QUIT:
			quitGame();
			break;
		}
	}


	/**
	 *
	 */
	private void saveGame() {
		Gdx.app.log("Save", "Todo");
	}


	/**
	 *
	 */
	private void quitGame() {
		loadScreen(MainMenuScreen.class);
	}


	/**
	 *
	 */
	private void renderPauseOverlay() {
		Gdx.gl.glEnable(GL20.GL_BLEND);
		shapeRenderer.setProjectionMatrix(getGuiCamera().combined);

		float windowWidth = getWindowWidth();
		float windowHeight = getWindowHeight();

		shapeRenderer.begin(ShapeType.Filled);
		float colour = 0.2f;
		float alpha = 0.5f;
		shapeRenderer.setColor(colour, colour, colour, alpha);
		shapeRenderer.rect(0, 0, getWindowWidth(), getWindowHeight());

		shapeRenderer.setColor(colour, colour, colour, 1f);

		float width = pauseButtonBackgroundWidth;
		float height = pauseButtonBackgroundHeight;
		float x = (windowWidth / 2) - (width / 2);
		float y = (windowHeight / 2) - (height / 2);

		shapeRenderer.rect(x, y, width, height);

		shapeRenderer.end();

		Gdx.gl.glDisable(GL20.GL_BLEND);

		SpriteBatch batch = getBatch();

		float textWidth = pauseTextLayout.width;
		float textX = (windowWidth / 2) - (textWidth / 2);
		float textY = (y + height) - pauseButtonTextYOffset;

		batch.begin();
		getTitleFont().draw(batch, pauseTextLayout, textX, textY);
		batch.end();

		pauseButtons.render(batch);
	}
}
