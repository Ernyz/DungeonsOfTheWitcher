package com.ernyz.dotw;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "DungeonsOfTheWitcher " + DOTW.VERSION;
		cfg.width = 1000;
		cfg.height = 650;
		cfg.resizable = false;
		cfg.vSyncEnabled = false;
		cfg.x = -1;
		cfg.y = -1;
		//cfg.addIcon(path, fileType);
		
		new LwjglApplication(new DOTW(), cfg);
	}
}
