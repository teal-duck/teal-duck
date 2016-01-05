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
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.AssetLocations;
import com.tealduck.game.EventName;
import com.tealduck.game.Tag;
import com.tealduck.game.TextureMap;
import com.tealduck.game.collision.AABB;
import com.tealduck.game.collision.Circle;
import com.tealduck.game.component.CollisionComponent;
import com.tealduck.game.component.DamageComponent;
import com.tealduck.game.component.HealthComponent;
import com.tealduck.game.component.KnockbackComponent;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PatrolRouteComponent;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.component.SpriteComponent;
import com.tealduck.game.component.UserInputComponent;
import com.tealduck.game.component.ViewconeComponent;
import com.tealduck.game.component.WeaponComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EventManager;
import com.tealduck.game.event.EnemyCollision;
import com.tealduck.game.event.GoalCollision;
import com.tealduck.game.event.PlayerCollision;
import com.tealduck.game.input.Action;
import com.tealduck.game.input.ControlMap;
import com.tealduck.game.input.ControllerBindingType;
import com.tealduck.game.input.controller.ControllerHelper;
import com.tealduck.game.input.controller.PS4;
import com.tealduck.game.weapon.MachineGun;


public class EntityLoader {
	private EntityLoader() {
	};


	private static final ArrayList<Vector2> loadPatrolRoute(PolylineMapObject polylineMapObject) {
		ArrayList<Vector2> routeVertices = new ArrayList<Vector2>();

		Polyline polyline = polylineMapObject.getPolyline();
		float[] polylineVertices = polyline.getTransformedVertices();
		int length = polylineVertices.length / 2;

		for (int i = 0; i < length; i += 1) {
			routeVertices.add(new Vector2(polylineVertices[i * 2], polylineVertices[(i * 2) + 1]));
		}

		if (routeVertices.size() > 2) {
			if (routeVertices.get(0).epsilonEquals(routeVertices.get(routeVertices.size() - 1), 0.1f)) {
				routeVertices.remove(routeVertices.size() - 1);
			}
		}

		return routeVertices;
	}


	public static final HashMap<String, ArrayList<Vector2>> loadPatrolRoutes(TiledMap tiledMap) {
		HashMap<String, ArrayList<Vector2>> patrolRoutes = new HashMap<String, ArrayList<Vector2>>();

		MapLayer lineLayer = tiledMap.getLayers().get("Lines");
		MapObjects lineObjects = lineLayer.getObjects();

		for (MapObject object : lineObjects) {
			String name = object.getName();

			if (!(object instanceof PolylineMapObject)) {
				Gdx.app.log("Patrol", name + " isn't a patrol route");
				continue;
			}

			if (patrolRoutes.containsKey(name)) {
				Gdx.app.log("Patrol", "Duplicate route " + name);
				continue;
			}

			PolylineMapObject polyline = (PolylineMapObject) object;
			ArrayList<Vector2> route = EntityLoader.loadPatrolRoute(polyline);

			Gdx.app.log("Patrol", "Loaded \"" + name + "\": " + route.toString());
			patrolRoutes.put(name, route);

		}

		return patrolRoutes;
	}


	public static void loadEntities(World world, TextureMap textureMap) {
		// TODO: Rewrite loadEntities method, it's very large
		boolean loadedPlayer = false;
		boolean loadedGoal = false;

		TiledMap tiledMap = world.getTiledMap();
		EntityEngine entityEngine = world.getEntityEngine();
		MapLayer entityLayer = tiledMap.getLayers().get("Entities");
		MapObjects objects = entityLayer.getObjects();
		EntityManager entityManager = entityEngine.getEntityManager();

		for (MapObject object : objects) {
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
					if (loadedPlayer) {
						throw new IllegalArgumentException("More than 1 player");
					}

					EntityLoader.createPlayer(entityEngine,
							textureMap.getTexture(AssetLocations.DUCK), new Vector2(x, y),
							textureMap.getTexture(AssetLocations.BULLET));
					loadedPlayer = true;

				} else if (name.equals("Enemy")) {
					int enemyId = EntityLoader.createEnemy(entityEngine,
							textureMap.getTexture(AssetLocations.ENEMY), new Vector2(x, y));

					if (t.getProperties().containsKey("patrolRoute")) {
						String routeName = t.getProperties().get("patrolRoute", String.class);
						EntityLoader.addPatrolRouteToEnemy(entityManager, world, routeName,
								enemyId);
					}
				} else if (name.equals("Goal")) {
					if (loadedGoal) {
						throw new IllegalArgumentException("More than 1 goal");
					}
					EntityLoader.createGoal(entityEngine,
							textureMap.getTexture(AssetLocations.GOAL), new Vector2(x, y));
					loadedGoal = true;
				} else {
					Gdx.app.log("Load", "Unknown entity name: " + name);
				}
			} else {
				Gdx.app.log("Load", "Unknown entity type: " + object.getClass().getName() + "; Name: "
						+ object.getName());
			}
		}
	}


	public static CollisionComponent addEntityCircleCollisionComponent(EntityManager entityManager, int entityId,
			Vector2 entityPosition, float radius) {
		// TODO: addEntityCircleCollisionComponent offsetFromPosition
		Vector2 offsetFromPosition = new Vector2(32, 32);
		Circle circle = new Circle(entityPosition.cpy().add(offsetFromPosition), radius);
		CollisionComponent collisionComponent = new CollisionComponent(circle, offsetFromPosition);
		entityManager.addComponent(entityId, collisionComponent);
		return collisionComponent;
	}


	public static CollisionComponent addEntityAABBCollisionComponent(EntityManager entityManager, int entityId,
			Vector2 entityPosition, Vector2 entitySize) {
		Vector2 offsetFromPosition = (new Vector2(64, 64)).sub(entitySize).scl(0.5f);
		AABB aabb = new AABB(entityPosition.cpy(), entitySize.cpy());
		CollisionComponent collisionComponent = new CollisionComponent(aabb, offsetFromPosition);
		entityManager.addComponent(entityId, collisionComponent);
		return collisionComponent;

	}


	private static Animation animationFromTexture(Texture texture, int frameWidth, int frameHeight,
			float frameDuration) {
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


	private static ControlMap loadPlayerControls() {
		ControlMap controls = new ControlMap();

		controls.addKeyForAction(Action.RIGHT, Keys.D, Keys.RIGHT);
		controls.addKeyForAction(Action.LEFT, Keys.A, Keys.LEFT);
		controls.addKeyForAction(Action.UP, Keys.W, Keys.UP);
		controls.addKeyForAction(Action.DOWN, Keys.S, Keys.DOWN);
		controls.addKeyForAction(Action.SPRINT, Keys.SHIFT_LEFT, Keys.SHIFT_RIGHT);
		controls.addKeyForAction(Action.FIRE, Keys.SPACE);
		// TODO: Add mouse click for firing

		controls.addKeyForAction(Action.RELOAD, Keys.R);

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

		controls.addControllerForAction(Action.LOOK_RIGHT, ControllerBindingType.AXIS_POSITIVE,
				PS4.AXIS_RIGHT_X, deadzone);
		controls.addControllerForAction(Action.LOOK_LEFT, ControllerBindingType.AXIS_NEGATIVE, PS4.AXIS_RIGHT_X,
				deadzone);
		controls.addControllerForAction(Action.LOOK_UP, ControllerBindingType.AXIS_NEGATIVE, PS4.AXIS_RIGHT_Y,
				deadzone);
		controls.addControllerForAction(Action.LOOK_DOWN, ControllerBindingType.AXIS_POSITIVE, PS4.AXIS_RIGHT_Y,
				deadzone);
		controls.addControllerForAction(Action.FIRE, ControllerBindingType.AXIS_POSITIVE, PS4.AXIS_R2,
				deadzone);
		controls.addControllerForAction(Action.RELOAD, ControllerBindingType.BUTTON, PS4.BUTTON_L1);

		return controls;
	}


	/**
	 * @param entityManager
	 * @param entityTagManager
	 * @param texture
	 * @param location
	 * @return
	 */
	private static int createPlayer(EntityEngine entityEngine, Texture texture, Vector2 location,
			Texture bulletTexture) {
		EntityManager entityManager = entityEngine.getEntityManager();
		int playerId = entityManager.createEntityWithTag(entityEngine.getEntityTagManager(), Tag.PLAYER);

		entityManager.addComponent(playerId,
				new SpriteComponent(texture, EntityLoader.animationFromTexture(texture, 64, 64, 0.2f)));
		entityManager.addComponent(playerId, new PositionComponent(location));
		entityManager.addComponent(playerId, new MovementComponent(new Vector2(0, 0),
				EntityConstants.PLAYER_SPEED, EntityConstants.PLAYER_SPRINT));

		ControlMap controls = EntityLoader.loadPlayerControls();
		UserInputComponent uic = new UserInputComponent(controls, ControllerHelper.getFirstControllerOrNull());
		Gdx.app.log("Controls", uic.controls.toString());
		entityManager.addComponent(playerId, uic);

		EntityLoader.addEntityCircleCollisionComponent(entityManager, playerId, location,
				EntityConstants.PLAYER_RADIUS);
		// addEntityAABBCollisionComponent(entityManager, playerId, location, new Vector2(60, 60));

		entityManager.addComponent(playerId, new HealthComponent(EntityConstants.PLAYER_MAX_HEALTH));

		entityManager.addComponent(playerId, new KnockbackComponent(EntityConstants.PLAYER_KNOCKBACK_FORCE));

		entityManager.addComponent(playerId,
				new WeaponComponent(new MachineGun(bulletTexture),
						EntityConstants.MACHINE_GUN_CLIP_SIZE, EntityConstants.START_EXTRA_AMMO,
						EntityConstants.COOLDOWN_TIME, EntityConstants.RELOAD_TIME));

		EventManager eventManager = entityEngine.getEventManager();
		eventManager.addEvent(playerId, EventName.COLLISION, PlayerCollision.instance);

		return playerId;
	}


	/**
	 * @param entityManager
	 * @param texture
	 * @param location
	 * @return
	 */
	private static int createEnemy(EntityEngine entityEngine, Texture texture, Vector2 location) {
		EntityManager entityManager = entityEngine.getEntityManager();
		int enemyId = entityManager.createEntity();
		entityManager.addComponent(enemyId,
				new SpriteComponent(texture, EntityLoader.animationFromTexture(texture, 64, 64, 0.2f)));
		entityManager.addComponent(enemyId, new PositionComponent(location));
		entityManager.addComponent(enemyId,
				new MovementComponent(new Vector2(0, 0), EntityConstants.ENEMY_SPEED));
		entityManager.addComponent(enemyId, new DamageComponent(EntityConstants.ENEMY_DAMAGE));
		entityManager.addComponent(enemyId, new KnockbackComponent(EntityConstants.ENEMY_KNOCKBACK_FORCE));
		EntityLoader.addEntityCircleCollisionComponent(entityManager, enemyId, location,
				EntityConstants.ENEMY_RADIUS);

		entityManager.addComponent(enemyId, new ViewconeComponent());

		entityManager.addComponent(enemyId, new HealthComponent(3));

		EventManager eventManager = entityEngine.getEventManager();
		eventManager.addEvent(enemyId, EventName.COLLISION, EnemyCollision.instance);

		return enemyId;
	}


	private static void addPatrolRouteToEnemy(EntityManager entityManager, World world, String routeName,
			int enemyId) {
		ArrayList<Vector2> patrolRoute = world.getPatrolRoute(routeName);

		if (patrolRoute == null) {
			Gdx.app.log("Load", "Unknown route: " + routeName);
		} else {
			PatrolRouteComponent patrolRouteComponent = new PatrolRouteComponent(patrolRoute,
					EntityConstants.PAUSE_TIME);
			entityManager.addComponent(enemyId, patrolRouteComponent);
		}
	}


	private static int createGoal(EntityEngine entityEngine, Texture texture, Vector2 location) {
		EntityManager entityManager = entityEngine.getEntityManager();
		int goalId = entityManager.createEntityWithTag(entityEngine.getEntityTagManager(), Tag.GOAL);
		entityManager.addComponent(goalId, new SpriteComponent(texture));
		entityManager.addComponent(goalId, new PositionComponent(location));
		EntityLoader.addEntityAABBCollisionComponent(entityManager, goalId, location, new Vector2(64, 64));

		EventManager eventManager = entityEngine.getEventManager();
		eventManager.addEvent(goalId, EventName.COLLISION, GoalCollision.instance);

		return goalId;
	}
}
