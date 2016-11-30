package com.luckenbaughgdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.luckenbaughgdx.game.Assets;

public class Bee extends AbstractGameObject
{
    public TextureRegion regBee;
    public boolean collected;
    
    public Bee()
    {
        init();
    }
    
    private void init()
    {
        dimension.set(1.0f, 1.0f);

        
        setAnimation(Assets.instance.bee.animBee);
        stateTime = MathUtils.random(0.0f,1.0f);
        //regBee = Assets.instance.bee.bee;

        //Set bounding box for collection detection
        bounds.set(0, 0, dimension.x, dimension.y);
        collected = false;

    }

    @Override
    public void render(SpriteBatch batch)
    {
        if (collected)
            return;
        TextureRegion reg = null;
        reg = animation.getKeyFrame(stateTime,true);
        batch.draw(reg.getTexture(), position.x, position.y , origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(),
                reg.getRegionHeight(), false, false);
    }

    
    public int getScore()
    {
        return -200;
    }
}
