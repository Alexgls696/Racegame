package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
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
    private Racing racing;//Для возврата в главное меню

    private final int barCount = 4;
    private Bar[] bars = new Bar[barCount];
    public static Stage stage;

    private final int screen_height = Gdx.graphics.getHeight();

    private final int screen_width = Gdx.graphics.getWidth();

    private final int my_screen_height = 2400;
    private final int my_screen_width = 1080;

    private BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"));
    private Label.LabelStyle style = new Label.LabelStyle();

    private Button left;
    private Button right;
    private Button upgradeImage;

    private float pad;
    private ScrollPane scroller;
    private ArrayList<Car> cars;

    private float specificationPositionX;
    private float specificationPositionY;
    private Scene upgradesScene;

    private float fontScale = screen_width / 1280;

    private float carWidth;
    private float carHeight;
    private float scrollTableX;
    private float scrollTableY;

    public static int chooseCarIndex = 0; //Индекс выбранной машины в списке машин

    private void InitScrollTable() {
        float scaleTableWidth = screen_width / 2400f;
        float scaleTableHeight = screen_height / 1080f;
        Table scrollTable = new Table();
        carWidth = scaleTableWidth * 400;
        carHeight = scaleTableHeight * 600;

        float padRight = carWidth / 2;
        pad = padRight;

        for (int i = 0; i < cars.size(); i++) {
            Image image = new Image(cars.get(i).getStoreTexture());
            if (i == cars.size() - 1) {
                scrollTable.add(image).width(carWidth).height(carHeight);
            } else scrollTable.add(image).width(carWidth).height(carHeight).padRight(padRight);
        }
        float textHeight = carHeight / 8f;

        for (int i = 0; i < cars.size(); i++) {
            Label label = new Label(cars.get(i).getName(), style);
            if (i == cars.size() - 1) {
                scrollTable.add(label).width(carWidth).padTop(20);
            } else
                scrollTable.add(label).padTop(20).width(carWidth).height(textHeight).padRight(padRight);
        }
        scrollTable.add().row();
        for (int i = 0; i < cars.size(); i++) {
            Image image = new Image(cars.get(i).getStoreTexture());
            if (i == cars.size() - 1) {
                scrollTable.add(image).width(carWidth).height(carHeight);
            } else scrollTable.add(image).width(carWidth).height(carHeight).padRight(padRight);
        }

        scroller = new ScrollPane(scrollTable);
        scroller.setScrollingDisabled(false, true);
        scroller.setFlickScroll(false);

        scrollTableX = screen_width / 2f - carWidth / 2;
        scrollTableY = screen_height / 2f - carHeight / 2f - textHeight;

        final Table finalTable = new Table();
        finalTable.setSize(carWidth, carHeight + textHeight + 20);
        finalTable.setPosition(scrollTableX, scrollTableY);
        finalTable.add(scroller);
        finalTable.setName("scroll_table");

        stage.addActor(finalTable);
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
        specificationsTable.setPosition(screen_width - screen_width / 4f, screen_height - height * 2);
        specificationsTable.setName("specTable");
        stage.addActor(specificationsTable);
    }

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
        upgradeImage = new Button(new TextureRegionDrawable(new Texture(Gdx.files.internal("Store/upgrades.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("Store/upgrades_hover.png"))));

        float size = screen_height / 6f;
        upgradeImage.setSize(size, size);
        upgradeImage.setPosition(screen_width - size - 50, screen_height / 2f - size / 2);

        upgradeImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (cars.get(chooseCarIndex).isPurchased()) {
                    upgradesSceneFlag = true;
                    upgradesScene = new UpgradesScene(cars.get(chooseCarIndex), stage);
                }
            }
        });
        stage.addActor(upgradeImage);
    }

    private BitmapFont purchaseFont = new BitmapFont(Gdx.files.internal("font.fnt"));
    private Label.LabelStyle purchaseLabelStyle = new Label.LabelStyle();
    private Label purchaseLabel;

    private static BitmapFont carCostFont = new BitmapFont(Gdx.files.internal("font.fnt"));

    private void drawCarCostLabel(boolean isDrawing) {
        for (Actor actor : stage.getActors()) {
            if (actor.getName() != null && actor.getName().equals("costLabel")) {
                Label label = (Label) actor;
                int index = stage.getActors().indexOf(label, false);
                stage.getActors().removeIndex(index);
                break;
            }
        }

        if (isDrawing) {
            Label.LabelStyle carCostLabelStyle = new Label.LabelStyle(carCostFont, Color.YELLOW);
            carCostLabelStyle.font.getData().setScale(fontScale, fontScale);
            Label label = new Label("Цена: " + cars.get(chooseCarIndex).getCost(), carCostLabelStyle);
            label.setPosition(scrollTableX + carWidth / 4, scrollTableY - label.getHeight());
            label.setName("costLabel");
            stage.addActor(label);
        }
    }

    private void purchaseStatusLabelDraw(boolean isPurchased, boolean clean) {
        if (clean) {
            int index = stage.getActors().indexOf(purchaseLabel, false);
            if (index > 0) {
                stage.getActors().removeIndex(index);
            }
            return;
        }
        if (purchaseLabel != null) {
            int index = stage.getActors().indexOf(purchaseLabel, false);
            if (index > 0) {
                stage.getActors().removeIndex(index);
            }
        }
        purchaseLabelStyle.font = purchaseFont;
        if (isPurchased) {
            purchaseLabelStyle.fontColor = Color.GREEN;
            purchaseLabel = new Label("Покупка успешно совершена", purchaseLabelStyle);
        } else {
            purchaseLabelStyle.fontColor = Color.WHITE;
            purchaseLabel = new Label("Недостаточно средств", purchaseLabelStyle);
        }
        purchaseLabel.setPosition(screen_width / 2f - purchaseLabel.getWidth(), 25);
        stage.addActor(purchaseLabel);
    }

    private static Texture playTexture = new Texture(Gdx.files.internal("Store/play.png"));
    private static Texture playHoverTexture = new Texture(Gdx.files.internal("Store/play_hover.png"));
    private static Texture lockedTexture = new Texture(Gdx.files.internal("Store/locked.png"));

    private Button playOrLockedButton = null;

    private void InitPlayOrBuyButton(boolean isPurchased) {
        playOrLockedButton = null;
        if (isPurchased) {
            playOrLockedButton = new Button(new TextureRegionDrawable(playTexture), new TextureRegionDrawable(playHoverTexture));
            playOrLockedButton.setName("play");
        } else {
            playOrLockedButton = new Button(new TextureRegionDrawable(lockedTexture));
            playOrLockedButton.setName("locked");
        }
        float buttonWidth = 350 * screen_width / 1280f;
        float buttonHeight = 175 * screen_width / 1280f;
        playOrLockedButton.setSize(buttonWidth, buttonHeight);
        playOrLockedButton.setPosition(screen_width - buttonWidth - 50, 50);

        playOrLockedButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (playOrLockedButton.getName().equals("locked")) {
                    if (Racing.money >= cars.get(chooseCarIndex).getCost()) {
                        cars.get(chooseCarIndex).setPurchased();
                        Racing.money -= cars.get(chooseCarIndex).getCost();
                        purchaseStatusLabelDraw(true,false);
                        InitPlayOrBuyButton(true);
                        MoneyTable.changeAndGetMoneyTable(stage);
                        Racing.WriteMoneyInFile();
                        Racing.WriteCarsInformationInFile();
                        drawCarCostLabel(false);
                    } else {
                        purchaseStatusLabelDraw(false,false);
                    }
                }
                if (playOrLockedButton.getName().equals("play")) {
                    //----------------------------------------------------Отсюда запуск игры
                }
            }
        });
        stage.addActor(playOrLockedButton);
    }

    private void InitStoreNavigationButtons() {
        left = new Button(new TextureRegionDrawable(new Texture(Gdx.files.internal("Store/left.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("Store/hover_left.png"))));

        right = new Button(new TextureRegionDrawable(new Texture(Gdx.files.internal("Store/right.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("Store/hover_right.png"))));

        left.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                scroller.setScrollX(scroller.getScrollX() - carWidth - pad);
                scroller.addAction(Actions.sequence(Actions.delay(0.1f)));
                if (chooseCarIndex > 0) {
                    purchaseStatusLabelDraw(false,true);
                    chooseCarIndex--;
                    if (cars.get(chooseCarIndex).isPurchased()) {
                        int index = stage.getActors().indexOf(playOrLockedButton, false);
                        stage.getActors().removeIndex(index);
                        InitPlayOrBuyButton(true);
                        drawCarCostLabel(false);
                    } else {
                        int index = stage.getActors().indexOf(playOrLockedButton, false);
                        stage.getActors().removeIndex(index);
                        InitPlayOrBuyButton(false);
                        drawCarCostLabel(true);
                    }
                }
                changeCarSpecificationInStore();
            }
        });

        right.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                scroller.setScrollX(scroller.getScrollX() + carWidth + pad);
                scroller.addAction(Actions.sequence(Actions.delay(0.1f)));
                if (chooseCarIndex < cars.size() - 1) {
                    purchaseStatusLabelDraw(false,true);
                    chooseCarIndex++;
                    if (cars.get(chooseCarIndex).isPurchased()) {
                        int index = stage.getActors().indexOf(playOrLockedButton, false);
                        stage.getActors().removeIndex(index);
                        InitPlayOrBuyButton(true);
                        drawCarCostLabel(false);
                    } else {
                        int index = stage.getActors().indexOf(playOrLockedButton, false);
                        stage.getActors().removeIndex(index);
                        InitPlayOrBuyButton(false);
                        drawCarCostLabel(true);
                    }
                }
                changeCarSpecificationInStore();
            }
        });

        int buttonHeight = screen_height / 6;
        int buttonWidth = screen_height / 6;
        left.setSize(buttonWidth, buttonHeight);
        right.setSize(buttonWidth, buttonHeight);

        left.setPosition(25, screen_height / 2f - buttonHeight / 2f);
        right.setPosition(left.getX() + left.getWidth() + 50, screen_height / 2f - buttonHeight / 2f);

        stage.addActor(left);
        stage.addActor(right);
    }

    private void InitBackButton() //Изменить иконку при желании
    {
        Button backButton = new Button(new TextureRegionDrawable(new Texture(Gdx.files.internal("Store/left.png"))),
                new TextureRegionDrawable(new Texture(Gdx.files.internal("Store/hover_left.png"))));
        int buttonHeight = screen_height / 6;
        int buttonWidth = screen_height / 6;
        backButton.setSize(buttonWidth,buttonHeight);
        backButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                racing.setCurrentScene(racing.getMainMenuScene());
                Gdx.input.setInputProcessor(MainMenu.menuStage);
            }
        });
        backButton.setPosition(50,screen_height-backButton.getHeight()-50);
        stage.addActor(backButton);
    }

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

    public Store(Racing racing) {
        this.racing=racing;
        create();
    }

    @Override
    public void create() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        font.getData().setScale(fontScale, fontScale);
        style.font = font;
        style.fontColor = Color.BLACK;

        cars = Racing.cars;

        LoadBarTextures();
        InitScrollTable();
        InitStoreNavigationButtons();

        InitSpecificationsTable();
        MoneyTable.changeAndGetMoneyTable(stage);
        InitUpgradeButton();
        InitBackButton();
        InitPlayOrBuyButton(true);
    }

    @Override
    public void render() {
        if (upgradesSceneFlag) {
            upgradesScene.render();
        } else {
            ScreenUtils.clear(2, 0, 8, 0);
            stage.act();
            stage.draw();
        }
    }

    @Override
    public void dispose() {

    }
}
