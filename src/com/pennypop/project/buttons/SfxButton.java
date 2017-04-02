package com.pennypop.project.buttons;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;

/**
 * This is a class for the sfx button in the Main Screen. Clicking this
 * button will make a clicking sound effect. 
 * @author Angie (Wai Ying Li)
 */

public class SfxButton extends PennyPopButtons{
	/**
	 * This is the constructor to create a sfx button using the given file image
	 * and add a click event to the button.
	 * @param file (FileHandle) the file of the button's image
	 */
	public SfxButton(FileHandle image){
		super(image);
		addClickEvent(imgButton);
	}

	@Override
	public void addClickEvent(ImageButton button) {
		InputListener listener = new InputListener(){
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				// play sfx when clicked
				Sound sfx = Gdx.audio.newSound(Gdx.files.internal("button_click.wav"));
				sfx.play(0.5f);
				return true;
			}
		};
		
		button.addListener(listener);
	}
}
