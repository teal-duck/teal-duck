package com.tealduck.game.system;


import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.component.SpriteComponent;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.GameSystem;


/**
 * System handling rendering of all sprites.
 *
 * @author aacn500
 *
 */
public class RenderSystem extends GameSystem {
	/**
	 * SpriteBatch used to draw to screen
	 */
	private SpriteBatch batch;
	private OrthographicCamera camera;


	/**
	 *
	 * @param entityManager
	 *                EntityManager containing entities for game
	 * @param camera
	 * @param batch
	 *                SpriteBatch used to draw to screen
	 */
	public RenderSystem(EntityManager entityManager, OrthographicCamera camera, SpriteBatch batch) {
		super(entityManager);

		this.camera = camera;
		this.batch = batch;
	}


	/**
	 * Redraws all entities with sprites to the screen.
	 *
	 * @param deltaTime
	 *                time elapsed since last update
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void update(float deltaTime) {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();

		batch.setProjectionMatrix(camera.combined);

		batch.begin();

		Set<Integer> entitiesWithSpriteAndPositionComponents = entityManager
				.getAllEntitiesPossessingAllComponents(PositionComponent.class, SpriteComponent.class);

		for (int entity : entitiesWithSpriteAndPositionComponents) {
			Sprite sprite = entityManager.getComponent(entity, SpriteComponent.class).sprite;
			Vector2 position = entityManager.getComponent(entity, PositionComponent.class).position;

			batch.draw(sprite, position.x, position.y);
		}

		batch.end();
	}
}
