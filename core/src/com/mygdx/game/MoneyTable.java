package com.mygdx.game;

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
    private static Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);
    private static Image image = new Image(new Texture(Gdx.files.internal("Store/money.png")));
    private static Label label;

    public static void changeAndGetMoneyTable(Stage stage) {
        for (Actor actor : stage.getActors()) {
            if (actor.getName() != null && actor.getName().equals("moneyTable")) {
                int index = stage.getActors().indexOf(actor, false);
                stage.getActors().removeIndex(index);
                break;
            }
        }
        Table table = new Table();
        table.setName("moneyTable");

        float width = Gdx.graphics.getWidth() / 12f;
        table.add(image).padRight(20).width(width / 1.5f).height(width / 1.5f);
        label = null;
        label = new Label(String.valueOf(Racing.money), style);
        table.add(label);
        table.setSize(width, width / 2);
        table.setPosition(Gdx.graphics.getWidth() - table.getWidth() - 100, Gdx.graphics.getHeight() - table.getHeight() - 20);
        stage.addActor(table);
    }
}
