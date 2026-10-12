
import java.util.ArrayList;

public class HeuristicPlayer extends Player {

	ArrayList<int[]> path;

	public HeuristicPlayer() {
		super();
		path = new ArrayList<int[]>();
	}

	public HeuristicPlayer(int pid, String n, int sc, Board b) {
		super(pid, n, sc, b);
		path = new ArrayList<int[]>();
	}

	public HeuristicPlayer(int pid, String n, int sc, Board b, ArrayList<int[]> path) {
		super(pid, n, sc, b);
		this.path = path;
	}

	public double evaluate(int currentPos, int dice) {

		boolean[] laddersUsed = new boolean[board.getLadders().length];
		for (int i = 0; i < board.getLadders().length; i++) {
			laddersUsed[i] = board.getLadders()[i].getBroken();
		}
		int[] applePoints = new int[board.getApples().length];
		for (int i = 0; i < applePoints.length; i++) {
			applePoints[i] = board.getApples()[i].getPoints();
		}

		// we take the current state of the ladders and apples
		// and save it in new arrays
		// then we make the hypothetical move of the player for this dice using
		// the player's move
		// and at the end we save newid and how many red or black apples were eaten and
		// how many points in total were gained from this hypothetical
		// move; finally we feed them into our evaluation function
		// we make the hypothetical move
		int oldScore = this.getScore();
		//int newid = move(currentPos, dice)[0];
		int[] moveResults = new int[5];
		moveResults = move(currentPos, dice);
		System.out.println("MoveResults: ");
		for(int r=0; r<moveResults.length; r++)
		{
			System.out.print(moveResults[r] + ", ");
		}
		int newid = moveResults[0];
		int steps = newid - currentPos;
		int gainPoints = this.getScore() - oldScore;
		this.setScore(oldScore);
		// we restore the ladders to their initial state (not broken)
		
		for (int i = 0; i < board.getLadders().length; i++) {
			if (laddersUsed[i] != board.getLadders()[i].getBroken())
				board.getLadders()[i].setBroken(false);

		}
		// the simulated move must not eat the apples either
		for (int i = 0; i < applePoints.length; i++) {
			board.getApples()[i].setPoints(applePoints[i]);
		}
		
		double evaluation = (double) steps * 0.6 + (double) gainPoints * 0.4;
		System.out.println("Evaluation: " + evaluation + " steps: " + steps + " gainPoints: " + gainPoints);
		return evaluation;

	}

	public int getNextMove(int currentPos) {
		double[] posibleMoves = new double[6]; // the indices of the array correspond to the possible cases, i.e.
												// to the possible dice results; for this reason the size of the array is 6, as many as
												// the dice results
		System.out.println("Start of the evaluation \n");
		for (int i = 1; i < 7; i++) {
			System.out.println("EVALUATION for dice = " + i);
			posibleMoves[i-1] = evaluate(currentPos, i);
		}
		System.out.println("\nEnd of the evaluation \n");
		double bestMove = posibleMoves[0];
		int bestDice = 1;
		for (int i = 1; i < 6; i++) {
			if (posibleMoves[i] > bestMove) {
				bestMove = posibleMoves[i];
				bestDice = i + 1;
			}

		}
		int[] moveResults = new int[5];
		moveResults = move(currentPos, bestDice);
		path.add(moveResults);
		System.out.println("MoveResults: ");
		for(int r=0; r<moveResults.length; r++)
		{
			System.out.print(moveResults[r] + ", ");
		}
		
		System.out.println();
		return moveResults[0];
	}

	public void statistics() {
		int allSnakeBites = 0;
		int allLaddersUsed = 0;
		int allRedApplesEaten = 0;
		int allBlackApplesEaten = 0;
		System.out.println("Path size: " + path.size());
		for (int i = 0; i < path.size(); i++) {
			int[] info = new int[5];
			info = path.get(i);
			System.out.println("ROUND " + i);
			System.out.println(
					"New position: " + info[0] + "\n" + "Snake bites: " + info[1] + "\n" + "Ladders climbed: " + info[2]
							+ "\n" + "Red apples eaten: " + info[3] + "\n" + "Black apples eaten: " + info[4] + "\n");
			allSnakeBites += info[1];
			allLaddersUsed += info[2];
			allRedApplesEaten += info[3];
			allBlackApplesEaten += info[4];

		}
		System.out.println("Total snake bites: " + allSnakeBites);
		System.out.println("Total ladders used: " + allLaddersUsed);
		System.out.println("Total red apples eaten: " + allRedApplesEaten);
		System.out.println("Total black apples eaten: " + allBlackApplesEaten);

	}

}
