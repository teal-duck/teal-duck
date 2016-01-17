package com.tealduck.game.world;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Polyline;
import com.badlogic.gdx.math.Vector2;
import com.tealduck.game.AssetLocations;
import com.tealduck.game.Tag;
import com.tealduck.game.Team;
import com.tealduck.game.TextureMap;
import com.tealduck.game.collision.AABB;
import com.tealduck.game.collision.Circle;
import com.tealduck.game.component.ChaseComponent;
import com.tealduck.game.component.CollisionComponent;
import com.tealduck.game.component.DamageComponent;
import com.tealduck.game.component.DropComponent;
import com.tealduck.game.component.HealthComponent;
import com.tealduck.game.component.KnockbackComponent;
import com.tealduck.game.component.MovementComponent;
import com.tealduck.game.component.PatrolRouteComponent;
import com.tealduck.game.component.PickupComponent;
import com.tealduck.game.component.PositionComponent;
import com.tealduck.game.component.ScoreComponent;
import com.tealduck.game.component.SpriteComponent;
import com.tealduck.game.component.TeamComponent;
import com.tealduck.game.component.UserInputComponent;
import com.tealduck.game.component.ViewconeComponent;
import com.tealduck.game.component.WeaponComponent;
import com.tealduck.game.engine.EntityEngine;
import com.tealduck.game.engine.EntityManager;
import com.tealduck.game.engine.EventManager;
import com.tealduck.game.event.EnemyCollision;
import com.tealduck.game.event.EventName;
import com.tealduck.game.event.PlayerCollision;
import com.tealduck.game.event.SearchWorldCollision;
import com.tealduck.game.input.ControlMap;
import com.tealduck.game.pickup.AmmoPickup;
import com.tealduck.game.pickup.HealthPickup;
import com.tealduck.game.pickup.Pickup;
import com.tealduck.game.weapon.MachineGun;


public class EntityLoader {
	private EntityLoader() {
	};


	/**
	 * @param polylineMapObject
	 * @return
	 */
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


	public static void loadEntities(World world, TextureMap textureMap, ControlMap controlMap,
			Controller controller) {
		int playerId = -1;
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
					Gdx.app.log("Load", "null name");
					continue;
				}
				float x = t.getX();
				float y = t.getY();

				if (name.equals("Player")) {
					if (playerId != -1) {
						throw new IllegalArgumentException("More than 1 player");
					}

					playerId = EntityLoader.createPlayer(entityEngine,
							textureMap.getTexture(AssetLocations.DUCK), new Vector2(x, y),
							textureMap.getTexture(AssetLocations.BULLET), controlMap,
							controller);

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
				} else if (name.equals("AmmoPickup") || name.equals("HealthPickup")) {
					int defaultAmount = 0;
					if (name.equals("AmmoPickup")) {
						defaultAmount = EntityConstants.AMMO_PICKUP_DEFAULT_AMOUNT;
					} else {
						defaultAmount = EntityConstants.HEALTH_PICKUP_DEFAULT_AMOUNT;
					}
					int amount = EntityLoader.getAmountForPickup(t, defaultAmount);

					Texture texture = null;
					if (name.equals("AmmoPickup")) {
						texture = textureMap.getTexture(AssetLocations.AMMO_PICKUP);
					} else {
						texture = textureMap.getTexture(AssetLocations.HEALTH_PICKUP);
					}

					Pickup contents = null;
					if (name.equals("AmmoPickup")) {
						contents = new AmmoPickup(amount);
					} else {
						contents = new HealthPickup(amount);
					}

					EntityLoader.createPickup(entityEngine, texture, new Vector2(x, y), contents);
				} else {
					Gdx.app.log("Load", "Unknown entity name: " + name);
				}
			} else {
				Gdx.app.log("Load", "Unknown entity type: " + object.getClass().getName() + "; Name: "
						+ object.getName());
			}
		}

		if (playerId == -1) {
			Gdx.app.log("Load", "No player exists in map");
			return;
		}

		EntityLoader.addChaseComponentToEnemies(entityManager, playerId);
	}


	private static int getAmountForPickup(TiledMapTileMapObject t, int defaultAmount) {
		int amount = defaultAmount;
		if (t.getProperties().containsKey("amount")) {
			String amountProperty = t.getProperties().get("amount", String.class);
			try {
				amount = Integer.parseInt(amountProperty);
			} catch (NumberFormatException e) {

				Gdx.app.log("Load", "Pickup amount not valid integer, using default");
			}
		} else {
			Gdx.app.log("Load", "Pickup doesn't state amount, using default");
		}

		return amount;
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


	/**
	 * @param entityManager
	 * @param entityTagManager
	 * @param texture
	 * @param position
	 * @return
	 */
	private static int createPlayer(EntityEngine entityEngine, Texture texture, Vector2 position,
			Texture bulletTexture, ControlMap controlMap, Controller controller) {
		EntityManager entityManager = entityEngine.getEntityManager();
		int playerId = entityManager.createEntityWithTag(entityEngine.getEntityTagManager(), Tag.PLAYER);

		entityManager.addComponent(playerId,
				new SpriteComponent(texture, EntityLoader.animationFromTexture(texture, 64, 64, 0.2f)));
		entityManager.addComponent(playerId,
				new PositionComponent(position, new Vector2(1, 0), new Vector2(64, 64)));
		entityManager.addComponent(playerId,
				new MovementComponent(new Vector2(0, 0), EntityConstants.PLAYER_SPEED,
						EntityConstants.PLAYER_SPRINT, EntityConstants.PLAYER_SPRINT_TIME));

		UserInputComponent uic = new UserInputComponent(controlMap, controller);
		Gdx.app.log("Controls", controlMap.toString());
		entityManager.addComponent(playerId, uic);

		EntityLoader.addEntityCircleCollisionComponent(entityManager, playerId, position,
				EntityConstants.PLAYER_RADIUS);
		// addEntityAABBCollisionComponent(entityManager, playerId, position, new Vector2(60,
		// 60));

		entityManager.addComponent(playerId, new HealthComponent(EntityConstants.PLAYER_MAX_HEALTH));

		entityManager.addComponent(playerId, new KnockbackComponent(EntityConstants.PLAYER_KNOCKBACK_FORCE));

		MachineGun weapon = new MachineGun(bulletTexture);
		entityManager.addComponent(playerId, new WeaponComponent(weapon, EntityConstants.START_AMMO_IN_CLIP,
				EntityConstants.START_EXTRA_AMMO));

		entityManager.addComponent(playerId, new ScoreComponent());

		entityManager.addComponent(playerId, new TeamComponent(Team.GOOD));

		EventManager eventManager = entityEngine.getEventManager();
		eventManager.addEvent(playerId, EventName.COLLISION, PlayerCollision.instance);

		return playerId;
	}


	/**
	 * @param entityManager
	 * @param texture
	 * @param position
	 * @return
	 */
	private static int createEnemy(EntityEngine entityEngine, Texture texture, Vector2 position) {
		EntityManager entityManager = entityEngine.getEntityManager();
		int enemyId = entityManager.createEntity();
		entityManager.addComponent(enemyId,
				new SpriteComponent(texture, EntityLoader.animationFromTexture(texture, 64, 64, 0.2f)));
		entityManager.addComponent(enemyId,
				new PositionComponent(position, new Vector2(1, 0), new Vector2(64, 64)));
		entityManager.addComponent(enemyId,
				new MovementComponent(new Vector2(0, 0), EntityConstants.ENEMY_SPEED));
		entityManager.addComponent(enemyId, new DamageComponent(EntityConstants.ENEMY_DAMAGE));
		entityManager.addComponent(enemyId, new KnockbackComponent(EntityConstants.ENEMY_KNOCKBACK_FORCE));
		EntityLoader.addEntityCircleCollisionComponent(entityManager, enemyId, position,
				EntityConstants.ENEMY_RADIUS);

		entityManager.addComponent(enemyId, new ViewconeComponent(EntityConstants.ENEMY_VIEW_HALF_FOV,
				EntityConstants.ENEMY_VIEW_LENGTH));

		entityManager.addComponent(enemyId, new HealthComponent(EntityConstants.ENEMY_HEALTH));

		entityManager.addComponent(enemyId, new TeamComponent(Team.BAD));

		if (MathUtils.randomBoolean()) {
			entityManager.addComponent(enemyId, new DropComponent(EntityLoader.randomPickup()));
		}

		EventManager eventManager = entityEngine.getEventManager();
		eventManager.addEvent(enemyId, EventName.COLLISION, EnemyCollision.instance);

		eventManager.addEvent(enemyId, EventName.WORLD_COLLISION, SearchWorldCollision.instance);

		return enemyId;
	}


	private static Pickup randomPickup() {
		if (MathUtils.randomBoolean()) {
			return new AmmoPickup(EntityConstants.AMMO_PICKUP_DEFAULT_AMOUNT);
		} else {
			return new HealthPickup(EntityConstants.HEALTH_PICKUP_DEFAULT_AMOUNT);
		}
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


	private static void addChaseComponentToEnemies(EntityManager entityManager, int playerId) {
		// Enemies that don't have patrol route should chase player

		Set<Integer> entities = entityManager.getEntitiesWithComponent(TeamComponent.class);
		for (int entity : entities) {
			if (entityManager.entityHasComponent(entity, PatrolRouteComponent.class)) {
				continue;
			}

			TeamComponent teamComponent = entityManager.getComponent(entity, TeamComponent.class);
			Team team = teamComponent.team;

			if (team == Team.BAD) {
				entityManager.addComponent(entity, new ChaseComponent(playerId, false));
			}
		}
	}


	private static int createGoal(EntityEngine entityEngine, Texture texture, Vector2 position) {
		EntityManager entityManager = entityEngine.getEntityManager();
		int goalId = entityManager.createEntityWithTag(entityEngine.getEntityTagManager(), Tag.GOAL);
		entityManager.addComponent(goalId, new SpriteComponent(texture));
		entityManager.addComponent(goalId,
				new PositionComponent(position, new Vector2(1, 0), new Vector2(64, 64)));
		EntityLoader.addEntityAABBCollisionComponent(entityManager, goalId, position, new Vector2(64, 64));

		return goalId;
	}


	public static void createPickup(EntityEngine entityEngine, Texture texture, Vector2 position, Pickup contents) {
		EntityManager entityManager = entityEngine.getEntityManager();

		int pickupId = entityManager.createEntity();
		entityManager.addComponent(pickupId, new SpriteComponent(texture));
		entityManager.addComponent(pickupId,
				new PositionComponent(position, new Vector2(1, 0), new Vector2(64, 64)));

		entityManager.addComponent(pickupId, new PickupComponent(contents));

		EntityLoader.addEntityCircleCollisionComponent(entityManager, pickupId, position,
				EntityConstants.PICKUP_RADIUS);
	}
}
