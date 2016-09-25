package com.luckenbaughgdx.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/*
 * abstract method of the game objects
 */
public abstract class AbstractGameObject {

	public Vector2 position;
	public Vector2 dimension;
	public Vector2 origin;
	public Vector2 scale;
	public float rotation;
	
	/*
	 * overall attributes of all game objects
	 */
	public AbstractGameObject()
	{
		position = new Vector2();
		dimension = new Vector2(1,1);
		origin = new Vector2();
		scale = new Vector2(1,1);
		rotation = 0;
	}
	
	//each object will be update
	public void update (float deltaTime) {}
	//each object will be rendered
	public abstract void render (SpriteBatch batch);
	
}
