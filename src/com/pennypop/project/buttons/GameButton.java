package com.pennypop.project.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.pennypop.project.ProjectApplication;
import com.pennypop.project.SettingsScreen;

public class GameButton extends PennyPopButtons{
	private Screen mainScreen;
	private SettingsScreen settingsScreen;
	private SpriteBatch sb;
	
	public GameButton(FileHandle image, Screen mainScreen, SpriteBatch sb){
		super(image);
		this.mainScreen = mainScreen;
		this.sb = sb;
		addClickEvent(imgButton);
	}

	@Override
	public void addClickEvent(ImageButton button) {
		InputListener listener = new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				// Switch to Connect 4 SettingsScreen when game button is clicked
				mainScreen.hide();
				settingsScreen = new SettingsScreen(sb);
				settingsScreen.show();
				settingsScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				ProjectApplication.updateScreen(settingsScreen);
				return true;
			}
		};
		
		button.addListener(listener);
	}
}