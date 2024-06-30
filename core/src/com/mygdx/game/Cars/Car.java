package com.mygdx.game.Cars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Cars.upgrades.AbstractUpgrade;

import java.util.ArrayList;

public class Car {

    private int speed;
    private String name;
    private boolean doubleLive;
    private SpriteBatch storeBatch;
    private Texture storeTexture;
    private String storePath;

    private ArrayList<AbstractUpgrade>upgrades = new ArrayList<>();

    public Car(String storePath, String name, int speed){
        storeTexture = new Texture(Gdx.files.internal(storePath));
        storeBatch = new SpriteBatch();
        this.name=name;
        this.storePath=storePath;
        this.speed=speed;
    }

    public ArrayList<AbstractUpgrade> getUpgrades() {
        return upgrades;
    }

    private void initUpgradesFromFile(){
        FileHandle handle;
    }

    public void setDoubleLive(){
        doubleLive=true;
    }

    public void resetDoubleLive(){
        doubleLive=false;
    }

    public boolean isDoubleLive(){
        return doubleLive;
    }

    public int getSpeed() {
        return speed;
    }

    public String getName() {
        return name;
    }

    public SpriteBatch getStoreBatch() {
        return storeBatch;
    }

    public Texture getStoreTexture() {
        return storeTexture;
    }

    public String getStorePath() {
        return storePath;
    }

    public void setStorePath(String storePath) {
        this.storePath = storePath;
    }
}
