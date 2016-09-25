package com.luckenbaughgdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.luckenbaughgdx.game.util.Constants;

public class WorldRenderer implements Disposable 
{

	private OrthographicCamera camera;
	private OrthographicCamera cameraGUI;
	private SpriteBatch batch;
	private WorldController worldController;
	
	/*
	 * initiate the rendering
	 */
	public WorldRenderer (WorldController worldController) 
	{
		this.worldController = worldController;
		init();
	}
	
	/*
	 * initiate all of the parts of the scene
	 */
	private void init () 
	{
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(0,0,0);
		camera.update();
		cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
		
		cameraGUI.position.set(0,0,0);
		cameraGUI.setToOrtho(true); //flip y-axis
		cameraGUI.update();
	}
	
	/*
	 * render function
	 */
	public void render () 
	{
		renderWorld(batch);
		renderGui(batch);
	}


	/*
	 * render the world
	 */
	private void renderWorld (SpriteBatch batch)
	{
		WorldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		batch.end();
	}
	
	/*
	 * resizing the viewport
	 */
	public void resize (int width, int height) 
	{
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT/height) * width;
		camera.update();
		
		cameraGUI.viewportHeight =Constants.VIEWPORT_GUI_HEIGHT;
		cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT / (float)height) * (float)width;
		cameraGUI.position.set(cameraGUI.viewportWidth/2, cameraGUI.viewportHeight/2,0);
		cameraGUI.update();
	}
	
	/*
	 * render the score
	 */
	private void renderGuiScore (SpriteBatch batch)
	{
		float x = -15;
		float y = -15;
		batch.draw(Assets.instance.goldCoin.goldCoin, x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
		Assets.instance.fonts.defaultBig.draw(batch, "" + worldController.score, x+75, y+37);
	}
	
	/*
	 * render the lives into the gui
	 */
	private void renderGuiExtraLive (SpriteBatch batch)
	{
		float x = cameraGUI.viewportWidth - 50 - Constants.LIVES_START * 50;
		float y = -15;
		for ( int i = 0; i< Constants.LIVES_START; i++)
		{
			if(worldController.lives >= i)
			{
				batch.draw(Assets.instance.bunny.head, x + i * 50,y,50,50,120,100,0.35f,-0.35f,0);
				batch.setColor(1,1,1,1);
			}
		}
	}
	
	/*
	 * GUI FPS counter implementation
	 */
	private void renderGuiFpsCounter (SpriteBatch batch)
	{
		float x = cameraGUI.viewportWidth-55;
		float y = cameraGUI.viewportHeight - 15;
		int fps = Gdx.graphics.getFramesPerSecond();
		BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
		if (fps>=45)
		{
			//45 or more FPS show up in green
			fpsFont.setColor(0,1,0,1);
		}
		else if (fps>=30)
		{
			//30 or more fps show up in yellow
			fpsFont.setColor(1,1,0,1);
		}
		else
		{
			//less than 30 fps show up in red
			fpsFont.setColor(1,0,0,1);
		}
		fpsFont.draw(batch, "FPS: "+fps, x, y);
		fpsFont.setColor(1,1,1,1); //white
	}
	
	/*
	 * render the GUI
	 */
	private void renderGui (SpriteBatch batch)
	{
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		//draw collected gold coins icon and text
		//anchored to top left edge
		renderGuiScore(batch);
		//draw extra lives icon and text anchored to top right edge
		renderGuiExtraLive(batch);
		//draw fps text anchored to bottom right edge
		renderGuiFpsCounter(batch);
		batch.end();
	}
	
	/*
	 * dispose of the unneeded code
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.utils.Disposable#dispose()
	 */
	@Override public void dispose() 
	{
		batch.dispose();
	}
	
	
}
