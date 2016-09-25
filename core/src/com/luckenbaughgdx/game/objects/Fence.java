package com.luckenbaughgdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.luckenbaughgdx.game.Assets;

public class Fence extends AbstractGameObject
{

	private TextureRegion regFenceLeft;
	private TextureRegion regFenceRight;
	
	private int length;
	
	public Fence (int length)
	{
		this.length = length;
		init();
	}
	
	/*
	 * initilize the mountains
	 */
	private void init() {
		dimension.set(5,3);
		
		regFenceLeft = Assets.instance.levelDecoration.fence;
		regFenceRight = Assets.instance.levelDecoration.fence;
		
		//shift fence and extend length
		origin.x = -dimension.x *2;
		length += dimension.x *2;	
	}
	
	/*
	 * determine where the mountains wil be drawn
	 */
	private void drawFence (SpriteBatch batch, float offsetX, float offsetY, float tintColor)
	{
		TextureRegion reg = null;
		batch.setColor(tintColor, tintColor, tintColor, 1);
		float xRel = dimension.x * offsetX;
		float yRel = dimension.y * offsetY;
		
		//mountains span the whole level
		int mountainLength = 0;
		mountainLength += MathUtils.ceil(length/(2*dimension.x));
		mountainLength += MathUtils.ceil(0.5f + offsetX);
		for (int i = 0; i < mountainLength; i++)
		{
			//mountainnLeft
			reg = regFenceLeft;
			batch.draw(reg.getTexture(), origin.x + xRel, position.y + origin.y + yRel, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			xRel += dimension.x;
			
			//mountainRight
			reg = regFenceRight;
			batch.draw(reg.getTexture(), origin.x + xRel, position.y + origin.y + yRel, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			xRel += dimension.x;
		}
		
		//reset color to white;
		batch.setColor(1,1,1,1);
	}

	/*
	 * draw the mountians
	 * @see com.luckenbaughgdx.game.objects.AbstractGameObject#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) 
	{
		//distant mountains light gray
		drawFence(batch, 0.0f, -0.5f, 0.9f);
	}

	
}
