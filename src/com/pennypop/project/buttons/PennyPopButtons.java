package com.pennypop.project.buttons;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public abstract class PennyPopButtons {
	private Texture buttonTexture;
	private TextureRegion buttonTextureReg;
	private TextureRegionDrawable buttonDrawable;
	protected ImageButton imgButton;
	
	public PennyPopButtons(FileHandle file){
		// set up image for the buttons
		buttonTexture = new Texture(file); 
		buttonTextureReg = new TextureRegion(buttonTexture);
		buttonDrawable = new TextureRegionDrawable(buttonTextureReg);
		imgButton = new ImageButton(buttonDrawable);
	}
	
	public ImageButton getImageButton(){
		return imgButton;
	}
	
	public abstract void addClickEvent(ImageButton button);
}
