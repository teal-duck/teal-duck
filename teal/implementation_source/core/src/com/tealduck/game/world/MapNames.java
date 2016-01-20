package com.tealduck.game.world;


import com.badlogic.gdx.Gdx;


/**
 *
 */
public class MapNames {
	public static final String MAPS_DIRECTORY = "maps/";
	public static final String TEST_MAP = MapNames.MAPS_DIRECTORY + "test_map.tmx";

	public static final int LEVEL_COUNT = 2;


	/**
	 * @param levelNumber
	 *                in range 1 to LEVEL_COUNT
	 * @return null if the level number is out of bounds
	 */
	public static String levelNumberToAssetName(int levelNumber) {
		if ((levelNumber < 1) || (levelNumber > MapNames.LEVEL_COUNT)) {
			return null;
		} else {
			// Level names are in the format level_XX.tmx where XX = 01, 02, ..., 98, 99
			return MapNames.MAPS_DIRECTORY + String.format("level_%02d.tmx", levelNumber);
		}
	}


	/**
	 * @param levelNumber
	 * @return
	 */
	public static boolean isLastLevel(int levelNumber) {
		return (levelNumber == MapNames.LEVEL_COUNT);
	}


	public static String levelNumberToName(int levelNumber) {
		String name = "";
		switch (levelNumber) {
		case 1:
			name = "Computer Science";
			break;
		case 2:
			name = "Test Level";
			break;
		default:
			Gdx.app.log("Level", "Unknown level number for name");
		}
		return name;
	}


	private MapNames() {
	}
}
