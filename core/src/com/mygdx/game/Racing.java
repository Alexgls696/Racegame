package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Cars.Car;

import java.util.ArrayList;



public class Racing extends ApplicationAdapter {

	public static int money = 100;
	SpriteBatch batch;
	Texture img;

	private Scene scene;
	public static ArrayList<Car>cars;

	@Override
	public void create () {
		cars = carsInitialize();
		scene = new Store();
	}

	@Override
	public void render () {
		scene.render();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}

	public int ReadMoneyCount(){
		FileHandle handle = null;
		String line = "";
		try{
			line = handle.readString();
		}catch (com.badlogic.gdx.utils.GdxRuntimeException ex){
			handle.writeString(String.valueOf(money),false);
			return 100;
		}
		return Integer.parseInt(line);
	}

	public ArrayList<Car>carsInitialize(){
		ArrayList<Car>cars = new ArrayList<>();

		FileHandle handle = Gdx.files.internal("Cars/car_list.txt");
		String line = handle.readString();
		String[]car_lines = line.split("\r\n");
		for(String it: car_lines){
			String[]split = it.split(" ");
			cars.add(new Car(split[0],split[1].replace('_',' '),Integer.parseInt(split[2])));
		}
		return cars;
	}
}
