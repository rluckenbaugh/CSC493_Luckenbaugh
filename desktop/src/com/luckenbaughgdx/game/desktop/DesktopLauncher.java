package com.luckenbaughgdx.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;

import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.luckenbaughgdx.game.LuckenbaughGdxGame;

public class DesktopLauncher {
	
	static boolean rebuildAtlas = false;
	static boolean drawDebugOutline = false;
	
	public static void main (String[] arg) {

		/*
		 * build the texture atlas and the texture png
		 */
		if(rebuildAtlas)
		{
			Settings settings = new Settings();
			settings.maxWidth = 1024;
			settings.maxHeight = 1024;
			settings.duplicatePadding = false;
			settings.debug = drawDebugOutline;
			TexturePacker.process(settings, "assets-raw/images", "../core/assets/images", "canyonbunny.pack");
		}
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "CanyonBunny";
		config.width = 800;
		config.height = 480;
		
		new LwjglApplication(new LuckenbaughGdxGame(), config);
	}
}
