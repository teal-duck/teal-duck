package com.tealduck.game.screen;


import java.util.Iterator;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.DuckGame;
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
	private EntityManager entityManager;
	private EntityTagManager entityTagManager;
	private SystemManager systemManager;


	public GameScreen(DuckGame gam) {
		game = gam;

		img = new Texture("badlogic.jpg");
		entityManager = new EntityManager();
		entityTagManager = new EntityTagManager();
		systemManager = new SystemManager();

		entityTagManager.addTag("DUCK", entityManager.createEntity());

		entityManager.addComponent(entityTagManager.getEntity("DUCK"), new SpriteComponent(img));
		entityManager.addComponent(entityTagManager.getEntity("DUCK"),
				new PositionComponent(new Vector2(0, 0)));

		systemManager.addSystem(new RenderSystem(entityManager, game.getBatch()), 5);

	}


	@Override
	public void show() {
	}


	@Override
	public void render(float delta) {
		Iterator<GameSystem> systems = systemManager.iterator();
		while (systems.hasNext()) {
			GameSystem system = systems.next();
			system.update(50.0f);

		}
	}


	@Override
	public void resize(int width, int height) {
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
		entityManager.clear();
		entityTagManager.clear();
		systemManager.clear();
	}

}
