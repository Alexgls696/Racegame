package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;

public class DailyTask {
    private String name;
    private int cost;
    private int index;
    private boolean completed;

    public DailyTask(String name, int cost, int index, boolean completed) {
        this.name = name;
        this.cost = cost;
        this.index = index;
        this.completed = completed;
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public int getIndex() {
        return index;
    }

    public boolean getCompleted() {
        return completed;
    }

    private static ArrayList<DailyTask> tasks;


    public static LocalDateTime lastTime;


    private static boolean isCurrentDateAfterLastDatePlusOneDay() {
        FileHandle handle = Gdx.files.local("timer.txt");
        String line = handle.readString();

        String[] date = line.split(" ");
        LocalDateTime lastDate = LocalDateTime.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]),
                Integer.parseInt(date[2]), Integer.parseInt(date[3]),
                Integer.parseInt(date[4]), Integer.parseInt(date[5]));
        LocalDateTime next = lastDate.plusDays(1);
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(next)) {
            return true;
        } else {
            lastTime = lastDate;
            return false;
        }
    }

    public static ArrayList<DailyTask> getCurrentDailyTasks() {
        tasks = new ArrayList<>();
        FileHandle handle = Gdx.files.local("daily_tasks.txt");
        String line = null;
        try {
            line = handle.readString();
        } catch (GdxRuntimeException ex) {
            ArrayList<DailyTask> tasks = ReadDailyTasksFromFile();
            WriteCurrentDayInFile();
            WriteCurrentTasksInFile();
            return tasks;
        }

        if(!isCurrentDateAfterLastDatePlusOneDay()){
            String[] lines = line.split("\r\n");
            for (String it : lines) {
                String[] current_line = it.split(" ");
                tasks.add(new DailyTask(current_line[0], Integer.parseInt(current_line[1]), Integer.parseInt(current_line[2]), Boolean.parseBoolean(current_line[3])));
            }
        }else{
            tasks = ReadDailyTasksFromFile();
        }
        WriteCurrentDayInFile();
        return tasks;
    }

    private static ArrayList<DailyTask> ReadDailyTasksFromFile() {
        ArrayList<DailyTask> dailyTasks = new ArrayList<>();
        FileHandle handle = Gdx.files.internal("daily_tasks.txt");
        String line = handle.readString();
        String[] lines = line.split("\r\n");

        ArrayList<Integer> randomIndexes = new ArrayList<>();
        while (randomIndexes.size() < 3) {
            int number = new Random().nextInt(9);
            if (!randomIndexes.contains(number)) {
                randomIndexes.add(number);
            }
        }
        for (Integer it : randomIndexes) {
            String[] current_line = lines[it].split(" ");
            dailyTasks.add(new DailyTask(current_line[0], Integer.parseInt(current_line[1]), Integer.parseInt(current_line[2]), Boolean.parseBoolean(current_line[3])));
        }
        tasks = dailyTasks;
        return tasks;
    }

    public static void WriteCurrentDayInFile() {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonth().getValue();
        int day = now.getDayOfMonth();

        StringBuffer buffer = new StringBuffer();
        List<Integer> list = Arrays.asList(year, month, day, 10, 0, 0);
        for (Integer it : list) {
            buffer.append(it + " ");
        }
        FileHandle handle = Gdx.files.local("timer.txt");
        handle.writeString(buffer.toString(), false);

        lastTime = LocalDateTime.of(year,month,day,10,0,0);
    }

    public static void WriteCurrentTasksInFile() {
        FileHandle handle = Gdx.files.local("daily_tasks.txt");
        StringBuffer buffer = new StringBuffer();
        for (DailyTask task : tasks) {
            buffer.append(task.getName() + " " + task.getCost() + " " + task.getIndex() + " " + task.getCompleted() + "\r\n");
        }
        handle.writeString(buffer.toString(), false);
    }
}
