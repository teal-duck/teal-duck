package com.tealduck.game.world;


import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import com.tealduck.game.EventName;
import com.tealduck.game.Tag;
import com.tealduck.game.collision.Circle;
import com.tealduck.game.collision.Intersection;
import com.tealduck.game.component.CollisionComponent;
import com.tealduck.game.component.DamageComponent;
import com.tealduck.game.component.HealthComponent;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PatrolRouteComponent;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.component.SpriteComponent;
import com.tealduck.game.component.UserInputComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EventManager;
import com.tealduck.game.engine.IEvent;
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

	// TODO: Clean up constants in world
	private final float playerSpeed = 2500.0f;
	private final float playerSprint = 1.8f;
	private final float enemySpeed = 2000.0f;
	private final float playerRadius = 20; // 28;
	private final float enemyRadius = 20; // 30;
	private final float playerBounce = 60000;
	private final int enemyDamage = 1;
	public static final int playerMaxHealth = 2; // 10;

	private HashMap<String, ArrayList<Vector2>> patrolRoutes;


	public World(EntityEngine entityEngine, TiledMap tiledMap) {
		this.entityEngine = entityEngine;
		this.tiledMap = tiledMap;

		MapProperties prop = tiledMap.getProperties();
		mapWidth = prop.get("width", Integer.class);
		mapHeight = prop.get("height", Integer.class);
		tileWidth = prop.get("tilewidth", Integer.class);
		tileHeight = prop.get("tileheight", Integer.class);

		patrolRoutes = new HashMap<String, ArrayList<Vector2>>();
	}


	public void loadEntities(Texture duckTexture, Texture enemyTexture) {
		int playerId = -1;

		MapLayer entityLayer = tiledMap.getLayers().get("Entities");
		MapObjects objects = entityLayer.getObjects();
		EntityManager entityManager = entityEngine.getEntityManager();

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
				} else if (name.equals("Enemy")) {
					int enemyId = createEnemy(entityEngine, enemyTexture, new Vector2(x, y));

					if (t.getProperties().containsKey("patrolRoute")) {
						String routeName = t.getProperties().get("patrolRoute", String.class);
						ArrayList<Vector2> patrolRoute = findPatrolRoute(routeName);
						if (patrolRoute.size() == 0) {
							Gdx.app.log("Patrol", "Route " + routeName + " is empty");
						}
						PatrolRouteComponent patrolRouteComponent = new PatrolRouteComponent(
								patrolRoute);
						entityManager.addComponent(enemyId, patrolRouteComponent);
					}
				}
			}
		}
	}


	public HashMap<String, ArrayList<Vector2>> getPatrolRoutes() {
		return patrolRoutes;
	}


	private ArrayList<Vector2> findPatrolRoute(String routeName) {
		ArrayList<Vector2> preloadedRoute = patrolRoutes.get(routeName);
		if (preloadedRoute != null) {
			return preloadedRoute;
		}

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
				int length = polylineVertices.length / 2;
				for (int i = 0; i < length; i += 1) {
					worldVertices.add(new Vector2(polylineVertices[i * 2],
							polylineVertices[(i * 2) + 1]));
				}

				patrolRoutes.put(routeName, worldVertices);
				Gdx.app.log("Patrol", routeName + " = " + worldVertices.toString());
			}
		}

		return worldVertices;
	}


	private CollisionComponent addEntityCollisionComponent(EntityManager entityManager, int entityId,
			Vector2 entityPosition, float radius) {
		Vector2 offsetFromPosition = new Vector2(32, 32);
		Circle circle = new Circle(entityPosition.cpy().add(offsetFromPosition), radius);
		CollisionComponent collisionComponent = new CollisionComponent(circle, offsetFromPosition);
		entityManager.addComponent(entityId, collisionComponent);
		return collisionComponent;
	}


	private Animation animationFromTexture(Texture texture, int frameWidth, int frameHeight, float frameDuration) {
		int textureWidth = texture.getWidth();
		int textureHeight = texture.getHeight();

		if (((textureWidth % frameWidth) != 0) || ((textureHeight % frameHeight) != 0)) {
			throw new IllegalArgumentException("Texture size isn't exactly divisible into frames");
		}

		int columns = textureWidth / frameWidth;
		int rows = textureHeight / frameHeight;

		TextureRegion[][] textureFrames = TextureRegion.split(texture, frameWidth, frameHeight);
		TextureRegion[] animationFrames = new TextureRegion[columns * rows];

		int frame = 0;
		for (int r = 0; r < rows; r += 1) {
			for (int c = 0; c < columns; c += 1) {
				animationFrames[frame] = textureFrames[r][c];
				frame += 1;
			}
		}

		return new Animation(frameDuration, animationFrames);
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

		entityManager.addComponent(playerId,
				new SpriteComponent(texture, animationFromTexture(texture, 64, 64, 0.2f)));
		entityManager.addComponent(playerId, new PositionComponent(location));
		entityManager.addComponent(playerId,
				new MovementComponent(new Vector2(0, 0), playerSpeed, playerSprint));

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

		addEntityCollisionComponent(entityManager, playerId, location, playerRadius);

		entityManager.addComponent(playerId, new HealthComponent(World.playerMaxHealth));

		EventManager eventManager = entityEngine.getEventManager();
		eventManager.addEvent(playerId, EventName.COLLISION, new IEvent() {
			@Override
			public boolean fire(EntityEngine entityEngine, int sender, int receiver, Object data) {
				// TODO: Maybe change how data is passed to events
				if (!(data instanceof Intersection)) {
					return false;
				}
				Intersection intersection = (Intersection) data;

				EntityManager entityManager = entityEngine.getEntityManager();

				// TODO: Test if the player should bounce
				// TODO: Calculate bounce from mass
				MovementComponent movementComponent = entityManager.getComponent(receiver,
						MovementComponent.class);
				movementComponent.acceleration.add(intersection.normal.cpy().scl(playerBounce));

				// TODO: Defence component
				if (entityManager.entityHasComponent(sender, DamageComponent.class)) {
					DamageComponent damageComponent = entityManager.getComponent(sender,
							DamageComponent.class);
					HealthComponent healthComponent = entityManager.getComponent(receiver,
							HealthComponent.class);

					// TODO: Maybe have invulnerability for a second after taking damage
					healthComponent.health -= damageComponent.damage;
					if (healthComponent.health < 0) {
						healthComponent.health = 0;
					}
				}

				return false;
			}
		});

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
		entityManager.addComponent(enemyId,
				new SpriteComponent(texture, animationFromTexture(texture, 64, 64, 0.2f)));
		entityManager.addComponent(enemyId, new PositionComponent(location));
		entityManager.addComponent(enemyId, new MovementComponent(new Vector2(0, 0), enemySpeed));
		entityManager.addComponent(enemyId, new DamageComponent(enemyDamage));
		addEntityCollisionComponent(entityManager, enemyId, location, enemyRadius);
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
