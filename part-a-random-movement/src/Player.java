public class Player {
	int playerId;
	String name;
	int score;
	Board board;

	public Player() {
		playerId = 0;
		name = "";
		score = 0;
		board = new Board();
	}

	public Player(int id, String n, int s, Board b) {
		playerId = id;
		name = n;
		score = s;
		board = b;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	// the player eats the apples on the given tile: a red apple adds its points, a black one subtracts them;
	// the counters of the move (result[3] red apples, result[4] black apples) are updated
	private void eatApples(int tile, int[] result) {
		for (int i = 0; i < board.getApples().length; i++) {
			Apple apple = board.getApples()[i];
			if (tile == apple.getAppleTileId() && apple.getColor().equals("red")) {
				score += apple.getPoints();
				result[3]++;
				if (apple.getPoints() != 0) {
					System.out.println("Nice!You ate a red apple " + name + " .You got " + apple.getPoints() + " now!Your current score is " + score);
				}
				apple.setPoints(0);
			} else if (tile == apple.getAppleTileId() && apple.getColor().equals("black")) {
				score -= apple.getPoints();
				result[4]++;
				if (apple.getPoints() != 0) {
					System.out.println("Oh!You ate a black apple " + name + " .Now you lost " + apple.getPoints() + " points.Your current score is " + score);
				}
				apple.setPoints(0);
			}
		}
	}

	// moves the player from tile id by die steps and returns an array with the new tile,
	// the snake bites, the ladders used, and the red and the black apples eaten in this move
	public int[] move(int id, int die) {
		int[] result = new int[5];
		int newId;

		// the player cannot go past the last tile
		if (id + die >= board.getN() * board.getM()) {
			newId = board.getN() * board.getM();
		} else {
			newId = id + die;
		}

		// first we check whether the player ate an apple or not and gains or loses points accordingly
		eatApples(newId, result);

		// one move can be a chain of ladders and snakes: we repeat as long as something moved the player
		boolean moved = true;
		while (moved) {
			moved = false;

			// the player climbs the ladder if it is not broken (a ladder can be used only once)
			// and we then check whether the tile where he ended up had an apple
			for (int p = 0; p < board.getLadders().length; p++) {
				if (board.getLadders()[p].getUpStepId() == newId && !board.getLadders()[p].getBroken()) {
					newId = board.getLadders()[p].getDownStepId();
					board.getLadders()[p].setBroken(true);
					result[2]++;
					System.out.println("Good!You climbed a ladder " + name + " ,keep up!");
					moved = true;
					eatApples(newId, result);
				}
			}

			// exactly the same with the snakes: he finds a snake, goes down to the tail, we check whether there is an apple there
			for (int y = 0; y < board.getSnakes().length; y++) {
				if (board.getSnakes()[y].getHeadId() == newId) {
					newId = board.getSnakes()[y].getTailId();
					result[1]++;
					System.out.println("Woah!A snake,well you went down..." + name);
					moved = true;
					eatApples(newId, result);
				}
			}
		}

		result[0] = newId;
		return result;
	}

}
