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

/**
 * This button class creates an Image Button for the Main Menu button in the Game Screen. It will add
 * a click event that will bring the application back to the Main Screen when the button is clicked.
 * @author Angie
 */
public class MainButton extends PennyPopButtons{
	private Screen gameScreen;
	private Screen mainScreen;
	private SpriteBatch sb;
	
	/**
	 * The constructor will initialize the Main Menu button with the given file image and add a click event
	 * to the button.
	 * @param image (FileHandle) the file of the button's image
	 * @param gameScreen (Screen) the game screen to transition from
	 * @param sb (SpriteBatch) the sprite batch of this application
	 */
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
				gameScreen.dispose();
				
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
