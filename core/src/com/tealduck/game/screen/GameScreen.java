package com.tealduck.game.screen;


import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.tealduck.game.DuckGame;
import com.tealduck.game.Tag;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PathfindingComponent;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.component.SpriteComponent;
import com.tealduck.game.component.UserInputComponent;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EntityTagManager;
import com.tealduck.game.engine.EventManager;
import com.tealduck.game.engine.GameSystem;
import com.tealduck.game.engine.SystemManager;
import com.tealduck.game.input.Action;
import com.tealduck.game.input.ControlMap;
import com.tealduck.game.input.ControllerBindingType;
import com.tealduck.game.system.CollisionSystem;
import com.tealduck.game.system.InputLogicSystem;
import com.tealduck.game.system.MovementSystem;
import com.tealduck.game.system.PatrolLogicSystem;
import com.tealduck.game.system.RenderSystem;


public class GameScreen implements Screen {
	private final DuckGame game;
	private OrthographicCamera camera;

	private Texture duckTexture;
	private Texture enemyTexture;


	public GameScreen(DuckGame gam) {
		game = gam;

		camera = new OrthographicCamera();
		resize(game.getWidth(), game.getHeight());

		duckTexture = new Texture("duck_64x64.png");
		duckTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
		enemyTexture = new Texture("badlogic_64x64.png");
		enemyTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

		EntityManager entityManager = game.getEntityManager();
		EntityTagManager entityTagManager = game.getEntityTagManager();
		EventManager eventManager = game.getEventManager();
		SystemManager systemManager = game.getSystemManager();

		int playerId = createPlayer(entityManager, entityTagManager, duckTexture, new Vector2(0, 0));

		Vector2[] enemyLocations = new Vector2[] { new Vector2(200, 200), new Vector2(100, 300),
				new Vector2(400, 100) };
		for (Vector2 location : enemyLocations) {
			createEnemy(entityManager, enemyTexture, location);
		}

		createPathfindingEnemy(entityManager, enemyTexture, new Vector2(0, 200), playerId);

		// TODO: Tidy up system instantiation
		systemManager.addSystem(new InputLogicSystem(entityManager, entityTagManager, eventManager), 0);
		systemManager.addSystem(new PatrolLogicSystem(entityManager, entityTagManager, eventManager), 1);
		systemManager.addSystem(new CollisionSystem(entityManager, entityTagManager, eventManager), 2);
		systemManager.addSystem(new MovementSystem(entityManager, entityTagManager, eventManager), 3);
		systemManager.addSystem(new RenderSystem(entityManager, entityTagManager, eventManager, camera,
				game.getBatch()), 4);
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
		entityManager.addComponent(playerId, new MovementComponent(new Vector2(0, 0), 150));

		ControlMap controls = new ControlMap();

		controls.addKeyForAction(Action.RIGHT, Keys.D, Keys.RIGHT);
		controls.addKeyForAction(Action.LEFT, Keys.A, Keys.LEFT);
		controls.addKeyForAction(Action.UP, Keys.W, Keys.UP);
		controls.addKeyForAction(Action.DOWN, Keys.S, Keys.DOWN);

		controls.addControllerForAction(Action.RIGHT, ControllerBindingType.AXIS_POSITIVE, 0, 0.3f);
		controls.addControllerForAction(Action.LEFT, ControllerBindingType.AXIS_NEGATIVE, 0, 0.3f);
		controls.addControllerForAction(Action.UP, ControllerBindingType.AXIS_NEGATIVE, 1, 0.3f);
		controls.addControllerForAction(Action.DOWN, ControllerBindingType.AXIS_POSITIVE, 1, 0.3f);

		UserInputComponent uic = new UserInputComponent(controls, getFirstControllerOrNull());
		entityManager.addComponent(playerId, uic);

		System.out.println(uic);

		return playerId;
	}


	/**
	 * @return
	 */
	private Controller getFirstControllerOrNull() {
		Array<Controller> controllers = Controllers.getControllers();
		Controller controller = (controllers.size > 0) ? controllers.first() : null;
		return controller;
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
		entityManager.addComponent(enemyId, new PathfindingComponent(targetId));
		return enemyId;
	}


	@Override
	public void show() {
	}


	@Override
	public void render(float deltaTime) {
		calculateFPS(deltaTime);

		for (GameSystem system : game.getSystemManager()) {
			system.update(deltaTime);
		}
	}


	private float time = 0;
	private int frames = 0;


	private void calculateFPS(float deltaTime) {
		time += deltaTime;
		frames += 1;

		while (time >= 1) {
			System.out.println("Frames Per Second: " + frames);
			frames = 0;
			time -= 1;
		}
	}


	@Override
	public void resize(int width, int height) {
		// System.out.println("Screen resized to (" + width + ", " + height + ")");
		camera.setToOrtho(false, game.getWidth(), game.getHeight());
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
		duckTexture.dispose();
		enemyTexture.dispose();
	}
}
