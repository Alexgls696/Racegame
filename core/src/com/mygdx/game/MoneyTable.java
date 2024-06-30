package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

public class MoneyTable {

    private static BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"));
    private static Label.LabelStyle style = new Label.LabelStyle(font, Color.BLACK);
    private static   Image image = new Image(new Texture(Gdx.files.internal("Store/money.png")));
    private static Label label;

    public static Table changeAndGetMoneyTable(Stage stage){
        for(Actor actor: stage.getActors()){
            if(actor.getName()!=null&&actor.getName().equals("moneyTable")){
                int index = stage.getActors().indexOf(actor,false);
                stage.getActors().removeIndex(index);
                break;
            }
        }
        Table table = new Table();
        table.setName("moneyTable");

        table.add(image).padRight(20).width(80).height(80);
        label=null;
        label = new Label(String.valueOf(Racing.money),style);
        table.add(label);
        table.setSize(160,80);
        return table;
    }
}
