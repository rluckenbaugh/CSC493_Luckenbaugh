package com.luckenbaughgdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.luckenbaughgdx.game.util.Constants;
/**
 * Hold the loaded instances for the game
 * @author Renae
 *
 */
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
	
    public AssetSounds sounds;

    public AssetMusic music;

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
        assetManager.load(Constants.TEXTURE_ATLAS_OBJECTS, TextureAtlas.class);
        //load sounds
        assetManager.load("sounds/jump.wav",Sound.class);
        assetManager.load("sounds/jump_with_pile.wav",Sound.class);
        assetManager.load("sounds/pickup_treat.wav",Sound.class);
        assetManager.load("sounds/hit_bee.wav",Sound.class);
        assetManager.load("sounds/hit_pile.wav",Sound.class);
        assetManager.load("sounds/livelost.wav",Sound.class);
        //load music
        assetManager.load("music/keith303_-_brand_new_highscore.mp3",Music.class);
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
        sounds = new AssetSounds(assetManager);
        music = new AssetMusic(assetManager);
	
	}
	
	/*
	 * pooch image
	 */
	public class AssetPooch
	{
		public final AtlasRegion stand;

        public final Animation animSit;
        public final Animation animRun;

        public final Animation animJump;
		public AssetPooch (TextureAtlas atlas)
		{

	        stand = atlas.findRegion("dog");

            Array<AtlasRegion> regions = null;
            AtlasRegion region = null;

            //Animation: sit
            regions = atlas.findRegions("dog_sit");
            animSit = new Animation(1.0f / 10.0f, regions);

            //Animation: run
            regions = atlas.findRegions("dog_run");
            animRun = new Animation(1.0f / 10.0f, regions, Animation.PlayMode.LOOP_PINGPONG);

            //Animation jump
            regions = atlas.findRegions("dog_jump");
            animJump = new Animation(1.0f / 4.0f, regions, Animation.PlayMode.NORMAL);
		}
	}
	
	/*
	 * ground images
	 */
	public class AssetGround
	{
		public final AtlasRegion edge;
		public final AtlasRegion middle;
		public final AtlasRegion mudEdge;
		public final AtlasRegion mudMiddle;
		
		
		public AssetGround (TextureAtlas atlas)
		{
			edge = atlas.findRegion("edge");
			middle = atlas.findRegion("middle");
			mudEdge = atlas.findRegion("mudEdge");
			mudMiddle = atlas.findRegion("mudMiddle");
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
		//public final AtlasRegion bee;
		public final Animation animBee;
		
		public AssetBee (TextureAtlas atlas)
		{
			//bee = atlas.findRegion("bee");
			
			//Animation Bee
			Array<AtlasRegion> regions = atlas.findRegions("bee");
			//AtlasRegion region = regions.first();
			//for (int i = 0;i<5;i++)
			    //regions.insert(0,region);
			animBee = new Animation(1.0f/ 10.0f,regions,Animation.PlayMode.LOOP_PINGPONG);
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
        fonts.defaultSmall.dispose();
        fonts.defaultNormal.dispose();
        fonts.defaultBig.dispose();
		
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
	
    /**
     * Hold the loaded instances of Sounds
     * @author Renae
     *
     */
    public class AssetSounds
    {
        public final Sound jump;

        public final Sound jumpWithPile;

        public final Sound pickupTreat;

        public final Sound hitPile;
        
        public final Sound hitBee;

        public final Sound liveLost;
	
        public AssetSounds(AssetManager am)
        {
            jump = am.get("sounds/jump.wav", Sound.class);
            jumpWithPile = am.get("sounds/jump_with_pile.wav", Sound.class);
            pickupTreat = am.get("sounds/pickup_treat.wav", Sound.class);
            hitPile = am.get("sounds/hit_pile.wav", Sound.class);
            liveLost = am.get("sounds/livelost.wav", Sound.class);
            hitBee = am.get("sounds/hit_bee.wav",Sound.class);
        }

    }
    
    /**
     * Hold the loaded instances of Music
     * @author Renae
     *
     */
    public class AssetMusic
    {
        public final Music song01;
        public AssetMusic (AssetManager am)
        {
            song01 = am.get("music/keith303_-_brand_new_highscore.mp3", Music.class);
        }
    }
	
}
