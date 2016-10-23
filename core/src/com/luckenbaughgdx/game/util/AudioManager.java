package com.luckenbaughgdx.game.util;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * Manage the audio settings
 * @author Renae
 *
 */
public class AudioManager
{
    public static final AudioManager instance = new AudioManager();

    private Music playingMusic;

    //singleton: prevent instantiantion from other classes
    private AudioManager()
    {
    };

    public void play(Sound sound)
    {
        play(sound, 1);
    }

    private void play(Sound sound, float volume)
    {
        play(sound, volume, 1);

    }

    public void play(Sound sound, float volume, float pitch)
    {
        play(sound, volume, pitch, 0);

    }

    private void play(Sound sound, float volume, float pitch, float pan)
    {
        if (!GamePreferences.instances.sound)
            return;
        sound.play(GamePreferences.instances.volSound * volume, pitch, pan);

    }

    //Play the music with the updated settings
    public void play(Music music)
    {
        stopMusic();
        playingMusic = music;
        if (GamePreferences.instances.music)
        {
            music.setLooping(true);
            music.setVolume(GamePreferences.instances.volMusic);
            music.play();
        }
    }

    //Stop the music
    private void stopMusic()
    {
        if (playingMusic != null)
            playingMusic.stop();
    }

    //Update when the settings are updaated
    public void onSettingsUpdated()
    {
        if (playingMusic == null)
            return;
        playingMusic.setVolume(GamePreferences.instances.volMusic);
        if (GamePreferences.instances.music)
        {
            if (!playingMusic.isPlaying())
                playingMusic.play();
            else
            {
                playingMusic.pause();
            }
        }
    }
}
