package com.luckenbaughgdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.luckenbaughgdx.game.Assets;

/*
 * clouds holds multiple clouds
 */
public class Clouds extends AbstractGameObject
{

	private float length;
	
	private Array<TextureRegion> regClouds;
	private Array<Cloud> clouds;
	
	/*
	 * single cloud class
	 */
	private class Cloud extends AbstractGameObject
	{

		private TextureRegion regCloud;
		
		public Cloud () {}
		//where the cloud is
		public void setRegion (TextureRegion region)
		{
			regCloud = region;
		}
		//render the cloud
		@Override
		public void render(SpriteBatch batch) {
			TextureRegion reg = regCloud;
			batch.draw(reg.getTexture(), position.x + origin.x, position.y + origin.x, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);			
		}
	}
	
	/*
	 * pass in the length of the clouds
	 */
	public Clouds (float length)
	{
		this.length = length;
		init();
	}
	
	/*
	 * initialize the clouds using the distFac and number of clouds
	 */
	private void init() {
		dimension.set(3.0f, 1.5f);
		regClouds = new Array<TextureRegion>();
		regClouds.add(Assets.instance.levelDecoration.cloud);
		
		int distFac = 5;
		int numClouds = (int) (length/distFac);
		clouds = new Array<Cloud> (2*numClouds);
		for (int i=0; i <numClouds; i++)
		{
			Cloud cloud = spawnCloud();
			cloud.position.x = i * distFac;
			clouds.add(cloud);

		}
		
	}

	/*
	 * create the cloud and its attributes
	 */
	private Cloud spawnCloud() {
		Cloud cloud = new Cloud();
		cloud.dimension.set(dimension);
		//select random cloud image
		cloud.setRegion(regClouds.random());
		//position
		Vector2 pos = new Vector2();
		pos.x = length + 10; //position after the end of the level
		pos.y += 1.75; //base position
		pos.y += MathUtils.random(0.0f, 0.2f) * (MathUtils.randomBoolean() ? 1 : -1); //random additional position
		cloud.position.set(pos);
		//speed
		Vector2 speed = new Vector2();
		speed.x += 0.5f; //base speed
		//random additional speed
		speed.x += MathUtils.random(0.0f, 0.75f);
		cloud.terminalVelocity.set(speed);
		speed.x *= -1; //move left
		cloud.velocity.set(speed);
		return cloud;
	}

	/*
	 * draw the clouds
	 * @see com.luckenbaughgdx.game.objects.AbstractGameObject#render(com.badlogic.gdx.graphics.g2d.SpriteBatch)
	 */
	@Override
	public void render(SpriteBatch batch) {
		for(Cloud cloud : clouds)
			cloud.render(batch);
		
	}

	@Override
	public void update (float deltaTime)
	{
	    for(int i = clouds.size - 1; i>=0; i--)
	    {
	        Cloud cloud = clouds.get(i);
	        cloud.update(deltaTime);
	        if(cloud.position.x < -10)
	        {
	            //cloud moved outside of world
	            //destory and spawn new cloud
	            clouds.removeIndex(i);
	            clouds.add(spawnCloud());
	            
	        }
	    }
	}
	
}
