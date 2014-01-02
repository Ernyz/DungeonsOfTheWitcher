package com.ernyz.dotw;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "DungeonsOfTheWitcher " + DOTW.VERSION;
		cfg.useGL20 = true;
		cfg.width = 1000;
		cfg.height = 650;
		
		new LwjglApplication(new DOTW(), cfg);
	}
}
