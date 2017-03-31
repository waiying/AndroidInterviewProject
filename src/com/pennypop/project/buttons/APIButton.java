package com.pennypop.project.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.pennypop.project.MainScreen;

import org.json.*;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

public class APIButton extends PennyPopButtons{
	private Table col2;
	private static final String URL = "http://api.openweathermap.org/data/2.5/weather?q=San%20Francisco,US&appid=2e32d2b4b825464ec8c677a49531e9ae";
	
	// JSON info to get from API
	private String city;
	private String description;
	private String temp;
	private String windSpeed;
	
	public APIButton(FileHandle image, Table col2){
		super(image);
		this.col2 = col2;
		addClickEvent(imgButton);
	}

	@Override
	public void addClickEvent(ImageButton button) {
		InputListener listener = new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				// display SF weather info when clicked
				try{
					JSONObject weatherInfo = readJSON();
					addWeatherToTable(weatherInfo);
				} catch(IOException e) {
					System.out.println(e);
				} catch(JSONException e) {
					System.out.println(e);
				}
				
				return true;
			}
		};
		
		button.addListener(listener);
	}
	
	private JSONObject readJSON() throws IOException, JSONException {
		InputStream is = new URL(URL).openStream();
		
		// parse in Open weather API's JSON
		try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
				String jsonText = readAll(reader);
				JSONObject json = new JSONObject(jsonText);
				return json;
	    } finally {
	    	is.close();
	    }
	}
	
	private String readAll(Reader rd) throws IOException {
		// use String Builder instead of String for speed just in case if the JSON is super long
		// Note: string concatenation creates a new StringBuilder every loop, making String the less optimal choice
		StringBuilder sb = new StringBuilder();
		int character;
		
		// read in one character at a time
		while ((character = rd.read()) != -1) {
		  sb.append((char) character);
		}
		
		return sb.toString();
	}

	private void addWeatherToTable(JSONObject info){		
		// get all the string info from the JSONObject
		city = info.getString("name");
		
		description = info.getJSONArray("weather").getJSONObject(0).getString("description");
		description = description.substring(0,1).toUpperCase() + description.substring(1);
		
		Double kelvin = info.getJSONObject("main").getDouble("temp");
		Double fahrenheit = (9.0/5.0)*(kelvin - 273) + 32;
		temp = String.format("%.0f", fahrenheit);
		windSpeed = Double.toString(info.getJSONObject("wind").getDouble("speed"));
		String tempWind = temp + " degrees F, " + windSpeed + " mph wind";
		
		// create labels for all strings
		Label title = new Label("Current Weather", new Label.LabelStyle(MainScreen.font, Color.valueOf("7D1E0B")));
		Label cityLabel = new Label(city, new Label.LabelStyle(MainScreen.font, Color.valueOf("1160BA")));
		Label descrptnLabel = new Label(description, new Label.LabelStyle(MainScreen.font, Color.RED));
		
		// make new smaller bitmap font for tempWind 
		BitmapFont fontSmall = new BitmapFont(Gdx.files.internal("font.fnt"), Gdx.files.internal("font.png"), false);
		fontSmall.setScale(0.6f);
		Label tempWindLabel = new Label(tempWind, new Label.LabelStyle(fontSmall, Color.RED));
		
		// add all the labels to col2
		col2.clear(); // reset to display updated weather info every time API button is clicked
		col2.padLeft(100);
		col2.add(title).height(title.getHeight() - 10);
		col2.row();
		col2.add(cityLabel).height(cityLabel.getHeight() - 10).padBottom(35);
		col2.row();
		col2.add(descrptnLabel);
		col2.row();
		col2.add(tempWindLabel);	
	}
}