package com.tealduck.game.system;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.AssetLocations;
import com.tealduck.game.Tag;
import com.tealduck.game.TextureMap;
import com.tealduck.game.collision.AABB;
import com.tealduck.game.collision.Circle;
import com.tealduck.game.collision.CollisionShape;
import com.tealduck.game.component.CollisionComponent;
import com.tealduck.game.component.HealthComponent;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PickupComponent;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.component.SpriteComponent;
import com.tealduck.game.component.ViewconeComponent;
import com.tealduck.game.component.WeaponComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.GameSystem;
import com.tealduck.game.world.EntityConstants;
import com.tealduck.game.world.World;


/**
 *
 */
public class WorldRenderSystem extends GameSystem {
	// http://www.alcove-games.com/opengl-es-2-tutorials/lightmap-shader-fire-effect-glsl/
	// http://gamedev.stackexchange.com/questions/104785/2d-smooth-lighting-with-shadows-for-a-tile-based-game
	// http://ncase.me/sight-and-light/
	// http://www.gamedev.net/page/reference/index.html/_/technical/graphics-programming-and-theory/dynamic-2d-soft-shadows-r2032

	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;
	private Batch batch;

	private Texture coneTexture;
	private FrameBuffer fbo;
	private ShaderProgram renderLightShader;
	private ShaderProgram renderWorldShader;

	private final String vertexShader = Gdx.files.internal("shaders/vertexShader.glsl").readString();
	private final String renderLightPixelShader = Gdx.files.internal("shaders/renderLightPixelShader.glsl")
			.readString();
	private final String renderWorldPixelShader = Gdx.files.internal("shaders/renderWorldPixelShader.glsl")
			.readString();

	private boolean debugPatrol = false;
	private boolean debugCollision = false;

	private World world;

	private final int mapWidth;
	private final int mapHeight;
	private final int tileWidth;
	private final int tileHeight;

	private float unitScale;

	// Only render the floor and wall layers, not collision or objects
	private final int[] mapRenderLayers = new int[] { 0, 1 };

	private ShapeRenderer shapeRenderer;

	private float pickupTime = 0;
	private float pickupRotation = 0;
	private float pickupY = 0;

	private float healthBarWidth = 32;
	private float healthBarHeight = 4;

	private Texture reloadingTexture;


	public WorldRenderSystem(EntityEngine entityEngine, World world, TextureMap textureMap) {
		super(entityEngine);

		this.world = world;
		TiledMap tiledMap = world.getTiledMap();
		unitScale = 1 / 1;
		renderer = new OrthogonalTiledMapRenderer(tiledMap, unitScale);
		camera = new OrthographicCamera();

		batch = renderer.getBatch();
		batch.enableBlending();

		mapWidth = world.getMapWidth();
		mapHeight = world.getMapHeight();
		tileWidth = world.getTileWidth();
		tileHeight = world.getTileHeight();

		shapeRenderer = new ShapeRenderer();

		coneTexture = textureMap.getTexture(AssetLocations.CONE_LIGHT);

		ShaderProgram.pedantic = false;
		renderLightShader = new ShaderProgram(vertexShader, renderLightPixelShader);
		renderWorldShader = new ShaderProgram(vertexShader, renderWorldPixelShader);

		renderWorldShader.begin();
		renderWorldShader.setUniformi("u_lightmap", 1);
		renderWorldShader.setUniformf("ambientColour", EntityConstants.AMBIENT_COLOUR.x,
				EntityConstants.AMBIENT_COLOUR.y, EntityConstants.AMBIENT_COLOUR.z,
				EntityConstants.AMBIENT_INTENSITY);
		renderWorldShader.end();

		reloadingTexture = textureMap.getTexture(AssetLocations.RELOADING);
	}


	/**
	 * @return
	 */
	public OrthographicCamera getCamera() {
		return camera;
	}


	/**
	 * Resizes the camera, recreates the frame buffer and sends the new resolution to the shader.
	 *
	 * @param windowWidth
	 * @param windowHeight
	 */
	public void resizeCamera(int windowWidth, int windowHeight) {
		camera.setToOrtho(false, windowWidth * renderer.getUnitScale(), windowHeight * renderer.getUnitScale());
		camera.update();

		fbo = new FrameBuffer(Format.RGBA8888, windowWidth, windowHeight, false);

		renderWorldShader.begin();
		renderWorldShader.setUniformf("resolution", windowWidth, windowHeight);
		renderWorldShader.end();
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
	 * Clamp the camera to be within the bounds of the map
	 */
	private void clampCamera() {
		float viewportWidth = camera.viewportWidth;
		float viewportHeight = camera.viewportHeight;

		float cameraLeft = camera.position.x - (viewportWidth / 2);
		float cameraRight = camera.position.x + (viewportWidth / 2);
		float cameraTop = camera.position.y + (viewportHeight / 2);
		float cameraBottom = camera.position.y - (viewportHeight / 2);

		if (cameraRight > (mapWidth * tileWidth)) {
			camera.position.x = (mapWidth * tileWidth) - (viewportWidth / 2);
		}

		if (cameraLeft < 0) {
			camera.position.x = viewportWidth / 2;
		}

		if (cameraBottom < 0) {
			camera.position.y = viewportHeight / 2;
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
		updateSprites(deltaTime);
		renderLightsToFrameBuffer(deltaTime);
		clearScreen();
		updateCamera();
		renderWorld();
		renderEntities(deltaTime);
		renderHealthBars();
	}


	/**
	 * For each entity that has a position and sprite, updates the position in the sprite to be the same as the
	 * position. Also rotates the pickup sprites.
	 *
	 * @param deltaTime
	 */
	private void updateSprites(float deltaTime) {
		EntityManager entityManager = getEntityManager();

		pickupRotation += -deltaTime * (360 / EntityConstants.PICKUP_FULL_ROTATION_TIME);

		pickupTime += deltaTime;
		pickupY += 1 * Math.sin(pickupTime * 5);

		Set<Integer> entities = entityManager.getEntitiesWithComponent(SpriteComponent.class);
		for (int entity : entities) {
			Sprite sprite = entityManager.getComponent(entity, SpriteComponent.class).sprite;
			sprite.setOrigin(sprite.getWidth() / 2, sprite.getHeight() / 2);

			if (entityManager.entityHasComponent(entity, PositionComponent.class)) {
				PositionComponent positionComponent = entityManager.getComponent(entity,
						PositionComponent.class);
				Vector2 position = positionComponent.position;
				Vector2 lookAt = positionComponent.lookAt;

				float x = position.x;
				float y = position.y;

				boolean hasPickupComponent = entityManager.entityHasComponent(entity,
						PickupComponent.class);
				if (hasPickupComponent) {
					lookAt.setAngle(pickupRotation);
					y += pickupY;
				}
				y = position.y;

				sprite.setPosition(x, y);
				sprite.setRotation(lookAt.angle());
			}
		}

	}


	/**
	 * @param deltaTime
	 */
	@SuppressWarnings("unchecked")
	private void renderLightsToFrameBuffer(float deltaTime) {
		EntityManager entityManager = getEntityManager();

		fbo.begin();
		clearScreen();
		batch.setProjectionMatrix(camera.combined);
		batch.setShader(renderLightShader);
		batch.begin();
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setColor(1f, 1f, 1f, 1f);

		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE);

		int lightSize = 512;
		int halfLightSize = lightSize / 2;
		float lightScale = 0.5f;

		float coneOriginX = halfLightSize;
		float coneOriginY = lightSize;

		Set<Integer> entities = entityManager.getEntitiesWithComponents(PositionComponent.class,
				ViewconeComponent.class);
		for (int entity : entities) {
			PositionComponent positionComponent = entityManager.getComponent(entity,
					PositionComponent.class);
			ViewconeComponent viewconeComponent = entityManager.getComponent(entity,
					ViewconeComponent.class);
			Vector2 position = positionComponent.position;
			Vector2 lookAt = positionComponent.lookAt;
			float length = viewconeComponent.length;

			float x = position.x;
			float y = position.y;
			float angle = lookAt.angle();

			float xLength = 2 * length * (float) Math.tan(viewconeComponent.getHalfFovRadians());

			float scaleX = xLength / lightSize;
			float scaleY = length / lightSize;

			float drawX = (x + 32f) - halfLightSize;
			float drawY = (y + 32f) - lightSize;
			batch.draw(coneTexture, // texture
					drawX, // x
					drawY, // y
					coneOriginX, // origin x
					coneOriginY, // origin y
					lightSize, // width
					lightSize, // height
					scaleX, // scale x
					scaleY, // scale y
					angle + 90f, // rotation
					0, 0, lightSize, lightSize, // src
					false, false // flip
			);

		}

		// Render muzzle flashes as smaller cones
		lightScale = 0.1f;
		entities = getEntityManager().getEntitiesWithComponents(PositionComponent.class, WeaponComponent.class);
		for (int entity : entities) {
			WeaponComponent weaponComponent = entityManager.getComponent(entity, WeaponComponent.class);
			if (weaponComponent.justFired) {
				weaponComponent.justFired = false;
				Vector2 position = weaponComponent.fireLocation;
				Vector2 direction = weaponComponent.fireDirection;

				float x = position.x;
				float y = position.y;
				float angle = direction.angle();

				batch.draw(coneTexture, (x + 32f) - halfLightSize, (y + 32f) - lightSize, coneOriginX,
						coneOriginY, lightSize, lightSize, lightScale, lightScale, angle + 90f,
						0, 0, lightSize, lightSize, false, false);
			}
		}

		batch.end();
		batch.setColor(Color.WHITE);
		fbo.end();
	}


	/**
	 *
	 */
	private void clearScreen() {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}


	/**
	 *
	 */
	private void updateCamera() {
		try {
			centerCameraToEntity(getEntityTagManager().getEntity(Tag.PLAYER));
		} catch (NullPointerException e) {
		}

		clampCamera();
		camera.update();
	}


	/**
	 *
	 */
	private void renderWorld() {
		batch.setShader(renderWorldShader);
		batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

		renderer.setView(camera);
		fbo.getColorBufferTexture().bind(1);
		coneTexture.bind(0);
		renderer.render(mapRenderLayers);
		shapeRenderer.setProjectionMatrix(camera.combined);
	}


	/**
	 * @param deltaTime
	 */
	private void renderEntities(float deltaTime) {
		if (debugPatrol) {
			renderPatrolRoutes();
		}

		renderAllEntities(deltaTime);

		if (debugCollision) {
			renderCollisionOverlay();
		}
	}


	/**
	 *
	 */
	private void renderPatrolRoutes() {
		HashMap<String, ArrayList<Vector2>> patrolRoutes = world.getPatrolRoutes();
		for (ArrayList<Vector2> patrolRoute : patrolRoutes.values()) {
			shapeRenderer.begin(ShapeType.Line);
			shapeRenderer.setColor(Color.GRAY);

			int routeLength = patrolRoute.size();
			for (int i = 0; i < routeLength; i += 1) {
				Vector2 p0 = patrolRoute.get(i);
				Vector2 p1 = patrolRoute.get((i + 1) % routeLength);
				shapeRenderer.line(p0, p1);
			}
			shapeRenderer.end();

		}
	}


	/**
	 * @param deltaTime
	 */
	private void renderAllEntities(float deltaTime) {
		EntityManager entityManager = getEntityManager();

		batch.begin();
		batch.enableBlending();

		Set<Integer> entities = entityManager.getEntitiesWithComponent(SpriteComponent.class);
		for (int entity : entities) {
			SpriteComponent spriteComponent = entityManager.getComponent(entity, SpriteComponent.class);

			// Update the animation
			// If the entity can move, only update when they are moving
			if (entityManager.entityHasComponent(entity, MovementComponent.class)) {
				MovementComponent movementComponent = entityManager.getComponent(entity,
						MovementComponent.class);
				if (movementComponent.velocity.len2() > 1) {
					// Speed up animation when sprinting
					float sprintScale = movementComponent.sprinting ? movementComponent.sprintScale
							: 1;
					spriteComponent.stateTime += deltaTime * sprintScale;
				}
			} else {
				spriteComponent.stateTime += deltaTime;
			}
			spriteComponent.setSpriteToAnimationFrame();

			Sprite sprite = spriteComponent.sprite;
			if (isSpriteOnScreen(sprite)) {
				sprite.draw(batch);
			}
		}

		batch.end();
	}


	/**
	 *
	 */
	@SuppressWarnings("unchecked")
	private void renderHealthBars() {
		EntityManager entityManager = getEntityManager();
		shapeRenderer.begin(ShapeType.Filled);

		Set<Integer> entities = entityManager.getEntitiesWithComponents(SpriteComponent.class,
				HealthComponent.class);
		for (int entity : entities) {
			if (getEntityTagManager().doesEntityIdHaveTag(entity, Tag.PLAYER)) {
				continue;
			}

			SpriteComponent spriteComponent = entityManager.getComponent(entity, SpriteComponent.class);
			HealthComponent healthComponent = entityManager.getComponent(entity, HealthComponent.class);

			Sprite sprite = spriteComponent.sprite;
			int health = healthComponent.health;
			int maxHealth = healthComponent.maxHealth;

			float spriteX = sprite.getX();
			float spriteY = sprite.getY();
			float spriteWidth = sprite.getWidth();
			float spriteHeight = sprite.getHeight();

			float healthBarX = (spriteX + (spriteWidth / 2)) - (healthBarWidth / 2);
			float healthBarY = (spriteY + spriteHeight) - healthBarHeight;

			shapeRenderer.setColor(Color.RED);
			shapeRenderer.rect(healthBarX, healthBarY, healthBarWidth, healthBarHeight);

			float greenBarWidth = (healthBarWidth / maxHealth) * health;
			shapeRenderer.setColor(Color.GREEN);
			shapeRenderer.rect(healthBarX, healthBarY, greenBarWidth, healthBarHeight);
		}
		shapeRenderer.end();

		// Reloading ! above head
		batch.begin();
		entities = entityManager.getEntitiesWithComponents(SpriteComponent.class, WeaponComponent.class);
		for (int entity : entities) {
			if (!entityManager.getComponent(entity, WeaponComponent.class).isReloading()) {
				continue;
			}
			SpriteComponent spriteComponent = entityManager.getComponent(entity, SpriteComponent.class);
			Sprite sprite = spriteComponent.sprite;

			float spriteX = sprite.getX();
			float spriteY = sprite.getY();

			float reloadX = spriteX + 16;
			float reloadY = spriteY + 52;

			batch.draw(reloadingTexture, reloadX, reloadY, 32, 32);
		}
		batch.end();
	}


	/**
	 *
	 */
	private void renderCollisionOverlay() {
		EntityManager entityManager = getEntityManager();

		Gdx.gl.glEnable(GL20.GL_BLEND);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		shapeRenderer.begin(ShapeType.Filled);

		Set<Integer> entities = entityManager.getEntitiesWithComponent(CollisionComponent.class);
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
				shapeRenderer.circle(circle.getCenterX(), circle.getCenterY(), circle.getRadius());
			}

			Vector2 bottomLeft = aabb.getBottomLeft();
			int xTile = world.xPixelToTile(bottomLeft.x);
			int yTile = world.yPixelToTile(bottomLeft.y);
			float tileSize = 64;

			if (world.isTileCollidable(xTile, yTile)) {
				shapeRenderer.rect(world.xTileToPixel(xTile), world.yTileToPixel(yTile), tileSize,
						tileSize);
			}
			if (world.isTileCollidable(xTile + 1, yTile)) {
				shapeRenderer.rect(world.xTileToPixel(xTile + 1), world.yTileToPixel(yTile), tileSize,
						tileSize);
			}
			if (world.isTileCollidable(xTile, yTile + 1)) {
				shapeRenderer.rect(world.xTileToPixel(xTile), world.yTileToPixel(yTile + 1), tileSize,
						tileSize);
			}
			if (world.isTileCollidable(xTile + 1, yTile + 1)) {
				shapeRenderer.rect(world.xTileToPixel(xTile + 1), world.yTileToPixel(yTile + 1),
						tileSize, tileSize);
			}
		}

		shapeRenderer.end();
		Gdx.gl.glDisable(GL20.GL_BLEND);
	}


	/**
	 * @param sprite
	 * @return
	 */
	private boolean isSpriteOnScreen(Sprite sprite) {
		return true;
	}
}
