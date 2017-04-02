package com.pennypop.project;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.Color;
import com.pennypop.project.buttons.APIButton;
import com.pennypop.project.buttons.GameButton;
import com.pennypop.project.buttons.SfxButton;

/**
 * This class creates the Main Screen's UI.
 * 
 * @author Richard Taylor
 * @author Angie (Wai Ying Li)
 */
public class MainScreen implements Screen {
	private final SpriteBatch spriteBatch;
	private final Stage stage;
	
	public static Table rootTable;
	private final Label pennyPopLabel;
	public static final BitmapFont font = new BitmapFont(Gdx.files.internal("font.fnt"), Gdx.files.internal("font.png"), false);
	
	// the buttons
	private final SfxButton sfx; 
	private final APIButton api;
	private final GameButton game;
	
	/**
	 * The constructor sets up all the UI to be rendered.
	 * @param spriteBatch (SpriteBatch) the sprite batch of this application
	 */
	public MainScreen(SpriteBatch spriteBatch) {
		this.spriteBatch = spriteBatch;
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, this.spriteBatch);
		
		// set up the root table with 2 columns to organize our widgets
		rootTable = new Table();
		rootTable.setFillParent(true);
		Table col1 = new Table(); // col1 table is the main menu's UI
		Table col2 = new Table(); // weather info are added into col2 when API button is clicked
		
		// set up text for column 1
		pennyPopLabel = new Label("PennyPop", new Label.LabelStyle(font, Color.RED));
		
		// set up the 3 buttons
		sfx = new SfxButton(Gdx.files.internal("sfxButton.png"));
		api = new APIButton(Gdx.files.internal("apiButton.png"), col2);
		game = new GameButton(Gdx.files.internal("gameButton.png"), this, spriteBatch);
		
		// add in the widgets for column 1
		col1.add(pennyPopLabel).colspan(3).padBottom(30);
		col1.row();
		col1.add(sfx.getImageButton()).padRight(10);
		col1.add(api.getImageButton()).padRight(10);
		col1.add(game.getImageButton());
		
		rootTable.add(col1);
		rootTable.add(col2);
		
		stage.addActor(rootTable);
	}
	
	@Override
	public void dispose() {
		stage.dispose();
	}

	@Override
	public void render(float delta) {
		stage.act(delta);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, false);
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void pause() {
		// Irrelevant on desktop, ignore this
	}

	@Override
	public void resume() {
		// Irrelevant on desktop, ignore this
	}

}
