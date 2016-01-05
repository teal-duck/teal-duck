package com.tealduck.game.world;


import com.badlogic.gdx.math.Vector3;


public class EntityConstants {
	// TODO: Clean up constants
	public static final float PLAYER_SPEED = 2400.0f;
	public static final float PLAYER_SPRINT = 1.8f;
	public static final float ENEMY_SPEED = 1800.0f;
	public static final float PLAYER_RADIUS = 20;
	public static final float ENEMY_RADIUS = 20;
	public static final float ENEMY_KNOCKBACK_FORCE = 60000;
	public static final float PLAYER_KNOCKBACK_FORCE = EntityConstants.ENEMY_KNOCKBACK_FORCE * 0.6f;
	public static final int ENEMY_DAMAGE = 1;
	public static final int PLAYER_MAX_HEALTH = 5;
	public static final float PAUSE_TIME = 0.5f;

	public static final int MACHINE_GUN_CLIP_SIZE = 10;
	public static final int START_AMMO_IN_CLIP = 10;
	public static final int START_CLIPS = 0; // 200; // 2;
	public static final int START_EXTRA_AMMO = EntityConstants.START_AMMO_IN_CLIP * EntityConstants.START_CLIPS;
	public static final float COOLDOWN_TIME = 0.15f;
	public static final float RELOAD_TIME = 4f;
	public static final float BULLET_SPEED = 900f; // 3000f;
	public static final int BULLET_RADIUS = 12;
	public static final float BULLET_KNOCKBACK_FORCE = 20000;

	public static final float PICKUP_FULL_ROTATION_TIME = 2f;
	public static final float PICKUP_RADIUS = 20;

	public static final int AMMO_PICKUP_DEFAULT_AMOUNT = EntityConstants.MACHINE_GUN_CLIP_SIZE;
	public static final int HEALTH_PICKUP_DEFAULT_AMOUNT = 1;

	public static final boolean USE_CONE_LIGHTS = true;

	public static final float AMBIENT_INTENSITY = 0.2f;
	private static final float colour = 0.8f;
	public static final Vector3 AMBIENT_COLOUR = new Vector3(EntityConstants.colour, EntityConstants.colour,
			EntityConstants.colour);
}
