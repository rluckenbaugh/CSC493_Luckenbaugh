package com.luckenbaughgdx.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.luckenbaughgdx.game.util.CameraHelper;
import com.luckenbaughgdx.game.util.Constants;


public class WorldController extends InputAdapter 
{

	private static final String TAG = WorldController.class.getName();
	
	public Sprite[] testSprites;
	public Level level;
	public int lives;
	public int score;

	
	public static CameraHelper cameraHelper;
	
	public WorldController()
	{
		init();
	}
	
	/*
	 * initiate the level
	 */
	private void initLevel()
	{
		score = 0;
		level = new Level (Constants.LEVEL_01);
	}

	
	/*
	 * initiate the drawing process
	 */
	private void init() 
	{
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		lives = Constants.LIVES_START;
		initLevel();
	}
	
	
	/*
	 * creat the pixmap and color the objects
	 */
	private Pixmap createProceduralPixmap(int width, int height) 
	{
		Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
		//Fill square with red color at 50%opacity
		pixmap.setColor(1, 0, 0, 0.5f);
		pixmap.fill();
		//draw a yellow-colored x shape on square
		pixmap.setColor(1, 1, 0, 1);
		pixmap.drawLine(0, 0, width, height);
		pixmap.drawLine(width, 0, 0, height);
		//draw a cyan colored border around the square
		pixmap.setColor(0, 1, 1, 1);
		pixmap.drawRectangle(0, 0, width, height);
		return pixmap;
	}
	
	/*
	 * update the movement of the scene
	 */
	public void update (float deltaTime) 
	{
		handlerDebugInput(deltaTime);
		cameraHelper.update(deltaTime);
	}
	
	/*
	 * handle keyboard input to control elements of the scene
	 */
	private void handlerDebugInput(float deltaTime) 
	{
		if(Gdx.app.getType() != ApplicationType.Desktop) return;
		
		//Camera Controls (move)
		float camMoveSpeed = 5 * deltaTime;
		float camMoveSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
			camMoveSpeed *= camMoveSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.LEFT))
			moveCamera(-camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed(Keys.RIGHT))
			moveCamera(camMoveSpeed, 0);
		if (Gdx.input.isKeyPressed(Keys.UP))
			moveCamera(0, camMoveSpeed);
		if (Gdx.input.isKeyPressed(Keys.DOWN))
			moveCamera(0,-camMoveSpeed);
		if (Gdx.input.isKeyPressed(Keys.BACKSPACE))
			cameraHelper.setPosition(0, 0);
		
		//Camera Controls (zoom)
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAcclerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
			camZoomSpeed *= camZoomSpeedAcclerationFactor;
		if (Gdx.input.isKeyPressed(Keys.COMMA))
			cameraHelper.addZoom(camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.PERIOD))
			cameraHelper.addZoom(-camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.SLASH))
			cameraHelper.setZoom(1);
			
	}

	/*
	 * adjust the camera values
	 */
	private void moveCamera (float x, float y)
	{
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
	}
	

	/*
	 * (non-Javadoc)
	 * @see com.badlogic.gdx.InputAdapter#keyUp(int)
	 * read keyboard input
	 */
	@Override
	public boolean keyUp (int keycode)
	{
		//reset game world
		if(keycode == Keys.R)
		{
			init();
			Gdx.app.debug(TAG, "Game world reset");
		}
		return false;
	}
	
	
}
