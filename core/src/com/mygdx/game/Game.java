package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
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
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Cars.Car;
import com.mygdx.game.Cars.EnemyCar;
import com.mygdx.game.Cars.upgrades.AbstractUpgrade;
import com.mygdx.game.Music.GameMusic;

import java.util.ArrayList;
import java.util.Random;

public class Game implements Scene{
    private Racing racing;
    private Settings settings;
    private ArrayList<DailyTask> tasks;
    private Stage stage;
    private Stage stage_end;
    private Stage stage_pause;
    private BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"));
    private Label.LabelStyle style = new Label.LabelStyle();
    Label resultLabel;
    Label finalResultLabel;
    Label taskCompleteLabel;
    Label moneyLabel;
    ImageButton leftButton;
    ImageButton rightButton;

    private SpriteBatch fon = new SpriteBatch();
    private SpriteBatch fon2 = new SpriteBatch();
    private SpriteBatch endBatch = new SpriteBatch();
    private SpriteBatch pauseBatch = new SpriteBatch();
    private SpriteBatch znakBatch = new SpriteBatch();
    private static Texture field = new Texture("Game/Field.jpg");
    private static Texture desert = new Texture("Game/Desert.jpg");
    private static Texture snow = new Texture("Game/Snow.jpg");
    private static Texture city = new Texture("Game/City.jpg");
    private static Texture znakTexture = new Texture("Game/znak.png");
    private static Texture endTexture = new Texture("Game/end.jpg");
    private static Texture pauseTexture = new Texture("Game/pause.jpg");
    private Texture drawFonTexture1=field;
    private Texture drawFonTexture2=field;
    int positionFon1=0, positionFon2=2400;
    private boolean flag_fon=false;
    int speedFon=15, speedFonBreake=10;

    private SpriteBatch carBatch = new SpriteBatch();
    private static Car gameCar;
    boolean flag_right=false, flag_left=false;
    boolean flag_gas=false, flag_breake=false;
    boolean flag_changeControl=false;
    float accelerometerValue;
    boolean flag_znak=false;

    private GameMusic music;

    int score=0;
    boolean flag_score=false;
    boolean flag_end=false;
    boolean flag_pause=false;
    boolean flag_money=false;

    private static EnemyCar[] enemyCar=new EnemyCar[6];
    SpriteBatch[] enemyCarBatch=new SpriteBatch[6];

    int breakeScore=0;
    int gasScore=0;
    int accelerometerScore=0;
    boolean flag_taskComplete=false;
    float taskCompleteLabelTime = 0;
    public static boolean flag_textures=false;

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

        resultLabel = new Label("Счет: "+score, style);
        resultLabel.setPosition((int)(820*Gdx.graphics.getWidth() / 1080),(int)(2100*Gdx.graphics.getHeight() / 2400));
        stage.addActor(resultLabel);

        finalResultLabel= new Label("Счет: "+score, style);
        finalResultLabel.setPosition((int)(50*Gdx.graphics.getWidth() / 1080),(int)(1500*Gdx.graphics.getHeight() / 2400));
        stage_end.addActor(finalResultLabel);

        moneyLabel= new Label("Денег: ", style);
        moneyLabel.setPosition((int)(50*Gdx.graphics.getWidth() / 1080),(int)(1200*Gdx.graphics.getHeight() / 2400));
        stage_end.addActor(moneyLabel);

        taskCompleteLabel = new Label("", style);
        taskCompleteLabel.setPosition((int)(850*Gdx.graphics.getWidth() / 1080),(int)(1800*Gdx.graphics.getHeight() / 2400));
        stage.addActor(taskCompleteLabel);

        settings=Settings.InitializeSettings(this, stage_pause);

        init_buttons();
        if(!flag_textures){
            createEnemyCars();
            flag_textures=true;
        }
        if(!settings.isAccelerometerFlag()){
            initLeftRightButtons();
        }
        create();

        music = GameMusic.MusicInitialize();
        music.getMenuMusic().stop();
        music.getGameMusic().play();
    }
    public void create()
    {
        score=0;
        resultLabel.setText("Счет: "+score);
        positionFon1=0; positionFon2=2400;
        drawFonTexture1=field;
        drawFonTexture2=field;
        flag_fon=false;
        flag_right=false; flag_left=false;
        flag_gas=false; flag_breake=false;
        flag_changeControl = settings.isAccelerometerFlag();
        flag_score=false;
        flag_end=false;
        flag_pause=false;
        flag_money=false;
        gameCar.setPositionCar(gameCar.getBorderRight());

        tasks=racing.getTasks();
        breakeScore=0;
        gasScore=0;
        accelerometerScore=0;
    }

    private void createEnemyCars()
    {
        enemyCar[0]=new EnemyCar("Cars/Enemies/RingLeft.png",-12, false);
        enemyCar[1]=new EnemyCar("Cars/Enemies/NeVestaLeft.png",-10,false);
        enemyCar[2]=new EnemyCar("Cars/Enemies/FuraLeft.png",-8, false);
        enemyCarBatch[0]=new SpriteBatch();
        enemyCarBatch[1]=new SpriteBatch();
        enemyCarBatch[2]=new SpriteBatch();

        enemyCar[3]=new EnemyCar("Cars/Enemies/RingRight.png",12, false);
        enemyCar[4]=new EnemyCar("Cars/Enemies/NeVestaRight.png",10,false);
        enemyCar[5]=new EnemyCar("Cars/Enemies/FuraRight.png",8, false);
        enemyCarBatch[3]=new SpriteBatch();
        enemyCarBatch[4]=new SpriteBatch();
        enemyCarBatch[5]=new SpriteBatch();
    }

  /*  private void upgrades(){
        if(gameCar.getUpgrades().get(0).getType()== AbstractUpgrade.UpgradeType.SLOW_MOTION){

        }
    }*/

    private void init_buttons()
    {
        ImageButton gasButton = new ImageButton(new TextureRegionDrawable(new Texture("Game/Gas.png")));
        gasButton.setPosition((int)(950*Gdx.graphics.getWidth() / 1080), (int)(300*Gdx.graphics.getHeight() / 2400));
        gasButton.getImage().setFillParent(true);
        gasButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                flag_gas=true;
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                flag_gas=false;
                gasScore=0;
            }
        });
        stage.addActor(gasButton);

        ImageButton breakeButton = new ImageButton(new TextureRegionDrawable(new Texture("Game/Breake.png")));
        breakeButton.setPosition((int)(840*Gdx.graphics.getWidth() / 1080), (int)(355*Gdx.graphics.getHeight() / 2400));
        breakeButton.getImage().setFillParent(true);
        breakeButton.addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                flag_breake=true;
                return true;
            }
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                flag_breake=false;
                breakeScore=0;
            }
        });
        stage.addActor(breakeButton);


        ImageButton restartButton = new ImageButton(new TextureRegionDrawable(new Texture("Game/restartButtonUp.png")),new TextureRegionDrawable(new Texture("Game/restartButtonDown.png")));
        restartButton.setPosition((int)(230*Gdx.graphics.getWidth() / 1080), (int)(320*Gdx.graphics.getHeight() / 2400));
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
                GameMusic.MusicInitialize().getGameMusic().play();
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
                Settings.InitializeSettings(racing.getMainMenuScene(),MainMenu.menuStage);
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
                GameMusic.MusicInitialize().getGameMusic().pause();
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
                GameMusic.MusicInitialize().getGameMusic().play();
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
                Settings.InitializeSettings(racing.getMainMenuScene(),MainMenu.menuStage);
                super.clicked(event, x, y);
            }
        });
        stage_pause.addActor(menuButton);
    }

    private void initLeftRightButtons()
    {
        leftButton = new ImageButton(new TextureRegionDrawable(new Texture("Game/Left.png")));
        leftButton.setPosition((int)(30*Gdx.graphics.getWidth() / 1080), (int)(320*Gdx.graphics.getHeight() / 2400));
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

        rightButton = new ImageButton(new TextureRegionDrawable(new Texture("Game/Right.png")));
        rightButton.setPosition((int)(160*Gdx.graphics.getWidth() / 1080), (int)(320*Gdx.graphics.getHeight() / 2400));
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
                    positions[0] =567; positions[1]=700;
                }
                int randomPosition = positions[random.nextInt(positions.length)];
                enemyCar[numbersEnemyCar[i]].setPositionX(randomPosition);
                enemyCar[numbersEnemyCar[i]].setPositionY(2400);
                for(int j=0;j<enemyCar.length;j++){
                    if(j==numbersEnemyCar[i]) continue;
                    if(enemyCar[j].getGo() && enemyCar[j].getPositionX()==enemyCar[numbersEnemyCar[i]].getPositionX()){
                        if(enemyCar[j].getPositionY()+(int)(enemyCar[j].getCarTexture().getHeight()*Gdx.graphics.getHeight() / 2400)>enemyCar[numbersEnemyCar[i]].getPositionY()){
                            enemyCar[numbersEnemyCar[i]].setGo(false);
                        }
                        if(enemyCar[numbersEnemyCar[i]].getPositionY()+(int)(enemyCar[numbersEnemyCar[i]].getCarTexture().getHeight()*Gdx.graphics.getHeight() / 2400)>enemyCar[j].getPositionY()){
                            enemyCar[numbersEnemyCar[i]].setGo(false);
                        }
                    }
                }
                int[] numberSpeedLeft = {-8, -10, -12};
                int[] numberSpeedRight = {8, 10, 12};
                int randomSpeed = random.nextInt(3);
                if(numbersEnemyCar[i]<3){
                    enemyCar[numbersEnemyCar[i]].setSpeed(numberSpeedLeft[randomSpeed]);
                }else{
                    enemyCar[numbersEnemyCar[i]].setSpeed(numberSpeedRight[randomSpeed]);
                }
                break;
            }
        }
    }

    private void completeTaskCheck(int indexTask){
        for(DailyTask task: tasks){
            if(task.getIndex()==indexTask && !task.getCompleted()){
                taskCompleteLabel.setText("Задание\nвыполнено");
                flag_taskComplete=true;
                task.setCompleted(true);
                MainMenu.flag_changeTasks=true;
                Racing.money+=task.getCost();
                Racing.WriteMoneyInFile();
                MoneyTable.changeAndGetMoneyTable(Store.stage);
                DailyTask.WriteCurrentTasksInFile();
                break;
            }
        }
    }
    private void fonDraw()
    {
        fon.begin();
        fon.draw(drawFonTexture1, 0, (int) (positionFon1 * Gdx.graphics.getHeight() / 2400),
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        fon.end();

        fon2.begin();
        fon2.draw(drawFonTexture2, 0, (int) (positionFon2 * Gdx.graphics.getHeight() / 2400),
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        fon2.end();

        if(flag_znak){
            znakBatch.begin();
            znakBatch.draw(znakTexture, (int)(840*Gdx.graphics.getWidth() / 1080),
                    (int)(positionFon1*Gdx.graphics.getHeight() / 2400),
                    (int)(znakTexture.getWidth()*Gdx.graphics.getWidth() / 2400),
                    (int)(znakTexture.getHeight()*Gdx.graphics.getHeight() / 1080));
            znakBatch.end();
            if(positionFon1<-500) flag_znak=false;
        }

        positionFon1 -= speedFon;
        positionFon2 -= speedFon;
        if (positionFon1 <= -2400){
            positionFon1 = positionFon2+2400;
            flag_score=true;
            if(score%50==0 && score!=0) flag_znak=true;
            if(flag_fon){
                if(drawFonTexture1==field){
                    drawFonTexture1=desert;
                }else if(drawFonTexture1==desert){
                    style.fontColor = Color.BLACK;
                    drawFonTexture1=snow;
                }else if(drawFonTexture1==snow){
                    style.fontColor = Color.WHITE;
                    drawFonTexture1=city;
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
                }else if(drawFonTexture2==desert){
                    style.fontColor = Color.BLACK;
                    drawFonTexture2=snow;
                }else if(drawFonTexture2==snow){
                    style.fontColor = Color.WHITE;
                    drawFonTexture2=city;
                }else{
                    drawFonTexture2=field;
                }
            }
        }

        if(flag_score==true)
        {
            flag_score=false;
            score+=1;
            resultLabel.setText("Счет: "+score);

            if(score==200){
                completeTaskCheck(0);
            }
            if(score==51){
                completeTaskCheck(1);
            }
            if(flag_breake==true){
                breakeScore+=1;
            }
            if(breakeScore==5){
                completeTaskCheck(3);
            }
            if(flag_gas==true){
                gasScore+=1;
            }
            if(gasScore==50){
                completeTaskCheck(4);
            }
            if(settings.isAccelerometerFlag()){
                accelerometerScore+=1;
            }
            if(accelerometerScore==50){
                completeTaskCheck(6);
            }
            if(score==151){
                completeTaskCheck(7);
            }
            if(score==101){
                completeTaskCheck(8);
            }

            if(score%3==0 && score<=8){
                enemySpawn();
            }else if(score%2==0 && score>8 && score<=20){
                enemySpawn();
            }else if(score>=20 && score<=50){
                enemySpawn();
            }

            if(score%50==0) flag_fon=true;
        }

        if(score>50 && ((positionFon1<-1200 && positionFon1>-1300) || (positionFon2<-1200 && positionFon2>-1300))){
            enemySpawn();
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
                    enemyCar[i].setPositionY(enemyCar[i].getPositionY() + enemyCar[i].getSpeed() - (gameCar.getSpeed()+speedFon));
                }else if(flag_breake){
                    enemyCar[i].setPositionY(enemyCar[i].getPositionY() + enemyCar[i].getSpeed() - (speedFon-speedFonBreake));
                }else{
                    enemyCar[i].setPositionY(enemyCar[i].getPositionY() + enemyCar[i].getSpeed() - speedFon);
                }

                if(enemyCar[i].getPositionY()<-enemyCar[i].getCarTexture().getHeight()){
                    enemyCar[i].setGo(false);
                    if(i==0) enemyCar[i].setSpeed(-12);
                    if(i==1) enemyCar[i].setSpeed(-10);
                    if(i==2) enemyCar[i].setSpeed(-8);
                    if(i==3) enemyCar[i].setSpeed(12);
                    if(i==4) enemyCar[i].setSpeed(10);
                    if(i==5) enemyCar[i].setSpeed(8);
                }
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
                    (int)(gameCar.getPositionCarY()*Gdx.graphics.getHeight() / 2400),
                    (int)(gameCar.getCarTexture().getWidth()*Gdx.graphics.getWidth() / 1800),
                    (int)(gameCar.getCarTexture().getHeight()*Gdx.graphics.getHeight() / 2400));
            carBatch.end();
            enemyDraw();
            stage.draw();

            for(int i=0;i<enemyCar.length;i++){
                if(isCollisionGameCarWithEnemy(enemyCar[i]) && enemyCar[i].getGo())
                {
                    if(i==2 || i==5) completeTaskCheck(2);
                    flag_end=true;
                    style.fontColor = Color.WHITE;
                    finalResultLabel.setText("Счет: "+score);
                    int resultScore = score+Racing.money;
                    moneyLabel.setText("Денег: "+String.valueOf(resultScore));
                    Gdx.input.setInputProcessor(stage_end);
                    font.getData().setScale(2.2f*Gdx.graphics.getWidth() / 1080, 4.0f*Gdx.graphics.getHeight() / 1080);
                }
            }

            for(int i=0;i<enemyCar.length;i++){
                for(int j=0;j<enemyCar.length;j++){
                    if(isCollisionEnemy(enemyCar[i], enemyCar[j]) && enemyCar[i].getGo() && enemyCar[j].getGo() && i!=j){
                        if(enemyCar[i].getSpeed()>enemyCar[i].getSpeed()){
                            enemyCar[i].setSpeed(enemyCar[j].getSpeed());
                        }else{
                            enemyCar[j].setSpeed(enemyCar[i].getSpeed());
                        }
                    }
                }
            }

            if(flag_changeControl!=settings.isAccelerometerFlag()){
                flag_changeControl = settings.isAccelerometerFlag();
                if(flag_changeControl){
                    stage.getActors().removeValue(leftButton, true);
                    stage.getActors().removeValue(rightButton, true);
                }else{
                    initLeftRightButtons();
                }
            }

            if(flag_taskComplete){
                taskCompleteLabelTime += Gdx.graphics.getDeltaTime();
                if(taskCompleteLabelTime>=2)
                {
                    flag_taskComplete=false;
                    taskCompleteLabelTime=0;
                    taskCompleteLabel.setText("");
                }
            }
        }else if(flag_end){
            endBatch.begin();
            endBatch.draw(endTexture, 0, 0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
            endBatch.end();
            if(score<4){
                completeTaskCheck(5);
            }
            if(!flag_money){
                for(int i = 0; i < stage_end.getActors().size; i++){
                    if(stage_end.getActors().get(i).getName()!=null&&stage_end.getActors().get(i).getName().equals("maxScoreLabel")){
                        stage_end.getActors().removeIndex(i); break;
                    }
                }
                GameMusic.MusicInitialize().getGameMusic().stop();
                flag_money=true;
                Racing.money+=score;
                Racing.WriteMoneyInFile();
                MoneyTable.changeAndGetMoneyTable(Store.stage);

                int maxScore = CheckMaxScore();
                Label label = null;
                if (score <= maxScore) {
                    label = new Label("Рекорд:\n     " + maxScore, style);
                } else {
                    label = new Label("Новый рекорд:\n          " + score, style);
                    WriteMaxScore(score);
                }
                label.setName("maxScoreLabel");
                label.setPosition(Gdx.graphics.getWidth()-label.getWidth()-50,moneyLabel.getY()-40);
                stage_end.addActor(label);
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
        if(!settings.isAccelerometerFlag()){
            if(flag_right && gameCar.getPositionCar()<gameCar.getBorderRight())
            {
                gameCar.setPositionCar(gameCar.getPositionCar()+10);
            }
            if(flag_left && gameCar.getPositionCar() > gameCar.getBorderLeft())
            {
                gameCar.setPositionCar(gameCar.getPositionCar()-10);
            }
        }else{
            accelerometerValue = Gdx.input.getAccelerometerY();
            if (accelerometerValue > 5 && gameCar.getPositionCar() < gameCar.getBorderRight()) {
                gameCar.setPositionCar(gameCar.getPositionCar()+10);
            } else if(accelerometerValue > 2 && accelerometerValue < 5 && gameCar.getPositionCar() < gameCar.getBorderRight()) {
                gameCar.setPositionCar(gameCar.getPositionCar()+5);
            }

            if (accelerometerValue < -5 && gameCar.getPositionCar()>gameCar.getBorderLeft()) {
                gameCar.setPositionCar(gameCar.getPositionCar()-10);
            } else if(accelerometerValue < -2 && accelerometerValue > -5 && gameCar.getPositionCar()>gameCar.getBorderLeft()) {
                gameCar.setPositionCar(gameCar.getPositionCar()-5);
            }
        }
        if(flag_gas)
        {
            positionFon1-=gameCar.getSpeed();
            positionFon2-=gameCar.getSpeed();
        }
        if(flag_breake)
        {
            positionFon1+=speedFonBreake;
            positionFon2+=speedFonBreake;
        }
    }

    public static boolean isCollisionGameCarWithEnemy(EnemyCar enemy) {
        Rectangle rect1= new Rectangle((int)(gameCar.getPositionCar()*Gdx.graphics.getWidth() / 1080),
                (int)(gameCar.getPositionCarY()*Gdx.graphics.getHeight() / 2400),
                (int)(gameCar.getCarTexture().getWidth()*Gdx.graphics.getWidth() / 1800),
                (int)(gameCar.getCarTexture().getHeight()*Gdx.graphics.getHeight() / 2400));
        Rectangle rect2=new Rectangle((int)(enemy.getPositionX()*Gdx.graphics.getWidth() / 1080),
                (int)(enemy.getPositionY()*Gdx.graphics.getHeight() / 2400),
                (int)(enemy.getCarTexture().getWidth()*Gdx.graphics.getWidth() / 1800),
                (int)(enemy.getCarTexture().getHeight()*Gdx.graphics.getHeight() / 2400));
        return rect1.overlaps(rect2);
    }
    public static boolean isCollisionEnemy(EnemyCar enemy, EnemyCar enemy2) {
        Rectangle rect1=new Rectangle((int)(enemy.getPositionX()*Gdx.graphics.getWidth() / 1080),
                (int)(enemy.getPositionY()*Gdx.graphics.getHeight() / 2400),
                (int)(enemy.getCarTexture().getWidth()*Gdx.graphics.getWidth() / 1800),
                (int)(enemy.getCarTexture().getHeight()*Gdx.graphics.getHeight() / 2400));
        Rectangle rect2=new Rectangle((int)(enemy2.getPositionX()*Gdx.graphics.getWidth() / 1080),
                (int)(enemy2.getPositionY()*Gdx.graphics.getHeight() / 2400),
                (int)(enemy2.getCarTexture().getWidth()*Gdx.graphics.getWidth() / 1800),
                (int)(enemy2.getCarTexture().getHeight()*Gdx.graphics.getHeight() / 2400));
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

    private int CheckMaxScore() {
        FileHandle handle = Gdx.files.local("max_store.txt");
        try {
            String line = handle.readString();
            return Integer.parseInt(line);
        } catch (GdxRuntimeException ex) {
            return 0;
        }
    }

    private void WriteMaxScore(int score) {
        FileHandle handle = Gdx.files.local("max_store.txt");
        String line = String.valueOf(score);
        handle.writeString(line, false);

    }
}