package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
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

import java.util.ArrayList;

public class Store implements Scene {
    private final int barCount = 4;
    private Bar[] bars = new Bar[barCount];
    private Stage stage;

    private final int screen_height = Gdx.graphics.getHeight();

    private final int screen_width = Gdx.graphics.getWidth();

    private final int my_screen_height = 2400;
    private final int my_screen_width = 1080;

    BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"));
    Label.LabelStyle style = new Label.LabelStyle();

    private ImageButton left;
    private ImageButton right;

    private int pad;
    private ScrollPane scroller;
    private ArrayList<Car> cars;

    private float specificationPositionX;
    private float specificationPositionY = Gdx.graphics.getHeight()-200;

    private void InitScrollTable() {
        Table scrollTable = new Table();
        int carWidth = screen_height / 3;
        int carHeight = screen_width / 3;
        int padRight = carWidth / 2;
        pad = padRight;
        for (int i = 0; i < cars.size(); i++) {
            Image image = new Image(cars.get(i).getStoreTexture());
            if (i == cars.size() - 1) {
                scrollTable.add(image).width(carWidth).height(carHeight);
            } else scrollTable.add(image).width(carWidth).height(carHeight).padRight(padRight);
        }

        scrollTable.add().row();
        for (int i = 0; i < cars.size(); i++) {
            Label label = new Label(cars.get(i).getName(), style);
            if (i == cars.size() - 1) {
                scrollTable.add(label).padTop(40);
            } else scrollTable.add(label).padTop(40).padRight(padRight);
        }
        scroller = new ScrollPane(scrollTable);
        scroller.setScrollingDisabled(false, true);
        scroller.setFlickScroll(false);

        final Table finalTable = new Table();
        finalTable.setSize(carWidth, carHeight + 100);
        finalTable.setPosition(screen_width / 2f - carWidth / 2f, 0);
        finalTable.add(scroller);
        finalTable.setName("scroll_table");

        specificationPositionX = screen_width / 2f + carWidth*1.7f;

        stage.addActor(finalTable);
    }

    private void InitMoneyTable(){
        Table table = MoneyTable.changeAndGetMoneyTable(stage);
        table.setPosition(100,screen_height-100);
        stage.addActor(table);
    }

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

        Table specificationsTable = new Table();

        int pad = 20;
        specificationsTable.add(new Label("Скорость", style)).padRight(pad);
        specificationsTable.add(bars[barId].image).width(width).height(height).padRight(pad);
        specificationsTable.add(new Label(String.valueOf(bars[barId].number), style)).row();
        specificationsTable.setPosition(specificationPositionX, specificationPositionY);
        specificationsTable.setName("specTable");
        stage.addActor(specificationsTable);
    }

    public static int chooseCarIndex = 0;

    private void changeCarInStore(){
        Array<Actor> actors = stage.getActors();
        for(Actor actor:actors){
            if(actor.getName()!=null&&actor.getName().equals("specTable")){
                Table table = (Table)actor;
                int index = stage.getActors().indexOf(table,false);
                stage.getActors().removeIndex(index);
                InitSpecificationsTable();
                break;
            }
        }
    }

    private void initStoreButtons() {

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

        left.setPosition(50, screen_height / 3f);
        right.setPosition(screen_width - right.getWidth() - 100, screen_height / 3f);

        left.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                scroller.setScrollX(scroller.getScrollX() - screen_height / 3-pad);
                scroller.addAction(Actions.sequence(Actions.delay(0.1f)));
                Racing.money--;
                InitMoneyTable();
                Racing.WriteMoneyInFile();
                if(chooseCarIndex>0){
                    chooseCarIndex--;
                }
                changeCarInStore();
            }
        });

        right.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                scroller.setScrollX(scroller.getScrollX() + screen_height / 3+pad);
                scroller.addAction(Actions.sequence(Actions.delay(0.1f)));
                Racing.money++;
                InitMoneyTable();
                Racing.WriteMoneyInFile();
                if(chooseCarIndex<cars.size()-1){
                    chooseCarIndex++;
                }
                changeCarInStore();
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

        style.font = font;
        style.fontColor = Color.BLACK;
        cars = Racing.cars;

        LoadBarTextures();
        InitScrollTable();
        initStoreButtons();

        InitSpecificationsTable();
        InitMoneyTable();
    }

    @Override
    public void render() {
        ScreenUtils.clear(2, 0, 8, 0);
        stage.act();
        stage.draw();
    }

    @Override
    public void dispose() {

    }
}
