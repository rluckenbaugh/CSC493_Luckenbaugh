package com.luckenbaughgdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.luckenbaughgdx.game.Assets;

public class Rock extends AbstractGameObject {

	private TextureRegion regEdge;
	private TextureRegion regMiddle;
	private int length;
	
	
	public Rock()
	{
		init();
	}
	
	
	//initialize the drawing of the rock
	private void init() {
		dimension.set(1,1);
		
		regEdge = Assets.instance.ground.edge;
		regMiddle = Assets.instance.ground.middle;
		
		//Start length of this rock
		setLength(1);
		
	}

	
	//set the initial length of the rock
	private void setLength(int length)
	{
		this.length = length;
		
	}
	
	
	//increase the length of the rock
	public void increaseLength (int amount)
	{
		setLength(length+amount);
	}

	
	//render the rock edges and middle pieces
	@Override
	public void render(SpriteBatch batch)
	{
		TextureRegion reg = null;
		
		float relX = 0;
		float relY = 0;
		//Draw left edge
		reg = regEdge;
		relX -= dimension.x /4;
		batch.draw(reg.getTexture(), position.x + relX , position.y + relY, origin.x, origin.y, dimension.x/4, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
		
		
		//draw middle
		relX = 0;
		reg = regMiddle;
		for (int i = 0; i < length; i++)
		{
			batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
			relX += dimension.x;
		}
		
		//draw right edge
		reg = regEdge;
		batch.draw(reg.getTexture(), position.x + relX, position.y + relY, origin.x + dimension.x/8, origin.y, dimension.x/4, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), true, false);

	}
		
}
