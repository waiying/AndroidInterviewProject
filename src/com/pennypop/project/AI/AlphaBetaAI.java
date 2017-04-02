package com.pennypop.project.AI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.pennypop.project.GameScreen;
import com.pennypop.project.SettingsScreen;

/**
 * This class is an AI created for the Penny Pop project's connect 4 game. 
 * This AI uses the Minimax and Alpha-beta pruning algorithm to predict the human
 * player's action with the assumption that he or she is playing optimally.
 * AI is treated as the "MAX" (yellow) player while the human is treated as the
 * "MIN" (red) player.
 * 
 * This AI performs iterative deepening search to explore the game tree with the
 * limited amount of time that it has.
 * 
 * To use this AI, simply initialize it in the GameScreen class.
 * 
 * @author Angie (Wai Ying Li)
 * @see GameScreen
 */

public class AlphaBetaAI extends AIModule{
	/** A list of the utility values for each possible move(the columns) that the AI can make**/
	private List<Integer> colValues;
	/**The lowest row index with an unfilled spot**/
	private int firstOpenRow;
	/**The number of levels down from the game tree's root that the AI should explore**/
	private int plyLimit;
	
	public AlphaBetaAI(GameScreen game){
		super(game);
		firstOpenRow = 0;
		colValues = new ArrayList<Integer>(SettingsScreen.columns);
		
		// initialize the utility values to arbitrary zeros
		for (int i = 0; i < SettingsScreen.columns; ++i){
			colValues.add(0);
		}
	} // end constructor
	
	@Override
	public void getNextMove(){
		turn = true; // variable inherited from AIModule class
		int max = Integer.MIN_VALUE; // "Negative infinity"
		int alpha = Integer.MIN_VALUE; // The maximum lower bound of the possible utility values
		int beta = Integer.MAX_VALUE; // The minimum upper bound of the possible utility values
		final long startTime = System.currentTimeMillis(); // start the timer of the AI's turn
		float chosenMove = 0; // y coordinate of the column that the AI will make its move in
		plyLimit = 2; // AI will initially look 2 levels down from the game tree's root
		
		// perform Iterative Deepening Search
		while (!terminate(startTime)){ // terminate is an AIModule method
			// reset values when starting search from the root again
			max = Integer.MIN_VALUE;
			alpha = Integer.MIN_VALUE;
			beta = Integer.MAX_VALUE;
			
			// get the utility value of each successor and find the max value
			for (int i = 0; i < SettingsScreen.columns; ++i){
				if(terminate(startTime)){
					plyLimit--;
					game.makeMove(chosenMove);
					turn = false;
					return;
				}
				
				// make sure the column isn't full. null for that column means that it's full.
				if (game.getFirstCellUnoccupiedCoord(i) != null){
					float colX = game.getFirstCellUnoccupiedCoord(i).x;
					game.makeMove(colX);
					colValues.set(i,minVal(startTime, 1, alpha, beta));
					
					// update the max value
					if (colValues.get(i) > max){
						max = colValues.get(i);
					}
					game.unMakeMove();
				}				
			} // finished search
			
			// still have time, increase search level
			plyLimit += 1;
			
			// record the chosen move so far
			float colX = game.getFirstCellUnoccupiedCoord(colValues.indexOf(max)).x;
			chosenMove = colX;
		}
		game.makeMove(chosenMove);
		turn = false;
	}// end getNextMove
	
	/**
	 * minVal gets the minimum value of its successors (each possible move). In other words,
	 * this method basically gets the simulated move of the human opponent. As the "MIN"
	 * player, it will try to minimize the AI's utility value.
	 * @param startTime (long) the start time of the AI's turn
	 * @param ply (int) the current level in the game tree
	 * @param alpha (int) The maximum lower bound of the possible utility values of this node
	 * @param beta (int) The minimum upper bound of the possible utility values of this node
	 * @return (int) the minimum utility value
	 */
	private int minVal(long startTime, int ply, int alpha, int beta){
		int min = Integer.MAX_VALUE; // positive infinity
		int value; // utility value
		
		// check if MAX made a winning move or a tied game
		if (game.isGameOver())
		{
			if (game.getWinner() == 0)
				return 0;
			else
				return Integer.MAX_VALUE;
		}

		if (ply == plyLimit) // "terminal state"
			return evalfunc();
		
		// get the utility value of each successor and get the min value
		for (int i = 0; i < SettingsScreen.columns; ++i)
		{
			if (terminate(startTime))
				break;
			
			// make sure the column isn't full. null for that column means that it's full.
			if (game.getFirstCellUnoccupiedCoord(i) != null)
			{
				float colX = game.getFirstCellUnoccupiedCoord(i).x;
				game.makeMove(colX);
				value = maxVal(startTime, ply+1, alpha, beta);
				game.unMakeMove();
				
				// update the min value
				if (value < min)
				{
					min = value;
					beta = value;
				}
				
				// prune the rest of the successors if alpha > beta
				if (alpha > beta)
					return beta;
			}
		}
		return min;
	} // End minVal
	
	/**
	 * maxVal gets the maximum value of its successors (each possible move). In other words,
	 * this method basically gets the simulated move of the AI. As the "MAX" player, it will 
	 * try to maximize the AI's utility value.
	 * @param startTime (long) the start time of the AI's turn
	 * @param ply (int) the current level in the game tree
	 * @param alpha (int) The maximum lower bound of the possible utility values of this node
	 * @param beta (int) The minimum upper bound of the possible utility values of this node
	 * @return (int) the maximum utility value
	 */
	private int maxVal(long startTime, int ply, int alpha, int beta){
		int max = Integer.MIN_VALUE; // positive infinity
		int value; // utility value
		
		// check if MIN made a winning move or a tied game
		if (game.isGameOver())
		{
			if (game.getWinner() == 0)
				return 0;
			else 
				return Integer.MIN_VALUE;
		}

		if (ply == plyLimit) // "terminal state"
			return evalfunc();
		
		// get the utility value of each successor and get the max value
		for (int i = 0; i < SettingsScreen.columns; ++i)
		{
			if (terminate(startTime))
				break;
			
			// make sure the column isn't full. null for that column means that it's full.
			if (game.getFirstCellUnoccupiedCoord(i) != null)
			{
				float colX = game.getFirstCellUnoccupiedCoord(i).x;
				game.makeMove(colX);
				value = minVal(startTime, ply+1, alpha, beta);
				game.unMakeMove();
				
				// update the max value
				if (value > max)
				{
					max = value; 
					alpha = value;
				}
				// prune the rest of the successors if alpha > beta
				if (alpha > beta)
					return alpha;
			}
		}

		return max;
	} // End maxVal
	
	/**
	 * evalfunc calculates the utility value of the current state of the game.<br>
	 * <b>a</b> is an array of the number of each possible connects for the AI.<br>
	 * <b>b</b> is an array of the number of each possible connects for the opponent.<br>
	 * The higher the number of connects, the bigger the weight in order to favor wins.
	 * 
	 * <h4>Example using a winning size of 4:</h4>
	 * Evaluation Function: ((3^5)*a[2] + (2^5)*a[1] + (1^5)*a[0]) - ((3^5)*b[2] + (2^5)*b[1] + (1^5)*b[0])<br>
	 * a[2] is the number of 3 yellow coins and 1 empty space in a row.<br>
	 * a[1] is the number of 2 yellow coins and 2 empty spaces in a row.<br>
	 * a[0] is the number of 1 yellow coin and 3 empty spaces in a row.<br>
	 * b[2] is the number of 3 red coins and 1 empty space in a row.<br>
	 * b[1] is the number of 2 red coins and 2 empty spaces in a row.<br>
	 * b[0] is the number of 1 red coin and 3 empty spaces in a row.<br>
	 * 
	 * @return (int) the utility value of the current state of the game
	 */
	private int evalfunc(){
		int[] a = new int[SettingsScreen.win_size-1];
		int[] b = new int[SettingsScreen.win_size-1];
		
		int a_value=0, b_value=0; // sums of the values in the respective arrays multiplied by weights
		int value = 0; // utility value to be calculated
		
		int row = 0; // index of rows to iterate through
		int lastrow = 0; // index of the first row without any coin pieces
		float firstOpenRowY = Integer.MAX_VALUE; // y coordinate of first open row
		float lastRowY = Integer.MIN_VALUE; // y coordinate of last row

		// find first open row's y coordinate
		for (int i = 0; i < SettingsScreen.columns; ++i){
			if (game.getFirstCellUnoccupiedCoord(i) != null){
				if (game.getFirstCellUnoccupiedCoord(i).y < firstOpenRowY){
					firstOpenRowY = game.getFirstCellUnoccupiedCoord(i).y;
				}
			}
		}
		
		firstOpenRow = game.getRowIndex(firstOpenRowY);
		row = firstOpenRow;
		
		// find the last row's y coordinate
		for (int k = 0; k < SettingsScreen.columns; ++k){
			if (game.getFirstCellUnoccupiedCoord(k) != null){
				if (game.getFirstCellUnoccupiedCoord(k).y > lastRowY)
					lastRowY = game.getFirstCellUnoccupiedCoord(k).y;
			}
		}
		
		lastrow = game.getRowIndex(lastRowY);

		// goes through each row starting from the first open row up to the last row with coin pieces
		// to update a and b arrays
		for (; row < lastrow; ++row){
			getVerticals(a, b, row);
			getDiagonals(a, b, row);
			getHorizontals(a, b, row);
		}
		
		// get sums for a_value and b_value
		for (int i = 0; i < SettingsScreen.win_size-1; ++i){
			a_value += a[i] * Math.pow((i+1), 5);
			b_value += b[i] * Math.pow((i+1), 5);
		}
		value = a_value - b_value;

		return value;
	} //end evalFunc
	
	/**
	 * updateABList updates the elements of the arrays a and b according to the number of connects
	 * <br>Using the example in evalfunc's javadoc, if there are 3 yellow coins and 1 blank, then 
	 * add 1 for a[2].
	 * 
	 * @param currCoin (int) the current player we're updating for (AI or human opponent)
	 * @param currCoinFreq (int) the frequency of the current player's piece in the winning size connect line
	 * @param emptyFreq (int) the frequency of empty slots in the winning size connect line
	 * @param a (int[]) an array of the number of each possible connects for the AI.
	 * @param b (int[]) an array of the number of each possible connects for the opponent.
	 * @see evalfunc, getVerticals, getHorizontals, getDiagonals
	 */
	
	private void updateABList(int currCoin, int currCoinFreq, int emptyFreq, int[] a, int[] b){
		// check if there is an opponent's piece in the line
		// if there is, then currCoinFreq + empty Freq != SettingsScreen.win_size
		if (currCoinFreq + emptyFreq == SettingsScreen.win_size){
			for (int i = 1; i < SettingsScreen.win_size; ++i){
				// check which player to update for
				if (currCoin == 2){
					if (currCoinFreq == i){
						a[i-1]++;
					}
				}
				else {
					if (currCoinFreq == i){
						b[i-1]++;
					}
				}
			}
		}
	} // end updateABList
	
	/**
	 * getVerticals gets the SettingsScreen.winning_size elements (current player's coin, empty slots) 
	 * connected in a vertical line to update the number of connects in that line for the currCoin. 
	 * If the row we're in is the first open row, we must consider the lines formed with the slots
	 * below the current coin we're updating for. Otherwise,we only look at the line going up from the current piece we're at.
	 * <br>currCoin is the current player we're updating for (AI or human opponent).
	 * @param a (int[]) an array of the number of each possible connects for the AI.
	 * @param b (int[]) an array of the number of each possible connects for the opponent.
	 * @param row (int) the row index we're currently at to count the number of connects
	 * @see updateABList
	 */
	
	private void getVerticals(int[] a, int[] b, int row)
	{
		int currCoin = 0; // 1 means player 1, 2 means AI, 0 means empty slot
		// an array list to store the elements in the line we're looking at
		List<Integer> line = new ArrayList<Integer>(); 
		// Frequencies of the coin we're looking at, and frequencies of empty slots
		int currCoinFreq, emptyFreq;

		// exit function if making a line goes out of boundary
		if (row+SettingsScreen.win_size-1 > SettingsScreen.rows - 1)
			return;

		for (int i = 0; i < SettingsScreen.columns; ++i){
			line.clear();
			// if row is the first open row and not the first row, must look at previous rows to update...
			if (row == firstOpenRow && row != 0){
				// creates line list
				for (int j = 0; j < SettingsScreen.win_size; ++j){
					if (row-j < 0) // out of bound
						break;
					line.add(game.getCellInfo(row - j, i));

					if (line.get(0) == 2 && game.getCellInfo(row+1, i) == 0 && j != SettingsScreen.win_size-1) // if currCoin is AI's coin
					{
						if (line.get(j) == 2 && j != 0)
						{
							a[j] += 1;
						}
					}
					else if (line.get(0) != 0 && game.getCellInfo(row+1, i) == 0 && j != SettingsScreen.win_size-1) // if currCoin is Opponent's coin
					{
						if (line.get(j) != 2 && j != 0)
						{
							b[j] += 1;
						}
					}
					else // if the first element of the line is an empty slot
					{
						if (j != 0 && line.get(j) == 2)
						{
							a[j-1] +=1;
						}
						else if (j != 0)
						{
							b[j-1] += 1;
						}
					}
				}
			}
			
			// reset line list and start looking at lines formed with slots above the current coin
			line.clear();
			// get currCoin first. If empty slot, nothing to look at and move on to next column.
			if (game.getCellInfo(row, i) != 0) // current slot isn't empty
				currCoin = game.getCellInfo(row, i);
			else continue;

			// creates line list
			for (int j = 0; j < 4; ++j){
				line.add(game.getCellInfo(row + j, i));
			}

			currCoinFreq = Collections.frequency(line, currCoin);
			emptyFreq = Collections.frequency(line, 0);

			updateABList(currCoin, currCoinFreq, emptyFreq, a, b);
		}
	}// End getVerticals
	
	/**
	 * getDiagonals gets the SettingsScreen.winning_size elements (current player's coin, empty slots) 
	 * connected in a diagonal line to update the number of connects in that line for the currCoin. 
	 * If the row we're in is the first open row, we must consider the lines formed with the slots
	 * below the current coin we're updating for. Otherwise,we only look at the diagonal line going up from the current piece we're at.
	 * <br>currCoin is the current player we're updating for (AI or human opponent).
	 * @param a (int[]) an array of the number of each possible connects for the AI.
	 * @param b (int[]) an array of the number of each possible connects for the opponent.
	 * @param row (int) the row index we're currently at to count the number of connects
	 * @see updateABList
	 */
	
	private void getDiagonals(int[] a, int[] b, int row){
		int currCoin = 0; // 1 means player 1, 2 means AI, 0 means empty slot
		// an array list to store the elements in the line we're looking at
		List<Integer> line = new ArrayList<Integer>();
		// Frequencies of the coin we're looking at, and frequencies of empty slots
		int currCoinFreq=0, emptyFreq=0;

		// exit function if height goes out of boundary
		if (row+SettingsScreen.win_size-1 > SettingsScreen.rows - 1)
			return;

		for (int x = 0;x < SettingsScreen.columns; x++){
			line.clear();
			// if row is the first open row, must look at diagonals formed with previous rows...
			if (row == firstOpenRow && row != 0){
				// creates line list for positive direction
				for (int j = 0; j < 4; ++j){
					if (row-j < 0 || x-j < 0) // out of bound
						break;

					line.add(game.getCellInfo(row-j, x-j));
					
					// if the first element is AI's coin
					if (line.get(0) == 2 && 
							((row+1 == SettingsScreen.rows || x+1 == SettingsScreen.columns) || game.getCellInfo(row+1, x+1) == 0) && 
							j != SettingsScreen.win_size-1)
					{
						if (line.get(j) == 2 && j != 0)
							a[j] += 1;
					}
					// if the first element is Opponent's coin
					else if (line.get(0) != 0 && 
							((row+1 == SettingsScreen.rows || x+1 == SettingsScreen.columns) || game.getCellInfo(row+1, x+1) == 0) && 
							j != SettingsScreen.win_size-1)
					{
						if (line.get(j) != 2 && j != 0)
							b[j] += 1;
					}
					else // if the first element is empty
					{
						if (j != 0 && line.get(j) == 2)
							a[j-1] +=1;

						else if (j != 0)
							b[j-1] += 1;
					}
				}

				// creates line list for negative direction
				for (int j = 0; j < SettingsScreen.win_size; ++j)
				{
					if (row-j < 0 || x+j >= SettingsScreen.columns) // out of bound
						break;

					line.add(game.getCellInfo(row-j, x+j));
					
					// if the first element is AI's coin
					if (line.get(0) == 2 && 
							((row+1 == SettingsScreen.rows || x-1 < 0) || game.getCellInfo(row+1, x-1) == 0) && 
							j != SettingsScreen.win_size-1)
					{
						if (line.get(j) == 2 && j != 0)
							a[j] += 1;
					}
					// if the first element is Opponent's coin
					else if (line.get(0) != 0 && 
							((row+1 == SettingsScreen.rows || x-1 < 0) || game.getCellInfo(row+1, x-1) == 0) && 
							j != SettingsScreen.win_size-1) 
					{
						if (line.get(j) != 2 && j != 0)
						{
							b[j] += 1;
						}
					}
					else // if the first element is an empty slot
					{
						if (j != 0 && line.get(j) == 2)
						{
							a[j-1] +=1;
						}
						else if (j != 0)
						{
							b[j-1] += 1;
						}
					}
				}
			}
			
			// reset line list and start looking at lines formed with slots above the current coin
			line.clear();

			if (x+SettingsScreen.win_size-1 <= SettingsScreen.columns - 1)
			{
				// creates line list for positive slope
				for (int j = 0; j < SettingsScreen.win_size; ++j)
				{
					line.add(game.getCellInfo(row + j, x + j));
					if (game.getCellInfo(row + j, x + j) != 0)
					{
						currCoin = game.getCellInfo(row + j, x + j);
					}
				}

				currCoinFreq = Collections.frequency(line, currCoin);
				emptyFreq = Collections.frequency(line, 0);

				updateABList(currCoin, currCoinFreq, emptyFreq, a, b);
				line.clear();
			}

			if (x-(SettingsScreen.win_size-1) >= 0)
			{
				// creates line list for negative slope
				for (int j = 0; j < SettingsScreen.win_size; ++j)
				{
					line.add(game.getCellInfo(row + j, x - j));
					if (game.getCellInfo(row + j, x - j) != 0)
					{
						currCoin = game.getCellInfo(row + j, x - j);
					}
				}

				currCoinFreq = Collections.frequency(line, currCoin);
				emptyFreq = Collections.frequency(line, 0);

				updateABList(currCoin, currCoinFreq, emptyFreq, a, b);
			}
		}
	} // End getDiagonals
	
	/**
	 * getHorizontals gets the SettingsScreen.winning_size elements (current player's coin, empty slots) 
	 * connected in a horizontal line to update the number of connects in that line for the currCoin. 
	 * <br>currCoin is the current player we're updating for (AI or human opponent).
	 * @param a (int[]) an array of the number of each possible connects for the AI.
	 * @param b (int[]) an array of the number of each possible connects for the opponent.
	 * @param row (int) the row index we're currently at to count the number of connects
	 * @see updateABList
	 */
	
	private void getHorizontals(int[] a, int[] b, int row){
		int currCoin = 0; // 1 means player 1, 2 means AI, 0 means empty slot
		// an array list to store the elements in the line we're looking at
		List<Integer> line = new ArrayList<Integer>();
		// Frequencies of the coin we're looking at, and frequencies of empty slots
		int currCoinFreq, emptyFreq;
		
		// construct line list from left to right
		for (int i = 0; i < SettingsScreen.columns - (SettingsScreen.win_size-1); ++i){
			line.clear(); // reset line list for the new column
			currCoin = 0; // reset currCoin
			
			for (int j = 0; j < 4; ++j)	{
				line.add(game.getCellInfo(row, i+j));
				if (game.getCellInfo(row, i+j) != 0 && currCoin == 0)
					currCoin = game.getCellInfo(row, i+j);
			}

			currCoinFreq = Collections.frequency(line, currCoin);
			emptyFreq = Collections.frequency(line, 0);
			updateABList(currCoin, currCoinFreq, emptyFreq, a, b);
		}
	}
}