package com.mygdx.game;

public interface GameMainActivity {
    void openFileChooser();
    String getAudioFilePath();

    void checkPermission();
    String getAudioPermission();

}
