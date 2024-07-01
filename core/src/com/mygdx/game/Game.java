package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Game implements Scene{
    private Stage stage;
    private BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"));
    private Label.LabelStyle style = new Label.LabelStyle();
    Label resultLabel;
    private SpriteBatch fon = new SpriteBatch();
    private SpriteBatch fon2 = new SpriteBatch();
    private Texture field = new Texture("Game/Field.jpg");
    int positionFon1=0, positionFon2=2400;
    private SpriteBatch carBatch = new SpriteBatch();
    private Texture car = new Texture("Game/Yagoda.png");
    int positionCar=700;
    boolean flag_right=false, flag_left=false;
    boolean flag_gas=false, flag_breake=false;
    int score=0;
    public Game()
    {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        init_buttons();
        create();
    }
    public void create()
    {
        font.getData().setScale(Gdx.graphics.getWidth() / 1080, 2.0f*Gdx.graphics.getHeight() / 1080);
        style.font = font;
        style.fontColor = Color.WHITE;
        resultLabel = new Label(""+score, style);
        resultLabel.setPosition((int)(850*Gdx.graphics.getWidth() / 1080),(int)(2100*Gdx.graphics.getHeight() / 2400));
        stage.addActor(resultLabel);

        score=0;
        positionFon1=0; positionFon2=2400;
        positionCar=700;
        flag_right=false; flag_left=false;
        flag_gas=false; flag_breake=false;
    }

    private void init_buttons() {
        int buttonHeight = Gdx.graphics.getHeight() / 8;
        int buttonWidth = Gdx.graphics.getWidth() / 8;

        ImageButton leftButton = new ImageButton(new TextureRegionDrawable(new Texture("Game/Left.png")));
        leftButton.setPosition((int)(10*Gdx.graphics.getWidth() / 1080), (int)(300*Gdx.graphics.getHeight() / 2400));
        leftButton.setSize(buttonWidth, buttonHeight);
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
        rightButton.setPosition((int)(110*Gdx.graphics.getWidth() / 1080), (int)(300*Gdx.graphics.getHeight() / 2400));
        rightButton.setSize(buttonWidth, buttonHeight);
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
        gasButton.setPosition((int)(950*Gdx.graphics.getWidth() / 1080), (int)(300*Gdx.graphics.getHeight() / 2400));
        gasButton.setSize(buttonWidth, buttonHeight);
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
        breakeButton.setPosition((int)(850*Gdx.graphics.getWidth() / 1080), (int)(300*Gdx.graphics.getHeight() / 2400));
        breakeButton.setSize(buttonWidth, buttonHeight);
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

    private void fonDraw() {
        fon.begin();
        fon.draw(field, 0, (int) (positionFon1 * Gdx.graphics.getHeight() / 2400),
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        fon.end();

        fon2.begin();
        fon2.draw(field, 0, (int) (positionFon2 * Gdx.graphics.getHeight() / 2400),
                Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        fon2.end();

        positionFon1 -= 15;
        positionFon2 -= 15;
        if (positionFon1 < -2405)
            positionFon1 = 2400;
        if (positionFon2 < -2405)
            positionFon2 = 2400;

        if(positionFon1==2400 || positionFon2==2400)
        {
            score+=1;
            resultLabel.setText(""+score);
        }
    }

    public void render()
    {
        fonDraw();
        motion();
        carBatch.begin();
        carBatch.draw(car, (int)(positionCar*Gdx.graphics.getWidth() / 1080),(int)(100*Gdx.graphics.getHeight() / 2400),(int)(100*Gdx.graphics.getWidth() / 1800), (int)(510*Gdx.graphics.getHeight() / 2400));
        carBatch.end();
        stage.draw();
    }

    private void motion()
    {
        if(flag_right && positionCar<700)
        {
            positionCar+=10;
        }
        if(flag_left && positionCar > 320)
        {
            positionCar -= 10;
        }
        if(flag_gas)
        {
            positionFon1-=10;
            positionFon2-=10;
        }
        if(flag_breake)
        {
            positionFon1+=5;
            positionFon2+=5;
        }
    }
    public void dispose()
    {
        fon.dispose();
        fon2.dispose();
        field.dispose();
        stage.dispose();
    }
}
