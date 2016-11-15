package com.luckenbaughgdx.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.luckenbaughgdx.game.objects.AbstractGameObject;
import com.luckenbaughgdx.game.objects.Bee;
import com.luckenbaughgdx.game.objects.Bone;
import com.luckenbaughgdx.game.objects.Clouds;
import com.luckenbaughgdx.game.objects.House;
import com.luckenbaughgdx.game.objects.Pile;
import com.luckenbaughgdx.game.objects.Pooch;
import com.luckenbaughgdx.game.objects.Treat;
import com.luckenbaughgdx.game.objects.Rock;
import com.luckenbaughgdx.game.screens.MenuScreen;
import com.luckenbaughgdx.game.util.AudioManager;
import com.luckenbaughgdx.game.util.CameraHelper;
import com.luckenbaughgdx.game.util.CollisionHandler;
import com.luckenbaughgdx.game.util.Constants;

/*
 * control the objects int he game so that the world renderer can draw it
 */
public class WorldController extends InputAdapter implements Disposable
{
    public float timeLeftGameOverDelay;

    private Game game;

    private static final String TAG = WorldController.class.getName();

    public Array<AbstractGameObject> objectsToRemove;

    public Level level;

    public int lives;

    public int score;

    public static CameraHelper cameraHelper;

    public float livesVisual;

    float scoreVisual;

    public boolean goalReached;
    
    public boolean spacePressed;

    // Box2D Collisions
    public World b2World;

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
        scoreVisual = score;
        goalReached = false;
        level = new Level(Constants.LEVEL_01);
        cameraHelper.setTarget(level.pooch);

        initPhysics();
    }

    /*
     * Initiate the physics
     */
    private void initPhysics()
    {
        if (b2World != null)
            b2World.dispose();
        b2World = new World(new Vector2(0, -9.81f), true);
        b2World.setContactListener(new CollisionHandler(this)); // Not in the book

        //Rocks
        Vector2 origin = new Vector2();

        //initiate physics for each object??
        for (Rock rock : level.rocks)
        {
            BodyDef rockbodyDef = new BodyDef();
            rockbodyDef.type = BodyType.KinematicBody;
            rockbodyDef.position.set(rock.position);
            Body rockbody = b2World.createBody(rockbodyDef);
            rockbody.setUserData(rock);
            rock.body = rockbody;
            PolygonShape rockpolygonShape = new PolygonShape();
            origin.x = rock.bounds.width / 2.0f;
            origin.y = rock.bounds.height / 2.0f;
            rockpolygonShape.setAsBox(rock.bounds.width / 2.0f, rock.bounds.height / 2.0f, origin, 0);
            FixtureDef rockfixtureDef = new FixtureDef();
            rockfixtureDef.shape = rockpolygonShape;
            rockbody.createFixture(rockfixtureDef);
            rockpolygonShape.dispose();
        }

        // For PLayer
        Pooch pooch = level.pooch;
        BodyDef poochbodyDef = new BodyDef();
        poochbodyDef.position.set(pooch.position);
        poochbodyDef.fixedRotation = true;

        Body poochBody = b2World.createBody(poochbodyDef);
        poochBody.setType(BodyType.DynamicBody);
        poochBody.setGravityScale(0.5f);
        poochBody.setUserData(pooch);
        pooch.body = poochBody;

        PolygonShape poochpolygonShape = new PolygonShape();
        origin.x = (pooch.bounds.width) / 2.0f;
        origin.y = (pooch.bounds.height) / 2.0f;
        poochpolygonShape.setAsBox((pooch.bounds.width - 0.7f) / 2.0f, (pooch.bounds.height - 0.15f) / 2.0f, origin, 0);

        FixtureDef poochfixtureDef = new FixtureDef();
        poochfixtureDef.shape = poochpolygonShape;
        // fixtureDef.friction = 0.5f;
        poochBody.createFixture(poochfixtureDef);
        poochpolygonShape.dispose();
        
        // For bone
        Bone bone = level.bone;
        BodyDef bonebodyDef = new BodyDef();
        bonebodyDef.position.set(bone.position);
        bonebodyDef.fixedRotation = true;

        Body boneBody = b2World.createBody(bonebodyDef);
        boneBody.setType(BodyType.KinematicBody);
        boneBody.setGravityScale(0.5f);
        boneBody.setUserData(bone);
        bone.body = boneBody;

        PolygonShape bonepolygonShape = new PolygonShape();
        origin.x = (bone.bounds.width) / 2.0f;
        origin.y = (bone.bounds.height) / 2.0f;
        bonepolygonShape.setAsBox((bone.bounds.width - 0.7f) / 2.0f, (bone.bounds.height - 0.15f) / 2.0f, origin, 0);

        FixtureDef bonefixtureDef = new FixtureDef();
        bonefixtureDef.shape = bonepolygonShape;
        // fixtureDef.friction = 0.5f;
        boneBody.createFixture(bonefixtureDef);
        bonepolygonShape.dispose();

        for (Treat treat : level.treats)
        {
            BodyDef treatBodyDef = new BodyDef();
            treatBodyDef.type = BodyType.KinematicBody;
            treatBodyDef.position.set(treat.position);
            Body treatBody = b2World.createBody(treatBodyDef);
            treatBody.setUserData(treat);
            treat.body = treatBody;
            PolygonShape treatpolygonShape = new PolygonShape();
            origin.x = treat.bounds.width / 2.0f;
            origin.y = treat.bounds.height / 2.0f;
            treatpolygonShape.setAsBox(treat.bounds.width / 2.0f, treat.bounds.height / 2.0f, origin, 0);
            FixtureDef treatfixtureDef = new FixtureDef();
            treatfixtureDef.shape = treatpolygonShape;
            treatBody.createFixture(treatfixtureDef);
            treatpolygonShape.dispose();
        }

        for (Bee bee : level.bees)
        {
            BodyDef beeBodyDef = new BodyDef();
            beeBodyDef.type = BodyType.KinematicBody;
            beeBodyDef.position.set(bee.position);
            Body beeBody = b2World.createBody(beeBodyDef);
            beeBody.setUserData(bee);
            bee.body = beeBody;
            PolygonShape beepolygonShape = new PolygonShape();
            origin.x = bee.bounds.width / 2.0f;
            origin.y = bee.bounds.height / 2.0f;
            beepolygonShape.setAsBox(bee.bounds.width / 2.0f, bee.bounds.height / 2.0f, origin, 0);
            FixtureDef beeFixtureDef = new FixtureDef();
            beeFixtureDef.shape = beepolygonShape;
            beeBody.createFixture(beeFixtureDef);
            beepolygonShape.dispose();
        }

        for (Pile pile : level.piles)
        {
            BodyDef pileBodyDef = new BodyDef();
            pileBodyDef.type = BodyType.KinematicBody;
            pileBodyDef.position.set(pile.position);
            Body pileBody = b2World.createBody(pileBodyDef);
            pileBody.setUserData(pile);
            pile.body = pileBody;
            PolygonShape pilePolygonShape = new PolygonShape();
            origin.x = pile.bounds.width / 2.0f;
            origin.y = pile.bounds.height / 2.0f;
            pilePolygonShape.setAsBox(pile.bounds.width / 2.0f, pile.bounds.height / 2.0f, origin, 0);
            FixtureDef pileFixtureDef = new FixtureDef();
            pileFixtureDef.shape = pilePolygonShape;
            pileBody.createFixture(pileFixtureDef);
            pilePolygonShape.dispose();
        }

    }

    /*
     * initiate the drawing process
     */
    private void init()
    {
        objectsToRemove = new Array<AbstractGameObject>();
        Gdx.input.setInputProcessor(this);
        cameraHelper = new CameraHelper();
        lives = Constants.LIVES_START - 1;
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
  

    public void flagForRemoval(AbstractGameObject obj)
    {
        objectsToRemove.add(obj);
    }

    /*
     * update the movement of the scene
     */
    public void update(float deltaTime)
    {

        // Because the Box2D step function is not running I know
        // that nothing new is being added to objectsToRemove.
        if (objectsToRemove.size > 0)
        {
            for (AbstractGameObject obj : objectsToRemove)
            {
                if (obj instanceof Bee)
                {
                    int index = level.bees.indexOf((Bee) obj, true);
                    if (index != -1)
                    {
                        level.bees.removeIndex(index);
                        b2World.destroyBody(obj.body);
                    }
                }
                if (obj instanceof Treat)
                {
                    int index = level.treats.indexOf((Treat) obj, true);
                    if (index != -1)
                    {
                        level.treats.removeIndex(index);
                        b2World.destroyBody(obj.body);
                    }
                }
                if (obj instanceof Pile)
                {
                    int index = level.piles.indexOf((Pile) obj, true);
                    if (index != -1)
                    {
                        level.piles.removeIndex(index);
                        b2World.destroyBody(obj.body);
                    }
                }
            }
            objectsToRemove.removeRange(0, objectsToRemove.size - 1);
        }


        b2World.step(deltaTime, 8, 3); // Tell the Box2D world to update.
        level.update(deltaTime);
        //checkForCollisions();

        cameraHelper.update(deltaTime);
        handleDebugInput(deltaTime);
        if (isGameOver() || goalReached)
        {
            timeLeftGameOverDelay -= deltaTime;
            if (timeLeftGameOverDelay < 0)
                backToMenu();
        }
        else
        {
            handleInputGame(deltaTime);
        }
        b2World.step(deltaTime, 8, 3);
        cameraHelper.update(deltaTime);
        if (!isGameOver() && isPlayerInWater())
        {
            AudioManager.instance.play(Assets.instance.sounds.liveLost);
            lives--;
            if (isGameOver())
                timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
            else
                initLevel();
        }
        if (livesVisual > lives)
            livesVisual = Math.max(lives, livesVisual - 1 * deltaTime);
        if (scoreVisual < score)
            scoreVisual = Math.min(score, scoreVisual + 250 * deltaTime);

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
        else if (keycode == Keys.ESCAPE || keycode == Keys.BACK)
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

            //Pooch Jump
            if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE) && spacePressed == false)
            {

                level.pooch.setJumping(true);
                
            }

            else
                level.pooch.setJumping(false);
        }
    }

    public void spawnCarrots(Vector2 pos, int numCarrots, float radius)
    {
        float carrotShapeScale = 0.5f;
        //create carrots with box2d body and fixture
        for (int i = 0; i < numCarrots; i++)
        {
            Treat carrot = new Treat();
            //calculate random spawn position, rotation, and scale
            float x = MathUtils.random(-radius, radius);
            float y = MathUtils.random(5.0f, 15.0f);
            float rotation = MathUtils.random(0.0f, 360.0f) * MathUtils.degreesToRadians;
            float carrotScale = MathUtils.random(0.5f, 1.5f);
            carrot.scale.set(carrotScale, carrotScale);
            // create box2d body for carrot with start position
            //and angle of rotation
            BodyDef bodyDef = new BodyDef();
            bodyDef.position.set(pos);
            bodyDef.position.add(x, y);
            bodyDef.angle = rotation;
            Body body = b2World.createBody(bodyDef);
            body.setType(BodyType.DynamicBody);
            carrot.body = body;
            //create rectangular shape for carrot to allow interactionswith other objects
            PolygonShape polygonShape = new PolygonShape();
            float halfWidth = carrot.bounds.width / 2.0f * carrotScale;
            float halfHeight = carrot.bounds.height / 2.0f * carrotScale;
            polygonShape.setAsBox(halfWidth * carrotShapeScale, halfHeight * carrotShapeScale);
            //set physics attributes
            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.shape = polygonShape;
            fixtureDef.density = 50;
            fixtureDef.restitution = 0.5f;
            fixtureDef.friction = 0.5f;
            body.createFixture(fixtureDef);
            polygonShape.dispose();
            //finally add new carrot to list for updating/rendering
            level.treats.add(carrot);
        }
    }

    @Override
    public void dispose()
    {
        if (b2World != null)
            b2World.dispose();

    }

}
