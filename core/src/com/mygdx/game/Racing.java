package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.mygdx.game.Cars.Car;
import com.mygdx.game.Cars.upgrades.AbstractUpgrade;
import com.mygdx.game.Cars.upgrades.SlowMotionUpgrade;
import com.mygdx.game.Cars.upgrades.TwoLivesUpgrade;
import com.mygdx.game.Music.GameMusic;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class Racing extends ApplicationAdapter {

    public static int money = 0;

    private Scene storeScene;
    private Scene currentScene;
    private MainMenu mainMenuScene;
    private Settings settingScene;
    private Scene game;

    public static ArrayList<Car> cars;
    private GameMusic music;

    GameMainActivity activity;

    public Racing(GameMainActivity activity) {
        this.activity = activity;
    }

    private ArrayList<DailyTask> tasks;

    @Override
    public void create() {
        cars = carsInitialize();
        carUpgradesInitializeFromFile();

        money = ReadMoneyCount();
        storeScene = new Store(this);
        mainMenuScene = new MainMenu(this);

        settingScene = Settings.InitializeSettings(mainMenuScene, MainMenu.menuStage);
        settingScene.setRacing(this);
        currentScene = mainMenuScene;

        GameMusic.MusicInitialize().getMenuMusic().play();
        GameMusic.MusicInitialize().getMenuMusic().setLooping(true);

        tasks = DailyTask.getCurrentDailyTasks();
        setDailyTaskTimer();
        mainMenuScene.DailyTaskLabelDraw();
    }

    @Override
    public void render() {
        currentScene.render();
    }

    @Override
    public void dispose() {
        storeScene.dispose();
        mainMenuScene.dispose();
    }

    public ArrayList<DailyTask> getTasks() {
        return tasks;
    }

    private boolean isChangeDailyTasks = false;

    private boolean drawDiffLabel = false;

    private String difference = "";

    public String getDiffLine() {
        return difference;
    }

    private void setDailyTaskTimer() {
        new Thread(() -> {
            try {
                while (true) {
                    LocalDateTime now = LocalDateTime.now();
                    LocalDateTime nextTime = DailyTask.lastTime;
                    Duration duration = Duration.between(now, nextTime);

                    long hour = duration.toHours() % 24;
                    long minute = duration.toMinutes() % 60;
                    long second = duration.getSeconds() - 3600*hour- minute*60;

                    if(second==-1){
                        second=59;
                    }
                    difference = hour + ":" + minute + ":" + second;
                    if (now.isAfter(nextTime)) {
                        isChangeDailyTasks = true;
                    }
                    drawDiffLabel = true;
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }).start();
    }

    public boolean isChangeDailyTasks() //Меняет список задач
    {
        if (isChangeDailyTasks) {
            isChangeDailyTasks = false;
            tasks = DailyTask.getCurrentDailyTasks();
            DailyTask.WriteCurrentTasksInFile();
            return true;
        }
        return false;
    }


    public boolean isDrawDiffLabel(){
        return drawDiffLabel;
    }

    public void setDrawDiffLabel(boolean flag){
        drawDiffLabel = flag;
    }

    public static void WriteCarsUpgradesInFile() {
        new Thread(() -> {
            FileHandle handle = Gdx.files.local("Cars/upgrades_list.txt");
            StringBuilder builder = new StringBuilder();
            for (Car car : cars) {
                builder.append(car.getName().replace(" ", "_") + " ");
                for (AbstractUpgrade upgrade : car.getUpgrades()) {
                    builder.append(upgrade.getType() + "=" + upgrade.isPurchased() + " ");
                }
                builder.append("\r\n");
            }
            handle.writeString(builder.toString(), false);
        }).start();
    }

    public static void WriteCarsInformationInFile() {
        new Thread(() -> {
            FileHandle handle = Gdx.files.local("Cars/car_list.txt");
            String line;
            StringBuffer buffer = new StringBuffer();
            for (Car car : cars) {
                line = car.getCarPath() + " " + car.getName().replace(" ", "_") + " " + car.getCost() + " " + car.getSpeed() + " " + car.isPurchased() + " " + car.getBorderLeft() + " " + car.getBorderRight() + "\r\n";
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
            cars.add(new Car(split[0], split[1].replaceFirst("_", "\n").replaceFirst("_", " "), Integer.parseInt(split[2]), Integer.parseInt(split[3]), Boolean.parseBoolean(split[4]), Integer.parseInt(split[5]), Integer.parseInt(split[6])));
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

    public void setCurrentScene(Scene currentScene) {
        this.currentScene = currentScene;
    }

    public Scene getStoreScene() {
        return storeScene;
    }

    public Scene getMainMenuScene() {
        return mainMenuScene;
    }

    public void setGameScene(Scene game) {
        this.game = game;
    }

    public Scene getGameScene() {
        return game;
    }

    public Scene getSettingScene() {
        return settingScene;
    }
}
