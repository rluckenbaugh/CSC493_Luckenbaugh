package com.luckenbaughgdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.luckenbaughgdx.game.Assets;

public class Bone extends AbstractGameObject
{

    private TextureRegion regBone;

    public Bone()
    {
        init();
    }

    private void init()
    {
        dimension.set(1.25f, 1.0f);

        regBone = Assets.instance.bone.bone;

        //Set bounding box for collection detection
        bounds.set(0, 0, dimension.x, dimension.y);

    }

    @Override
    public void render(SpriteBatch batch)
    {
        TextureRegion reg = regBone;
        batch.draw(reg.getTexture(), position.x, origin.y - 0.75f, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
                reg.getRegionHeight(), false, false);
    }
    
    public int getScore()
    {
        return -200;
    }

}
