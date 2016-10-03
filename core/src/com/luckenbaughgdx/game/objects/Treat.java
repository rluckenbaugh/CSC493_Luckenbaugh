package com.luckenbaughgdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.luckenbaughgdx.game.Assets;

/*
 * controls the gold coin and its attributes
 */
public class Treat extends AbstractGameObject
{

    private TextureRegion regTreat;

    public boolean collected;

    public Treat()
    {
        init();
    }

    /*
     * initate the treat 
     */
    private void init()
    {
        dimension.set(0.5f, 0.5f);

        regTreat = Assets.instance.treat.treats;

        //Set bounding box for collection detection
        bounds.set(0, 0, dimension.x, dimension.y);

        collected = false;

    }

    /*
     * render the image
     * (non-Javadoc)
     * @see com.luckenbaughgdx.game.objects.AbstractGameObject#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
     */
    @Override
    public void render(SpriteBatch batch)
    {
        if (collected)
            return;

        TextureRegion reg = null;
        reg = regTreat;
        batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
                reg.getRegionHeight(), false, false);
    }

    /*
     * get the score from the treat
     */
    public int getScore()
    {
        return 100;
    }
}
