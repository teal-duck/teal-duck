package com.tealduck.game;


public class AssetLocations {
	public static final String TEXTURES = "textures/";
	public static final String ENTITY = AssetLocations.TEXTURES + "entity/";
	public static final String GUI = AssetLocations.TEXTURES + "gui/";
	public static final String WORLD = AssetLocations.TEXTURES + "world/";

	public static final String LIGHT_DIR = AssetLocations.TEXTURES + "light/";
	public static final String PICKUP_DIR = AssetLocations.ENTITY + "pickup/";
	public static final String DUCK_DIR = AssetLocations.ENTITY + "duck/";
	public static final String ENEMY_DIR = AssetLocations.ENTITY + "enemies/";

	public static final String DUCK = AssetLocations.DUCK_DIR + "duck.png"; // "duck_64x64.png";
	public static final String BULLET = AssetLocations.DUCK_DIR + "beak_bullet_outline.png"; // "bullet_16x4.png";
	public static final String ENEMY = AssetLocations.ENEMY_DIR + "goose_w.png"; // "badlogic_64x64.png";
	public static final String GOAL = AssetLocations.ENTITY + "goal_64x64.png";

	public static final String AMMO_PICKUP = AssetLocations.PICKUP_DIR + "ammo_64x64.png";
	public static final String HEALTH_PICKUP = AssetLocations.PICKUP_DIR + "health_64x64.png";

	public static final String POINT_LIGHT = AssetLocations.LIGHT_DIR + "point_light_512x512.png";
	public static final String CONE_LIGHT = AssetLocations.LIGHT_DIR + "cone_light_512x512.png";

	public static final String GRID = AssetLocations.WORLD + "grid_64x64.png";

	public static final String LOADING_TEXT = AssetLocations.GUI + "loading_text.png";


	private AssetLocations() {
	}
}
