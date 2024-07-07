package com.mygdx.game.Music;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class GameMusic {
    private Music menuMusic = null;
    private Music gameMusic;
    private Sound menuClickSound;
    private String gameMusicPath;

    private Music defaultGameMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound/game.mp3"));

    private static GameMusic object;

    private GameMusic()
    {
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound/menu.mp3"));
    }


    public static GameMusic MusicInitialize() //Первая инициализация в MainMenu
    {
        if (object == null) {
            object = new GameMusic();
        }
        return object;
    }

    public void setVolume(boolean volume) {
        if (volume) {
            object.gameMusic.setVolume(1);
            object.menuMusic.setVolume(1);
        }else{
            object.gameMusic.setVolume(0);
            object.menuMusic.setVolume(0);
        }
    }

    public void setGameMusic(Music gameMusic) {
        object.gameMusic = gameMusic;
    }

    public Music getMenuMusic() {
        return object.menuMusic;
    }

    public Music getGameMusic() {
        return object.gameMusic;
    }

    public Sound getMenuClickSound() {
        return object.menuClickSound;
    }

    public Music getDefaultGameMusic() {
        return object.defaultGameMusic;
    }

    public String getGameMusicPath() {
        return object.gameMusicPath;
    }

    public void setGameMusicPath(String gameMusicPath) {
        object.gameMusicPath = gameMusicPath;
    }
}
