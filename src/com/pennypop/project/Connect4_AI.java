package com.pennypop.project;

import java.util.ArrayList;
import java.util.List;

public class Connect4_AI {
	public boolean turn;
	private final GameScreen game;
	private int plyLimit;
	
	public Connect4_AI(GameScreen game){
		this.game = game;
		plyLimit = 2;
		turn = false;
	}
	
	/** terminate after 500ms **/
	private boolean terminate(long startTime){
		return System.currentTimeMillis() - startTime > 500;
	}
	
	public void getNextMove(){
		int max = Integer.MIN_VALUE; // Integer.MIN_VALUE is like negative infinity
		int alpha = Integer.MIN_VALUE;
		int beta = Integer.MAX_VALUE;
		List<Integer> col = new ArrayList<Integer>();// initialize the list, doesn't matter what's used to fill each element
		long startTime = System.currentTimeMillis();
		
		//System.out.print("hi");
		while (!terminate(startTime)) // perform Iterative Deepening Search
		{
			max = Integer.MIN_VALUE;
			alpha = Integer.MIN_VALUE;
			beta = Integer.MAX_VALUE;
			// get the min value of each successor
			for (int i = 0; i < game.getWidth(); ++i)
			{
				if(terminate)
				{
					plyLimit -= 1;
					if (!game.canMakeMove(chosenMove))
					{
						chosenMove += 1;
					}
					//System.out.print("col.indexOf(max) = " + col.indexOf(max) + "\n");
					return;
				}

				if (game.canMakeMove(i))
				{
					game.makeMove(i);
					col.set(i,minVal(game, 1, alpha, beta));
					
					if (col.get(i) > max)
					{
						max = col.get(i); // update the max value
					}
					game.unMakeMove();
				}
			}
			plyLimit += 1;
			chosenMove = col.indexOf(max); // chosenMove is inherited from AIModule
		}
	}
}