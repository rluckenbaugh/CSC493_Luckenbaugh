package com.luckenbaughgdx.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.luckenbaughgdx.game.util.Constants;
import com.badlogic.gdx.graphics.g2d.Sprite;

public class WorldRenderer implements Disposable 
{

	private OrthographicCamera camera;
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
	}
	
	/*
	 * render function
	 */
	public void render () 
	{
		renderTestObjects();
	}
	
	/*
	 * render the test objects
	 */
	private void renderTestObjects() 
	{
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		for(Sprite sprite : worldController.testSprites)
		{
			sprite.draw(batch);
		}
		batch.end();
	}

	/*
	 * resizing the viewport
	 */
	public void resize (int width, int height) 
	{
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT/height) * width;
		camera.update();
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
