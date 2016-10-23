package com.luckenbaughgdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.luckenbaughgdx.game.screens.MenuScreen;
import com.luckenbaughgdx.game.util.AudioManager;
import com.luckenbaughgdx.game.util.GamePreferences;

public class LuckenbaughGdxGame extends Game
{

    @Override
    public void create()
    {
        //Set LibGDX log level
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        //Load assets
        Assets.instance.init(new AssetManager());
		
		//Load preferences for audio settings and start playing music
		GamePreferences.instances.load();
		AudioManager.instance.play(Assets.instance.music.song01);
		
        //Start game at menu screen
        setScreen(new MenuScreen(this));

    }

}
