package com.mygdx.game.Cars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.mygdx.game.Cars.upgrades.AbstractUpgrade;

import java.util.ArrayList;

public class EnemyCar {
    private int speed;
    private Texture carTexture;
    private String carPath;
    private int positionX;
    private int positionY;
    private boolean go;

    public EnemyCar(String carPath, int speed, boolean go) {
        carTexture = new Texture(Gdx.files.internal(carPath));
        this.carPath = carPath;
        this.speed = speed;
        this.go=go;
    }

    public void setSpeed(int speed) {this.speed=speed;}

    public int getSpeed() {
        return speed;
    }

    public Texture getCarTexture() {
        return carTexture;
    }

    public String getCarPath() {
        return carPath;
    }

    public void setPositionX(int positionX){this.positionX=positionX;}

    public int getPositionX(){return positionX;}

    public void setPositionY(int positionY){this.positionY=positionY;}

    public int getPositionY(){return positionY;}

    public void setGo(boolean go) {this.go = go;}

    public boolean getGo() {return go;}
}
