import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class Game {

	public int round;

	// the game ends after this round at the latest (the assignment suggests, for example, round 100)
	static final int MAX_ROUNDS = 100;

	Game() {
		round = 0;
	}

	Game(int l) {
		round = l;
	}

	public void setRound(int l) {
		round = l;
	}

	public int getRound() {
		return round;
	}

	private static Map<Integer, Integer> sortByValue(Map<Integer, Integer> unsortedMap) {

		LinkedList<Map.Entry<Integer, Integer>> list = new LinkedList<Map.Entry<Integer, Integer>>(
				unsortedMap.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
			public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<Integer, Integer> sortedMap = new LinkedHashMap<Integer, Integer>();
		for (Map.Entry<Integer, Integer> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}

	Map<Integer, Integer> setTurns(ArrayList<Player> players) {
		int randomdice;
		Map<Integer, Integer> map = new HashMap<Integer, Integer>();
		for (int i = 0; i < players.size(); i++) {
			randomdice = (int) (Math.random() * 6 + 1);
			Player p = players.get(i);
			map.put(p.getPlayerId(), randomdice);
		}
		Map<Integer, Integer> sortedMap = sortByValue(map);
		return sortedMap;
	}

	public static void main(String[] args) {
		int M = 10;
		int N = 20;
		Game game = new Game();
		Board gameBoard = new Board(M, N, 3, 3, 6);
		gameBoard.createBoard();
		gameBoard.createElementBoard();
		ArrayList<Player> players = new ArrayList<Player>(2);

		MinMaxPlayer playerZero = new MinMaxPlayer(0, "player 0", 0, gameBoard);
		Player playersOne = new Player(1, "player 1", 0, gameBoard);

		players.add(playerZero);
		players.add(playersOne);
		double[] endResults = new double[players.size()];
		int[] currentPosition = new int[players.size()];
		int newPosition = 0;
		int finisher = -1; // index of the player who reached the last tile

		for (int i = 0; i < players.size(); i++) {
			currentPosition[i] = 0;
		}

		Map<Integer, Integer> turns = new HashMap<Integer, Integer>();
		turns = game.setTurns(players);

		for (;;) {
			game.round++;
			System.out.println("Round " + game.round);

			for (Map.Entry<Integer, Integer> entry : turns.entrySet()) {
				for (int i = 0; i < players.size(); i++) {
					if (entry.getKey() == players.get(i).getPlayerId()) {
						if (i == 0)
							newPosition = playerZero.getNextMove(currentPosition[i],currentPosition[i+1], gameBoard);
						else {
							int die = 1 + (int) (Math.random() * 6);
							newPosition = players.get(i).move(currentPosition[i], die, gameBoard)[0];
						}
						if (newPosition >= N * M) {
							currentPosition[i] = N * M;
							finisher = i;
							break;
						}
						currentPosition[i] = newPosition;
						System.out.println(players.get(i).getName() + " moved, his current position is "
								+ currentPosition[i] + " and his current points " + players.get(i).getScore());
					}
				}
				if (newPosition >= N * M) {
					System.out.println("Someone reached the top");
					break;

				}
				System.out.println();
			}
			
			if (newPosition >= N * M) {
				System.out.println("Game Ended!");
				break;

			}
			if (game.round >= MAX_ROUNDS) {
				System.out.println("Round limit reached");
				break;
			}
			System.out.println();
		}
		System.out.println();
		// the winner has the best combined value of position and points, with the weights of the objective function
		for (int j = 0; j < players.size(); j++) {
			endResults[j] = (currentPosition[j] * 0.6) + (0.4 * players.get(j).getScore());
		}

		int winnerindex = 0;
		double max = endResults[0];
		for (int k = 0; k < players.size(); k++) {
			if (endResults[k] > max) {
				max = endResults[k];
				winnerindex = k;

			}

		}
		// on a tie the player who reached the last tile wins
		if (finisher >= 0 && endResults[finisher] == max) {
			winnerindex = finisher;
		}
		
		System.out.println("After " + game.getRound() + " rounds ");
		for (int g = 0; g < players.size(); g++) {
			System.out.println(players.get(g).getName() + " has done " + currentPosition[g] +
					" steps and has gained " + players.get(g).getScore() 
					+ " points, so his final score is " + endResults[g]);			
		}
		System.out.println("So the WINNER is " + players.get(winnerindex).getName() + "\n");
		System.out.println("The statistics of the player that used the MinMax algorithm: ");
		playerZero.statistics();

	}
}
