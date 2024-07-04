package com.mygdx.game.Cars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Cars.upgrades.AbstractUpgrade;

import java.util.ArrayList;

public class Car {

    private int speed;
    private int cost = 100;
    private String name;
    private Texture storeTexture;
    private String storePath;
    private boolean isPurchased = false;

    private ArrayList<AbstractUpgrade> upgrades = new ArrayList<>();

    public Car(String storePath, String name,int cost, int speed, boolean isPurchased) {
        storeTexture = new Texture(Gdx.files.internal(storePath));
        this.name = name;
        this.storePath = storePath;
        this.speed = speed;
        this.isPurchased = isPurchased;
        this.cost=cost;
    }

    public void setPurchased() {
        isPurchased = true;
    }

    public boolean isPurchased() {
        return isPurchased;
    }

    public ArrayList<AbstractUpgrade> getUpgrades() {
        return upgrades;
    }

    public int getSpeed() {
        return speed;
    }

    public String getName() {
        return name;
    }

    public Texture getStoreTexture() {
        return storeTexture;
    }

    public String getStorePath() {
        return storePath;
    }

    public int getCost() {
        return cost;
    }
}
