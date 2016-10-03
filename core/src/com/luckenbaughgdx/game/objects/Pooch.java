package com.luckenbaughgdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.luckenbaughgdx.game.Assets;
import com.luckenbaughgdx.game.util.Constants;

/*
 * controls the bunny head and its attributes
 */
public class Pooch extends AbstractGameObject
{
    public static final String TAG = Pooch.class.getName();

    private final float JUMP_TIME_MAX = 0.5f;

    private final float JUMP_TIME_MIN = 0.1f;

    private final float JUMP_TIME_OFFSET_FLYING = JUMP_TIME_MAX - 0.018f;

    public enum VIEW_DIRECTION
    {
        LEFT, RIGHT
    }

    public enum JUMP_STATE
    {
        GROUNDED, FALLING, JUMP_RISING, JUMP_FALLING
    }

    private TextureRegion regPooch;

    public VIEW_DIRECTION viewDirection;

    public float timeJumping;

    public JUMP_STATE jumpState;

    public boolean hasFeatherPowerup;

    public float timeLeftPilePowerdown;

    public Pooch()
    {
        init();
    }

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
        hasFeatherPowerup = false;
        timeLeftPilePowerdown = 0;
    }

    public void setJumping(boolean jumpKeyPressed)
    {
        switch (jumpState)
        {
        case GROUNDED: //Character is standing on a platform
            if (jumpKeyPressed)
            {
                timeJumping = 0;
                jumpState = JUMP_STATE.JUMP_RISING;
            }
            break;
        case JUMP_RISING: //rising in the air
            if (!jumpKeyPressed)
                jumpState = JUMP_STATE.JUMP_FALLING;
            if (jumpKeyPressed && hasFeatherPowerup)
            {
                jumpState = JUMP_STATE.JUMP_FALLING;
            }
            break;
        case FALLING: //falling down
        case JUMP_FALLING: //falling down after jump
            if (jumpKeyPressed && hasFeatherPowerup)
            {
              //  timeJumping = JUMP_TIME_OFFSET_FLYING;
                jumpState = JUMP_STATE.JUMP_FALLING;
            }
            break;
        }
    }

    public void setPilePowerdown(boolean pickedUp)
    {
        hasFeatherPowerup = pickedUp;
        if (pickedUp)
        {
            timeLeftPilePowerdown = Constants.ITEM_PILE_POWERDOWN_DURATION;
        }
    }

    public boolean hasFeatherPowerup()
    {
        return hasFeatherPowerup && timeLeftPilePowerdown > 0;
    }

    @Override
    public void update(float deltaTime)
    {
        super.update(deltaTime);
        if (velocity.x != 0)
        {
            viewDirection = velocity.x < 0 ? VIEW_DIRECTION.LEFT : VIEW_DIRECTION.RIGHT;
        }
        if (timeLeftPilePowerdown > 0)
        {
            timeLeftPilePowerdown -= deltaTime;
            if (timeLeftPilePowerdown < 0)
            {
                //disposable powerup
                timeLeftPilePowerdown = 0;
                setPilePowerdown(false);
            }
        }
    }

    @Override
    protected void updateMotionY(float deltaTime)
    {
        switch (jumpState)
        {
        case GROUNDED:
            jumpState = JUMP_STATE.FALLING;
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
            super.updateMotionY(deltaTime);
    }

    @Override
    public void render(SpriteBatch batch)
    {
        TextureRegion reg = null;

        //set special color when game object has a feather power up
        if (hasFeatherPowerup)
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
