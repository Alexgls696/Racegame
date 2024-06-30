package com.mygdx.game.Cars.upgrades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class AbstractUpgrade {

    public  enum UpgradeType {
        TWO_LIVES, SLOW_MOTION
    }
    protected int cost;

    public int getCost() {
        return cost;
    }

    private boolean purchased = false;

    public AbstractUpgrade(boolean purchased) {
        this.purchased=purchased;
    }

    public  abstract Texture getTexture();

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchasedValue(boolean flag) {
        purchased = flag;
    }

    public abstract UpgradeType getType();
}
