package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Music.GameMusic;

import java.io.File;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainMenu implements Scene {
    public static Stage menuStage;
    private Stage settingsStage;

    private SpriteBatch backgroundSprite;
    private Texture backgroundTexture;
    private final int SCREEN_WIDTH = Gdx.graphics.getWidth();

    private final int SCREEN_HEIGHT = Gdx.graphics.getHeight();
    private GameMusic gameMusic;


    private void ButtonsInit(Racing racing) {
        Button playButton = new Button(new TextureRegionDrawable(new Texture("Menu/play.png")),
                new TextureRegionDrawable(new Texture("Menu/play_hover.png")));
        Button settingButton = new Button(new TextureRegionDrawable(new Texture("Menu/settings.png")),
                new TextureRegionDrawable(new Texture("Menu/settings_hover.png")));
        Button exitButton = new Button(new TextureRegionDrawable(new Texture("Menu/exit.png")),
                new TextureRegionDrawable(new Texture("Menu/exit_hover.png")));

        Button everyDayButton = new Button(new TextureRegionDrawable(new Texture("Menu/everyday.png")));

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                racing.setCurrentScene(racing.getStoreScene());
                Gdx.input.setInputProcessor(Store.stage);
            }
        });

        settingButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                racing.setCurrentScene(racing.getSettingScene());
                Gdx.input.setInputProcessor(Settings.stage);
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        everyDayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Ежедневные задания
            }
        });

        int buttonHeight = Gdx.graphics.getHeight() / 6;


        everyDayButton.setSize(buttonHeight, buttonHeight);
        everyDayButton.setPosition(Gdx.graphics.getWidth() - everyDayButton.getWidth() - 50, Gdx.graphics.getHeight() - everyDayButton.getHeight() - 50);
        menuStage.addActor(everyDayButton);

        Table table = new Table();

        float width = 500 * SCREEN_WIDTH / 2400f;
        float height = 225 * SCREEN_HEIGHT / 1080f;
        table.add(playButton).width(width).height(height).padBottom((500 * SCREEN_WIDTH / 2400) / 10).row();
        table.add(settingButton).width(width).height(height).padBottom((500 * SCREEN_WIDTH / 2400) / 10).row();
        table.add(exitButton).width(width).height(height).padBottom((500 * SCREEN_WIDTH / 2400) / 10);
        table.setPosition(SCREEN_WIDTH / 2f, SCREEN_HEIGHT / 2.6f);
        menuStage.addActor(table);
    }

    public MainMenu(Racing racing) {
        menuStage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(menuStage);
        backgroundTexture = new Texture(Gdx.files.internal("background.png"));
        backgroundSprite = new SpriteBatch();
        ButtonsInit(racing);
    }

    @Override
    public void create() {

    }

    @Override
    public void render() {
        backgroundSprite.begin();
        backgroundSprite.draw(backgroundTexture, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        backgroundSprite.end();

        menuStage.act();
        menuStage.draw();
    }

    @Override
    public void dispose() {

    }
}
