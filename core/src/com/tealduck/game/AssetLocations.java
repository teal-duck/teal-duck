package com.tealduck.game;


public class AssetLocations {
	public static final String TEXTURES = "textures/";
	public static final String ENTITY = AssetLocations.TEXTURES + "entity/";
	public static final String GUI = AssetLocations.TEXTURES + "gui/";
	public static final String WORLD = AssetLocations.TEXTURES + "world/";
	public static final String LIGHT = AssetLocations.TEXTURES + "light/";

	public static final String DUCK = AssetLocations.ENTITY + "duck/duck.png"; // "duck_64x64.png";
	public static final String ENEMY = AssetLocations.ENTITY + "enemies/goose_w.png"; // "badlogic_64x64.png";
	public static final String GRID = AssetLocations.WORLD + "grid_64x64.png";
	public static final String GOAL = AssetLocations.ENTITY + "goal_64x64.png";

	public static final String BULLET = AssetLocations.ENTITY + "duck/beak_bullet_outline.png"; // "bullet_16x4.png";

	public static final String POINT_LIGHT = AssetLocations.LIGHT + "point_light_512x512.png";
	public static final String CONE_LIGHT = AssetLocations.LIGHT + "cone_light_512x512.png";
	// public static final String MUZZLE_FLASH = AssetLocations.LIGHT + "point_light_32x32.png";

	public static final String LOADING_TEXT = AssetLocations.GUI + "loading_text.png";


	private AssetLocations() {
	}
}
