package com.tealduck.game.system;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.Tag;
import com.tealduck.game.collision.AABB;
import com.tealduck.game.collision.Circle;
import com.tealduck.game.collision.CollisionShape;
import com.tealduck.game.component.CollisionComponent;
import com.tealduck.game.component.SpriteComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.GameSystem;
import com.tealduck.game.world.World;


@SuppressWarnings("unused")
public class WorldRenderSystem extends GameSystem {
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private Batch batch;

	private World world;

	private final int mapWidth;
	private final int mapHeight;
	private final int tileWidth;
	private final int tileHeight;

	private float unitScale;

	private final int[] wallLayer = new int[] { 0 };

	private int cornerSize;
	private int halfCornerSize;
	private Pixmap cornerPixmap;
	private Pixmap collideTilePixmap;
	private Texture cornerTexture;
	private Texture collideTileTexture;


	public WorldRenderSystem(EntityEngine entityEngine, World world, OrthographicCamera camera) {
		super(entityEngine);

		this.world = world;
		TiledMap tiledMap = world.getTiledMap();
		unitScale = 1 / 1;
		renderer = new OrthogonalTiledMapRenderer(tiledMap, unitScale);
		this.camera = camera;

		batch = renderer.getBatch();
		batch.disableBlending();

		mapWidth = world.getMapWidth();
		mapHeight = world.getMapHeight();
		tileWidth = world.getTileWidth();
		tileHeight = world.getTileHeight();

		cornerSize = 8;
		halfCornerSize = cornerSize / 2;

		cornerPixmap = new Pixmap(cornerSize, cornerSize, Format.RGBA8888);
		cornerPixmap.setColor(Color.RED);
		cornerPixmap.fill();
		cornerTexture = new Texture(cornerPixmap);

		collideTilePixmap = new Pixmap(tileWidth, tileHeight, Format.RGBA8888);
		collideTilePixmap.setColor(new Color(1f, 0, 0, 0.5f));
		collideTilePixmap.fill();
		collideTileTexture = new Texture(collideTilePixmap);

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


	ShapeRenderer shapeRenderer = new ShapeRenderer();


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
		try {
			centerCameraToEntity(getEntityTagManager().getEntity(Tag.PLAYER));
		} catch (NullPointerException e) {
		}
		clampCamera();
		camera.update();
		renderer.setView(camera);
		renderer.render(wallLayer);
		shapeRenderer.setProjectionMatrix(camera.combined);

		boolean useSortedRendering = false;
		if (useSortedRendering) {
			renderEntitiesSorted();
		} else {
			batch.begin();
			batch.enableBlending();
			// batch.disableBlending();

			Set<Integer> entities = entityManager.getEntitiesWithComponent(SpriteComponent.class);

			for (int entity : entities) {
				Sprite sprite = entityManager.getComponent(entity, SpriteComponent.class).sprite;
				if (isSpriteOnScreen(sprite)) {
					sprite.draw(batch);

				}
			}

			batch.end();

			Gdx.gl.glEnable(GL20.GL_BLEND);
			Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
			shapeRenderer.begin(ShapeType.Filled);

			entities = entityManager.getEntitiesWithComponent(CollisionComponent.class);
			for (int entity : entities) {
				CollisionComponent collisionComponent = entityManager.getComponent(entity,
						CollisionComponent.class);
				CollisionShape shape = collisionComponent.collisionShape;

				shapeRenderer.setColor(0, 1, 0, 0.5f);
				AABB aabb = shape.getAABB();
				shapeRenderer.rect(aabb.getLeft(), aabb.getBottom(), aabb.getWidth(), aabb.getHeight());

				shapeRenderer.setColor(1, 0, 0, 0.5f);
				if (shape instanceof Circle) {
					Circle circle = (Circle) shape;
					shapeRenderer.circle(circle.getCenterX(), circle.getCenterY(),
							circle.getRadius());
				}

				Vector2 bottomLeft = aabb.getBottomLeft();
				int xTile = world.xPixelToTile(bottomLeft.x);
				int yTile = world.yPixelToTile(bottomLeft.y);
				float tileSize = 64;

				if (world.isTileCollidable(xTile, yTile)) {
					shapeRenderer.rect(world.xTileToPixel(xTile), world.yTileToPixel(yTile),
							tileSize, tileSize);
				}
				if (world.isTileCollidable(xTile + 1, yTile)) {
					shapeRenderer.rect(world.xTileToPixel(xTile + 1), world.yTileToPixel(yTile),
							tileSize, tileSize);
				}
				if (world.isTileCollidable(xTile, yTile + 1)) {
					shapeRenderer.rect(world.xTileToPixel(xTile), world.yTileToPixel(yTile + 1),
							tileSize, tileSize);
				}
				if (world.isTileCollidable(xTile + 1, yTile + 1)) {
					shapeRenderer.rect(world.xTileToPixel(xTile + 1), world.yTileToPixel(yTile + 1),
							tileSize, tileSize);
				}

			}
			shapeRenderer.end();
			Gdx.gl.glDisable(GL20.GL_BLEND);
		}
	}


	boolean printed = false;


	// TODO: isSpriteOnScreen
	private boolean isSpriteOnScreen(Sprite sprite) {
		return true;
	}

	// float x = sprite.getX();
	// float y = sprite.getY();
	// float w = sprite.getWidth();
	// float h = sprite.getHeight();
	// w -= 2;
	// h -= 2;
	//
	// if (!printed) {
	// printed = true;
	// System.out.println("X: " + x + "; Y: " + y + "; W: " + w + "; H: " + h);
	// }
	//
	// Vector2 bottomLeftTile = world.pixelToTile(x, y);
	// // Vector2 bottomRight = world.tileToPixel(world.pixelToTile(x + w, y));
	// // Vector2 topLeft = world.tileToPixel(world.pixelToTile(x, y + h));
	// Vector2 topRightTile = world.pixelToTile(x + w, y + h);
	//
	// Vector2 bottomLeftPixel = world.tileToPixel(bottomLeftTile);
	// Vector2 topRightPixel = world.tileToPixel(topRightTile);
	//
	// int leftTile = (int) bottomLeftTile.x;
	// int rightTile = (int) topRightTile.x;
	// int bottomTile = (int) bottomLeftTile.y;
	// int topTile = (int) topRightTile.y;
	//
	// int leftPixel = (int) bottomLeftPixel.x;
	// int rightPixel = (int) topRightPixel.x;
	// int bottomPixel = (int) bottomLeftPixel.y;
	// int topPixel = (int) topRightPixel.y;
	//
	// if (world.isTileCollidable(leftTile, bottomTile)) {
	// batch.draw(collideTileTexture, leftPixel, bottomPixel);
	// }
	// if (world.isTileCollidable(rightTile, bottomTile)) {
	// batch.draw(collideTileTexture, rightPixel, bottomPixel);
	// }
	// if (world.isTileCollidable(leftTile, topTile)) {
	// batch.draw(collideTileTexture, leftPixel, topPixel);
	// }
	// if (world.isTileCollidable(rightTile, topTile)) {
	// batch.draw(collideTileTexture, rightPixel, topPixel);
	// }

	// batch.draw(cornerTexture, leftPixel - halfCornerSize,
	// bottomPixel - halfCornerSize);
	// batch.draw(cornerTexture, rightPixel - halfCornerSize,
	// bottomPixel - halfCornerSize);
	// batch.draw(cornerTexture, leftPixel - halfCornerSize,
	// topPixel - halfCornerSize);
	// batch.draw(cornerTexture, rightPixel - halfCornerSize,
	// topPixel - halfCornerSize);

	// Texture texture = sprite.getTexture();
	//
	// if (Gdx.app.getInput().isKeyPressed(Keys.R)) {
	// rotation += 10 * deltaTime;
	// }
	//
	// rotation = sprite.getRotation();
	//
	//
	// batch.draw(texture, x, y, w / 2, h / 2, w, h, 1, 1, rotation, 0, 0, (int) w,
	// (int) h, false, false);


	// Possibly sort all entities so that ones with the same texture get rendered together
	// Faster for the GPU to render lots of the same texture, then lots of a different
	// Rather than constantly binding a different texture
	// Possible implementation below
	private void renderEntitiesSorted() {
		EntityManager entityManager = getEntityManager();
		// Gdx.gl.glClearColor(0.6f, 0, 0, 1);
		// Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Use a map so that each sprite is categorised by the texture it uses
		// Order of textures varies each time game is played
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
