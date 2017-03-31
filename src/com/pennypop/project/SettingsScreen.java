package com.pennypop.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.pennypop.project.buttons.SinglePlayerButton;
import com.pennypop.project.buttons.TwoPlayersButton;

public class SettingsScreen implements Screen{
	private final SpriteBatch spriteBatch;
	private final Stage stage;
	private final Table rootTable;
	
	// Game settings specified by the player:
	public static int columns, rows;
	public static int win_size;
	
	// input fields
	private TextField colInput;
	private TextField rowInput;
	private TextField winSizeInput;
	
	public SettingsScreen(SpriteBatch spriteBatch) {
		this.spriteBatch = spriteBatch;
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, this.spriteBatch);
		
		TextureRegionDrawable textFieldDrawable = getDrawable("textfield.png");
		TextureRegionDrawable cursorDrawable = getDrawable("cursor.png");
		TextureRegionDrawable selectionDrawable = getDrawable("selection.png");
		TextureRegionDrawable titleDrawable = getDrawable("Connect4.png");
		
		// set up labels, text fields, and button widgets
		Image title = new Image(titleDrawable);
		
		Label boardWidthLabel = new Label("Number of Columns:", new Label.LabelStyle(MainScreen.font, Color.BLACK));
		colInput = new TextField("7", new TextField.TextFieldStyle(MainScreen.font, Color.BLACK, cursorDrawable, selectionDrawable, textFieldDrawable));
		colInput.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
		
		Label boardHeightLabel = new Label("Number of Rows:", new Label.LabelStyle(MainScreen.font, Color.BLACK));
		rowInput = new TextField("6", new TextField.TextFieldStyle(MainScreen.font, Color.BLACK, cursorDrawable, selectionDrawable, textFieldDrawable));
		rowInput.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
		
		Label winLineSizeLabel = new Label("Number of Connects to win:", new Label.LabelStyle(MainScreen.font, Color.BLACK));
		winSizeInput = new TextField("4", new TextField.TextFieldStyle(MainScreen.font, Color.BLACK, cursorDrawable, selectionDrawable, textFieldDrawable));
		winSizeInput.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
		
		SinglePlayerButton singleButton = new SinglePlayerButton(Gdx.files.internal("OnePlayerButton.png"), this, this.spriteBatch);
		TwoPlayersButton twoButton = new TwoPlayersButton(Gdx.files.internal("TwoPlayersButton.png"), this, this.spriteBatch);
		
		// set up tables and add widgets to table
		rootTable = new Table();
		rootTable.setFillParent(true);
		//rootTable.debug();
		
		Table userSettings = new Table();
		//userSettings.debug();
		userSettings.row().padBottom(60);
		userSettings.add(title).colspan(2);
		
		userSettings.row().padBottom(30);
		userSettings.add(boardWidthLabel).right();
		userSettings.add(colInput).width(50).height(40).padLeft(10);
		
		userSettings.row().padBottom(30);
		userSettings.add(boardHeightLabel).right();
		userSettings.add(rowInput).width(50).height(40).padLeft(10);
		
		userSettings.row().padBottom(50);
		userSettings.add(winLineSizeLabel);
		userSettings.add(winSizeInput).width(50).height(40).padLeft(10);
		
		Table startButtons = new Table();
		startButtons.add(singleButton.getImageButton()).right().spaceRight(20);
		startButtons.add(twoButton.getImageButton());
		
		rootTable.add(userSettings);
		rootTable.row();
		rootTable.add(startButtons);
		
		stage.addActor(rootTable);
	}
	
	private TextureRegionDrawable getDrawable(String string){
		Texture texture = new Texture(string); 
		TextureRegion textureReg = new TextureRegion(texture);
		TextureRegionDrawable drawable = new TextureRegionDrawable(textureReg);
		return drawable;
	}
	
	private void updateSettingsVariables() {
		if (!colInput.getText().equals(""))
			columns = Integer.valueOf(colInput.getText());
		if (!rowInput.getText().equals(""))
			rows = Integer.valueOf(rowInput.getText());
		if (!winSizeInput.getText().equals(""))
			win_size = Integer.valueOf(winSizeInput.getText());
	}
	
	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void render(float delta) {
		//System.out.println("GameScreen rendered.");
		updateSettingsVariables();
		stage.act(delta);
		stage.draw();
		Table.drawDebug(stage);
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, false);		
	}
	
	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);		
	}

	@Override
	public void resume() {
		// Irrelevant on desktop, ignore this		
	}
	
	@Override
	public void pause() {
		// Irrelevant on desktop, ignore this		
	}
}
