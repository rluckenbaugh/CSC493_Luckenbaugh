package com.luckenbaughgdx.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

public class GamePreferences
{

    public static final String TAG = GamePreferences.class.getName();

    public static final GamePreferences instances = new GamePreferences();

    public boolean sound;

    public boolean music;

    public float volSound;

    public float volMusic;

    public int charSkin;

    public boolean showFpsCounter;

    private Preferences prefs;

    public Array<Integer> highScores = new Array<Integer>();

    //singleton: prevent instantiation from other classes
    private GamePreferences()
    {
        prefs = Gdx.app.getPreferences(Constants.PREFERENCES);

    }

    public void load()
    {
        sound = prefs.getBoolean("sound", true);
        music = prefs.getBoolean("music", true);
        volSound = MathUtils.clamp(prefs.getFloat("volSound", 0.5f), 0.0f, 1.0f);
        volMusic = MathUtils.clamp(prefs.getFloat("volMusic", 0.5f), 0.0f, 1.0f);
        charSkin = MathUtils.clamp(prefs.getInteger("charSkin", 0), 0, 2);
        showFpsCounter = prefs.getBoolean("showFpsCounter", false);

    }

    public void loadScores()
    {

        highScores.add(prefs.getInteger("oneHigh"));
        highScores.add(prefs.getInteger("twoHigh"));
        highScores.add(prefs.getInteger("threeHigh"));
        highScores.add(prefs.getInteger("fourHigh"));
        highScores.add(prefs.getInteger("fiveHigh"));

    }

    public void saveScores()
    {
        prefs.putInteger("oneHigh", highScores.get(0));
        prefs.putInteger("twoHigh", highScores.get(1));
        prefs.putInteger("threeHigh", highScores.get(2));
        prefs.putInteger("fourHigh", highScores.get(3));
        prefs.putInteger("fiveHigh", highScores.get(4));
    }

    public void save()
    {
        prefs.putBoolean("sound", sound);
        prefs.putBoolean("music", music);
        prefs.putFloat("volSound", volSound);
        prefs.putFloat("volMusic", volMusic);
        prefs.putInteger("charSkin", charSkin);
        prefs.putBoolean("showFpsCounter", showFpsCounter);

        prefs.flush();

    }

    public void addHighScore(int score)
    {
        if (highScores.size < 5)
            for (int i = 0; i<5;i++)
            {
                highScores.add(0);
            }
        highScores.add(score);
        highScores.sort();
        highScores.reverse();

        highScores.removeIndex(5);
    }

}
