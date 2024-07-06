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


    private static GameMusic object;

    private GameMusic(){
        menuMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound/menu.mp3"));
        FileHandle handle = null;
        String musicPath = "";

        boolean localOrInternal = false;
        try {
            handle = Gdx.files.local("Sound/SelectedGameMusic.txt");
            musicPath = handle.readString();
            localOrInternal = true;
        } catch (Exception e) {
            handle = Gdx.files.internal("Sound/SelectedGameMusic.txt");
            musicPath = handle.readString();
        }
        if (localOrInternal) {
            try {
                gameMusic = Gdx.audio.newMusic(Gdx.files.local(musicPath));
            }catch (GdxRuntimeException ex){
                gameMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound/game.mp3"));
            }
        }else{
            gameMusic = Gdx.audio.newMusic(Gdx.files.internal("Sound/game.mp3"));
        }
    }

    private void WriteLastGameMusicInFile(String path){
        FileHandle handle = Gdx.files.local("Sound/SelectedGameMusic.txt");
        handle.writeString(path,false);
    }

    public static GameMusic MusicInitialize(){
      if(object==null){
          object = new GameMusic();
      }
      return object;
    }

    public void setVolume(int volume){
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
}
