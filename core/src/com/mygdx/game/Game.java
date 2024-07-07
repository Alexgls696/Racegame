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
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Cars.Car;
import com.mygdx.game.Cars.EnemyCar;
import com.mygdx.game.Cars.upgrades.AbstractUpgrade;
import com.mygdx.game.Music.GameMusic;

import java.util.Random;

public class Game implements Scene{
    private Racing racing;
    private Settings settings;
    private Stage stage;
    private Stage stage_end;
    private Stage stage_pause;
    private BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"));
    private Label.LabelStyle style = new Label.LabelStyle();
    Label resultLabel;
    Label finalResultLabel;

    private SpriteBatch fon = new SpriteBatch();
    private SpriteBatch fon2 = new SpriteBatch();
    private SpriteBatch endBatch = new SpriteBatch();
    private SpriteBatch pauseBatch = new SpriteBatch();
    private static Texture field = new Texture("Game/Field.jpg");
    private static Texture desert = new Texture("Game/Desert.jpg");
    private static Texture endTexture = new Texture("Game/end.jpg");
    private static Texture pauseTexture = new Texture("Game/pause.jpg");
    private Texture drawFonTexture1=field;
    private Texture drawFonTexture2=field;
    int positionFon1=0, positionFon2=2400;
    private boolean flag_fon=false;

    private SpriteBatch carBatch = new SpriteBatch();
    private static Car gameCar;
    boolean flag_right=false, flag_left=false;
    boolean flag_gas=false, flag_breake=false;
    static int positionCarY=100;

    private GameMusic music;

    int score=0;
    boolean flag_score=false;
    boolean flag_end=false;
    boolean flag_pause=false;
    boolean flag_money=false;

    EnemyCar[] enemyCar=new EnemyCar[6];
    SpriteBatch[] enemyCarBatch=new SpriteBatch[6];

    public Game(Car gameCar, Racing racing)
    {
        this.racing=racing;
        this.gameCar=gameCar;
        stage = new Stage(new ScreenViewport());
        stage_end=new Stage(new ScreenViewport());
        stage_pause=new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        font.getData().setScale(1.2f*Gdx.graphics.getWidth() / 1080, 2.0f*Gdx.graphics.getHeight() / 1080);
        style.font = font;
        style.fontColor = Color.WHITE;
        resultLabel = new Label(""+score, style);
        resultLabel.setPosition((int)(850*Gdx.graphics.getWidth() / 1080),(int)(2100*Gdx.graphics.getHeight() / 2400));
        stage.addActor(resultLabel);
        finalResultLabel= new Label(""+score, style);
        finalResultLabel.setPosition((int)(230*Gdx.graphics.getWidth() / 1080),(int)(1470*Gdx.graphics.getHeight() / 2400));
        stage_end.addActor(finalResultLabel);

        init_buttons();
        create();
        createEnemyCars();

        music = GameMusic.MusicInitialize();
        music.getMenuMusic().stop();
        music.getGameMusic().play();

        settings=Settings.InitializeSettings(this, stage_pause);
    }
    public void create()
    {
        score=0;
        resultLabel.setText(""+score);
        positionFon1=0; positionFon2=2400;
        flag_fon=false;
        flag_right=false; flag_left=false;
        flag_gas=false; flag_breake=false;
        flag_score=false;
        flag_end=false;
        flag_pause=false;
        flag_money=false;
        gameCar.setPositionCar(gameCar.getBorderRight());
    }

    private void createEnemyCars()
    {
        enemyCar[0]=new EnemyCar("Cars/Enemies/RingLeft.png",-8, false);
        enemyCar[1]=new EnemyCar("Cars/Enemies/NeVestaLeft.png",-8,false);
        enemyCar[2]=new EnemyCar("Cars/Enemies/FuraLeft.png",-8, false);
        enemyCarBatch[0]=new SpriteBatch();
        enemyCarBatch[1]=new SpriteBatch();
        enemyCarBatch[2]=new SpriteBatch();

        enemyCar[3]=new EnemyCar("Cars/Enemies/RingRight.png",8, false);
        enemyCar[4]=new EnemyCar("Cars/Enemies/NeVestaRight.png",8,false);
        enemyCar[5]=new EnemyCar("Cars/Enemies/FuraRight.png",8, false);
        enemyCarBatch[3]=new SpriteBatch();
        enemyCarBatch[4]=new SpriteBatch();
        enemyCarBatch[5]=new SpriteBatch();
    }

  /*  private void upgrades(){
        if(gameCar.getUpgrades().get(0).getType()== AbstractUpgrade.UpgradeType.SLOW_MOTION){

        }
    }*/

    private void init_buttons() {
        ImageButton leftButton = new ImageButton(new TextureRegionDrawable(new Texture("Game/Left.png")));
        leftButton.setPosition((int)(10*Gdx.graphics.getWidth() / 1080), (int)(320*Gdx.graphics.getHeight() / 2400));
        leftButton.getImage().setFillParent(true);
        leftButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
               flag_left=true;
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                flag_left=false;
            }
        });
        stage.addActor(leftButton);

        ImageButton rightButton = new ImageButton(new TextureRegionDrawable(new Texture("Game/Right.png")));
        rightButton.setPosition((int)(130*Gdx.graphics.getWidth() / 1080), (int)(320*Gdx.graphics.getHeight() / 2400));
        rightButton.getImage().setFillParent(true);
        rightButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                flag_right=true;
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                flag_right=false;
            }
        });
        stage.addActor(rightButton);

        ImageButton gasButton = new ImageButton(new TextureRegionDrawable(new Texture("Game/Gas.png")));
        gasButton.setPosition((int)(950*Gdx.graphics.getWidth() / 1080), (int)(240*Gdx.graphics.getHeight() / 2400));
        gasButton.getImage().setFillParent(true);
        gasButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                flag_gas=true;
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                flag_gas=false;
            }
        });
        stage.addActor(gasButton);

        ImageButton breakeButton = new ImageButton(new TextureRegionDrawable(new Texture("Game/Breake.png")));
        breakeButton.setPosition((int)(840*Gdx.graphics.getWidth() / 1080), (int)(320*Gdx.graphics.getHeight() / 2400));
        breakeButton.getImage().setFillParent(true);
        breakeButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                flag_breake=true;
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                flag_breake=false;
            }
        });
        stage.addActor(breakeButton);


        ImageButton restartButton = new ImageButton(new TextureRegionDrawable(new Texture("Game/restartButtonUp.png")),new TextureRegionDrawable(new Texture("Game/restartButtonDown.png")));
        restartButton.setPosition((int)(330*Gdx.graphics.getWidth() / 1080), (int)(320*Gdx.graphics.getHeight() / 2400));
        restartButton.getImage().setFillParent(true);
        restartButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.input.setInputProcessor(stage);
                font.getData().setScale(1.2f*Gdx.graphics.getWidth() / 1080, 2.0f*Gdx.graphics.getHeight() / 1080);
                for(int i=0;i<enemyCar.length;i++){
                    enemyCar[i].setPositionY(2400);
                    enemyCar[i].setGo(false);
                }
                create();
                super.clicked(event, x, y);
            }
        });
        stage_end.addActor(restartButton);

        ImageButton mainMenuButton = new ImageButton(new TextureRegionDrawable(new Texture("Game/menuButtonUp.png")),new TextureRegionDrawable(new Texture("Game/menuButtonDown.png")));
        mainMenuButton.setPosition((int)(530*Gdx.graphics.getWidth() / 1080), (int)(320*Gdx.graphics.getHeight() / 2400));
        mainMenuButton.getImage().setFillParent(true);
        mainMenuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameMusic.MusicInitialize().getGameMusic().stop();
                GameMusic.MusicInitialize().getMenuMusic().play();
                racing.setCurrentScene(racing.getMainMenuScene());
                Gdx.input.setInputProcessor(MainMenu.menuStage);
                super.clicked(event, x, y);
            }
        });
        stage_end.addActor(mainMenuButton);


        ImageButton pauseButton = new ImageButton(new TextureRegionDrawable(new Texture("Game/pauseButtonUp.png")),new TextureRegionDrawable(new Texture("Game/pauseButtonDown.png")));
        pauseButton.setPosition((int)(25*Gdx.graphics.getWidth() / 1080), (int)(2125*Gdx.graphics.getHeight() / 2400));
        pauseButton.getImage().setFillParent(true);
        pauseButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                flag_pause=true;
                Gdx.input.setInputProcessor(stage_pause);
                super.clicked(event, x, y);
            }
        });
        stage.addActor(pauseButton);

        ImageButton continueButton = new ImageButton(new TextureRegionDrawable(new Texture("Game/continueButtonUp.png")),new TextureRegionDrawable(new Texture("Game/continueButtonDown.png")));
        continueButton.setPosition((int)(420*Gdx.graphics.getWidth() / 1080), (int)(1375*Gdx.graphics.getHeight() / 2400));
        continueButton.getImage().setFillParent(true);
        continueButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                flag_pause=false;
                Gdx.input.setInputProcessor(stage);
                super.clicked(event, x, y);
            }
        });
        stage_pause.addActor(continueButton);

        ImageButton settingsButton = new ImageButton(new TextureRegionDrawable(new Texture("Game/settingsButtonUp.png")),new TextureRegionDrawable(new Texture("Game/settingsButtonDown.png")));
        settingsButton.setPosition((int)(420*Gdx.graphics.getWidth() / 1080), (int)(875*Gdx.graphics.getHeight() / 2400));
        settingsButton.getImage().setFillParent(true);
        settingsButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                racing.setCurrentScene(racing.getSettingScene());
                Gdx.input.setInputProcessor(Settings.stage);
                super.clicked(event, x, y);
            }
        });
        stage_pause.addActor(settingsButton);

        ImageButton menuButton = new ImageButton(new TextureRegionDrawable(new Texture("Game/menuButtonPauseUp.png")),new TextureRegionDrawable(new Texture("Game/menuButtonPauseDown.png")));
        menuButton.setPosition((int)(420*Gdx.graphics.getWidth() / 1080), (int)(375*Gdx.graphics.getHeight() / 2400));
        menuButton.getImage().setFillParent(true);
        menuButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameMusic.MusicInitialize().getGameMusic().stop();
                GameMusic.MusicInitialize().getMenuMusic().play();
                racing.setCurrentScene(racing.getMainMenuScene());
                Gdx.input.setInputProcessor(MainMenu.menuStage);
                super.clicked(event, x, y);
            }
        });
        stage_pause.addActor(menuButton);
    }

    private void enemySpawn()
    {
        int[] numbersEnemyCar = {0, 1, 2, 3, 4, 5};
        int totalWeight = 0;
        Random random = new Random();

        int[] weights=new int[]{22, 22, 6, 22, 22, 6};
        for (int weight : weights) {
            totalWeight += weight;
        }

        int randomValue = random.nextInt(totalWeight);
        int cumulativeWeight = 0;

        for (int i = 0; i < numbersEnemyCar.length; i++) {
            cumulativeWeight += weights[i];
            if (randomValue < cumulativeWeight) {
                if(enemyCar[numbersEnemyCar[i]].getGo())
                    break;
                enemyCar[numbersEnemyCar[i]].setGo(true);
                int[] positions=new int[2];
                if(numbersEnemyCar[i]<3){
                    positions[0] =315; positions[1]=440;
                }else {
                    positions[0] =575; positions[1]=700;
                }
                int randomPosition = positions[random.nextInt(positions.length)];
                enemyCar[numbersEnemyCar[i]].setPositionX(randomPosition);
                enemyCar[numbersEnemyCar[i]].setPositionY(2400);
                break;
            }
        }
    }


    private void fonDraw() {
        fon.begin();
        fon.draw(drawFonTexture1, 0, (int) (positionFon1 * Gdx.graphics.getHeight() / 2400),
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        fon.end();

        fon2.begin();
        fon2.draw(drawFonTexture2, 0, (int) (positionFon2 * Gdx.graphics.getHeight() / 2400),
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        fon2.end();

        positionFon1 -= 15;
        positionFon2 -= 15;
        if (positionFon1 <= -2400){
            positionFon1 = positionFon2+2400;
            flag_score=true;
            if(flag_fon){
                if(drawFonTexture1==field){
                    drawFonTexture1=desert;
                }else{
                    drawFonTexture1=field;
                }
            }
        }
        if (positionFon2 <= -2400){
            positionFon2 = positionFon1+2400;
            flag_score=true;
            if(flag_fon){
                flag_fon=false;
                if(drawFonTexture2==field){
                    drawFonTexture2=desert;
                }else{
                    drawFonTexture2=field;
                }
            }
        }

        if(flag_score==true)
        {
            flag_score=false;
            score+=1;
            resultLabel.setText(""+score);

            if(score%4==0 && score<=8){
                enemySpawn();
            }else if(score%3==0 && score>8 && score<=20){
                enemySpawn();
            }else if(score%2==0 && score>=20 && score<=40){
                enemySpawn();
            }else if(score>40){
                enemySpawn();
            }

            if(score%50==0) flag_fon=true;
        }
    }
    private void enemyDraw()
    {
        for(int i=0;i<enemyCar.length;i++)
        {
            if(enemyCar[i].getGo())
            {
                enemyCarBatch[i].begin();
                enemyCarBatch[i].draw(enemyCar[i].getCarTexture(),
                        (int)(enemyCar[i].getPositionX()*Gdx.graphics.getWidth() / 1080),
                        (int)(enemyCar[i].getPositionY()*Gdx.graphics.getHeight() / 2400),
                        (int)(enemyCar[i].getCarTexture().getWidth()*Gdx.graphics.getWidth() / 1800),
                        (int)(enemyCar[i].getCarTexture().getHeight()*Gdx.graphics.getHeight() / 2400));
                enemyCarBatch[i].end();

                if(flag_gas) {
                    enemyCar[i].setPositionY(enemyCar[i].getPositionY() + enemyCar[i].getSpeed() - (gameCar.getSpeed()+15));
                }else if(flag_breake){
                    enemyCar[i].setPositionY(enemyCar[i].getPositionY() + enemyCar[i].getSpeed() - 10);
                }else{
                    enemyCar[i].setPositionY(enemyCar[i].getPositionY() + enemyCar[i].getSpeed() - 15);
                }

                if(enemyCar[i].getPositionY()<-enemyCar[i].getCarTexture().getHeight())
                    enemyCar[i].setGo(false);
            }
        }
    }
    public void render()
    {
        if(!flag_end && !flag_pause){
            fonDraw();
            motion();
            carBatch.begin();
            carBatch.draw(gameCar.getCarTexture(), (int)(gameCar.getPositionCar()*Gdx.graphics.getWidth() / 1080),
                    (int)(positionCarY*Gdx.graphics.getHeight() / 2400),
                    (int)(gameCar.getCarTexture().getWidth()*Gdx.graphics.getWidth() / 1800),
                    (int)(gameCar.getCarTexture().getHeight()*Gdx.graphics.getHeight() / 2400));
            carBatch.end();
            enemyDraw();
            stage.draw();
            for(int i=0;i<enemyCar.length;i++){
                if(isCollisionEnemy(enemyCar[i]) && enemyCar[i].getGo())
                {
                    flag_end=true;
                    finalResultLabel.setText(""+score);
                    Gdx.input.setInputProcessor(stage_end);
                    font.getData().setScale(2.2f*Gdx.graphics.getWidth() / 1080, 4.0f*Gdx.graphics.getHeight() / 1080);
                }
            }
        }else if(flag_end){
            endBatch.begin();
            endBatch.draw(endTexture, 0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
            endBatch.end();
            if(!flag_money){
                flag_money=true;
                Racing.money+=score;
                Racing.WriteMoneyInFile();
                MoneyTable.changeAndGetMoneyTable(Store.stage);
            }
            stage_end.draw();
        } else if(flag_pause){
            pauseBatch.begin();
            pauseBatch.draw(pauseTexture, 0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
            pauseBatch.end();

            stage_pause.draw();
        }
    }

    private void motion()
    {
        if(flag_right && gameCar.getPositionCar()<gameCar.getBorderRight())
        {
            gameCar.setPositionCar(gameCar.getPositionCar()+10);
        }
        if(flag_left && gameCar.getPositionCar() > gameCar.getBorderLeft())
        {
            gameCar.setPositionCar(gameCar.getPositionCar()-10);
        }
        if(flag_gas)
        {
            positionFon1-=gameCar.getSpeed();
            positionFon2-=gameCar.getSpeed();
        }
        if(flag_breake)
        {
            positionFon1+=5;
            positionFon2+=5;
        }
    }

    public static boolean isCollisionEnemy(EnemyCar enemy) {
        Rectangle rect1= new Rectangle((int)(gameCar.getPositionCar()*Gdx.graphics.getWidth() / 1080),
                (int)(positionCarY*Gdx.graphics.getHeight() / 2400),
                (int)(gameCar.getCarTexture().getWidth()*Gdx.graphics.getWidth() / 1800),
                (int)(gameCar.getCarTexture().getHeight()*Gdx.graphics.getHeight() / 2400));
        Rectangle rect2=new Rectangle((int)(enemy.getPositionX()*Gdx.graphics.getWidth() / 1080),
                (int)(enemy.getPositionY()*Gdx.graphics.getHeight() / 2400),
                (int)(enemy.getCarTexture().getWidth()*Gdx.graphics.getWidth() / 1800),
                (int)(enemy.getCarTexture().getHeight()*Gdx.graphics.getHeight() / 2400));
        return rect1.overlaps(rect2);
    }

    public void dispose()
    {
        fon.dispose();
        fon2.dispose();
        field.dispose();
        desert.dispose();
        endBatch.dispose();
        pauseBatch.dispose();
        stage.dispose();
        stage_end.dispose();
        stage_pause.dispose();
    }
}