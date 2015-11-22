package com.tealduck.game.screen;


import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.DuckGame;
import com.tealduck.game.Tag;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.component.SpriteComponent;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EntityTagManager;
import com.tealduck.game.engine.GameSystem;
import com.tealduck.game.engine.SystemManager;
import com.tealduck.game.system.RenderSystem;


public class GameScreen implements Screen {
	private final DuckGame game;

	private Texture img;
	private OrthographicCamera camera;


	public GameScreen(DuckGame gam) {
		game = gam;

		camera = new OrthographicCamera();
		resize(game.getWidth(), game.getHeight());

		img = new Texture("badlogic.jpg");

		EntityManager entityManager = game.getEntityManager();
		EntityTagManager entityTagManager = game.getEntityTagManager();

		int duckId = entityManager.createEntityWithTag(entityTagManager, Tag.DUCK);

		entityManager.addComponent(duckId, new SpriteComponent(img));
		entityManager.addComponent(duckId, new PositionComponent(new Vector2(0, 0)));

		SystemManager systemManager = game.getSystemManager();
		systemManager.addSystem(new RenderSystem(entityManager, camera, game.getBatch()), 5);
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
		img.dispose();
	}
}
