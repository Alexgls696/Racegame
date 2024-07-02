package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.Cars.Car;
import com.mygdx.game.Cars.upgrades.SlowMotionUpgrade;
import com.mygdx.game.Cars.upgrades.TwoLivesUpgrade;

import java.util.ArrayList;


public class Racing extends ApplicationAdapter {

    public static int money = 500;
    SpriteBatch batch;
    Texture img;

    private Scene scene;
    public static ArrayList<Car> cars;

    @Override
    public void create() {
        cars = carsInitialize();
        carUpgradesInitializeFromFile();
        money = ReadMoneyCount();
        scene = new Store();
    }

    @Override
    public void render() {
        scene.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }

    public static void WriteCarsInformationInFile() {
        new Thread(() -> {
            FileHandle handle = Gdx.files.local("Cars/car_list.txt");
            String line;
            StringBuffer buffer = new StringBuffer();
            for (Car car : cars) {
                line = car.getStorePath() + " " + car.getName().replace(" ", "_") + " " + car.getCost() + " " + car.getSpeed() + " " + car.isPurchased() + "\r\n";
                buffer.append(line);
            }
            handle.writeString(buffer.toString(), false);
        }).start();
    }

    public static void WriteMoneyInFile() {
        FileHandle handle = Gdx.files.local("money.txt");
        handle.writeString(String.valueOf(money), false);
    }

    public static int ReadMoneyCount() {
        FileHandle handle = null;
        String line = "";
        try {
            handle = Gdx.files.local("money.txt");
            line = handle.readString();
            return Integer.parseInt(line);
        } catch (com.badlogic.gdx.utils.GdxRuntimeException ex) {
            handle.writeString(String.valueOf(money), false);
            return money;
        }
    }

    public ArrayList<Car> carsInitialize() {
        ArrayList<Car> cars = new ArrayList<>();
        FileHandle handle;
        String line = "";
        try {
            handle = Gdx.files.local("Cars/car_list.txt");
            line = handle.readString();
        } catch (com.badlogic.gdx.utils.GdxRuntimeException ex) {
            handle = Gdx.files.internal("Cars/car_list.txt");
            line = handle.readString();
        }
        String[] car_lines = line.split("\r\n");
        for (String it : car_lines) {
            String[] split = it.split(" ");
            cars.add(new Car(split[0], split[1].replaceFirst("_", "\n").replaceFirst("_", " "), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Boolean.parseBoolean(split[4])));
        }
        return cars;
    }

    public void carUpgradesInitializeFromFile() {
        FileHandle handle;
        String content = "";
        try {
            handle = Gdx.files.local("Cars/upgrades_list.txt");
            content = handle.readString();
        } catch (com.badlogic.gdx.utils.GdxRuntimeException ex) {
            handle = Gdx.files.internal("Cars/upgrades_list.txt");
            content = handle.readString();
        }
        String[] lines = content.split("\r\n");
        for (int i = 0; i < cars.size(); i++) {
            String[] line = lines[i].split(" ");
            for (int j = 1; j < line.length; j++) {
                String[] subline = line[j].split("=");
                String value = subline[0];
                boolean flag = Boolean.parseBoolean(subline[1]);
                if (value.equals("TWO_LIVES")) {
                    cars.get(i).getUpgrades().add(new TwoLivesUpgrade(flag));
                }
                if (value.equals("SLOW_MOTION")) {
                    cars.get(i).getUpgrades().add(new SlowMotionUpgrade(flag));
                }
            }
        }
        System.out.println();
    }
}
