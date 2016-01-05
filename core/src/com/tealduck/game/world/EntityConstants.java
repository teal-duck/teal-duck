package com.tealduck.game.world;


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
	public static final int PLAYER_MAX_HEALTH = 10;
	public static final float PAUSE_TIME = 0.5f;

	public static final int START_AMMO = 10000;
	public static final float COOLDOWN_TIME = 0.1f;
	public static final float BULLET_SPEED = 1000f; // 3000f;
	public static final int BULLET_RADIUS = 12;
	public static final float BULLET_KNOCKBACK_FORCE = 20000;

	public static final boolean USE_CONE_LIGHTS = true;
}
