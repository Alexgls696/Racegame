package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Cars.Car;
import com.mygdx.game.Cars.upgrades.AbstractUpgrade;

import java.util.ArrayList;

public class UpgradesScene implements Scene {

    private Texture backgroundTexture;
    private SpriteBatch backgroundSprite;
    private final int SCREEN_WIDTH = Gdx.graphics.getWidth();
    private final int SCREEN_HEIGHT = Gdx.graphics.getHeight();

    private final float upgradeSize = SCREEN_WIDTH / 6f;
    private final float tableRightPad = upgradeSize / 5f;

    private Stage stage;
    private Stage mainStage; //Stage магазина
    private Stage purchaseStage;
    private boolean purchaseMenuFlag = false;

    private static Texture moneyTexture = new Texture(Gdx.files.internal("Store/money.png"));
    private static BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"));
    private static Label.LabelStyle style = new Label.LabelStyle(font, Color.BLACK);

    private BitmapFont descriptionFont = new BitmapFont(Gdx.files.internal("font.fnt"));
    private Label.LabelStyle descriptionLabelStyle;
    private Label desctiptionLabel;

    private int noScaleWidth = 1280;
    float scaleC = (float) SCREEN_WIDTH / noScaleWidth;

    public UpgradesScene(Car car, Stage mainStage) {
        this.mainStage = mainStage;
        stage = new Stage(new ScreenViewport());
        purchaseStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        descriptionLabelStyle = new Label.LabelStyle(descriptionFont, Color.BLACK);
        descriptionFont.getData().setScale(2 * scaleC, 2 * scaleC);
        desctiptionLabel = new Label("Доступные улучшения", descriptionLabelStyle);
        desctiptionLabel.setPosition(SCREEN_WIDTH / 2f - desctiptionLabel.getWidth() / 2, SCREEN_HEIGHT - desctiptionLabel.getHeight() - 50);

        font.getData().setScale(descriptionFont.getScaleX() / 1.8f, descriptionFont.getScaleY() / 1.8f);

        stage.addActor(desctiptionLabel);

        Table upgradesTable = InitUpgradesTable(car);
        stage.addActor(upgradesTable);

        InitBackButton();
        MoneyTable.changeAndGetMoneyTable(stage);
    }

    private Table InitUpgradesTable(Car car){
        Table table = new Table();
        table.setName("upgradesTable");
        ArrayList<AbstractUpgrade> upgrades = car.getUpgrades();
        float maxLabelWidth = Float.MIN_NORMAL;
        float current_max = 0f;
        float labelHeight = 0f;
        float currentColumnSize = -1;
        for (AbstractUpgrade upgrade : upgrades) {
            Label label = new Label(upgrade.getType().toString().replace("_", " "), style);
            float width = label.getWidth();
            labelHeight = label.getHeight();
            current_max = Math.max(maxLabelWidth, width);
            if (current_max > maxLabelWidth) {
                maxLabelWidth = current_max;
            }
            currentColumnSize = current_max / 1.3f;
            table.add(label).width(current_max).padRight(tableRightPad);
        }
        table.add().row();

        for (AbstractUpgrade upgrade : upgrades) {
            ImageButton button = new ImageButton(new TextureRegionDrawable(upgrade.getTexture()));
            button.setName(upgrade.getType().toString());
            table.add(button)
                    .height(currentColumnSize).width(currentColumnSize)
                    .padRight(tableRightPad).padBottom(tableRightPad / 2);

            if (!upgrade.isPurchased()) {
                button.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        purchaseMenuFlag = true;
                        int index = table.getChildren().indexOf(button, false);
                        int nameIndex = index - table.getColumns() + 1;
                        int tableIndex = index + table.getColumns() - 1;
                        Label nameLabel = (Label) table.getChild(nameIndex);
                        ShowSelectedBonusPurchaseMenu(nameLabel.getText().toString(), upgrade.getCost(), upgrade.getType(), car);
                        Gdx.input.setInputProcessor(purchaseStage);
                    }
                });
            }
        }
        table.add().row();

        for (AbstractUpgrade upgrade : upgrades) {
            Table table1 = new Table();
            if (upgrade.isPurchased()) {
                table1.add(new Label("Уже куплено", style));
            } else {
                table1.add(new ImageButton(new TextureRegionDrawable(moneyTexture))).width(40).height(40).padRight(10);
                table1.add(new Label(String.valueOf(upgrade.getCost()), style));
            }
            table.add(table1).padRight(tableRightPad);
        }

        table.setSize(currentColumnSize, currentColumnSize + labelHeight);
        table.setPosition(50, SCREEN_HEIGHT / 3f - table.getHeight());
        table.setFillParent(true);
        return table;
    }

    private static Texture okTexture = new Texture(Gdx.files.internal("Store/ok.png"));

    private BitmapFont noMoneyFont = new BitmapFont(Gdx.files.internal("font.fnt"));

    private void DrawNoMoneyLabel(){
        for(int i = 0; i < purchaseStage.getActors().size;i++){
            if(purchaseStage.getActors().get(i).getName()!=null&&purchaseStage.getActors().get(i).getName().equals("noMoneyLabel")){
                purchaseStage.getActors().removeIndex(i); break;
            }
        }

        Label.LabelStyle labelStyle = new Label.LabelStyle(font,Color.WHITE);
        Label label = new Label("Недостаточно средств",labelStyle);
        label.setPosition(SCREEN_WIDTH/2f-label.getWidth()/2,50);
        purchaseStage.addActor(label);
    }
    private void ShowSelectedBonusPurchaseMenu(String name, int cost, AbstractUpgrade.UpgradeType type, Car car) {
        Label selectedDescriptionLabel = new Label("Подтвердить покупку?", descriptionLabelStyle);
        selectedDescriptionLabel.setPosition(SCREEN_WIDTH / 2f - selectedDescriptionLabel.getWidth() / 2f, SCREEN_HEIGHT - selectedDescriptionLabel.getHeight() - 50);

        Label.LabelStyle selectedBonusStyle = new Label.LabelStyle(font, Color.BLACK);
        Table table = new Table();
        float size = 100 * scaleC;
        table.add(new Label(name + ": ", selectedBonusStyle)).padRight(40);
        table.add(new ImageButton(new TextureRegionDrawable(moneyTexture))).width(size).height(size).padRight(10);
        table.add(new Label(String.valueOf(cost), selectedBonusStyle));
        table.setPosition(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2f);

        purchaseStage.addActor(selectedDescriptionLabel);
        purchaseStage.addActor(table);

        Button back = new Button(new TextureRegionDrawable(backTexture),
                new TextureRegionDrawable(backHoverTexture));
        back.setSize(SCREEN_HEIGHT / 6f, SCREEN_HEIGHT / 6f);
        back.setPosition(table.getX() - table.getChild(0).getWidth(), table.getY() - back.getHeight() * 2);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                purchaseMenuFlag = false;
                purchaseStage.getActors().clear();
                Gdx.input.setInputProcessor(stage);
            }
        });

        Button okButton = new Button(new TextureRegionDrawable(okTexture));
        okButton.setSize(SCREEN_HEIGHT / 6f, SCREEN_HEIGHT / 6f);
        okButton.setPosition(back.getX() + back.getWidth() + 300 * scaleC, back.getY());
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (Racing.money >= cost) {
                    Racing.money -= cost;
                    AbstractUpgrade upgrade = car.getUpgrades().stream().filter(o -> o.getType().equals(type)).findFirst().get();
                    upgrade.setPurchasedValue(true);
                    purchaseMenuFlag = false;
                    purchaseStage.getActors().clear();

                    for(int i = 0; i < stage.getActors().size; i++){
                        if(stage.getActors().get(i).getName()!=null&&stage.getActors().get(i).getName().equals("upgradesTable")){
                            stage.getActors().removeIndex(i); break;
                        }
                    }
                    Table newUpgradesTable = InitUpgradesTable(car);
                    stage.addActor(newUpgradesTable);
                    Racing.WriteCarsUpgradesInFile();
                    MoneyTable.changeAndGetMoneyTable(stage);
                    Racing.WriteMoneyInFile();
                    Gdx.input.setInputProcessor(stage);
                }else{
                    DrawNoMoneyLabel();
                }
            }
        });

        purchaseStage.addActor(okButton);
        purchaseStage.addActor(back);

        MoneyTable.changeAndGetMoneyTable(purchaseStage);
    }

    boolean backFlag = false;

    private static Texture backTexture = new Texture(Gdx.files.internal("Store/left.png"));
    private static Texture backHoverTexture = new Texture(Gdx.files.internal("Store/hover_left.png"));

    private void InitBackButton() {
        ImageButton back = new ImageButton(new TextureRegionDrawable(backTexture),
                new TextureRegionDrawable(backHoverTexture));
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
            purchaseStage.dispose();
            MoneyTable.changeAndGetMoneyTable(Store.stage);
            backFlag = false;
            Store.upgradesSceneFlag = false;
            Gdx.input.setInputProcessor(mainStage);
        } else if (!purchaseMenuFlag) {
            ScreenUtils.clear(0, 0, 8, 0);
            stage.act();
            stage.draw();
        } else {
            ScreenUtils.clear(0, 0, 8, 0);
            purchaseStage.act();
            purchaseStage.draw();
        }

    }

    @Override
    public void dispose() {

    }
}
