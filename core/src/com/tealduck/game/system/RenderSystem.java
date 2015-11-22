package com.tealduck.game.system;


import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.tealduck.game.Tag;
import com.tealduck.game.component.SpriteComponent;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EntityTagManager;
import com.tealduck.game.engine.EventManager;
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
	public RenderSystem(EntityManager entityManager, EntityTagManager entityTagManager, EventManager eventManager,
			OrthographicCamera camera, SpriteBatch batch) {
		super(entityManager, entityTagManager, eventManager);

		this.camera = camera;
		this.batch = batch;
	}


	/**
	 * Translates the camera so that it is centered on an entity.
	 *
	 * @param entityId
	 *                id of the entity to center on
	 */
	private void centerCameraToEntity(int entityId) {
		Sprite entitySprite = entityManager.getComponent(entityId, SpriteComponent.class).sprite;

		camera.position.set(entitySprite.getX() + (entitySprite.getWidth() / 2),
				entitySprite.getY() + (entitySprite.getHeight() / 2), 0);

	}


	/**
	 * Redraws all entities with sprites to the screen.
	 *
	 * @param deltaTime
	 *                time elapsed since last update
	 */
	@Override
	public void update(float deltaTime) {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		centerCameraToEntity(entityTagManager.getEntity(Tag.PLAYER));
		camera.update();

		batch.setProjectionMatrix(camera.combined);
		batch.begin();

		Set<Integer> entities = entityManager.getEntitiesWithComponent(SpriteComponent.class);

		for (int entity : entities) {
			Sprite sprite = entityManager.getComponent(entity, SpriteComponent.class).sprite;
			sprite.draw(batch);
		}

		batch.end();
	}
}
