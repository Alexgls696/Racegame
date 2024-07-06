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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.Cars.Car;
import com.mygdx.game.Cars.EnemyCar;
import com.mygdx.game.Music.GameMusic;

import java.util.Random;

public class Game implements Scene{
    private Stage stage;
    private BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"));
    private Label.LabelStyle style = new Label.LabelStyle();
    Label resultLabel;

    private SpriteBatch fon = new SpriteBatch();
    private SpriteBatch fon2 = new SpriteBatch();
    private static Texture field = new Texture("Game/Field.jpg");
    private static Texture desert = new Texture("Game/Desert.jpg");
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

    EnemyCar[] enemyCar=new EnemyCar[6];
    SpriteBatch[] enemyCarBatch=new SpriteBatch[6];

    public Game(Car gameCar)
    {
        this.gameCar=gameCar;
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        init_buttons();
        create();
        createEnemyCars();

        music = GameMusic.MusicInitialize();
        music.getMenuMusic().stop();
        music.getGameMusic().play();
    }
    public void create()
    {
        font.getData().setScale(1.2f*Gdx.graphics.getWidth() / 1080, 2.0f*Gdx.graphics.getHeight() / 1080);
        style.font = font;
        style.fontColor = Color.WHITE;
        resultLabel = new Label(""+score, style);
        resultLabel.setPosition((int)(850*Gdx.graphics.getWidth() / 1080),(int)(2100*Gdx.graphics.getHeight() / 2400));
        stage.addActor(resultLabel);

        score=0;
        positionFon1=0; positionFon2=2400;
        flag_right=false; flag_left=false;
        flag_gas=false; flag_breake=false;
        flag_score=false;
        gameCar.setBorderLeft(310);
        gameCar.setBorderRight(690);
        gameCar.setPositionCar(gameCar.getBorderRight());
    }

    private void createEnemyCars()
    {
        enemyCar[0]=new EnemyCar("Cars/Enemies/RingLeft.png",-10, false);
        enemyCar[1]=new EnemyCar("Cars/Enemies/NeVestaLeft.png",-10,false);
        enemyCar[2]=new EnemyCar("Cars/Enemies/FuraLeft.png",-10, false);
        enemyCarBatch[0]=new SpriteBatch();
        enemyCarBatch[1]=new SpriteBatch();
        enemyCarBatch[2]=new SpriteBatch();

        enemyCar[3]=new EnemyCar("Cars/Enemies/RingRight.png",10, false);
        enemyCar[4]=new EnemyCar("Cars/Enemies/NeVestaRight.png",10,false);
        enemyCar[5]=new EnemyCar("Cars/Enemies/FuraRight.png",10, false);
        enemyCarBatch[3]=new SpriteBatch();
        enemyCarBatch[4]=new SpriteBatch();
        enemyCarBatch[5]=new SpriteBatch();
    }

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

        ImageButton gasButton = new ImageButton(new TextureRegionDrawable(new Texture("Game/Gas.png")));
        gasButton.setPosition((int)(970*Gdx.graphics.getWidth() / 1080), (int)(240*Gdx.graphics.getHeight() / 2400));
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

        stage.addActor(leftButton);
        stage.addActor(rightButton);
        stage.addActor(gasButton);
        stage.addActor(breakeButton);
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

            if(score%5==0 && score<=10){
                enemySpawn();
            }else if(score%4==0 && score>10 && score<500){
                enemySpawn();
            }else if(score%3==0 && score>500 && score<1000){
                enemySpawn();
            }else if(score%2==0 && score>1000){
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
                //score=0;
                resultLabel.setText(""+score);
            }
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
        stage.dispose();
    }
}
