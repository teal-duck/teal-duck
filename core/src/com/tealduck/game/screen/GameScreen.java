package com.tealduck.game.screen;


import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.DuckGame;
import com.tealduck.game.Tag;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.component.SpriteComponent;
import com.tealduck.game.component.UserInputComponent;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EntityTagManager;
import com.tealduck.game.engine.EventManager;
import com.tealduck.game.engine.GameSystem;
import com.tealduck.game.engine.SystemManager;
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
		enemyTexture = new Texture("badlogic_64x64.png");

		EntityManager entityManager = game.getEntityManager();
		EntityTagManager entityTagManager = game.getEntityTagManager();
		EventManager eventManager = game.getEventManager();
		SystemManager systemManager = game.getSystemManager();

		createPlayer(entityManager, entityTagManager, duckTexture, new Vector2(0, 0));

		Vector2[] enemyLocations = new Vector2[] { new Vector2(200, 200), new Vector2(100, 300),
				new Vector2(400, 100) };
		for (Vector2 location : enemyLocations) {
			createEnemy(entityManager, enemyTexture, location);
		}

		// TODO: Tidy up system instantiation
		systemManager.addSystem(new InputLogicSystem(entityManager, entityTagManager, eventManager), 0);
		systemManager.addSystem(new PatrolLogicSystem(entityManager, entityTagManager, eventManager), 1);
		systemManager.addSystem(new CollisionSystem(entityManager, entityTagManager, eventManager), 2);
		systemManager.addSystem(new MovementSystem(entityManager, entityTagManager, eventManager), 3);
		systemManager.addSystem(new RenderSystem(entityManager, entityTagManager, eventManager, camera,
				game.getBatch()), 4);
	}


	private int createPlayer(EntityManager entityManager, EntityTagManager entityTagManager, Texture texture,
			Vector2 location) {
		int playerId = entityManager.createEntityWithTag(entityTagManager, Tag.PLAYER);

		entityManager.addComponent(playerId, new SpriteComponent(texture));
		entityManager.addComponent(playerId, new PositionComponent(location));

		float speed = 30;
		Vector2 direction = new Vector2(1, 1);
		Vector2 velocity = direction.setLength(speed);

		entityManager.addComponent(playerId, new MovementComponent(velocity));

		entityManager.addComponent(playerId, new UserInputComponent());

		return playerId;
	}


	private int createEnemy(EntityManager entityManager, Texture texture, Vector2 location) {
		int enemyId = entityManager.createEntity();

		entityManager.addComponent(enemyId, new SpriteComponent(texture));
		entityManager.addComponent(enemyId, new PositionComponent(location));

		return enemyId;
	}


	@Override
	public void show() {
	}


	@Override
	public void render(float deltaTime) {
		for (GameSystem system : game.getSystemManager()) {
			system.update(deltaTime);
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
