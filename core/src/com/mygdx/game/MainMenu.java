package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Music.GameMusic;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class MainMenu implements Scene {
    public static Stage menuStage;
    private Stage tasksStage;
    private Stage settingsStage;

    private BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"));
    private Label.LabelStyle styleTasks = new Label.LabelStyle(font, Color.BLACK);
    Label[] taskLabel=new Label[3];

    private SpriteBatch backgroundSprite;
    private Texture backgroundTexture;
    private SpriteBatch tasksSprite;
    private Texture tasksTexture;
    private SpriteBatch logoSprite;
    private Texture logoTexture;
    private final int SCREEN_WIDTH = Gdx.graphics.getWidth();

    private final int SCREEN_HEIGHT = Gdx.graphics.getHeight();
    private GameMusic gameMusic;
    private Racing racing;

    private boolean flag_tasks=false;
    public static boolean flag_changeTasks=false;

    private void ButtonsInit(Racing racing) {
        Button playButton = new Button(new TextureRegionDrawable(new Texture("Menu/play.png")),
                new TextureRegionDrawable(new Texture("Menu/play_hover.png")));
        Button settingButton = new Button(new TextureRegionDrawable(new Texture("Menu/settings.png")),
                new TextureRegionDrawable(new Texture("Menu/settings_hover.png")));
        Button exitButton = new Button(new TextureRegionDrawable(new Texture("Menu/exit.png")),
                new TextureRegionDrawable(new Texture("Menu/exit_hover.png")));

        Button everyDayButton = new Button(new TextureRegionDrawable(new Texture("Menu/everyday.png")),
                new TextureRegionDrawable(new Texture("Menu/everydayUp.png")));

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
                racing.activity.finishAffinity();
                Gdx.app.exit();
            }
        });

        everyDayButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                flag_tasks=true;
                Gdx.input.setInputProcessor(tasksStage);
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

        this.racing=racing;

        ImageButton back  = new ImageButton(new TextureRegionDrawable(new Texture("Store/left.png")),new TextureRegionDrawable(new Texture("Store/hover_left.png")));
        back.setSize(SCREEN_HEIGHT / 6f, SCREEN_HEIGHT / 6f);
        back.setPosition(50, SCREEN_HEIGHT - back.getHeight() - 100);
        back.getImage().setFillParent(true);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                flag_tasks=false;
                Gdx.input.setInputProcessor(menuStage);
            }
        });
        tasksStage.addActor(back);
    }

    public MainMenu(Racing racing) {
        menuStage = new Stage(new ScreenViewport());
        tasksStage=new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(menuStage);
        backgroundTexture = new Texture(Gdx.files.internal("Menu/Menu_fon.jpg"));
        backgroundSprite = new SpriteBatch();
        tasksTexture=new Texture(Gdx.files.internal("Menu/tasks.png"));
        tasksSprite=new SpriteBatch();
        logoTexture=new Texture("Menu/logo.png");
        logoSprite=new SpriteBatch();
        ButtonsInit(racing);

        font.getData().setScale(1.5f*Gdx.graphics.getWidth() / 2400, 1.5f*Gdx.graphics.getHeight() / 1080);
    }

    @Override
    public void create() {

    }

    public void DailyTaskLabelDraw(){
        for(int i = 0; i < tasksStage.getActors().size; i++){
            if (tasksStage.getActors().get(i).getName()!=null&&tasksStage.getActors().get(i).getName().equals("taskLabel")) {
                tasksStage.getActors().removeIndex(i); break;
            }
        }
        ArrayList<DailyTask>tasks = racing.getTasks();
        String line = "";
        for(DailyTask task: tasks){
            if(!task.getCompleted())
                line+=task.getName().replace("_"," ")+"\nНаграда: "+task.getCost()+"\n\n";
        }
        Label label = new Label(line ,styleTasks);
        label.setName("taskLabel");
        label.setPosition((int)(620*Gdx.graphics.getWidth() / 2400),(int)(290*Gdx.graphics.getHeight() / 1080));
        tasksStage.addActor(label);
    }

    private void DrawDifferenceLabel(){
        for(int i = 0; i < tasksStage.getActors().size; i++){
            if (tasksStage.getActors().get(i).getName()!=null&&tasksStage.getActors().get(i).getName().equals("diff")) {
                tasksStage.getActors().removeIndex(i); break;
            }
        }
        Label label = new Label("До обновления\nзаданий осталось: "+racing.getDiffLine(),styleTasks);
        label.setPosition((int)(700*Gdx.graphics.getWidth() / 2400),(int)(250*Gdx.graphics.getHeight() / 1080));
        label.setName("diff");
        tasksStage.addActor(label);
    }


    @Override
    public void render() {
        backgroundSprite.begin();
        backgroundSprite.draw(backgroundTexture, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        backgroundSprite.end();

        if(flag_tasks){
            tasksSprite.begin();
            tasksSprite.draw(tasksTexture,
                    (int)(600*Gdx.graphics.getWidth() / 2400),
                    (int)(200*Gdx.graphics.getHeight() / 1080),
                    (int)(tasksTexture.getWidth()*Gdx.graphics.getWidth() / 2400),
                    (int)(tasksTexture.getHeight()*Gdx.graphics.getHeight() / 1080));
            tasksSprite.end();

            if(racing.isChangeDailyTasks() || flag_changeTasks)
            {
                flag_changeTasks=false;
                DailyTaskLabelDraw();
            }
            if(racing.isDrawDiffLabel()){
                racing.setDrawDiffLabel(false);
                DrawDifferenceLabel();
            }

            tasksStage.draw();
        }else{
            logoSprite.begin();
            logoSprite.draw(logoTexture,
                    (int)(765*Gdx.graphics.getWidth() / 2400),
                    (int)(900*Gdx.graphics.getHeight() / 1080),
                    (int)(logoTexture.getWidth()*Gdx.graphics.getWidth() / 2400),
                    (int)(logoTexture.getHeight()*Gdx.graphics.getHeight() / 1080));
            logoSprite.end();
            menuStage.act();
            menuStage.draw();
        }
    }

    @Override
    public void dispose() {

    }
}
