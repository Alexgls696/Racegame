package com.mygdx.game.Cars;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Cars.upgrades.AbstractUpgrade;
import com.mygdx.game.Scene;
import com.mygdx.game.Store;

import org.w3c.dom.Text;

import java.awt.Image;
import java.util.ArrayList;

public class UpgradesScene implements Scene {

    private Texture backgroundTexture;
    private SpriteBatch backgroundSprite;
    private Car currentCar;

    private final int SCREEN_WIDTH = Gdx.graphics.getWidth();
    private final int SCREEN_HEIGHT = Gdx.graphics.getHeight();

    private final float upgradeSize = SCREEN_WIDTH / 6f;
    private final float tableRightPad = upgradeSize / 5f;
    private Stage stage;
    private Stage mainStage;

    private static Texture moneyTexture = new Texture(Gdx.files.internal("Store/money.png"));
    private static BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"));
    private static Label.LabelStyle style = new Label.LabelStyle(font, Color.BLACK);

    private static BitmapFont descriptionFont = new BitmapFont(Gdx.files.internal("font.fnt"));
    private static Label.LabelStyle descriptionLabelStyle = new Label.LabelStyle(descriptionFont, Color.BLACK);
    Label desctiptionLabel = new Label("Доступные улучшения", descriptionLabelStyle);

    private int noScaleWidth = 1280;
    float scaleC = (float) SCREEN_WIDTH / noScaleWidth;

    public UpgradesScene(Car car, Stage mainStage) {
        this.mainStage = mainStage;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        currentCar = car;
        Table table = new Table();

        ArrayList<AbstractUpgrade> upgrades = car.getUpgrades();

        descriptionFont.getData().setScale(2 * scaleC, 2 * scaleC);
        desctiptionLabel.setPosition(SCREEN_WIDTH / 2f - desctiptionLabel.getWidth() / 2f, SCREEN_HEIGHT - desctiptionLabel.getHeight() - 50);

        float maxLabelWidth = Float.MIN_NORMAL;
        float current_max = 0f;
        float labelHeight = 0f;
        float currentColumnSize = -1;
        for (AbstractUpgrade upgrade : upgrades) {
            Label label = new Label(upgrade.getType().toString(), style);
            float width = label.getWidth();
            labelHeight = label.getHeight();
            current_max = Math.max(maxLabelWidth, width);
            currentColumnSize = current_max * scaleC;
            table.add(label).width(current_max).padRight(tableRightPad);
        }
        table.add().row();

        for (AbstractUpgrade upgrade : upgrades) {
            ImageButton button = new ImageButton(new TextureRegionDrawable(upgrade.getTexture()));
            button.setName(upgrade.getType().toString());
            table.add(button)
                    .height(currentColumnSize).width(currentColumnSize)
                    .padRight(tableRightPad).padBottom(tableRightPad / 2);
        }
        table.add().row();

        for (AbstractUpgrade upgrade : upgrades) {
            Table table1 = new Table();
            table1.add(new ImageButton(new TextureRegionDrawable(moneyTexture))).width(40).height(40).padRight(10);
            table1.add(new Label(String.valueOf(upgrade.getCost()), style));
            table.add(table1).padRight(tableRightPad);
        }

        table.setSize(currentColumnSize, currentColumnSize + labelHeight);
        table.setPosition(50, SCREEN_HEIGHT / 2.5f - table.getHeight());
        table.setFillParent(true);
        stage.addActor(table);
        stage.addActor(desctiptionLabel);

        InitBackButton();
    }

    boolean backFlag = false;

    private void InitBackButton() {
        ImageButton back = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("Store/left.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("Store/hover_left.png"))));
        back.setSize(SCREEN_HEIGHT / 6f, SCREEN_HEIGHT / 6f);
        back.setPosition(15 * scaleC, SCREEN_HEIGHT - back.getHeight() - 50 * scaleC);
        back.getImage().setFillParent(true);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                backFlag = true;
            }
        });
        stage.addActor(back);
    }

    @Override
    public void create() {

    }

    @Override
    public void render() {
        if (backFlag) {
            stage.dispose();
            backFlag = false;
            Store.upgradesSceneFlag = false;
            Gdx.input.setInputProcessor(mainStage);
        } else {
            ScreenUtils.clear(0, 0, 8, 0);
            stage.act();
            stage.draw();
        }
    }

    @Override
    public void dispose() {

    }
}
