package com.ernyz.dotw.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ernyz.dotw.DOTW;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "DungeonsOfTheWitcher " + DOTW.VERSION;
		config.width = 1000;
		config.height = 650;
		config.resizable = false;
		config.vSyncEnabled = false;
		//config.useGL30 = true;
		config.x = -1;
		config.y = -1;
		config.addIcon("data/icon16x16.png", FileType.Internal);
		
		new LwjglApplication(new DOTW(), config);
	}
}
