package com.tealduck.game.desktop;


import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.tealduck.game.DuckGame;


public class DesktopLauncher {
	public static void main(String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title = DuckGame.GAME_TITLE; // "Teal Duck Awesome Game";
		config.x = -1;
		config.y = -1;
		config.width = 64 * 10;
		config.height = 64 * 8 + 0;
		config.useGL30 = false;
		config.allowSoftwareMode = true;
		config.vSyncEnabled = true;
		config.foregroundFPS = 60;
		config.backgroundFPS = 60;

		// config.resizable = false;
		// FileType fileType = FileType.Local;
		// String path = "teal_duck_icon.png";
		// config.addIcon(path, fileType);

		new LwjglApplication(new DuckGame(), config);
	}
}
