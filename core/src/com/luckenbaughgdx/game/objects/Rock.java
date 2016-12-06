package com.luckenbaughgdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.luckenbaughgdx.game.Assets;

/*
 * sets the attribute for the rocks
 */
public class Rock extends AbstractGameObject
{

    private TextureRegion regEdge;

    private TextureRegion regMiddle;

    private int length;

    int landType;

    private AtlasRegion regMudEdge;

    private AtlasRegion regMudMiddle;

    public Rock(int type)
    {
        landType = type;
        init();
    }

    //initialize the drawing of the rock
    private void init()
    {
        dimension.set(1, 1);

        regEdge = Assets.instance.ground.edge;
        regMiddle = Assets.instance.ground.middle;
        regMudEdge = Assets.instance.ground.mudEdge;
        regMudMiddle = Assets.instance.ground.mudMiddle;

        //Start length of this rock
        setLength(1);

    }

    //set the initial length of the rock
    private void setLength(int length)
    {
        this.length = length;
        //update bounding box for collision detection
        bounds.set(0, 0, dimension.x * length, dimension.y);

    }

    public int getLandType()
    {
        return landType;
    }

    //increase the length of the rock
    public void increaseLength(int amount)
    {
        setLength(length + amount);
    }

    //render the rock edges and middle pieces
    @Override
    public void render(SpriteBatch batch)
    {
        TextureRegion reg = null;

        float relX = 0;
        float relY = 0;

        if (landType == 1)
        {
            //Draw left edge
            reg = regEdge;
            relX -= dimension.x / 4;
            batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x, origin.y, dimension.x / 4, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
                    reg.getRegionWidth(), reg.getRegionHeight(), false, false);

            //draw middle
            relX = 0;
            reg = regMiddle;
            for (int i = 0; i < length; i++)
            {
                batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
                        reg.getRegionWidth(), reg.getRegionHeight(), false, false);
                relX += dimension.x;
            }

            //draw right edge
            reg = regEdge;
            batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x + dimension.x / 8, origin.y, dimension.x / 4, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
                    reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), true, false);

        }

        if (landType == 2)
        {
            //Draw left edge
            reg = regMudEdge;
            relX -= dimension.x / 4;
            batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x, origin.y, dimension.x / 4, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
                    reg.getRegionWidth(), reg.getRegionHeight(), false, false);

            //draw middle
            relX = 0;
            reg = regMudMiddle;
            for (int i = 0; i < length; i++)
            {
                batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(),
                        reg.getRegionWidth(), reg.getRegionHeight(), false, false);
                relX += dimension.x;
            }

            //draw right edge
            reg = regMudEdge;
            batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x + dimension.x / 8, origin.y, dimension.x / 4, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(),
                    reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), true, false);

        }
    }

}
