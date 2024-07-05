package com.mygdx.game;

import com.badlogic.gdx.Gdx;
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

public class MainMenu implements Scene{
    public static Stage menuStage;
    private Stage settingsStage;

    private SpriteBatch backgroundSprite;
    private Texture backgroundTexture;
    private final int SCREEN_WIDTH = Gdx.graphics.getWidth();;
    private final int SCREEN_HEIGHT = Gdx.graphics.getHeight();


    private void ButtonsInit(Racing racing){
        Button playButton = new Button(new TextureRegionDrawable(new Texture("Menu/play.png")),
                new TextureRegionDrawable(new Texture("Menu/play_hover.png")));
        Button settingButton = new Button(new TextureRegionDrawable(new Texture("Menu/settings.png")),
                new TextureRegionDrawable(new Texture("Menu/settings_hover.png")));
        Button exitButton = new Button(new TextureRegionDrawable(new Texture("Menu/exit.png")),
                new TextureRegionDrawable(new Texture("Menu/exit_hover.png")));

        Button everyDayButton = new Button(new TextureRegionDrawable(new Texture("Menu/everyday.png")));

        playButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                racing.setCurrentScene(racing.getStoreScene());
                Gdx.input.setInputProcessor(Store.stage);
            }
        });

        settingButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event,float x, float y){

            }
        });

        exitButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        everyDayButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                //Ежедневные задания
            }
        });

        int buttonHeight = Gdx.graphics.getHeight() / 6;


        everyDayButton.setSize(buttonHeight,buttonHeight);
        everyDayButton.setPosition(Gdx.graphics.getWidth()-everyDayButton.getWidth()-50,Gdx.graphics.getHeight()-everyDayButton.getHeight()-50);
        menuStage.addActor(everyDayButton);

        Table table = new Table();
        table.add(playButton).width(600*SCREEN_WIDTH/2400).height(250*SCREEN_HEIGHT/1080).padBottom((500*SCREEN_WIDTH/2400)/4).row();
        table.add(settingButton).width(600*SCREEN_WIDTH/2400).height(250*SCREEN_HEIGHT/1080).padBottom((500*SCREEN_WIDTH/2400)/4).row();
        table.add(exitButton).width(600*SCREEN_WIDTH/2400).height(250*SCREEN_HEIGHT/1080).padBottom((500*SCREEN_WIDTH/2400)/4);
        table.setPosition(SCREEN_WIDTH/2f,SCREEN_HEIGHT/2.2f);
        menuStage.addActor(table);
    }

    public MainMenu(Racing racing){
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
        backgroundSprite.draw(backgroundTexture,0,0,SCREEN_WIDTH,SCREEN_HEIGHT);
        backgroundSprite.end();

        menuStage.act();
        menuStage.draw();
    }

    @Override
    public void dispose() {

    }
}
