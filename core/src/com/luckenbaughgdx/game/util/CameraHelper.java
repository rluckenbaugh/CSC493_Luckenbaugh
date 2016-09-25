package com.luckenbaughgdx.game.util;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.luckenbaughgdx.game.objects.AbstractGameObject;

public class CameraHelper 
{
	private static final String TAG = CameraHelper.class.getName();
	
	private final float MAX_ZOOM_IN = 0.25f;
	private final float MAX_ZOOM_OUT = 10.0f;
	
	private Vector2 position;
	private float zoom;
	private AbstractGameObject target;
	
	public CameraHelper ()
	{
		position = new Vector2();
		zoom = 1.0f;
	}
	
	/*
	 * update the target
	 */
	public void update (float deltaTime)
	{
		if (!hasTarget()) return;
		
		position.x = target.position.x + target.origin.x;
		position.y = target.position.y + target.origin.y;
	}
	
	/*
	 * set the position
	 */
	public void setPosition (float x, float y)
	{
		this.position.set(x,y);
	}
	
	/*
	 * get the position
	 */
	public Vector2 getPosition ()
	{
		return position;
	}
	
	/*
	 * add zoom to the amount
	 */
	public void addZoom (float amount)
	{
		setZoom(zoom+amount);
	}

	/*
	 * set the zoom of the camera
	 */
	public void setZoom(float zoom) 
	{
		this.zoom = MathUtils.clamp(zoom, MAX_ZOOM_IN, MAX_ZOOM_OUT);		
	}
	
	/*
	 * get the zoom of the camera
	 */
	public float getZoom()
	{
		return zoom;
	}
	
	/*
	 * set the target
	 */
	public void setTarget(AbstractGameObject target)
	{
		this.target = target;
	}
	
	/*
	 * get the target
	 */
	public AbstractGameObject getTarget ()
	{
		return target;
	}
	
	/*
	 * cheeck if it has the target
	 */
	public boolean hasTarget () 
	{
		return target != null;
	}
	
	/*
	 * check it it has a target with a parameter
	 */
	public boolean hasTarget (AbstractGameObject target)
	{
		return hasTarget() && this.target.equals(target);
	}
	
	/*
	 * apply to the camera
	 */
	public void applyTo(OrthographicCamera camera) 
	{
		camera.position.x = position.x;
		camera.position.y = position.y;
		camera.zoom = zoom;
		camera.update();
	}
	
	
	
	
}
