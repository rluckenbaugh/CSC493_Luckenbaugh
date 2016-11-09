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


    // private TextureRegion regPooch;

    public VIEW_DIRECTION viewDirection;

    public float timeJumping;

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
            hasPilePowerdown = true;
        }
        else
            hasPilePowerdown = false;
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

        
        dustParticles.setPosition(position.x + dimension.x / 2, position.y);
        //dustParticles.start();
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
