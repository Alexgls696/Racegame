package com.mygdx.game;

public interface GameMainActivity {
    void openFileChooser();
    String getAudioFilePath(String lastPath);
    void checkPermission();
    String getAudioPermission();
    void setPathNull();
    void finishAffinity();
}
