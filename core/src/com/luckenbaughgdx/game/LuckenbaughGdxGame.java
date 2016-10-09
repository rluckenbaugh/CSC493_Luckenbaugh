package com.luckenbaughgdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.luckenbaughgdx.game.screens.MenuScreen;

public class LuckenbaughGdxGame extends Game
{

    @Override
    public void create()
    {
        //Set LibGDX log level
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        //Load assets
        Assets.instance.init(new AssetManager());
        //Start game at menu screen
        setScreen(new MenuScreen(this));

    }

}
