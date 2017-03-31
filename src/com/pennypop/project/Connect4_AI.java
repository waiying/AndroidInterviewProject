package com.pennypop.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Connect4_AI {
	public boolean turn;
	private final GameScreen game;
	private int plyLimit;
	private List<Integer> col;
	private int firstOpenRow;
	
	public Connect4_AI(GameScreen game){
		this.game = game;
		plyLimit = 2;
		turn = false;
		firstOpenRow = 0;
		col = new ArrayList<Integer>(SettingsScreen.columns);
		
		// initialize col to arbitrary zeros
		for (int i = 0; i < SettingsScreen.columns; ++i){
			col.add(0);
		}
	}
	
	/** terminate after 500ms **/
	private boolean terminate(long startTime){
		return System.currentTimeMillis() - startTime > 500;
	}
	
	public void getNextMove(){
		turn = true;
		int max = Integer.MIN_VALUE; // Integer.MIN_VALUE is like negative infinity
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		long startTime = System.currentTimeMillis();
		float chosenMove = 0;
		boolean terminated = false;
		
		while (!terminate(startTime)) // perform Iterative Deepening Search
		{
			max = Integer.MIN_VALUE;
			alpha = Integer.MIN_VALUE;
			beta = Integer.MAX_VALUE;
			// get the min value of each successor
			for (int i = 0; i < SettingsScreen.columns; ++i)
			{
				if(terminate(startTime))
				{
					terminated = true;
					break;
				}

				if (game.getFirstCellUnoccupiedCoord(i) != null)
				{
					float colX = game.getFirstCellUnoccupiedCoord(i).x;
					game.makeMove(colX);
					col.set(i,minVal(startTime, 1, alpha, beta));
					
					if (col.get(i) > max)
					{
						max = col.get(i); // update the max value
					}
					game.unMakeMove();
				}
			}
			if (terminated)
				plyLimit--;
			else
				plyLimit += 1;
			float colX = game.getFirstCellUnoccupiedCoord(col.indexOf(max)).x;
			chosenMove = colX;
			//System.out.println(chosenMove);
		}
		game.makeMove(chosenMove);
		turn = false;
	}
	
	private int minVal(long startTime, int ply, int alpha, int beta)
	{
		int min = Integer.MAX_VALUE; // Integer.MAX_VALUE is like positive infinity
		int value;

		if (game.isGameOver())
		{
			System.out.println("AI detected a game over.");
			if (game.getWinner() == 0)
			{
				System.out.println("AI's opponent's predicted move is a tie.");
				return 0;
			}
			else
			{
				System.out.println("AI's opponent's predicted move is win.");
				return Integer.MAX_VALUE;
			}
		}

		if (ply == plyLimit) // "terminal state"
		{
			return evalfunc();
		}
		
		for (int i = 0; i < SettingsScreen.columns; ++i)
		{
			if (terminate(startTime))
			{
				break;
			}

			if (game.getFirstCellUnoccupiedCoord(i) != null)
			{
				float colX = game.getFirstCellUnoccupiedCoord(i).x;
				game.makeMove(colX);
				value = maxVal(startTime, ply+1, alpha, beta);
				game.unMakeMove();
				if (value < min)
				{
					min = value; // update the min value
					beta = value;
				}

				if (alpha > beta)
				{
					return beta;
				}
			}
		}

		return min;
	} // End minVal
	
	private int maxVal(long startTime, int ply, int alpha, int beta)
	{
		int max = Integer.MIN_VALUE; // Integer.MAX_VALUE is like positive infinity
		int value;

		if (game.isGameOver())
		{
			System.out.println("AI detected a game over.");
			if (game.getWinner() == 0)
			{
				System.out.println("AI's opponent's predicted move is a tie.");
				return 0;
			}
			else 
			{
				System.out.println("AI's opponent's predicted move is win.");
				return Integer.MIN_VALUE;
			}
		}

		if (ply == plyLimit) // "terminal state"
		{
			return evalfunc();
		}

		for (int i = 0; i < SettingsScreen.columns; ++i)
		{
			if (terminate(startTime))
			{
				break;
			}

			if (game.getFirstCellUnoccupiedCoord(i) != null)
			{
				float colX = game.getFirstCellUnoccupiedCoord(i).x;
				game.makeMove(colX);
				value = minVal(startTime, ply+1, alpha, beta);
				game.unMakeMove();
				if (value > max)
				{
					max = value; // update the min value
					alpha = value;
				}
				if (alpha > beta) // prune
				{
					return alpha;
				}
			}
		}

		return max;
	} // End maxVal
	
	private int evalfunc()
	{
		// calculates the value of this function: (10a[2] + 5a[1] + a[0]) - (10b[2] + 5b[1] + b[0])
		// a is this AI, b is the opponent
		// a[2] = # of lines with 3 coins and 1 blank, etc.
		int[] a = new int[SettingsScreen.win_size-1];
		int[] b = new int[SettingsScreen.win_size-1];
		int a_value=0, b_value=0;
		int value = 0;
		int row = 0;
		float firstOpenRowY = Integer.MAX_VALUE;
		float lastRowY = Integer.MIN_VALUE;
		int lastrow = 0;

		// find first open row
		for (int i = 0; i < SettingsScreen.columns; ++i){
			if (game.getFirstCellUnoccupiedCoord(i) != null){
				if (game.getFirstCellUnoccupiedCoord(i).y < firstOpenRowY){
					firstOpenRowY = game.getFirstCellUnoccupiedCoord(i).y;
				}
			}
		}
		
		firstOpenRow = game.getRowIndex(firstOpenRowY);
		row = firstOpenRow;

		for (int k = 0; k < SettingsScreen.columns; ++k)
		{
			if (game.getFirstCellUnoccupiedCoord(k) != null){
				if (game.getFirstCellUnoccupiedCoord(k).y > lastRowY)
					lastRowY = game.getFirstCellUnoccupiedCoord(k).y;
			}
		}
		
		lastrow = game.getRowIndex(lastRowY);

		// goes through each row starting from the first open row
		for (; row < lastrow; ++row)
		{
			getVerticals(a, b, row);
			getDiagonals(a, b, row);
			getHorizontals(a, b, row);
		}
		
		for (int i = 0; i < SettingsScreen.win_size-1; ++i){
			a_value += a[i] * Math.pow((i+1), 4);
			b_value += b[i] * Math.pow((i+1), 4);
		}
		value = a_value - b_value;

		return value;
	} //end evalFunc
	
	private void updateABList(int currCoin, int currCoinFreq, int emptyFreq, int[] a, int[] b)
	{
		// check if there is an opponent's piece in the line
		if (currCoinFreq + emptyFreq == SettingsScreen.win_size)
		{
			for (int i = 1; i < SettingsScreen.win_size; ++i){
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
	}
	
	private void getVerticals(int[] a, int[] b, int row)
	{
		int currCoin = 0; // 1 means player 1 at current slot, 2 means player 2 at current slot, 0 means empty slot
		List<Integer> line = new ArrayList<Integer>();
		int currCoinFreq, emptyFreq;

		// exit function if making a line goes out of boundary
		if (row+SettingsScreen.columns-1 > SettingsScreen.rows - 1)
		{
			return;
		}

		for (int i = 0; i < SettingsScreen.columns; ++i)
		{
			line.clear();
			// if row is the first open row, must look at previous rows...
			if (row == firstOpenRow && row != 0)
			{
				//System.out.print("enters");
				// creates line list
				for (int j = 0; j < SettingsScreen.win_size; ++j)
				{
					if (row-j < 0) // out of bound
					{
						break;
					}
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
					else // if empty
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

			line.clear();
			if (game.getCellInfo(row, i) != 0) // current slot isn't empty
			{
				currCoin = game.getCellInfo(row, i);
			}
			else continue;

			// creates line list
			for (int j = 0; j < 4; ++j)
			{
				line.add(game.getCellInfo(row + j, i));
			}

			currCoinFreq = Collections.frequency(line, currCoin);
			emptyFreq = Collections.frequency(line, 0);

			updateABList(currCoin, currCoinFreq, emptyFreq, a, b);
		}
	} // End getVerticals
	
	private void getDiagonals(int[] a, int[] b, int row)
	{
		int currCoin = 0; // 1 means player 1 at current slot, 2 means player 2 at current slot, 0 means empty slot
		List<Integer> line = new ArrayList<Integer>();
		int currCoinFreq=0, emptyFreq=0;
		int x = 0;

		// exit function if height goes out of boundary
		if (row+SettingsScreen.win_size-1 > SettingsScreen.rows - 1)
		{
			return;
		}

		for (;x < SettingsScreen.columns; x++)
		{
			line.clear();
			// if row is the first open row, must look at diagonals formed with previous rows...
			if (row == firstOpenRow && row != 0)
			{
				// creates line list for positive direction
				for (int j = 0; j < 4; ++j)
				{
					if (row-j < 0 || x-j < 0) // out of bound
					{
						break;
					}

					line.add(game.getCellInfo(row-j, x-j));

					if (line.get(0) == 2 && 
							((row+1 == SettingsScreen.rows || x+1 == SettingsScreen.columns) || game.getCellInfo(row+1, x+1) == 0) && j != SettingsScreen.win_size-1) // if currCoin is AI's coin
					{
						if (line.get(j) == 2 && j != 0)
						{
							a[j] += 1;
						}
					}
					else if (line.get(0) != 0 && 
							((row+1 == SettingsScreen.rows || x+1 == SettingsScreen.columns) || game.getCellInfo(row+1, x+1) == 0) && j != SettingsScreen.win_size-1) // if currCoin is Opponent's coin
					{
						if (line.get(j) != 2 && j != 0)
						{
							b[j] += 1;
						}
					}
					else // if empty
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

				// creates line list for negative direction
				for (int j = 0; j < SettingsScreen.win_size; ++j)
				{
					if (row-j < 0 || x+j >= SettingsScreen.columns) // out of bound
					{
						break;
					}

					line.add(game.getCellInfo(row-j, x+j));

					if (line.get(0) == 2 && 
							((row+1 == SettingsScreen.rows || x-1 < 0) || game.getCellInfo(row+1, x-1) == 0) && j != SettingsScreen.win_size-1) // if currCoin is AI's coin
					{
						if (line.get(j) == 2 && j != 0)
						{
							a[j] += 1;
						}
					}
					else if (line.get(0) != 0 && 
							((row+1 == SettingsScreen.rows || x-1 < 0) || game.getCellInfo(row+1, x-1) == 0) && j != SettingsScreen.win_size-1) // if currCoin is Opponent's coin
					{
						if (line.get(j) != 2 && j != 0)
						{
							b[j] += 1;
						}
					}
					else // if empty
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
	
	private void getHorizontals(int[] a, int[] b, int row)
	{
		int currCoin = 0; // 1 means player 1 at current slot, 2 means player 2 at current slot, 0 means empty slot
		List<Integer> line = new ArrayList<Integer>();
		int currCoinFreq, emptyFreq;

		for (int i = 0; i < SettingsScreen.columns - (SettingsScreen.win_size-1); ++i)
		{
			line.clear();
			currCoin = 0;
			// construct line
			for (int j = 0; j < 4; ++j)
			{
				line.add(game.getCellInfo(row, i+j));
				if (game.getCellInfo(row, i+j) != 0 && currCoin == 0)
				{
					currCoin = game.getCellInfo(row, i+j);
				}
			}

			currCoinFreq = Collections.frequency(line, currCoin);
			emptyFreq = Collections.frequency(line, 0);
			updateABList(currCoin, currCoinFreq, emptyFreq, a, b);
		}
	}
}