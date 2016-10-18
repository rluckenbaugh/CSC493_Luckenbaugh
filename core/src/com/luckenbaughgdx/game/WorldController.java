package com.luckenbaughgdx.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.luckenbaughgdx.game.objects.Bee;
import com.luckenbaughgdx.game.objects.Pile;
import com.luckenbaughgdx.game.objects.Pooch;
import com.luckenbaughgdx.game.objects.Pooch.JUMP_STATE;
import com.luckenbaughgdx.game.objects.Treat;
import com.luckenbaughgdx.game.objects.Rock;
import com.luckenbaughgdx.game.screens.MenuScreen;
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

    private Game game;

    private static final String TAG = WorldController.class.getName();

    public Level level;

    public int lives;

    public int score;

    public static CameraHelper cameraHelper;

    public float livesVisual;

    float scoreVisual;

    public WorldController(Game game)
    {
        this.game = game;
        init();
    }

    
    /*
     * initiate the level
     */
    private void initLevel()
    {
        score = 0;
        level = new Level(Constants.LEVEL_01);
        cameraHelper.setTarget(level.pooch);
    }

    /*
     * initiate the drawing process
     */
    private void init()
    {
        Gdx.input.setInputProcessor(this);
        cameraHelper = new CameraHelper();
        lives = Constants.LIVES_START-1;
        livesVisual = lives;
        timeLeftGameOverDelay = 0;
        initLevel();
    }
    
    private void backToMenu()
    {
        //switch to menu screen
        game.setScreen(new MenuScreen(game));
    }

    /*
     * check if the game is over or not
     */
    public boolean isGameOver()
    {
        return lives < 0;
    }

    /*
     * check if the player is in the water
     */
    public boolean isPlayerInWater()
    {
        return level.pooch.position.y < -5;
    }

    /*
     * when the bunny collides with a rock it should not fall through
     */
    private void onCollisionBunnyHeadWithRock(Rock rock)
    {
        Pooch pooch = level.pooch;
        float heightDifference = Math.abs(pooch.position.y - (rock.position.y + rock.bounds.height));
        if (heightDifference > 0.5f)
        {
            boolean hitRightEdge = pooch.position.x > (rock.position.x + rock.bounds.width / 2.0f);
            if (hitRightEdge)
                pooch.position.x = rock.position.x + rock.bounds.width;
            else
                pooch.position.x = rock.position.x - pooch.bounds.width;
            return;
        }

        switch (pooch.jumpState)
        {
        case GROUNDED:
            break;
        case FALLING:
        case JUMP_FALLING:
            pooch.position.y = rock.position.y + pooch.bounds.height;
            pooch.jumpState = JUMP_STATE.GROUNDED;
            break;
        case JUMP_RISING:
            pooch.position.y = rock.position.y + pooch.bounds.height + pooch.origin.y;
            break;
        }
    }

    /*
     * when the bunny collides with a treat it should collect it
     */
    private void onCollisionBunnyWithTreat(Treat treat)
    {
        treat.collected = true;
        score += treat.getScore();
        Gdx.app.log(TAG, "Gold coin collected");
    }

    /*
     * when the bunny collides with a pile it can't jump as high
     */
    private void onCollisionBunnyWithPile(Pile pile)
    {
        pile.collected = true;
        score += pile.getScore();
        level.pooch.setPilePowerdown(true);
        Gdx.app.log(TAG, "Pile Hit");
    }

    /*
     * when the bunny collides with a bee it should loose points
     */
    private void onCollisionBunnyWithBee(Bee bee)
    {
        bee.collected = true;
        score += bee.getScore();
        Gdx.app.log(TAG, "Bee Hit");

    }

    /*
     * test all of the collisions 
     */
    private void testCollisions()
    {
        r1.set(level.pooch.position.x, level.pooch.position.y, level.pooch.bounds.width, level.pooch.bounds.height);
        //test collison bunny head with rocks
        for (Rock rock : level.rocks)
        {
            r2.set(rock.position.x, rock.position.y, rock.bounds.width, rock.bounds.height);
            if (!r1.overlaps(r2))
                continue;
            onCollisionBunnyHeadWithRock(rock);
            //IMPORTANT: must do all collisions for valid edge testing on rocks
        }

        //Test collsion: pooch with a treat
        for (Treat treat : level.treats)
        {
            if (treat.collected)
                continue;
            r2.set(treat.position.x, treat.position.y, treat.bounds.width, treat.bounds.height);

            if (!(r1.overlaps(r2)))
                continue;

            onCollisionBunnyWithTreat(treat);

            break;
        }

        //Test collsion: pooch with piles
        for (Pile pile : level.piles)
        {

            if (pile.collected)
                continue;
            r2.set(pile.position.x, pile.position.y, pile.bounds.width, pile.bounds.height);

            if (!r1.overlaps(r2))
                continue;
            onCollisionBunnyWithPile(pile);
            break;
        }

        //Test collsion: pooch with bees
        for (Bee bee : level.bees)
        {

            if (bee.collected)
                continue;
            r2.set(bee.position.x, bee.position.y, bee.bounds.width, bee.bounds.height);

            if (!r1.overlaps(r2))
                continue;
            onCollisionBunnyWithBee(bee);
            break;
        }
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
                backToMenu();
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
        if(livesVisual>lives)
            livesVisual = Math.max(lives, livesVisual - 1 * deltaTime);
        if(scoreVisual< score)
            scoreVisual= Math.min(score, scoreVisual + 250 * deltaTime);
            
    }

    /*
     * handle keyboard input to control elements of the scene
     */
    private void handleDebugInput(float deltaTime)
    {
        if (Gdx.app.getType() != ApplicationType.Desktop)
            return;

        if (!cameraHelper.hasTarget(level.pooch))
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
            cameraHelper.setTarget(cameraHelper.hasTarget() ? null : level.pooch);
            Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
        }
        //Back to Menu
        else if ( keycode == Keys.ESCAPE || keycode == Keys.BACK)
        {
            backToMenu();
        }
        return false;
    }

    private void handleInputGame(float deltaTime)
    {
        if (cameraHelper.hasTarget(level.pooch))
        {
            //player movement
            if (Gdx.input.isKeyPressed(Keys.LEFT))
            {
                level.pooch.velocity.x = -level.pooch.terminalVelocity.x;
            }
            else if (Gdx.input.isKeyPressed(Keys.RIGHT))
            {
                level.pooch.velocity.x = level.pooch.terminalVelocity.x;
            }
            else
            {
                //execute auto-forward movement on non-destop platform
                if (Gdx.app.getType() != ApplicationType.Desktop)
                {
                    level.pooch.velocity.x = level.pooch.terminalVelocity.x;
                }
            }

            //Bunny Jump
            if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE))
                level.pooch.setJumping(true);
            else
                level.pooch.setJumping(false);
        }
    }

}
