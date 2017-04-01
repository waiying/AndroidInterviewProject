package com.pennypop.project;

import java.awt.Point;
import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.pennypop.project.buttons.MainButton;

public class GameScreen implements Screen{
	private final SpriteBatch spriteBatch;
	private final ShapeRenderer sr;
	
	private final Stage stage;
	private final boolean AI;
	private boolean drawRect;
	public boolean gameOver, tiedGame, redWon;
	private int currPlayer; // 1 = red player, 2 = yellow player/AI
	private final Texture red, yellow;
	private int boardCellWidth, boardCellHeight;
	private Connect4_AI connectAI;
	
	public Stack<Object[]> historyStack;
	
	// dimensions and coordinates for the rectangle outline of columns to play in
	private float rectWidth, rectHeight, rectX, rectY;
	
	// array of coordinates of each board cell's top left corner
	private Point[][] coordinates;
	// 2d array of which player occupied each cell
	private int[][] cellInfo;
	// array of the coordinates of the first unoccupied cell of each column
	private Point[] firstCellUnoccupied; 
	
	public GameScreen(SpriteBatch spriteBatch, boolean ai){
		this.spriteBatch = spriteBatch;
		sr = new ShapeRenderer();
		stage = new Stage(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false, this.spriteBatch);
		AI = ai;
		drawRect = false;
		gameOver = false;
		tiedGame = false;
		redWon = false;
		currPlayer = 1;	// red goes first
		
		historyStack = new Stack<Object[]>();
		
		// initialize arrays
		coordinates = new Point[SettingsScreen.rows][SettingsScreen.columns];
		firstCellUnoccupied = new Point[SettingsScreen.columns];
		cellInfo = new int[SettingsScreen.rows][SettingsScreen.columns];
		
		// load the red and yellow coin pieces
		red = new Texture(Gdx.files.internal("red.png"));
		yellow = new Texture(Gdx.files.internal("yellow.png"));
		
		connectAI = new Connect4_AI(this);
		
		makeBoard();
		
		// create main menu button
		Table buttonTab = new Table();
		buttonTab.setFillParent(true);
		buttonTab.bottom().left().pad(30);
		MainButton button = new MainButton(Gdx.files.internal("mainButton.png"), this, this.spriteBatch);
		buttonTab.add(button.getImageButton());
		
		// create Connect 4 logo
		Texture logo = new Texture(Gdx.files.internal("Connect4_small.png"));
		Image title = new Image(logo);
		Table titleTab = new Table();
		titleTab.setFillParent(true);
		titleTab.bottom().right().pad(20);
		titleTab.add(title);

		stage.addActor(buttonTab);
		stage.addActor(titleTab);
	}
	
	private void makeBoard(){
		Texture boardTexture = new Texture(Gdx.files.internal("board.png"));
		
		int numCols = SettingsScreen.columns;
		int numRows = SettingsScreen.rows;
		float boardWidth = numCols * boardTexture.getWidth();
		float boardHeight = numRows * boardTexture.getHeight();
		
		boardCellWidth = boardTexture.getWidth();
		boardCellHeight = boardTexture.getHeight();
		
		rectWidth = boardTexture.getWidth();
		rectHeight = boardTexture.getHeight() * numRows;
		rectY = Gdx.graphics.getHeight()/2 - boardHeight/2;
		
		// create each board cell from left to right and bottom to top while centering entire board
		for (int i = 0; i < numRows; ++i){
			float y = Gdx.graphics.getHeight()/2 - boardHeight/2 + i * boardTexture.getHeight();
			for (int j = 0; j < numCols; ++j){
				Image boardCell = new Image(boardTexture);
				float x = Gdx.graphics.getWidth()/2 - boardWidth/2 + j * boardTexture.getWidth();
				boardCell.setPosition(x, y);
				addHoverClickEvent(boardCell);
				stage.addActor(boardCell);
				
				coordinates[i][j] = new Point((int)x,(int)y);
				if (i == 0){
					firstCellUnoccupied[j] = new Point((int)x, (int)y);
				}
			}
		}
	}
	
	/** adds a hovering effect for each column and click event to make move **/
	private void addHoverClickEvent(final Actor actor) {
		InputListener listener = new InputListener(){
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor){
				// draw box around the clicked board cell's column using renderRectOutline
				drawRect = true;
				rectX = actor.getX();
			}
			
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				// stop drawing box around that actor's column
				drawRect = false;
			}
			
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button){
				// play move when a column is clicked
				if (!gameOver && !connectAI.turn)
					makeMove(actor.getX());
				return true;
			}
		};
		
		actor.addListener(listener);
	}
	
	public Point getCoordinates(int row, int col){
		return coordinates[row][col];
	}
	
	public int getCellInfo(int row, int col){
		return cellInfo[row][col];
	}
	
	public Point getFirstCellUnoccupiedCoord(int col){
		return firstCellUnoccupied[col];
	}
	
	/** Converts the y coordinate to the row index of the board **/
	public int getRowIndex(float y){
		int row = -1;
		
		for (int i = 0; i < SettingsScreen.rows; ++i){
			if ((int)y == coordinates[i][0].y)
				row = i;
		}
		
		return row;
	}
	
	/** Converts the x coordinate to the column index of the board **/
	public int getColIndex(float x){
		int col = -1;
		
		for (int j = 0; j < SettingsScreen.columns; ++j){
			if ((int)x == coordinates[0][j].x)
				col = j;
		}
		
		return col;
	}
	
	/** Draws the outline of the column being hovered over **/
	private void renderRectOutline() {
		Gdx.gl.glLineWidth(5); // width of line
		sr.begin(ShapeType.Rectangle);
		
		if (currPlayer == 1)
			sr.setColor(Color.RED);
		else if (currPlayer == 2)
			sr.setColor(Color.valueOf("fffa00"));
		
		sr.rect(rectX, rectY, rectWidth, rectHeight);
		sr.end();
	}
	
	/** makes a move in colX, which is the x coordinate of the column to play in**/
	public void makeMove(float colX){
		float x, y;
		Image playerPiece;
		
		// get the correct play piece for the current player
		if (currPlayer == 1){
			playerPiece = new Image(red);
		} else {
			playerPiece = new Image(yellow);
		}
		
		int col = getColIndex(colX);
		
		// check if move is valid;
		if (firstCellUnoccupied[col] == null)
			return;
		
		int row = getRowIndex(firstCellUnoccupied[col].y);
		
		// get the piece's coordinates to draw
		x = colX + boardCellWidth - boardCellWidth/2 - playerPiece.getWidth()/2;	
		y = firstCellUnoccupied[col].y + boardCellHeight/2 - playerPiece.getHeight()/2;
		
		// place piece on board
		playerPiece.setPosition(x, y);
		stage.addActor(playerPiece);
		
		// update the board's info
		cellInfo[row][col] = currPlayer;
		if (row == SettingsScreen.rows - 1)
			firstCellUnoccupied[col] = null; // null means that column is full
		else firstCellUnoccupied[col] = new Point((int)colX, firstCellUnoccupied[col].y + boardCellHeight);

		// update history info
		Object[] historyInfo = new Object[4];
		historyInfo[0] = playerPiece;
		historyInfo[1] = col;
		historyInfo[2] = colX;
		historyInfo[3] = row;
		historyStack.push(historyInfo);
		
		// switch players
		currPlayer = currPlayer%2 + 1;
	}
	
	public void unMakeMove(){
		Object[] recentHistory = historyStack.pop();
		Image prevPiece = (Image) recentHistory[0];
		int prevCol = (Integer) recentHistory[1];
		float prevColX = (Float) recentHistory[2];
		int prevRow = (Integer) recentHistory[3];
		
		prevPiece.remove();
		cellInfo[prevRow][prevCol] = 0;
		if (firstCellUnoccupied[prevCol] == null){
			float prevRowY = coordinates[SettingsScreen.rows-1][SettingsScreen.columns-1].y;
			firstCellUnoccupied[prevCol] = new Point((int)prevColX, (int)prevRowY);
		} else
			firstCellUnoccupied[prevCol] = new Point((int)prevColX, firstCellUnoccupied[prevCol].y - boardCellHeight);
		currPlayer = currPlayer%2 + 1; // switch back to previous player
	}
	
	/** finds if there are n consecutive pieces connected from the current cell **/
	private boolean foundConsec(int row, int col){
		int player = cellInfo[row][col]; // get player
		int count = 0;
		boolean exceedLeftBounds = false;
		boolean exceedRightBounds = false;
		boolean exceedBottomBounds = false;
		
		// check left
		if (col - (SettingsScreen.win_size - 1) >= 0){
			for (int i = 0; i < SettingsScreen.win_size; ++i){
				if (cellInfo[row][col-i] == player)
					count++;
			}
			if (count == SettingsScreen.win_size) {
				if (player == 1)
					redWon = true;
				return true;
			}
			count = 0; // reset
		} else
			exceedLeftBounds = true;
		
		// check left diagonal
		if (!exceedLeftBounds && row - (SettingsScreen.win_size - 1) >= 0) {
			for (int i = 0; i < SettingsScreen.win_size; ++i){
				if (cellInfo[row-i][col-i] == player)
					count++;
			}
			if (count == SettingsScreen.win_size) {
				if (player == 1)
					redWon = true;
				return true;
			}
			count = 0; // reset
		} else{
			if (row - (SettingsScreen.win_size - 1) < 0)
				exceedBottomBounds = true;
		}
		
		// check bottom
		if (!exceedBottomBounds){
			for (int i = 0; i < SettingsScreen.win_size; ++i){
				if (cellInfo[row-i][col] == player)
					count++;
			}
			if (count == SettingsScreen.win_size) {
				if (player == 1)
					redWon = true;
				return true;
			}
			count = 0;
		}
		
		// check right
		if (col + (SettingsScreen.win_size - 1) < SettingsScreen.columns) {
			for (int i = 0; i < SettingsScreen.win_size; ++i){
				if (cellInfo[row][col+i] == player)
					count++;
			}
			if (count == SettingsScreen.win_size) {
				if (player == 1)
					redWon = true;
				return true;
			}
			count = 0; // reset
		} else
			exceedRightBounds = true;
		
		// check right diagonal
		if (!exceedBottomBounds && !exceedRightBounds){
			for (int i = 0; i < SettingsScreen.win_size; ++i){
				if (cellInfo[row-i][col+i] == player)
					count++;
			}
			if (count == SettingsScreen.win_size) {
				if (player == 1)
					redWon = true;
				return true;
			}
		}
		
		return false;
	}
	
	/** checks for game over and tied game **/
	private void checkGameState(){
		int y;
		int row;
		int nullCount = 0;
		
		for (int i = 0; i < SettingsScreen.columns; ++i){
			if (firstCellUnoccupied[i] != null)
				y = firstCellUnoccupied[i].y;
			else {
				// column is full, check if the last piece is the winning player
				if (foundConsec(SettingsScreen.rows - 1, i)){
					gameOver = true;
					//System.out.println("Game Over1");
					if (!connectAI.turn)
						displayResults(redWon, tiedGame);
					return;
				}
				nullCount += 1;
				continue;
			}
			
			row = getRowIndex(y);
			
			if (row != 0){
				if (foundConsec(row-1, i)){
					gameOver = true;
					//System.out.println("Game Over2");
					if (!connectAI.turn)
						displayResults(redWon, tiedGame);
					return;
				}					
			}
		}
		
		// check for tied game
		if (nullCount == SettingsScreen.columns){
			tiedGame = true;
			gameOver = true;
			if (!connectAI.turn)
				displayResults(redWon, tiedGame);
			//System.out.println("Game Over3");
		}
	}
	
	public boolean isGameOver(){
		checkGameState();
		if (gameOver || tiedGame){
			gameOver = false;
			tiedGame = false;
			return true;
		}else
			return false;
	}
	
	public int getWinner(){
		if (redWon){
			redWon = false;
			return 1;
		}
		else if (tiedGame){
			tiedGame = false;
			return 0;
		}
		else
			return 2;
	}
	
	private void displayResults(boolean redWins, boolean tied){
		Table result = new Table();
		result.setFillParent(true);
		result.bottom().pad(30);
		stage.addActor(result);
		
		if (redWins){
			Label redWinner = new Label("Red Player Won!", new Label.LabelStyle(MainScreen.font, Color.RED));
			result.add(redWinner).pad(10);
		}
		else if (tied){
			Label tie = new Label("A Tied Game!", new Label.LabelStyle(MainScreen.font, Color.BLACK));
			result.add(tie).pad(10);
		}
		else {
			Label yellowWinner = new Label("Yellow Player Won!", new Label.LabelStyle(MainScreen.font, Color.YELLOW));
			result.add(yellowWinner).pad(10);
		}
	}
	
	@Override
	public void dispose() {
		stage.dispose();
		sr.dispose();
	}

	@Override
	public void hide() {
		Gdx.input.setInputProcessor(null);
	}

	@Override
	public void render(float delta) {
		checkGameState();
		stage.act(delta);
		stage.draw();
		// spriteBatch ended
		
		if (drawRect && !gameOver)
			renderRectOutline();
		
		if (AI && currPlayer == 2 && !connectAI.turn && !gameOver){
			connectAI.getNextMove();
		}
	}

	@Override
	public void resize(int width, int height) {
		stage.setViewport(width, height, true);	
	}

	@Override
	public void resume() {
		// Irrelevant on desktop, ignore this		
	}
	
	@Override
	public void pause() {
		// Irrelevant on desktop, ignore this		
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);	
	}

}
