package com.mygdx.game.Cars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Cars.upgrades.AbstractUpgrade;

import java.util.ArrayList;

public class Car {

    private int speed;
    private int cost = 100;
    private int positionCar;
    private int positionCarY=100;
    private String name;
    private Texture carTexture;
    private String carPath;
    private int borderLeft;
    private int borderRight;
    private boolean isPurchased = false;

    private ArrayList<AbstractUpgrade> upgrades = new ArrayList<>();

    public Car(String carPath, String name,int cost, int speed, boolean isPurchased, int borderLeft, int borderRight) {
        carTexture = new Texture(Gdx.files.internal(carPath));
        this.name = name;
        this.carPath = carPath;
        this.speed = speed;
        this.isPurchased = isPurchased;
        this.cost=cost;
        this.borderLeft=borderLeft;
        this.borderRight=borderRight;
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

    public Texture getCarTexture() {
        return carTexture;
    }

    public String getCarPath() {
        return carPath;
    }

    public void setBorderLeft(int borderLeft){this.borderLeft=borderLeft;}

    public void setBorderRight(int borderRight){this.borderRight=borderRight;}

    public int getBorderLeft(){return borderLeft;}

    public int getBorderRight(){return borderRight;}

    public void setPositionCar(int positionCar){this.positionCar=positionCar;}

    public int getPositionCar(){return positionCar;}
    public int getPositionCarY(){return positionCarY;}
    public int getCost() {
        return cost;
    }
}
