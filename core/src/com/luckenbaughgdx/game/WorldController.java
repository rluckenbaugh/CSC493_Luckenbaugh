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


public class WorldController extends InputAdapter 
{

	private static final String TAG = WorldController.class.getName();
	
	public Sprite[] testSprites;
	public int selectedSprite;
	public CameraHelper cameraHelper;
	
	public WorldController()
	{
		init();
	}
	
	/*
	 * initiate the drawing process
	 */
	private void init() 
	{
		Gdx.input.setInputProcessor(this);
		cameraHelper = new CameraHelper();
		initTestObjects();
	}
	
	/*
	 * initiate the test objects and fill them with the textures
	 */
	private void initTestObjects() 
	{
		//create new array for 5 sprite
		testSprites = new Sprite[5];
		//create a list of texture regions
		Array<TextureRegion> regions = new Array<TextureRegion>();
		regions.add(Assets.instance.bunny.head);
		regions.add(Assets.instance.feather.feather);
		regions.add(Assets.instance.goldCoin.goldCoin);
		//Create empty POT-sized Pixmap with 8 bit RGBA pixel data
		//int width = 32;
		//int height = 32;
		//Pixmap pixmap = createProceduralPixmap(width,height);
		//Create a new texture from pixmap data
		//Texture texture = new Texture(pixmap);
		//create new sprite using the just created texture
		for(int i = 0; i < testSprites.length; i++)
		{
			Sprite spr = new Sprite(regions.random());
			//Define sprite size to be 1m x 1m in game world
			spr.setSize(1,1);
			//Set origin to sprite's center
			spr.setOrigin(spr.getWidth() / 2.0f, spr.getHeight()/2.0f);
			//calculate random position for sprite
			float randomX =  MathUtils.random();
			float randomY = MathUtils.random();
			spr.setPosition(randomX, randomY);
			//put new sprite into array
			testSprites[i] = spr;
		}
		//set first sprite as selected one 
		selectedSprite = 0;
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
		updateTestObjects(deltaTime);
		cameraHelper.update(deltaTime);
	}
	
	/*
	 * handle keyboard input to control elements of the scene
	 */
	private void handlerDebugInput(float deltaTime) 
	{
		if(Gdx.app.getType() != ApplicationType.Desktop) return;
		
		//Selected Sprite Controls
		float sprMoveSpeed = 5 * deltaTime;
		if(Gdx.input.isKeyPressed(Keys.A)) 
			moveSelectedSprite(-sprMoveSpeed, 0);
		if(Gdx.input.isKeyPressed(Keys.D))
			moveSelectedSprite(sprMoveSpeed,0);
		if(Gdx.input.isKeyPressed(Keys.W))
			moveSelectedSprite(0,sprMoveSpeed);
		if(Gdx.input.isKeyPressed(Keys.S))
			moveSelectedSprite(0,-sprMoveSpeed);
		
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
	 * move a selected sprite with the translate function
	 */
	private void moveSelectedSprite (float x, float y)
	{
		testSprites[selectedSprite].translate(x, y);
		
	}

	/*
	 * update the objects as they are rotating
	 */
	private void updateTestObjects(float deltaTime) 
	{
		//Get current rotation from selected sprite
		float rotation = testSprites[selectedSprite].getRotation();
		//Rotate sprite by 90 degrees pre second
		rotation += 90 * deltaTime;
		//Wrap around at 360 degrees
		rotation %= 360;
		//Set new rotation value to selected sprite
		testSprites[selectedSprite].setRotation(rotation);
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
		//Select next sprite
		else if (keycode == Keys.SPACE)
		{
			selectedSprite = (selectedSprite + 1) % testSprites.length;
			//Update cameras target to follow the currently selected sprite
			if (cameraHelper.hasTarget())
			{
				cameraHelper.setTarget(testSprites[selectedSprite]);
			}
			Gdx.app.debug(TAG, "Sprite #" + selectedSprite +  "selected");
		}
		//Toggle camera follow
		else if (keycode == Keys.ENTER)
		{
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null : testSprites[selectedSprite]);
			Gdx.app.debug(TAG,  "Camera follow enabled: " + cameraHelper.hasTarget());
		}
		return false;
	}
	
	
}
