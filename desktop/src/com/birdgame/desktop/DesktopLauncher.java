package com.birdgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.birdgame.BirdGameMain;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
                config.title = "Bird game";
                config.width = 640;
                config.height = 480;
		new LwjglApplication(new BirdGameMain(), config);
	}
}
