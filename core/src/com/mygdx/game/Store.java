package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Cars.Car;
import com.mygdx.game.Cars.UpgradesScene;

import java.awt.Font;
import java.util.ArrayList;

public class Store implements Scene {
    private final int barCount = 4;
    private Bar[] bars = new Bar[barCount];
    private Stage stage;

    private final int screen_height = Gdx.graphics.getHeight();

    private final int screen_width = Gdx.graphics.getWidth();

    private final int my_screen_height = 2400;
    private final int my_screen_width = 1080;

    private BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"));
    private Label.LabelStyle style = new Label.LabelStyle();

    private ImageButton left;
    private ImageButton right;
    private ImageButton upgradeImage;

    private float pad;
    private ScrollPane scroller;
    private ArrayList<Car> cars;

    private float specificationPositionX;
    private float specificationPositionY;
    private Scene upgradesScene;

    private float fontScale = screen_width/1280;

    private float carWidth = screen_width / 4.5f;
    private float carHeight = screen_height / 1.5f;

    private float specificationTableX;
    private float specificationTableY;

    private void InitScrollTable() {
        Table scrollTable = new Table();
        carWidth = screen_width / 4.5f;
        float padRight = carWidth / 2;
        pad = padRight;

        for (int i = 0; i < cars.size(); i++) {
            Image image = new Image(cars.get(i).getCarTexture());
            if (i == cars.size() - 1) {
                scrollTable.add(image).width(carWidth).height(carHeight);
            } else scrollTable.add(image).width(carWidth).height(carHeight).padRight(padRight);
        }
        float textHeight = carHeight / 8f;
        scrollTable.add().row();
        for (int i = 0; i < cars.size(); i++) {
            Label label = new Label(cars.get(i).getName(), style);
            if (i == cars.size() - 1) {
                scrollTable.add(label).width(carWidth).padTop(20);
            } else
                scrollTable.add(label).padTop(20).width(carWidth).height(textHeight).padRight(padRight);
        }
        scroller = new ScrollPane(scrollTable);
        scroller.setScrollingDisabled(false, true);
        scroller.setFlickScroll(false);

        specificationTableX = screen_width / 2f - carWidth / 2;
        specificationTableY = screen_height - carHeight - textHeight - 100;

        final Table finalTable = new Table();
        finalTable.setSize(carWidth, carHeight + textHeight + 20);
        finalTable.setPosition(specificationTableX, specificationTableY);
        finalTable.add(scroller);
        finalTable.setName("scroll_table");

        specificationPositionX = specificationTableX + finalTable.getWidth() + screen_width / 8f;
        specificationPositionY = specificationTableY + finalTable.getHeight();

        stage.addActor(finalTable);
    }

    private void InitMoneyTable() {
        Table table = MoneyTable.changeAndGetMoneyTable(stage);
        float width = table.getWidth();
        table.setPosition(screen_width - table.getWidth() - 20, screen_height - 100);
        stage.addActor(table);
    }

    private Table specificationsTable;

    private void InitSpecificationsTable() {
        int width = screen_width / 6;
        int height = screen_height / 16;
        int speed = cars.get(chooseCarIndex).getSpeed();

        int barId = -1;
        for (int i = 0; i < bars.length; i++) {
            if (bars[i].number == speed) {
                barId = i;
                break;
            }
        }

        specificationsTable = new Table();

        int pad = 20;
        specificationsTable.add(new Label("Скорость", style)).padRight(pad).row();
        specificationsTable.add(bars[barId].image).width(width).height(height).padRight(pad);
        specificationsTable.add(new Label(String.valueOf(bars[barId].number), style)).row();
        specificationsTable.setPosition(specificationPositionX, specificationPositionY);
        specificationsTable.setName("specTable");
        stage.addActor(specificationsTable);
    }


    public static int chooseCarIndex = 0;

    private void changeCarSpecificationInStore() {
        Array<Actor> actors = stage.getActors();
        for (Actor actor : actors) {
            if (actor.getName() != null && actor.getName().equals("specTable")) {
                Table table = (Table) actor;
                int index = stage.getActors().indexOf(table, false);
                stage.getActors().removeIndex(index);
                InitSpecificationsTable();
                break;
            }
        }
    }


    public static boolean upgradesSceneFlag = false;

    private void InitUpgradeButton() {
        upgradeImage = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("Store/upgrades.png"))));

        Table table = new Table();
        Label label = new Label("Улучшения", style);
        float size = label.getWidth();
        table.add(label).width(size).height(size/2).row();

        table.setSize(size, size + size / 2f);
        table.setPosition(screen_width - size-50, screen_height / 3f);
        table.add(upgradeImage).width(size).height(size);
        table.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                upgradesSceneFlag=true;
                upgradesScene = new UpgradesScene(cars.get(chooseCarIndex),stage);
            }
        });
        stage.addActor(table);
    }

    private void InitStoreNavigationButtons() {

        left = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("Store/left.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("Store/hover_left.png"))));

        right = new ImageButton(new TextureRegionDrawable(new Texture(Gdx.files.internal("Store/right.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("Store/hover_right.png"))));

        int buttonHeight = screen_height / 6;
        int buttonWidth = screen_height / 6;

        left.setSize(buttonWidth, buttonHeight);
        right.setSize(buttonWidth, buttonHeight);

        left.getImage().setFillParent(true);
        right.getImage().setFillParent(true);

        left.setPosition(25, screen_height / 3f);
        right.setPosition(left.getX() + left.getWidth() + 50, screen_height / 3f);

        left.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                scroller.setScrollX(scroller.getScrollX() - carWidth - pad);
                scroller.addAction(Actions.sequence(Actions.delay(0.1f)));
                Racing.money--;
                InitMoneyTable();
                Racing.WriteMoneyInFile();
                if (chooseCarIndex > 0) {
                    chooseCarIndex--;
                }
                changeCarSpecificationInStore();
            }
        });

        right.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                scroller.setScrollX(scroller.getScrollX() + carWidth + pad);
                scroller.addAction(Actions.sequence(Actions.delay(0.1f)));
                Racing.money++;
                InitMoneyTable();
                Racing.WriteMoneyInFile();
                if (chooseCarIndex < cars.size() - 1) {
                    chooseCarIndex++;
                }
                changeCarSpecificationInStore();
            }
        });

        stage.addActor(left);
        stage.addActor(right);
    }
    //коэф нужная координата* (Координата устройства/Координата своего устройста)


    private class Bar {
        private Image image;
        private int number;

        public Bar(String texturePath, int number) {
            image = new Image(new Texture(Gdx.files.internal(texturePath)));
            this.number = number;
        }
    }

    private void LoadBarTextures() {
        for (int i = 0; i < barCount; i++) {
            bars[i] = new Bar("Progress bar/bar_" + (i + 1) + ".png", 25 * (i + 1));
        }
    }

    public Store() {
        create();
    }

    @Override
    public void create() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        font.getData().setScale(fontScale,fontScale);
        style.font = font;
        style.fontColor = Color.BLACK;

        cars = Racing.cars;

        LoadBarTextures();
        InitScrollTable();
        InitStoreNavigationButtons();

        InitSpecificationsTable();
        InitMoneyTable();
        InitUpgradeButton();
    }

    @Override
    public void render() {
        if(upgradesSceneFlag){
            upgradesScene.render();
        }else {
            ScreenUtils.clear(2, 0, 8, 0);
            stage.act();
            stage.draw();
        }
    }

    @Override
    public void dispose() {

    }
}
