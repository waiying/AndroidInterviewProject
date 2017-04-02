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

/**
 * This is a class for the single player button in the Settings Screen. Clicking this
 * button will bring the application to the Game Screen in AI mode from the Settings Screen. 
 * @author Angie (Wai Ying Li)
 */
public class TwoPlayersButton extends PennyPopButtons{
	private Screen settingsScreen;
	private GameScreen gameScreen;
	private SpriteBatch sb;
	
	/**
	 * This is the constructor to create a single player button using the given file image
	 * and add a click event to the button.
	 * @param file (FileHandle) the file of the button's image
	 * @param settingsScreen (Screen) the instance of the screen to transition from
	 * @param sb (SpriteBatch) the sprite batch of this application
	 */
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
				// Switch to Connect 4 GameScreen when 2 Players button is clicked
				settingsScreen.hide();
				settingsScreen.dispose();
				
				gameScreen = new GameScreen(sb, false); // 2nd arg is false to disable AI mode
				gameScreen.show();
				gameScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
				ProjectApplication.updateScreen(gameScreen);
				return true;
			}
		};
		
		button.addListener(listener);
	}

}
