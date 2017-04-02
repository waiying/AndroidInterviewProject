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

/**
 * This button class creates an Image Button for the Game button in the Main Screen. It will add
 * a click event that will bring the application to the Settings Screen when the button is clicked.
 * @author Angie
 */
public class GameButton extends PennyPopButtons{
	private Screen mainScreen;
	private SettingsScreen settingsScreen;
	private SpriteBatch sb;
	
	/**
	 * The constructor will initialize the Game button with the given file image and add a click event
	 * to the button.
	 * @param image (FileHandle) the file of the button's image
	 * @param mainScreen (Screen) the Main Screen to transition from
	 * @param sb (SpriteBatch) the sprite batch of this application
	 */
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
				mainScreen.dispose();
				
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