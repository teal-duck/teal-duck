package com.tealduck.game.world;


public class EntityConstants {
	// TODO: Clean up constants in world
	public static final float PLAYER_SPEED = 2500.0f;
	public static final float PLAYER_SPRINT = 1.8f;
	public static final float ENEMY_SPEED = 2000.0f;
	public static final float PLAYER_RADIUS = 20;
	public static final float ENEMY_RADIUS = 20;
	public static final float ENEMY_KNOCKBACK_FORCE = 60000;
	public static final float PLAYER_KNOCKBACK_FORCE = EntityConstants.ENEMY_KNOCKBACK_FORCE * 0.6f;
	public static final int ENEMY_DAMAGE = 1;
	public static final int PLAYER_MAX_HEALTH = 10;
	public static final float PAUSE_TIME = 0.5f;
}
