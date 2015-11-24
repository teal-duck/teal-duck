package com.tealduck.game.system;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
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

	// TODO: Improve handling of gridTexture
	private Texture gridTexture;


	/**
	 *
	 * @param entityManager
	 *                EntityManager containing entities for game
	 * @param camera
	 * @param batch
	 *                SpriteBatch used to draw to screen
	 */
	public RenderSystem(EntityManager entityManager, EntityTagManager entityTagManager, EventManager eventManager,
			OrthographicCamera camera, SpriteBatch batch, Texture gridTexture) {
		super(entityManager, entityTagManager, eventManager);

		this.camera = camera;
		this.batch = batch;

		this.gridTexture = gridTexture;
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


	private float findBiggestMultipleOfBelow(float x, float y) {
		float m = 0;

		if (y > 0) {
			while (m < y) {
				m += x;
			}
			m -= x;
		} else {
			while (m > y) {
				m -= x;
			}
		}

		return m;
	}


	/**
	 * Redraws all entities with sprites to the screen.
	 *
	 * @param deltaTime
	 *                time elapsed since last update
	 */
	@Override
	public void update(float deltaTime) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		centerCameraToEntity(entityTagManager.getEntity(Tag.PLAYER));
		camera.update();

		batch.setProjectionMatrix(camera.combined);

		float viewportWidth = camera.viewportWidth;
		float viewportHeight = camera.viewportHeight;

		float cameraLeft = camera.position.x - (viewportWidth / 2);
		float cameraRight = camera.position.x + (viewportWidth / 2);
		float cameraTop = camera.position.y - (viewportHeight / 2);
		float cameraBottom = camera.position.y + (viewportHeight / 2);

		int textureWidth = gridTexture.getWidth();
		int textureHeight = gridTexture.getHeight();

		float startX = findBiggestMultipleOfBelow(textureWidth, cameraLeft) - textureWidth;
		float endX = findBiggestMultipleOfBelow(textureWidth, cameraRight) + textureWidth;
		float startY = findBiggestMultipleOfBelow(textureHeight, cameraTop) - textureHeight;
		float endY = findBiggestMultipleOfBelow(textureHeight, cameraBottom) + textureHeight;

		int renderWidth = (int) (endX - startX);
		int renderHeight = (int) (endY - startY);

		// Y is from endY and size of -renderHeight to flip it
		batch.begin();
		batch.draw(gridTexture, startX, endY, renderWidth, -renderHeight, 0, 0, renderWidth / textureWidth,
				renderHeight / textureHeight);
		batch.end();

		boolean useSortedRendering = false;
		if (useSortedRendering) {
			renderEntitiesSorted();
		} else {
			batch.begin();
			// TODO: Is batch.disableBlending() needed?
			batch.disableBlending();

			Set<Integer> entities = entityManager.getEntitiesWithComponent(SpriteComponent.class);

			for (int entity : entities) {
				Sprite sprite = entityManager.getComponent(entity, SpriteComponent.class).sprite;
				sprite.draw(batch);
			}

			batch.end();
		}
	}

	// TODO: Possibly sort all entities so that ones with the same texture get rendered together
	// Faster for the GPU to render lots of the same texture, then lots of a different
	// Rather than constantly binding a different texture
	// Possible implementation below


	// TODO: Is renderEntitiesSorted() better than not sorting and is its implementation correct?
	// It feels smoother
	private void renderEntitiesSorted() {
		// Gdx.gl.glClearColor(0.6f, 0, 0, 1);
		// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Use a map so that each sprite is categorised by the texture it uses
		// TODO: Order of textures varies each time game is played
		HashMap<Texture, ArrayList<Sprite>> textures = new HashMap<Texture, ArrayList<Sprite>>();

		// Iterate through the entities
		// Filter out the ones that aren't on the screen
		// Put them into the map based on texture
		Set<Integer> entities = entityManager.getEntitiesWithComponent(SpriteComponent.class);
		for (int entity : entities) {
			Sprite sprite = entityManager.getComponent(entity, SpriteComponent.class).sprite;
			if (isSpriteOnScreen(sprite)) {
				Texture texture = sprite.getTexture();
				ArrayList<Sprite> sprites = textures.get(texture);
				if (sprites == null) {
					sprites = new ArrayList<Sprite>();
				}
				sprites.add(sprite);
				textures.put(texture, sprites);
			}
		}

		// Iterate through the map of texture to sprites
		// Bind the texture, then render each sprite
		for (Entry<Texture, ArrayList<Sprite>> entry : textures.entrySet()) {
			Texture texture = entry.getKey();

			Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
			batch.begin();
			batch.disableBlending();
			texture.bind();
			for (Sprite sprite : entry.getValue()) {

				sprite.draw(batch);
			}
			batch.end();
		}
	}


	private boolean isSpriteOnScreen(Sprite sprite) {
		return true;
	}
}
