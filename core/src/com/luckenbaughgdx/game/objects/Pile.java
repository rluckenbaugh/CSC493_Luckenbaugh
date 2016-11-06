package com.luckenbaughgdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.luckenbaughgdx.game.Assets;

/*
 * controls the feather and its attributes
 */
public class Pile extends AbstractGameObject
{
    private TextureRegion regPile;
    public boolean collected;

    public Pile()
    {
        init();
    }

    /*
     * initate the image of the pile
     */
    private void init()
    {
        dimension.set(0.5f, 0.25f);

        regPile = Assets.instance.pile.pile;

        //Set bounding box for collection detection
        bounds.set(0, 0, dimension.x, dimension.y);
        collected = false;
    }

    /*
     * render the pile
     * (non-Javadoc)
     * @see com.luckenbaughgdx.game.objects.AbstractGameObject#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
     */
    @Override
    public void render(SpriteBatch batch)
    {
        if (collected)
            return;
        TextureRegion reg = null;
        reg = regPile;
        batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
                reg.getRegionHeight(), false, false);
    }

    /*
     * return the score
     */
    public int getScore()
    {
        return -100;
    }
}
