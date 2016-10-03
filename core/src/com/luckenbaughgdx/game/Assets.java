package com.luckenbaughgdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Disposable;
import com.luckenbaughgdx.game.util.Constants;

public class Assets implements Disposable, AssetErrorListener
{

	public static final String TAG = Assets.class.getName();
	public static final Assets instance = new Assets();
	private AssetManager assetManager;
	
	
	public AssetPooch pooch;
	public AssetGround ground;
	public AssetTreat treat;
	public AssetBone bone;
	public AssetBee bee;
	public AssetPile pile;
	public AssetHeart heart;
	public AssetMedal medal;
	public AssetLevelDecoration levelDecoration;
	public AssetFonts fonts;
	
	//singleton: prevent instantiation from other classes
	private Assets() {}
	
	/*
	 * establish the fonts and thier sizes
	 */
	public class AssetFonts 
	{
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;
		
		public AssetFonts()
		{
			//create three fonts using Libgdx's 15px bitmap font
			defaultSmall = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"),true);
			defaultNormal = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"), true);
			defaultBig = new BitmapFont(Gdx.files.internal("images/arial-15.fnt"),true);
			
			//set font sizes
			defaultSmall.getData().setScale(0.75f);
			defaultNormal.getData().setScale(1.0f);
			defaultBig.getData().setScale(2.0f);
			
			//enable linear texturing for smooth fonts
			defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			
		}
	}
	
	/*
	 * initialize assets
	 * enable smoothing
	 * create resources
	 */
	public void init (AssetManager assetManager)
	{
		this.assetManager = assetManager;
		//set asset manager error handler
		assetManager.setErrorListener(this);
		//load texture atlas
		assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS,TextureAtlas.class);
		//start loafing assets and wait until finished
		assetManager.finishLoading();
		Gdx.app.debug(TAG,"# of assets loaded: " + assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames())
			Gdx.app.debug(TAG, "asset: " + a);
		
	TextureAtlas atlas = assetManager.get(Constants.TEXTURE_ATLAS_OBJECTS);
	
	
	//enable texture filtering for pixel smoothing
	for (Texture t : atlas.getTextures())
	{
		t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
	}
	
	//create game resource objects
	fonts = new AssetFonts();
	pooch = new AssetPooch(atlas);
	ground = new AssetGround(atlas);
	treat = new AssetTreat(atlas);
	bone = new AssetBone(atlas);
	bee = new AssetBee(atlas);
	pile = new AssetPile(atlas);
	heart = new AssetHeart(atlas);
	medal = new AssetMedal(atlas);
	levelDecoration = new AssetLevelDecoration(atlas);
	
	}
	
	/*
	 * pooch image
	 */
	public class AssetPooch
	{
		public final AtlasRegion stand;
		public final AtlasRegion sit;
		public final AtlasRegion lunge;
		public final AtlasRegion run;
		
		public AssetPooch (TextureAtlas atlas)
		{
			stand = atlas.findRegion("dog2");
			sit = atlas.findRegion("dog1");
			lunge = atlas.findRegion("dog3");
			run = atlas.findRegion("dog4");
			
		}
	}
	
	/*
	 * ground images
	 */
	public class AssetGround
	{
		public final AtlasRegion edge;
		public final AtlasRegion middle;
		
		public AssetGround (TextureAtlas atlas)
		{
			edge = atlas.findRegion("edge");
			middle = atlas.findRegion("middle");
		}
	}
	
	/*
	 * treat image
	 */
	public class AssetTreat
	{
		public final AtlasRegion treats;
		
		public AssetTreat (TextureAtlas atlas)
		{
			treats = atlas.findRegion("treat");
		}
	}
	
	/*
	 * bone image
	 */
	public class AssetBone
	{
		public final AtlasRegion bone;
		
		public AssetBone (TextureAtlas atlas)
		{
			bone = atlas.findRegion("bigbone");
		}
	}
	
	/*
	 * bee image
	 */
	public class AssetBee
	{
		public final AtlasRegion bee;
		
		public AssetBee (TextureAtlas atlas)
		{
			bee = atlas.findRegion("bee");
		}
	}
	/*
	 * pile image
	 */
	public class AssetPile
	{
		public final AtlasRegion pile;
		
		public AssetPile (TextureAtlas atlas)
		{
			pile = atlas.findRegion("dogpoop");
		}
	}
	/*
	 * heart images
	 */
	public class AssetHeart
	{
		public final AtlasRegion full;
		public final AtlasRegion half;
		public final AtlasRegion empty;
		
		public AssetHeart (TextureAtlas atlas)
		{
			full = atlas.findRegion("heart1");
			half = atlas.findRegion("heart2");
			empty = atlas.findRegion("heart3");
		}
	}
	/*
	 * bone image
	 */
	public class AssetMedal
	{
		public final AtlasRegion gold;
		public final AtlasRegion silver;
		public final AtlasRegion bronze;

		
		public AssetMedal (TextureAtlas atlas)
		{
			gold = atlas.findRegion("gold");
			silver = atlas.findRegion("silver");
			bronze = atlas.findRegion("bronze");
		}
	}
	
	/*
	 * other level decorations
	 */
	public class AssetLevelDecoration
	{
		public final AtlasRegion grass;
		public final AtlasRegion grass2;
		public final AtlasRegion grass3;
		public final AtlasRegion fence;
		public final AtlasRegion flower1;
		public final AtlasRegion flower2;
		public final AtlasRegion cloud;
		public final AtlasRegion house;
		
		public AssetLevelDecoration (TextureAtlas atlas)
		{
			grass = atlas.findRegion("grass");
			grass2 = atlas.findRegion("grass2");
			grass3 = atlas.findRegion("grass3");
			fence = atlas.findRegion("cream-picket-fence-hi");
			flower1 = atlas.findRegion("flower1");
			flower2 = atlas.findRegion("flower2");
			cloud = atlas.findRegion("cloud");
			house = atlas.findRegion("doghouse");
		}
		
	}

	@Override
	public void dispose ()
	{
		assetManager.dispose();
		
	}
	
	@Override
	public void error (AssetDescriptor asset, Throwable throwable) 
	{
		Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception)throwable);
		
	}
	
	public void error (String filename, Class type, Throwable throwable) 
	{
		Gdx.app.error(TAG, "Couldn't load asset '" + filename + "'", (Exception)throwable);
		
	}
	




	
	
}
