package com.luckenbaughgdx.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.luckenbaughgdx.game.Assets;

public class AbstractGameScreen implements Screen
{

    protected Game game;

    public AbstractGameScreen (Game game)
    {
        this.game = game;
    }
    
    @Override
    public void show()
    {        
    }

    @Override
    public void render(float deltaTime)
    {        
    }

    @Override
    public void resize(int width, int height)
    {        
    }

    @Override
    public void pause()
    {        
    }

    @Override
    public void resume()
    {    
        Assets.instance.init(new AssetManager());
    }

    @Override
    public void hide()
    {        
    }

    @Override
    public void dispose()
    {
     Assets.instance.dispose();
        
    }

}
