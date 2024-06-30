package com.mygdx.game.Cars.upgrades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SlowMotionUpgrade extends AbstractUpgrade{
    private static Texture texture = new Texture(Gdx.files.internal("Cars/Upgrades/slow_motion.png"));

    public SlowMotionUpgrade(boolean purchased) {
        super(purchased);
        cost=150;
    }

    @Override
    public Texture getTexture() {
        return texture;
    }

    @Override
    public UpgradeType getType() {
        return UpgradeType.SLOW_MOTION;
    }
}
