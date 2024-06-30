package com.mygdx.game.Cars.upgrades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class TwoLivesUpgrade extends AbstractUpgrade {
    private static Texture texture = new Texture(Gdx.files.internal("Cars/Upgrades/hearts.png"));

    public TwoLivesUpgrade(boolean purchased) {
        super(purchased);
        cost = 120;
    }

    @Override
    public UpgradeType getType() {
        return UpgradeType.TWO_LIVES;
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

}
