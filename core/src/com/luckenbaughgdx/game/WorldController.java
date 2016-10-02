package com.luckenbaughgdx.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.luckenbaughgdx.game.objects.BunnyHead;
import com.luckenbaughgdx.game.objects.BunnyHead.JUMP_STATE;
import com.luckenbaughgdx.game.objects.Feather;
import com.luckenbaughgdx.game.objects.GoldCoin;
import com.luckenbaughgdx.game.objects.Rock;
import com.luckenbaughgdx.game.util.CameraHelper;
import com.luckenbaughgdx.game.util.Constants;

/*
 * control the objects int he game so that the world renderer can draw it
 */
public class WorldController extends InputAdapter
{

    //rectangles for collision detection
    private Rectangle r1 = new Rectangle();

    private Rectangle r2 = new Rectangle();

    private float timeLeftGameOverDelay;

    public boolean isGameOver()
    {
        return lives < 0;
    }

    public boolean isPlayerInWater()
    {
        return level.bunnyHead.position.y < -5;
    }

    private void onCollisionBunnyHeadWithRock(Rock rock)
    {
        BunnyHead bunnyHead = level.bunnyHead;
        float heightDifference = Math.abs(bunnyHead.position.y - (rock.position.y + rock.bounds.height));
        if (heightDifference > 0.25f)
        {
            boolean hitRightEdge = bunnyHead.position.x > (rock.position.x + rock.bounds.width / 2.0f);
            if (hitRightEdge)
                bunnyHead.position.x = rock.position.x + rock.bounds.width;
            else
                bunnyHead.position.x = rock.position.x - bunnyHead.bounds.width;
            return;
        }

        switch (bunnyHead.jumpState)
        {
        case GROUNDED:
            break;
        case FALLING:
        case JUMP_FALLING:
            bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
            bunnyHead.jumpState = JUMP_STATE.GROUNDED;
            break;
        case JUMP_RISING:
            bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
            break;
        }
    }

    private void onCollisionBunnyWithGoldCoin(GoldCoin goldcoin)
    {
        goldcoin.collected = true;
        score += goldcoin.getScore();
        Gdx.app.log(TAG, "Gold coin collected");
    }

    private void onCollisionBunnyWithFeather(Feather feather)
    {
        feather.collected = true;
        score += feather.getScore();
        level.bunnyHead.setFeatherPowerup(true);
        Gdx.app.log(TAG, "Feather collected");
    }

    private void testCollisions()
    {
        r1.set(level.bunnyHead.position.x, level.bunnyHead.position.y, level.bunnyHead.bounds.width, level.bunnyHead.bounds.height);
        //test collison bunny head with rocks
        for (Rock rock : level.rocks)
        {
            r2.set(rock.position.x, rock.position.y, rock.bounds.width, rock.bounds.height);
            if (!r1.overlaps(r2))
                continue;
            onCollisionBunnyHeadWithRock(rock);
            //IMPORTANT: must do all collisions for valid edge testing on rocks
        }

        //Test collsion: bunny head with a gold coin
        for (GoldCoin goldcoin : level.goldcoins)
        {
            if (goldcoin.collected)
                continue;
            r2.set(goldcoin.position.x, goldcoin.position.y, goldcoin.bounds.width, goldcoin.bounds.height);

            if (!(r1.overlaps(r2)))
                continue;

            onCollisionBunnyWithGoldCoin(goldcoin);

            break;
        }

        //Test collsion: bunny head with feathers
        for (Feather feather : level.feathers)
        {
            if (feather.collected)
                continue;
            r2.set(feather.position.x, feather.position.y, feather.bounds.width, feather.bounds.height);

            if (!r1.overlaps(r2))
                continue;
            onCollisionBunnyWithFeather(feather);
            break;
        }
    }

    private static final String TAG = WorldController.class.getName();

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
        level = new Level(Constants.LEVEL_01);
        cameraHelper.setTarget(level.bunnyHead);
    }

    /*
     * initiate the drawing process
     */
    private void init()
    {
        Gdx.input.setInputProcessor(this);
        cameraHelper = new CameraHelper();
        lives = Constants.LIVES_START - 1;
        timeLeftGameOverDelay = 0;
        initLevel();
    }

    /*
     * update the movement of the scene
     */
    public void update(float deltaTime)
    {
        handleDebugInput(deltaTime);
        if (isGameOver())
        {
            timeLeftGameOverDelay -= deltaTime;
            if (timeLeftGameOverDelay < 0)
                init();
        }
        else
        {
            handleInputGame(deltaTime);
        }
        level.update(deltaTime);
        testCollisions();
        cameraHelper.update(deltaTime);
        if (!isGameOver() && isPlayerInWater())
        {
            lives--;
            if (isGameOver())
                timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
            else
                initLevel();
        }
    }

    /*
     * handle keyboard input to control elements of the scene
     */
    private void handleDebugInput(float deltaTime)
    {
        if (Gdx.app.getType() != ApplicationType.Desktop)
            return;

        if (!cameraHelper.hasTarget(level.bunnyHead))
        {
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
                moveCamera(0, -camMoveSpeed);
            if (Gdx.input.isKeyPressed(Keys.BACKSPACE))
                cameraHelper.setPosition(0, 0);
        }

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
    private void moveCamera(float x, float y)
    {
        x += cameraHelper.getPosition().x;
        y += cameraHelper.getPosition().y;
        cameraHelper.setPosition(x, y);
    }

    /*
     * (non-Javadoc)
     * @see com.badlogic.gdx.InputAdapter#keyUp(int)
     * read keyboard input
     */
    @Override
    public boolean keyUp(int keycode)
    {
        //reset game world
        if (keycode == Keys.R)
        {
            init();
            Gdx.app.debug(TAG, "Game world reset");
        }
        //Toggle camera follow
        else if (keycode == Keys.ENTER)
        {
            cameraHelper.setTarget(cameraHelper.hasTarget() ? null : level.bunnyHead);
            Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
        }
        return false;
    }

    private void handleInputGame(float deltaTime)
    {
        if (cameraHelper.hasTarget(level.bunnyHead))
        {
            //player movement
            if (Gdx.input.isKeyPressed(Keys.LEFT))
            {
                level.bunnyHead.velocity.x = -level.bunnyHead.terminalVelocity.x;
            }
            else if (Gdx.input.isKeyPressed(Keys.RIGHT))
            {
                level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
            }
            else
            {
                //execute auto-forward movement on non-destop platform
                if (Gdx.app.getType() != ApplicationType.Desktop)
                {
                    level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
                }
            }

            //Bunny Jump
            if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE))
                level.bunnyHead.setJumping(true);
            else
                level.bunnyHead.setJumping(false);
        }
    }

}
