import java.util.ArrayList;

public class MinMaxPlayer extends Player {

	ArrayList<int[]> path;

	public MinMaxPlayer() {
		super();
		path = new ArrayList<int[]>();
	}

	public MinMaxPlayer(int pid, String n, int sc, Board b) {
		super(pid, n, sc, b);
		path = new ArrayList<int[]>();
	}

	public MinMaxPlayer(int pid, String n, int sc, Board b, ArrayList<int[]> path) {
		super(pid, n, sc, b);
		this.path = path;
	}

	public double evaluate(int currentPos, int dice, Board b) {

		int oldScore = this.getScore();

		// the simulated move must leave the board as it was: remember the ladders and the apples
		boolean[] laddersBroken = new boolean[b.getLadders().length];
		for (int i = 0; i < laddersBroken.length; i++) {
			laddersBroken[i] = b.getLadders()[i].getBroken();
		}
		int[] applePoints = new int[b.getApples().length];
		for (int i = 0; i < applePoints.length; i++) {
			applePoints[i] = b.getApples()[i].getPoints();
		}

		int[] moveResults = new int[5];
		moveResults = move(currentPos, dice, b);

		int newid = moveResults[0];
		int steps = newid - currentPos; // calculating the steps of the player
		int gainPoints = (this.getScore() - oldScore); // and the points gained during the move
		this.setScore(oldScore); // resetting the score of the player that called the function
		for (int i = 0; i < laddersBroken.length; i++) {
			b.getLadders()[i].setBroken(laddersBroken[i]);
		}
		for (int i = 0; i < applePoints.length; i++) {
			b.getApples()[i].setPoints(applePoints[i]);
		}

		double evaluation = (double) steps * 0.6 + (double) gainPoints * 0.4;
		//System.out.println("Evaluation for dice " + dice + " and current position " + currentPos + " is " + evaluation);
		return evaluation;

	}

	public void createOpponentSubtree(Node parent, int depth, int currentPos, int opponentCurrentPos,
			double parentEval) {

		Board boardParent = parent.getNodeBoard();
		for (int i = 0; i < 6; i++) {

			double evaluation = evaluate(currentPos, i + 1, boardParent);
			
			evaluation *= parentEval;
			int[] nodeMove = new int[2];
			nodeMove[0] = currentPos;
			nodeMove[1] = i + 1;
			Node newOpponentNode = new Node(parent, depth + 1, nodeMove, boardParent, evaluation);
			parent.children.add(newOpponentNode);

		}

	}

	public void createMySubtree(Node parent, int depth, int currentPos, int opponentCurrentPos) {

		Board boardParent = parent.getNodeBoard(); //boardParent is used so that the gameBoard won't be changed during evaluation
		for (int i = 0; i < 6; i++) {

			double evaluation = evaluate(currentPos, i + 1, boardParent);
			
			int[] nodeMove = new int[2]; //we use nodeMove to store the current position and dice to the node object
			nodeMove[0] = currentPos;
			nodeMove[1] = i + 1;
			Node newNode = new Node(parent, depth + 1, nodeMove, boardParent, evaluation);
			parent.children.add(newNode);

			createOpponentSubtree(newNode, depth + 1, opponentCurrentPos, currentPos, evaluation);

		}

	}

	int chooseMinMaxMove(Node root) {
		
		
		double[] kid1 = new double[6]; //will store the evaluations of the children of the root - depth 1
		double[] kid2 = new double[6]; //will store the evaluations of the children of the children of the root - depth 2

		Node rootsChild = new Node();
		Node childsChild = new Node();
		System.out.println("Matrix of evaluations:");
		System.out.println("                             If the opponent chooses dice\n"
				+ "                             1    2    3    4    5    6");
		for (int i = 0; i < root.getChildren().size(); i++) {

			rootsChild = root.getChildren().get(i);
			System.out.print("If the player chooses dice " + (i+1) +" ");
			for (int j = 0; j < 6; j++) {
				childsChild = rootsChild.children.get(j);
				kid2[j] = childsChild.getNodeEvaluation();

				System.out.printf("%.2f ",kid2[j]);	
			}			
			//we find the minimum of the array kid2:
			double min;
			min = kid2[0];
			for (int k = 1; k < 6; k++) {
				if (kid2[k] < min) 
					min = kid2[k];
			}
			System.out.print("  min=" + min + "\n");
			//and the array kid1 gets filled with the minimum of the current kid2 array
			kid1[i] = min;
		}
		
		double max = kid1[0];
		int indexofmax = 1;
		for (int i = 1; i < 6; i++) {
			if (kid1[i] > max) {
				max = kid1[i];
				indexofmax = i + 1;
			}
		}
		
		return indexofmax;

	}

	public int getNextMove(int currentPos, int opponentCurrentPos, Board board) {
		
		System.out.println("Implementation of the MinMax Algorithm:");
		Node root = new Node();
		root.setNodeBoard(board);
		System.out.println("START of the evaluation process and the creation of the tree");
		createMySubtree(root, 0, currentPos, opponentCurrentPos);
		System.out.println("END of the evaluation process and the creation of the tree");
		int choosenDice = chooseMinMaxMove(root);
		System.out.println("After using the MinMax method " + this.getName() + " will choose dice " + choosenDice + "\n");
		int[] moveResults = new int[5];
		moveResults = move(currentPos, choosenDice, board);
		path.add(moveResults);
		return moveResults[0];
		
	}

	public void statistics() {
		
		int allSnakeBites = 0;
		int allLaddersUsed = 0;
		int allRedApplesEaten = 0;
		int allBlackApplesEaten = 0;

		for (int i = 0; i < path.size(); i++) {
			int[] info = new int[5];
			info = path.get(i);
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
