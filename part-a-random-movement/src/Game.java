import java.util.Random;

public class Game {

	public int round;

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

	public static void main(String[] args) {
		Game game = new Game();
		int whoWonFirst1 = 0;
		int whoWonFirst2 = 0;

		Random r = new Random();
		int player1FirstRoll = 1 + r.nextInt(6);
		int player2FirstRoll = 1 + r.nextInt(6);
		Board b = new Board(20, 10, 3, 3, 6);
		b.createBoard();
		Player player1 = new Player(1, "Player 1", 0, b);
		Player player2 = new Player(2, "Player 2", 0, b);
		System.out.println("Welcome to snakes and ladders\t" + player1.getName() + " and " + player2.getName());
		System.out.println("This is your board!\tGood luck!	" + "\t\t");
		for (int i = 0; i < b.getN(); i++) {
			System.out.println();
			for (int j = 0; j < b.getM(); j++) {
				System.out.print(b.getTiles()[i][j] + "\t");
			}
		}
		System.out.println("\t\t\t\t");
		b.createElementBoard();
		System.out.println(player1.getName() + " 's first roll was " + player1FirstRoll);
		System.out.println(player2.getName() + " 's first roll was " + player2FirstRoll);

		// the player with the higher first roll plays first, on a tie the second player goes first
		Player first;
		Player second;
		if (player1FirstRoll > player2FirstRoll) {
			first = player1;
			second = player2;
		} else {
			first = player2;
			second = player1;
		}
		System.out.println("So " + first.getName() + " goes first");

		// I see who plays first and makes a move, then the other one moves too and
		// the process continues until one of them reaches the last tile;
		// each round is played to the end, so both can reach the last tile in the same round
		int lastTile = b.getN() * b.getM();
		int firstId = first.move(1, r.nextInt(6) + 1)[0];
		int secondId = second.move(1, r.nextInt(6) + 1)[0];
		game.setRound(game.getRound() + 1);
		while (firstId < lastTile && secondId < lastTile) {
			int die1 = r.nextInt(6) + 1;
			int die2 = r.nextInt(6) + 1;
			firstId = first.move(firstId, die1)[0];
			secondId = second.move(secondId, die2)[0];
			game.setRound(game.getRound() + 1);
		}

		// now we see who won: the winner is the one who finishes with the higher score,
		// but if they finished with the same score
		// this is where whoWonFirst is used, and I know which of them
		// is smaller and therefore who won first
		if (player1.getScore() > player2.getScore()) {
			System.out.println("Well," + player1.getName() + " wins after " + game.getRound() + " rounds with a score of " + player1.getScore() + "!" + " .Congratulations!");
			System.out.println(player2.getName() + " finished with " + player2.getScore() + " points."
					+ "Next time " + player2.getName() + "...");
		} else if (player2.getScore() > player1.getScore()) {
			System.out.println("Well," + player2.getName() + " wins after " + game.getRound() + " rounds with a score of " + player2.getScore() + " ! .Congratulations!");
			System.out.println(player1.getName() + " finished with " + player1.getScore() + " points."
					+ " Next time " + player1.getName() + "...");
		} else if (whoWonFirst1 < whoWonFirst2) {
			System.out.println("Well," + player1.getName() + " wins after " + game.getRound() + " rounds with a score of " + player1.getScore() + "!" + " .Congratulations!");
			System.out.println(player2.getName() + " finished with " + player2.getScore() + " points too but he was second."
					+ "Next time " + player2.getName() + "...");
		} else {
			System.out.println("Well," + player2.getName() + " wins after " + game.getRound() + " rounds with a score of " + player2.getScore() + " ! .Congratulations!");
			System.out.println(player1.getName() + " finished with " + player1.getScore() + " points too but he was second."
					+ " Next time " + player1.getName() + "...");
		}
	}

}
