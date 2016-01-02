package com.tealduck.game;


public class AssetLocations {
	public static final String TEXTURES = "textures/";
	public static final String ENTITIES = AssetLocations.TEXTURES + "entities/";
	public static final String GUI = AssetLocations.TEXTURES + "gui/";
	public static final String WORLD = AssetLocations.TEXTURES + "world/";

	public static final String DUCK = AssetLocations.ENTITIES + "duck/duck.png"; // "duck_64x64.png";
	public static final String ENEMY = AssetLocations.ENTITIES + "enemies/goose_w.png"; // "badlogic_64x64.png";
	public static final String GRID = AssetLocations.WORLD + "grid_64x64.png";
	public static final String GOAL = AssetLocations.ENTITIES + "goal_64x64.png";

	public static final String LOADING_TEXT = AssetLocations.GUI + "loading_text.png";


	private AssetLocations() {
	}
}