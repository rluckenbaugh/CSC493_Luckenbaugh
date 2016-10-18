package com.luckenbaughgdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Disposable;
import com.luckenbaughgdx.game.util.Constants;
import com.luckenbaughgdx.game.util.GamePreferences;

/*
 * draw the game scene
 */
public class WorldRenderer implements Disposable
{

    private OrthographicCamera camera;

    private OrthographicCamera cameraGUI;

    private SpriteBatch batch;

    private WorldController worldController;

    /*
     * initiate the rendering
     */
    public WorldRenderer(WorldController worldController2)
    {
        this.worldController = worldController2;
        init();
    }

    /*
     * initiate all of the parts of the scene
     */
    private void init()
    {
        batch = new SpriteBatch();
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(0, 0, 0);
        camera.update();
        cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);

        cameraGUI.position.set(0, 0, 0);
        cameraGUI.setToOrtho(true); //flip y-axis
        cameraGUI.update();
    }

    /*
     * render function
     */
    public void render()
    {
        renderWorld(batch);
        renderGui(batch);
    }

    /*
     * render the world
     */
    private void renderWorld(SpriteBatch batch)
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
    public void resize(int width, int height)
    {
        camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
        camera.update();
        cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
        cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT / (float) height) * (float) width;
        cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
        cameraGUI.update();
    }

    /*
     * render the score
     */
    private void renderGuiScore(SpriteBatch batch)
    {
        float x = -15;
        float y = -15;
        float offsetX = 50;
        float offsetY = 50;
        if(worldController.scoreVisual< worldController.score)
        {
            long shakeAlpha = System.currentTimeMillis() % 360;
            float shakeDist = 1.5f;
            offsetX += MathUtils.sinDeg(shakeAlpha * 2.2f) * shakeDist;
            offsetY += MathUtils.sinDeg(shakeAlpha * 2.9f) * shakeDist;
        }
        batch.draw(Assets.instance.treat.treats, x, y, offsetX, offsetY, 100, 100, 0.35f, -0.35f, 0);
        Assets.instance.fonts.defaultBig.draw(batch, "" + (int)worldController.scoreVisual, x + 75, y + 37);
    }

    /*
     * render the lives into the gui
     */
    private void renderGuiExtraLive(SpriteBatch batch)
    {
        float x = cameraGUI.viewportWidth - 50 - Constants.LIVES_START * 50;
        float y = -15;
        for (int i = 0; i < Constants.LIVES_START; i++)
        {
            batch.setColor(0.5f,0.5f,0.5f,0.5f);
            batch.draw(Assets.instance.pooch.stand, x + i * 50, y, 50, 50, 120, 100, 0.35f, -0.35f, 0);
            
            if (worldController.lives >= i)
            {
                batch.setColor(1, 1, 1, 1);
                batch.draw(Assets.instance.pooch.stand, x + i * 50, y, 50, 50, 120, 100, 0.35f, -0.35f, 0);
            }
        }
        if(worldController.lives>=0 && worldController.livesVisual>worldController.lives)
        {
            int i = worldController.lives;
            float alphaColor = Math.max(0, worldController.livesVisual - worldController.lives - 0.5f);
            float alphaScale = 0.35f * (2 + worldController.lives - worldController.livesVisual) * 2;
            float alphaRotate = -45 * alphaColor;
            batch.setColor(1.0f, 0.7f, 0.7f, alphaColor);
            batch.draw(Assets.instance.pooch.stand, x + i * 50, y, 50, 50, 120, 100, alphaScale, -alphaScale, alphaRotate);
            batch.setColor(1,1,1,1);
        }
    }

    /*
     * GUI FPS counter implementation
     */
    private void renderGuiFpsCounter(SpriteBatch batch)
    {
        float x = cameraGUI.viewportWidth - 55;
        float y = cameraGUI.viewportHeight - 15;
        int fps = Gdx.graphics.getFramesPerSecond();
        BitmapFont fpsFont = Assets.instance.fonts.defaultNormal;
        if (fps >= 45)
        {
            //45 or more FPS show up in green
            fpsFont.setColor(0, 1, 0, 1);
        }
        else if (fps >= 30)
        {
            //30 or more fps show up in yellow
            fpsFont.setColor(1, 1, 0, 1);
        }
        else
        {
            //less than 30 fps show up in red
            fpsFont.setColor(1, 0, 0, 1);
        }
        fpsFont.draw(batch, "FPS: " + fps, x, y);
        fpsFont.setColor(1, 1, 1, 1); //white
    }

    /*
     * render the GUI
     */
    private void renderGui(SpriteBatch batch)
    {
        batch.setProjectionMatrix(cameraGUI.combined);
        batch.begin();
        //draw collected gold coins icon and text
        //anchored to top left edge
        renderGuiScore(batch);
        //draw collected feather icon at the top right corner
        renderGuiPilePowerdown(batch);
        //draw extra lives icon and text anchored to top right edge
        renderGuiExtraLive(batch);
        //draw fps text anchored to bottom right edge
        if (GamePreferences.instances.showFpsCounter)
            renderGuiFpsCounter(batch);
        //draw game over text
        renderGuiGameOverMessage(batch);
        batch.end();
    }

    /*
     * dispose of the unneeded code
     * (non-Javadoc)
     * @see com.badlogic.gdx.utils.Disposable#dispose()
     */
    @Override
    public void dispose()
    {
        batch.dispose();
    }

    /*
     * render that the game is over
     */
    private void renderGuiGameOverMessage(SpriteBatch batch)
    {
        float x = cameraGUI.viewportWidth / 2;
        float y = cameraGUI.viewportHeight / 2;

        if (worldController.isGameOver())
        {
            BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
            fontGameOver.setColor(1, 0.75f, 0.25f, 1);
            fontGameOver.draw(batch, "GAME OVER", x, y, 0, 1, false);
            fontGameOver.setColor(1, 1, 1, 1);
        }
    }

    /*
     * render the image of the pile powerdown
     */
    private void renderGuiPilePowerdown(SpriteBatch batch)
    {
        float x = -15;
        float y = 30;
        float timeLeftFeatherPowerup = worldController.level.pooch.timeLeftPilePowerdown;
        if (timeLeftFeatherPowerup > 0)
        {
            //starticon fade in/out if the power up time is less thann four seconds the fade interval is set to 5 changes per second
            if (timeLeftFeatherPowerup < 4)
            {
                if (((int) (timeLeftFeatherPowerup * 5) % 2) != 0)
                {
                    batch.setColor(1, 1, 1, 0.5f);
                }
            }
            batch.draw(Assets.instance.pile.pile, x, y, 50, 50, 100, 100, 0.35f, -0.35f, 0);
            batch.setColor(1, 1, 1, 1);
            Assets.instance.fonts.defaultSmall.draw(batch, "" + (int) timeLeftFeatherPowerup, x + 60, y + 57);
        }
    }
}
