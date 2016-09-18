package com.luckenbaughgdx.game;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.luckenbaughgdx.game.Assets;


public class LuckenbaughGdxGame extends ApplicationAdapter{
	private static final String TAG = LuckenbaughGdxGame.class.getName();
	
	private WorldController worldController;
	private WorldRenderer worldRenderer;
	
	//for use on Android devices
	private boolean paused;
	
	/*
	 * create the controller and the renderer
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.ApplicationAdapter#create()
	 */
	@Override
	public void create () {
		//Set Libgdx log level to DEBUG
		Gdx.app.setLogLevel (Application.LOG_DEBUG);
		//load assets
		Assets.instance.init(new AssetManager());
		
		//Initialize controller and renderer
		worldController = new WorldController();
		worldRenderer = new WorldRenderer(worldController);
		
		//Game world is active on start
		paused = false;
		
	}

	/*
	 * render the scene
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.ApplicationAdapter#render()
	 */
	@Override
	public void render () {
		//Do not update game world when paused
		if(!paused){
			//update game world by the time that has passed since last rendered frame
			worldController.update(Gdx.graphics.getDeltaTime());
		}
		//Update game world by the time that has passed since laste rendered frame
		worldController.update(Gdx.graphics.getDeltaTime());
		//Sets the clear screen color to Cornflower Blue
		Gdx.gl.glClearColor(0x64/255.0f, 0x95/255.0f, 0xed/255.0f, 0xff/255.0f);
		//Clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		//Render game world to screen
		
		worldRenderer.render();
		
		
	}
	
	/*
	 * dispose of the scene
	 * @see com.badlogic.gdx.ApplicationAdapter#dispose()
	 */
	@Override
	public void dispose () { 
		worldRenderer.dispose();
		Assets.instance.dispose();
	}
		
	/*
	 * window resize
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.ApplicationAdapter#resize(int, int)
	 */
	@Override
	public void resize (int width, int height) {
		worldRenderer.resize(width, height);
	}
	
	@Override
	public void pause () {
		paused = true;
	}
	
	@Override
	public void resume () {
		Assets.instance.init(new AssetManager());
		paused = false;
	}
}
