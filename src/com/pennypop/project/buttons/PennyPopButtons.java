package com.pennypop.project.buttons;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * This is the base class for all the image buttons.
 * To use this class, simply extend PennyPopButtons and override the addClickEvent to 
 * give the button a specific event when clicked.
 * 
 * @author Angie (Wai Ying Li)
 *
 */

public abstract class PennyPopButtons {
	private Texture buttonTexture;
	private TextureRegion buttonTextureReg;
	private TextureRegionDrawable buttonDrawable;
	protected ImageButton imgButton;
	
	/**
	 * The constructor to load the button's image.
	 * @param file (FileHandle) the file path of the button's image
	 */
	public PennyPopButtons(FileHandle file){
		// set up image for the buttons
		buttonTexture = new Texture(file); 
		buttonTextureReg = new TextureRegion(buttonTexture);
		buttonDrawable = new TextureRegionDrawable(buttonTextureReg);
		imgButton = new ImageButton(buttonDrawable);
	}
	
	/**
	 * This method returns the ImageButton object.
	 */
	public ImageButton getImageButton(){
		return imgButton;
	}
	
	/**
	 * This method adds an event to the button when clicked. It must be
	 * overridden to do tasks specific to the button.
	 * @param button (ImageButton) the button initialized when the button class was first created.
	 */
	public abstract void addClickEvent(ImageButton button);
}
