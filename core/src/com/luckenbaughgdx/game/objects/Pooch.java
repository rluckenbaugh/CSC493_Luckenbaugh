package com.luckenbaughgdx.game.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.luckenbaughgdx.game.Assets;
import com.luckenbaughgdx.game.util.AudioManager;
import com.luckenbaughgdx.game.util.CharacterSkin;
import com.luckenbaughgdx.game.util.Constants;
import com.luckenbaughgdx.game.util.GamePreferences;

/*
 * controls the bunny head and its attributes
 */
public class Pooch extends AbstractGameObject
{
    public static final String TAG = Pooch.class.getName();

    private final float JUMP_TIME_MAX = 0.5f;

    private final float JUMP_TIME_MIN = 0.1f;

    private final float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX - 0.018f;

    public ParticleEffect dustParticles = new ParticleEffect();

    public enum VIEW_DIRECTION
    {
        LEFT, RIGHT
    }

    public enum JUMP_STATE
    {
        GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING
    }

    // private TextureRegion regPooch;

    public VIEW_DIRECTION viewDirection;

    public float timeJumping;

    public JUMP_STATE jumpState;

    public boolean hasPilePowerdown;

    public float timeLeftPilePowerdown;

    TextureRegion regPooch;

    public Pooch()
    {
        init();
    }

    /*
     * initate the pooch and all its attributes
     */
    public void init()
    {
        dimension.set(1, 1);

        regPooch = Assets.instance.pooch.stand;
        //center image on game object

        origin.set(dimension.x / 2, dimension.y / 2);
        //bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
        //set physics values
        terminalVelocity.set(3.0f, 4.0f);
        friction.set(12.0f, 0.0f);
        acceleration.set(0.0f, -25.0f);
        //view direction
        viewDirection = VIEW_DIRECTION.RIGHT;
        //jump state
        jumpState = JUMP_STATE.FALLING;
        timeJumping = 0;
        //power ups
        hasPilePowerdown = false;
        timeLeftPilePowerdown = 0;

        //Particles
        dustParticles.load(Gdx.files.internal("effects/CanyonBunnyDust.pfx"), Gdx.files.internal("effects"));
    }

    /*
     * set the jumping states
     */
    public void setJumping(boolean jumpKeyPressed)
    {

        if (jumpKeyPressed)
        {
            velocity.y = terminalVelocity.y;
            body.setLinearVelocity(velocity);
            position.set(body.getPosition());
        }
        /* switch (jumpState)
         {
         case GROUNDED: //Character is standing on a platform
             if (jumpKeyPressed)
             {
                 AudioManager.instance.play(Assets.instance.sounds.jump);
                 timeJumping = 0;
                 jumpState = JUMP_STATE.JUMP_RISING;
             }
             else if (velocity.x != 0)
             {
                 //Gdx.app.log(TAG, "starting particles");
                 dustParticles.setPosition(position.x + dimension.x / 2, position.y + 0.1f);
                 dustParticles.start();
             }
             else if (velocity.x == 0)
             {
                 dustParticles.allowCompletion();
             }
             break;
         case JUMP_RISING: //rising in the air
             if (!jumpKeyPressed)
                 jumpState = JUMP_STATE.JUMP_FALLING;
             if (jumpKeyPressed && hasPilePowerdown)
             {
                 jumpState = JUMP_STATE.JUMP_FALLING;
             }
             break;
         case FALLING: //falling down
         case JUMP_FALLING: //falling down after jump
             if (jumpKeyPressed && hasPilePowerdown)
             {
                 AudioManager.instance.play(Assets.instance.sounds.jumpWithPile, 1, MathUtils.random(1.0f, 1.1f));

                 //  timeJumping = JUMP_TIME_OFFSET_FLYING;
                 jumpState = JUMP_STATE.JUMP_FALLING;
             }
             break;
         }*/
    }

    /*
     * set the pile powerdown
     */
    public void setPilePowerdown(boolean pickedUp)
    {
        hasPilePowerdown = pickedUp;
        if (pickedUp)
        {
            timeLeftPilePowerdown = Constants.ITEM_PILE_POWERDOWN_DURATION;
        }
    }

    /*
     * check if it has the pile powerdown
     */
    public boolean hasPilePowerdown()
    {
        return hasPilePowerdown && timeLeftPilePowerdown > 0;
    }

    @Override
    public void update(float deltaTime)
    {
        super.update(deltaTime);

        updateMotionX(deltaTime);
        updateMotionY(deltaTime);

        if (body != null)
        {
            // Gdx.app.log(TAG, "velY: "+velocity.y+" state: "+jumpState);
            body.setLinearVelocity(velocity);
            position.set(body.getPosition());
        }

        dustParticles.update(deltaTime);
        if (velocity.x != 0)
        {
            viewDirection = velocity.x < 0 ? VIEW_DIRECTION.LEFT : VIEW_DIRECTION.RIGHT;
        }
        if (timeLeftPilePowerdown > 0)
        {
            timeLeftPilePowerdown -= deltaTime;

            terminalVelocity.set(2.0f, 4.0f);
            if (timeLeftPilePowerdown < 0)
            {
                terminalVelocity.set(3.0f, 4.0f);
                //disposable powerup
                timeLeftPilePowerdown = 0;
                setPilePowerdown(false);
            }
        }
    }

    /*
     * update the motion in the y direction
     * (non-Javadoc)
     * @see com.luckenbaughgdx.game.objects.AbstractGameObject#updateMotionY(float)
     */
    @Override
    protected void updateMotionY(float deltaTime)
    {
        switch (jumpState)
        {
        case GROUNDED:
            jumpState = JUMP_STATE.FALLING;
            if (velocity.x != 0)
            {
                dustParticles.setPosition(position.x + dimension.x / 2, position.y);
                dustParticles.start();
            }
            break;
        case JUMP_RISING:
            //Keep track of jump time
            timeJumping += deltaTime;
            //jump time left
            if (timeJumping <= JUMP_TIME_MAX)
            {
                //still jumping
                velocity.y = terminalVelocity.y;
            }
            break;
        case FALLING:
            break;
        case JUMP_FALLING:
            //add deltaTime to track the jump time
            timeJumping += deltaTime;
            //jump to minimal height if jump key was pressed too short
            if (timeJumping > 0 && timeJumping <= JUMP_TIME_MIN)
            {
                //still jumping
                velocity.y = terminalVelocity.y;
            }
        }
        if (jumpState != JUMP_STATE.GROUNDED)
        {
            super.updateMotionY(deltaTime);
            dustParticles.allowCompletion();
        }
    }

    /*
     * render the pooch
     * (non-Javadoc)
     * @see com.luckenbaughgdx.game.objects.AbstractGameObject#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
     */
    @Override
    public void render(SpriteBatch batch)
    {
        TextureRegion reg = null;

        //reset color to white
        batch.setColor(1, 1, 1, 1);
        dustParticles.draw(batch);

        //apply skin color
        batch.setColor(CharacterSkin.values()[GamePreferences.instances.charSkin].getColor());

        //set special color when game object has a feather power up
        if (hasPilePowerdown)
        {
            batch.setColor(0.0f, 0.87f, 0.8f, 1.0f);
        }
        //draw image
        reg = regPooch;
        batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
                reg.getRegionHeight(), viewDirection == VIEW_DIRECTION.LEFT, false);

        //reset color to white
        batch.setColor(1, 1, 1, 1);

    }

}
