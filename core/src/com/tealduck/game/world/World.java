package com.tealduck.game.world;


import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.Tag;
import com.tealduck.game.collision.Circle;
import com.tealduck.game.component.CollisionComponent;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PatrolRouteComponent;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.component.SpriteComponent;
import com.tealduck.game.component.UserInputComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.input.Action;
import com.tealduck.game.input.ControlMap;
import com.tealduck.game.input.ControllerBindingType;
import com.tealduck.game.input.controller.ControllerHelper;
import com.tealduck.game.input.controller.PS4;


public class World {
	private final EntityEngine entityEngine;
	private final TiledMap tiledMap;

	private final int mapWidth;
	private final int mapHeight;
	private final int tileWidth;
	private final int tileHeight;

	private float playerSpeed = 2500.0f;
	private float playerSprint = 1.5f;
	private float enemySpeed = 2000.0f;


	public World(EntityEngine entityEngine, TiledMap tiledMap) {
		this.entityEngine = entityEngine;
		this.tiledMap = tiledMap;

		MapProperties prop = tiledMap.getProperties();
		mapWidth = prop.get("width", Integer.class);
		mapHeight = prop.get("height", Integer.class);
		tileWidth = prop.get("tilewidth", Integer.class);
		tileHeight = prop.get("tileheight", Integer.class);
	}


	public void loadEntities(Texture duckTexture, Texture enemyTexture) {
		int playerId = -1;

		MapLayer entityLayer = tiledMap.getLayers().get("Entities");
		MapObjects objects = entityLayer.getObjects();

		for (MapObject object : objects) {
			// System.out.println(object.getName());

			if (object instanceof TiledMapTileMapObject) {
				TiledMapTileMapObject t = (TiledMapTileMapObject) object;
				String name = t.getName();
				if (name == null) {
					System.out.println("null name");
					continue;
				}

				float x = t.getX();
				float y = t.getY();

				if (name.equals("Player")) {
					if (playerId != -1) {
						throw new IllegalArgumentException("More than 1 player");
					}

					playerId = createPlayer(entityEngine, duckTexture, new Vector2(x, y));

				} else if (name.equals("Enemy") && t.getProperties().containsKey("patrolRoute")) {
					String routeName = t.getProperties().get("patrolRoute", String.class);
					PatrolRouteComponent patrolRouteComponent = new PatrolRouteComponent(
							findPatrolRoute(routeName));
					createPatrollingEnemy(entityEngine, enemyTexture, new Vector2(x, y),
							patrolRouteComponent);

				} else if (name.equals("Enemy")) {
					createEnemy(entityEngine, enemyTexture, new Vector2(x, y));
				}
			}
		}
	}


	public ArrayList<Vector2> findPatrolRoute(String routeName) {
		MapLayer lineLayer = tiledMap.getLayers().get("Lines");
		MapObjects lineObjects = lineLayer.getObjects();
		ArrayList<Vector2> worldVertices = new ArrayList<Vector2>();

		for (MapObject object : lineObjects) {
			assert (object instanceof PolylineMapObject);
			PolylineMapObject polylineMapObject = (PolylineMapObject) object;
			String name = polylineMapObject.getName();

			if (name.equals(routeName)) {
				Polyline polyline = polylineMapObject.getPolyline();
				float[] polylineVertices = polyline.getTransformedVertices();
				for (int i = 0; i < (polylineVertices.length / 2); i++) {
					worldVertices.add(new Vector2(polylineVertices[i * 2],
							polylineVertices[(i * 2) + 1]));
				}
				System.out.println(worldVertices);
				return worldVertices;
			}
		}

		return worldVertices;
	}


	/**
	 * @param entityManager
	 * @param entityTagManager
	 * @param texture
	 * @param location
	 * @return
	 */
	private int createPlayer(EntityEngine entityEngine, Texture texture, Vector2 location) {
		EntityManager entityManager = entityEngine.getEntityManager();
		int playerId = entityManager.createEntityWithTag(entityEngine.getEntityTagManager(), Tag.PLAYER);

		entityManager.addComponent(playerId, new SpriteComponent(texture));
		entityManager.addComponent(playerId, new PositionComponent(location));

		// float maxSpeed = 2000.0f; // 100f; // 200.0f;
		// float sprintScale = 2f;
		// float friction = 0.8f; // 0.5f;
		entityManager.addComponent(playerId,
				new MovementComponent(new Vector2(0, 0), playerSpeed, playerSprint)); // , friction));

		ControlMap controls = new ControlMap();

		controls.addKeyForAction(Action.RIGHT, Keys.D, Keys.RIGHT);
		controls.addKeyForAction(Action.LEFT, Keys.A, Keys.LEFT);
		controls.addKeyForAction(Action.UP, Keys.W, Keys.UP);
		controls.addKeyForAction(Action.DOWN, Keys.S, Keys.DOWN);
		controls.addKeyForAction(Action.SPRINT, Keys.SHIFT_LEFT, Keys.SHIFT_RIGHT);

		float deadzone = 0.2f;
		controls.addControllerForAction(Action.RIGHT, ControllerBindingType.AXIS_POSITIVE, PS4.AXIS_LEFT_X,
				deadzone);
		controls.addControllerForAction(Action.LEFT, ControllerBindingType.AXIS_NEGATIVE, PS4.AXIS_LEFT_X,
				deadzone);
		controls.addControllerForAction(Action.UP, ControllerBindingType.AXIS_NEGATIVE, PS4.AXIS_LEFT_Y,
				deadzone);
		controls.addControllerForAction(Action.DOWN, ControllerBindingType.AXIS_POSITIVE, PS4.AXIS_LEFT_Y,
				deadzone);
		controls.addControllerForAction(Action.SPRINT, ControllerBindingType.BUTTON, PS4.BUTTON_R1);

		UserInputComponent uic = new UserInputComponent(controls, ControllerHelper.getFirstControllerOrNull());
		Gdx.app.log("Controls", uic.controls.toString());
		entityManager.addComponent(playerId, uic);

		float radius = 28;
		Vector2 offsetFromPosition = new Vector2(32, 32);
		Circle circle = new Circle(location.cpy().add(offsetFromPosition), radius);
		entityManager.addComponent(playerId, new CollisionComponent(circle, offsetFromPosition));

		return playerId;
	}


	/**
	 * @param entityManager
	 * @param texture
	 * @param location
	 * @return
	 */
	private int createEnemy(EntityEngine entityEngine, Texture texture, Vector2 location) {
		EntityManager entityManager = entityEngine.getEntityManager();
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
	@SuppressWarnings("unused")
	private int createPathfindingEnemy(EntityEngine entityEngine, Texture texture, Vector2 location) {
		EntityManager entityManager = entityEngine.getEntityManager();
		int enemyId = createEnemy(entityEngine, texture, location);
		entityManager.addComponent(enemyId, new MovementComponent(new Vector2(0, 0), enemySpeed));
		// entityManager.addComponent(enemyId, new PathfindingComponent(targetId));
		return enemyId;
	}


	private int createPatrollingEnemy(EntityEngine entityEngine, Texture texture, Vector2 location,
			PatrolRouteComponent patrolRouteComponent) {
		EntityManager entityManager = entityEngine.getEntityManager();
		int enemyId = createEnemy(entityEngine, texture, location);
		entityManager.addComponent(enemyId, patrolRouteComponent);
		entityManager.addComponent(enemyId, new MovementComponent(new Vector2(0, 0), enemySpeed));
		return enemyId;
	}


	/**
	 * If tile is out of bounds, returns true (assumes collidable).
	 *
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isTileCollidable(int x, int y) {
		if ((x < 0) || (y < 0) || (x > mapWidth) || (y > mapHeight)) {
			return true;
		} else {

			TiledMapTileLayer layer = (TiledMapTileLayer) tiledMap.getLayers().get("Collision");

			Cell cell = layer.getCell(x, y);
			if (cell == null) {
				return false;
			}

			String collidableProperty = cell.getTile().getProperties().get("collidable", String.class);
			return ((collidableProperty != null) && collidableProperty.equals("true"));
		}
	}


	public int xPixelToTile(float pixelX) {
		return MathUtils.floor(pixelX / tileWidth);
	}


	public int yPixelToTile(float pixelY) {
		return MathUtils.floor(pixelY / tileHeight);
	}


	public float xPixelToTileExact(float pixelX) {
		return pixelX / tileWidth;
	}


	public float yPixelToTileExact(float pixelY) {
		return pixelY / tileHeight;
	}


	public float xTileToPixel(float tileX) {
		return tileX * tileWidth;
	}


	public float yTileToPixel(float tileY) {
		return tileY * tileHeight;
	}


	public Vector2 pixelToTile(float x, float y) {
		return new Vector2(xPixelToTile(x), yPixelToTile(y));
	}


	public Vector2 pixelToTileExact(float x, float y) {
		return new Vector2(xPixelToTileExact(x), yPixelToTileExact(y));
	}


	public Vector2 tileToPixel(float x, float y) {
		return new Vector2(xTileToPixel(x), yTileToPixel(y));
	}


	// TODO: pixelToTile + pixelToTilExact + tileToPixel reuse vector2 instead of allocating new
	public Vector2 pixelToTile(Vector2 pixel) {
		if (pixel == null) {
			throw new IllegalArgumentException("pixel is null");
		}
		return new Vector2(xPixelToTile(pixel.x), yPixelToTile(pixel.y));
	}


	public Vector2 pixelToTileExact(Vector2 pixel) {
		if (pixel == null) {
			throw new IllegalArgumentException("pixel is null");
		}
		return new Vector2(xPixelToTileExact(pixel.x), yPixelToTileExact(pixel.y));
	}


	public Vector2 tileToPixel(Vector2 tile) {
		if (tile == null) {
			throw new IllegalArgumentException("tile is null");
		}
		return new Vector2(xTileToPixel(tile.x), yTileToPixel(tile.y));
	}


	public boolean isPixelCollidable(float x, float y) {
		return isTileCollidable(xPixelToTile(x), yPixelToTile(y));
	}


	public boolean isPixelCollidable(Vector2 pixel) {
		return isPixelCollidable(pixel.x, pixel.y);
	}


	public TiledMap getTiledMap() {
		return tiledMap;
	}


	public EntityEngine getEntityEngine() {
		return entityEngine;
	}


	public int getMapWidth() {
		return mapWidth;
	}


	public int getMapHeight() {
		return mapHeight;
	}


	public int getTileWidth() {
		return tileWidth;
	}


	public int getTileHeight() {
		return tileHeight;
	}
}
