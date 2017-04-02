package com.pennypop.project.AI;

import com.pennypop.project.GameScreen;

/**
 * This is the base class for all AI's of connect 4.
 * In order to implement an AI, simply extend this class and
 * override the getNextMove method. 
 * 
 * getNextMove() will be invoked by the instance of the 
 * GameScreen class being passed in as the argument of the
 * AI's constructor. This method will run for the specified
 * amount of time being stored in the variable TIME_LIMIT.
 * 
 * @author Angie (Wai Ying Li)
 * @see AlphaBetaAI
 *
 */

public abstract class AIModule {
	protected final GameScreen game;
	protected boolean turn;
	private static final int TIME_LIMIT = 300;
	
	/**
	 * This is the AIModule constructor that should be called 
	 * when the AI is created. It sets the AI's turn to be false
	 * when initially created because right now we just assume the
	 * AI to be the second player. It also sets the GameScreen
	 * instance that the AI will be using to get information about
	 * the game.
	 * @param game: an instance of the GameScreen class for the AI
	 * to get information about the game.
	 */
	public AIModule(GameScreen game){
		this.game = game;
		turn = false;
	}
	
	/** 
	 * getTurn checks to see if it is currently the AI's
	 * turn to make a move. This method should be invoked by a
	 * GameScreen instance.
	 * @return a boolean of whether if it is the AI's turn or not.
	 * @see GameScreen
	 */
	public boolean getTurn(){
		return turn;
	}
	
	/** 
	 * terminate checks to see if the AI went pass the specified time
	 * limit (300 milliseconds) to make its move. If so, the AI will
	 * terminate and be forced to make a move with the information it 
	 * has gathered so far. 
	 * @param startTime
	 * (long) the time when the AI's getNextMove method was
	 * called by a GameScreen instance.
	 * @return a boolean of whether the AI should terminate or not.
	 */
	protected boolean terminate(long startTime){
		return System.currentTimeMillis() - startTime > TIME_LIMIT;
	}
	
	/**
	 * getNextMove gets the best possible move that the AI should make.
	 * This method is basically where the AI will go through a decision-
	 * making process to calculate the best move against the human player.
	 * This method should be invoked by a GameScreen instance.
	 * @see AlphaBeta, GameScreen
	 */
	public abstract void getNextMove();
}
