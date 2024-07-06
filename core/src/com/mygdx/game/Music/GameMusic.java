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

    private Music defaultGameMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound/game.mp3"));

    private static GameMusic object;

    private GameMusic()
    {
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound/menu.mp3"));
        FileHandle handle = null;
        String musicPath = "";

        boolean localOrInternal = false;
        try {
            handle = Gdx.files.local("Sound/SelectedGameMusic.txt");
            musicPath = handle.readString();
            localOrInternal = true;
        } catch (Exception e) {
            gameMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound/game.mp3"));
        }
        if (localOrInternal) {
            try {
                gameMusic = Gdx.audio.newMusic(Gdx.files.absolute(musicPath));
            } catch (GdxRuntimeException ex) {
                gameMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound/game.mp3"));
            }
        }
    }

    private void WriteLastGameMusicInFile(String path) {
        FileHandle handle = Gdx.files.local("Sound/SelectedGameMusic.txt");
        handle.writeString(path, false);
    }

    public static GameMusic MusicInitialize() //Первая инициализация в MainMenu
    {
        if (object == null) {
            object = new GameMusic();
        }
        return object;
    }

    public void setVolume(int volume) {
        gameMusic.setVolume((float) volume);
        menuMusic.setVolume((float) volume);
    }

    public void setGameMusic(Music gameMusic, String path) {
        this.gameMusic = gameMusic;
        WriteLastGameMusicInFile(path);
    }

    public Music getMenuMusic() {
        return menuMusic;
    }

    public Music getGameMusic() {
        return gameMusic;
    }

    public Sound getMenuClickSound() {
        return menuClickSound;
    }

    public Music getDefaultGameMusic() {
        return defaultGameMusic;
    }
}
