package com.luckenbaughgdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.luckenbaughgdx.game.objects.AbstractGameObject;
import com.luckenbaughgdx.game.objects.Bee;
import com.luckenbaughgdx.game.objects.Bone;
import com.luckenbaughgdx.game.objects.House;
import com.luckenbaughgdx.game.objects.Pile;
import com.luckenbaughgdx.game.objects.Pooch;
import com.luckenbaughgdx.game.objects.Clouds;
import com.luckenbaughgdx.game.objects.Fence;
import com.luckenbaughgdx.game.objects.Treat;
import com.luckenbaughgdx.game.objects.Rock;

/*
 * Create the level by loading the image of the level then initiate the level objects
 */
public class Level
{
    public static final String TAG = Level.class.getName();

    public Pooch pooch;
    
    public House house;
    
    public Bone bone;
    
    public Array<Bee> bees;

    public Array<Treat> treats;

    public Array<Pile> piles;

    public enum BLOCK_TYPE
    {
        EMPTY(0,0,0), //black
        ROCK(0, 255,0), // green
        PLAYER_SPAWNPOINT(255, 255, 255), //white
        ITEM_BEE(255,0,0), //red
        ITEM_TREAT(255,255,0), //yellow;
        ITEM_PILE(255,0,255), //purple
        ITEM_HOUSE(0, 255, 255),//teal
        ITEM_BONE(0, 0, 255); //blue 

        private int color;

        private BLOCK_TYPE(int r, int g, int b)
        {
            color = r << 24 | g << 16 | b << 8 | 0xff;
        }

        public boolean sameColor(int color)
        {
            return this.color == color;
        }

        public int getColor()
        {
            return color;
        }
    }

    //objects
    public Array<Rock> rocks;

    //decoration
    public Clouds clouds;

    public Fence fence;

    public Level(String filename)
    {
        init(filename);
    }

    private void init(String filename)
    {
        //player character
        pooch = null;
        house = null;
        bone = null;
        //objects
        rocks = new Array<Rock>();
        treats = new Array<Treat>();
        piles = new Array<Pile>();
        bees = new Array<Bee>();

        //load image file that represents the data
        Pixmap pixmap = new Pixmap(Gdx.files.internal(filename));
        //scan pixels from top-left to bottom-right
        int lastPixel = -1;
        for (int pixelY = 0; pixelY < pixmap.getHeight(); pixelY++)
        {
            for (int pixelX = 0; pixelX < pixmap.getWidth(); pixelX++)
            {
                AbstractGameObject obj = null;
                float offsetHeight = 0;
                //height grows from bottom to top
                float baseHeight = pixmap.getHeight() - pixelY;
                //get color of current pixel as a 32 bit RGBA value
                int currentPixel = pixmap.getPixel(pixelX, pixelY);
                //find matching color value to identify block type at (x,y)
                //point and create the corresponding game object if there ia a match


                //empty space
                if (BLOCK_TYPE.EMPTY.sameColor(currentPixel))
                {
                    //do nothing
                }

                //rock
                else if (BLOCK_TYPE.ROCK.sameColor(currentPixel))
                {
                    if (lastPixel != currentPixel)
                    {
                        obj = new Rock();
                        float heightIncreaseFactor = 0.25f;
                        offsetHeight = -2.5f;
                        obj.position.set(pixelX, baseHeight * obj.dimension.y * heightIncreaseFactor + offsetHeight);
                        rocks.add((Rock) obj);
                    }
                    else
                    {
                        rocks.get(rocks.size - 1).increaseLength(1);
                    }
                }

                
                //player spawn point
                else if (BLOCK_TYPE.PLAYER_SPAWNPOINT.sameColor(currentPixel))
                {
                    obj = new Pooch();
                    offsetHeight = -3.0f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    pooch = (Pooch) obj;
                }
                
                //dog house decoration
                else if (BLOCK_TYPE.ITEM_HOUSE.sameColor(currentPixel))
                {
                    obj = new House();
                    offsetHeight = -4.5f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    house = (House) obj;
                }
                //bee
                else if (BLOCK_TYPE.ITEM_BEE.sameColor(currentPixel))
                {
                    obj = new Bee();
                    offsetHeight = -1.5f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    bees.add((Bee) obj);
                }
                
                //pile
                else if (BLOCK_TYPE.ITEM_PILE.sameColor(currentPixel))
                {
                    obj = new Pile();
                    offsetHeight = -2.38f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    piles.add((Pile) obj);
                }
                //gold coin
                else if (BLOCK_TYPE.ITEM_TREAT.sameColor(currentPixel))
                {
                    obj = new Treat();
                    offsetHeight = -1.5f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    treats.add((Treat) obj);
                }
                
                //big bone
                else if (BLOCK_TYPE.ITEM_BONE.sameColor(currentPixel))
                {
                    obj = new Bone();
                    offsetHeight = -3.0f;
                    obj.position.set(pixelX, baseHeight * obj.dimension.y + offsetHeight);
                    bone = (Bone) obj;
                }
                

                
                //unkown object or pixel
                else
                {
                    int r = 0xff & (currentPixel >>> 24); //red color channel
                    int g = 0xff & (currentPixel >>> 16); //green color channel
                    int b = 0xff & (currentPixel >>> 8); //blue color channel
                    int a = 0xff & currentPixel; //alpha channel
                    Gdx.app.error(TAG, "Unkown object at x<" + pixelX + "> y<" + pixelY + ">: r<" + r + "> g<" + g + "> b<" + b + "> a<" + a + ">");
                }
                
                
                lastPixel = currentPixel;
            }
        }

        //decoration
        clouds = new Clouds(pixmap.getWidth());
        clouds.position.set(0, 2);
        fence = new Fence(pixmap.getWidth());
        fence.position.set(-1, -1);

        //free memory
        pixmap.dispose();
        Gdx.app.debug(TAG, "level '" + filename + "' loaded");

    }

    public void render(SpriteBatch batch)
    {
        //draw mountians
        fence.render(batch);

        //draw rocks
        for (Rock rock : rocks)
            rock.render(batch);

        //Draw gold Coins
        for (Treat goldCoin : treats)
            goldCoin.render(batch);

        //draw piles
        for (Pile pile : piles)
            pile.render(batch);
        
        //draw bees
        for(Bee bee : bees)
            bee.render(batch);
        
        //draw player character
        pooch.render(batch);

        //draw clouds
        clouds.render(batch);
        
        //draw house
        house.render(batch);
        
        //draw bone
        bone.render(batch);
        


    }

    public void update(float deltaTime)
    {
        pooch.update(deltaTime);
        house.update(deltaTime);
        for (Rock rock : rocks)
            rock.update(deltaTime);
        for (Treat treat : treats)
            treat.update(deltaTime);
        for (Pile feather : piles)
            feather.update(deltaTime);
        for (Bee bee : bees)
            bee.update(deltaTime);
        clouds.update(deltaTime);
        bone.update(deltaTime);

    }
}
