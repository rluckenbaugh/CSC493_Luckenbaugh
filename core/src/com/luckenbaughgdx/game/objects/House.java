package com.luckenbaughgdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.luckenbaughgdx.game.Assets;

public class House extends AbstractGameObject
{

    private TextureRegion regHouse;

    public House()
    {
        init();
    }

    /*
     * initate the house
     */
    private void init()
    {
        dimension.set(2.0f, 2.0f);

        regHouse = Assets.instance.levelDecoration.house;

        //Set bounding box for collection detection
        bounds.set(0, 0, dimension.x, dimension.y);

    }

    /*
     * render the house
     * (non-Javadoc)
     * @see com.luckenbaughgdx.game.objects.AbstractGameObject#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
     */
    @Override
    public void render(SpriteBatch batch)
    {
        TextureRegion reg = regHouse;
        batch.draw(reg.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
                reg.getRegionHeight(), false, false);
    }

}
