package com.pennypop.project.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.pennypop.project.GameScreen;
import com.pennypop.project.ProjectApplication;

public class TwoPlayersButton extends PennyPopButtons{
	private Screen settingsScreen;
	private GameScreen gameScreen;
	private SpriteBatch sb;
	
	public TwoPlayersButton(FileHandle file, Screen settingsScreen, SpriteBatch sb) {
		super(file);
		this.settingsScreen = settingsScreen;
		this.sb = sb;
		addClickEvent(imgButton);
	}

	@Override
	public void addClickEvent(ImageButton button) {
		InputListener listener = new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				// Switch to Connect 4 SettingsScreen when game button is clicked
				settingsScreen.hide();
				gameScreen = new GameScreen(sb, false);
				gameScreen.show();
				gameScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				ProjectApplication.updateScreen(gameScreen);
				return true;
			}
		};
		
		button.addListener(listener);
	}

}
