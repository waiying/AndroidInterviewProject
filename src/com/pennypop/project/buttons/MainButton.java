package com.pennypop.project.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.pennypop.project.MainScreen;
import com.pennypop.project.ProjectApplication;

public class MainButton extends PennyPopButtons{
	private Screen gameScreen;
	private Screen mainScreen;
	private SpriteBatch sb;
	
	public MainButton(FileHandle image, Screen gameScreen, SpriteBatch sb){
		super(image);
		this.gameScreen = gameScreen;
		this.sb = sb;
		addClickEvent(imgButton);
	}

	@Override
	public void addClickEvent(ImageButton button) {
		InputListener listener = new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				// Switch to Connect 4 SettingsScreen when game button is clicked
				gameScreen.hide();
				mainScreen = new MainScreen(sb);
				mainScreen.show();
				mainScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				ProjectApplication.updateScreen(mainScreen);
				return true;
			}
		};
		
		button.addListener(listener);
	}
}
