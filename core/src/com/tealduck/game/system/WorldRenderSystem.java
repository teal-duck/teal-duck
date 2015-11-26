package com.tealduck.game.system;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.tealduck.game.Tag;
import com.tealduck.game.component.SpriteComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.GameSystem;


public class WorldRenderSystem extends GameSystem {
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private Batch batch;

	private float mapWidth;
	private float mapHeight;
	private float tileWidth;
	private float tileHeight;

	private float unitScale;


	public WorldRenderSystem(EntityEngine entityEngine, TiledMap tiledMap) { // OrthogonalTiledMapRenderer renderer,
		// OrthographicCamera camera) {
		super(entityEngine);

		unitScale = 1 / 1;
		renderer = new OrthogonalTiledMapRenderer(tiledMap, unitScale);
		camera = new OrthographicCamera();

		// this.renderer = renderer;
		// this.camera = camera;

		batch = renderer.getBatch();
		batch.disableBlending();

		MapProperties prop = tiledMap.getProperties();
		mapWidth = prop.get("width", Integer.class);
		mapHeight = prop.get("height", Integer.class);
		tileWidth = prop.get("tilewidth", Integer.class);
		tileHeight = prop.get("tileheight", Integer.class);
	}


	public void resizeCamera(int windowWidth, int windowHeight) {
		camera.setToOrtho(false, windowWidth * renderer.getUnitScale(), windowHeight * renderer.getUnitScale());
		camera.update();
	}


	/**
	 * Translates the camera so that it is centered on an entity.
	 *
	 * @param entityId
	 *                id of the entity to center on
	 */
	private void centerCameraToEntity(int entityId) {
		EntityManager entityManager = getEntityManager();

		if (entityManager.entityHasComponent(entityId, SpriteComponent.class)) {
			Sprite entitySprite = entityManager.getComponent(entityId, SpriteComponent.class).sprite;

			camera.position.set(entitySprite.getX() + (entitySprite.getWidth() / 2),
					entitySprite.getY() + (entitySprite.getHeight() / 2), 0);
		}
	}


	/**
	 *
	 */
	private void clampCamera() {
		float viewportWidth = camera.viewportWidth;
		float viewportHeight = camera.viewportHeight;

		float cameraLeft = camera.position.x - (viewportWidth / 2);
		float cameraRight = camera.position.x + (viewportWidth / 2);
		float cameraTop = camera.position.y + (viewportHeight / 2);
		float cameraBottom = camera.position.y - (viewportHeight / 2);

		if (cameraLeft < 0) {
			camera.position.x = viewportWidth / 2;
		}
		if (cameraBottom < 0) {
			camera.position.y = viewportHeight / 2;
		}

		if (cameraRight > (mapWidth * tileWidth)) {
			camera.position.x = (mapWidth * tileWidth) - (viewportWidth / 2);
		}

		if (cameraTop > (mapHeight * tileHeight)) {
			camera.position.y = (mapHeight * tileHeight) - (viewportHeight / 2);
		}
	}


	/**
	 * Redraws all entities with sprites to the screen.
	 *
	 * @param deltaTime
	 *                time elapsed since last update
	 */
	@Override
	public void update(float deltaTime) {
		EntityManager entityManager = getEntityManager();
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		centerCameraToEntity(getEntityTagManager().getEntity(Tag.PLAYER));
		clampCamera();
		camera.update();
		renderer.setView(camera);
		renderer.render();

		boolean useSortedRendering = false;
		if (useSortedRendering) {
			renderEntitiesSorted();
		} else {
			batch.begin();
			batch.disableBlending();

			Set<Integer> entities = entityManager.getEntitiesWithComponent(SpriteComponent.class);

			for (int entity : entities) {
				Sprite sprite = entityManager.getComponent(entity, SpriteComponent.class).sprite;
				if (isSpriteOnScreen(sprite)) {
					sprite.draw(batch);
				}
			}

			batch.end();
		}
	}


	private boolean isSpriteOnScreen(Sprite sprite) {
		return true;
	}

	// TODO: Possibly sort all entities so that ones with the same texture get rendered together
	// Faster for the GPU to render lots of the same texture, then lots of a different
	// Rather than constantly binding a different texture
	// Possible implementation below


	// TODO: Is renderEntitiesSorted() better than not sorting and is its implementation correct?
	// It feels smoother
	private void renderEntitiesSorted() {
		EntityManager entityManager = getEntityManager();
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
}
