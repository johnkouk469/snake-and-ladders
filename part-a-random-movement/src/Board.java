import java.util.Random;

public class Board {
	int N, M;
	int[][] tiles;
	Snake[] snakes;
	Ladder[] ladders;
	Apple[] apples;

	public Board() {
		N = 0;
		M = 0;
		tiles = new int[N][M];
		snakes = new Snake[0];
		ladders = new Ladder[0];
		apples = new Apple[0];
	}

	// constructor that takes a Board object as its argument
	public Board(Board b) {
		this.N = b.N;
		this.M = b.M;
		this.tiles = b.getTiles();
		this.snakes = new Snake[b.getSnakes().length];
		for (int i = 0; i < b.getSnakes().length; i++) {
			this.snakes[i] = new Snake(b.getSnakes()[i]);
		}
		this.ladders = new Ladder[b.getLadders().length];
		for (int i = 0; i < b.getLadders().length; i++) {
			this.ladders[i] = new Ladder(b.getLadders()[i]);
		}
		this.apples = new Apple[b.getApples().length];
		for (int i = 0; i < b.getApples().length; i++) {
			this.apples[i] = new Apple(b.getApples()[i]);
		}
	}

	public Board(int n, int m, int s, int l, int a) {
		N = n;
		M = m;
		tiles = new int[n][m];
		snakes = new Snake[s];
		for (int i = 0; i < snakes.length; i++) {
			snakes[i] = new Snake();
		}
		ladders = new Ladder[l];
		for (int i = 0; i < ladders.length; i++) {
			ladders[i] = new Ladder();
		}
		apples = new Apple[a];
		for (int i = 0; i < apples.length; i++) {
			apples[i] = new Apple();
		}
	}

	public int getN() {
		return N;
	}

	public int getM() {
		return M;
	}

	public int[][] getTiles() {
		return tiles;
	}

	public Snake[] getSnakes() {
		return snakes;
	}

	public Ladder[] getLadders() {
		return ladders;
	}

	public Apple[] getApples() {
		return apples;
	}

	public void setN(int n) {
		N = n;
	}

	public void setM(int m) {
		M = m;
	}

	public void setTiles(int[][] t) {
		tiles = t;
	}

	public void setSnakes(Snake[] s) {
		snakes = s;
	}

	public void setLadders(Ladder[] l) {
		ladders = l;
	}

	public void setApples(Apple[] a) {
		apples = a;
	}

	public void createBoard() {
		Random random = new Random();

		// first we build the tiles array with the ids of the tiles:
		// the bottom row runs from left to right, the next one from right to left, and so on
		for (int i = N - 1; i >= 0; i--) {
			int rowFromBottom = N - 1 - i;
			for (int j = 0; j < M; j++) {
				if (rowFromBottom % 2 == 0) {
					tiles[i][j] = rowFromBottom * M + j + 1;
				} else {
					tiles[i][M - 1 - j] = rowFromBottom * M + j + 1;
				}
			}
		}

		// snakes: we give each snake its id and then a random tile of the board for its tail
		for (int k = 0; k < snakes.length; k++) {
			snakes[k].setSnakeId(k);
			snakes[k].setTailId(1 + random.nextInt((N - 1) * M));
		}

		// for every tile of the board we look for the tail of each snake (which cannot be on the top level)
		// and give the head any random tile on a level above the tail,
		// and we check that each new head value does not coincide with the head of another snake
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				for (int k = 0; k < snakes.length; k++) {
					if (tiles[i][j] == snakes[k].getTailId()) {
						snakes[k].setHeadId(drawHeadAbove(random, i));
						int c = 1;
						while (c != 0) {
							c = 0;
							snakes[k].setHeadId(drawHeadAbove(random, i));
							for (int r = 0; r < snakes.length; r++) {
								if (snakes[r].getHeadId() == snakes[k].getHeadId() && r != k) {
									c++;
								}
							}
							// a snake head on the last tile would make the game impossible to finish
							if (snakes[k].getHeadId() == N * M) {
								c++;
							}
						}
					}
				}
			}
		}

		// ladders: exactly the same procedure, here
		// I also check that a ladder does not start where a snake's head is
		// and AT THE SAME TIME that the bottom of a ladder does not coincide with another ladder's
		for (int k = 0; k < ladders.length; k++) {
			ladders[k].setLadderId(k);
			int c = 1;
			while (c != 0) {
				c = 0;
				ladders[k].setUpStepId(random.nextInt((N - 1) * M));
				for (int p = 0; p < ladders.length; p++) {
					if (ladders[p].getUpStepId() == ladders[k].getUpStepId() && p != k) {
						c++;
					}
				}
				for (int i = 0; i < snakes.length; i++) {
					if (snakes[i].getHeadId() == ladders[k].getUpStepId()) {
						c++;
					}
				}
			}
		}

		// same for the ladders: each time I find the bottom part
		// and then pick a random value for the top part, as long as it lies
		// above the bottom part
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				for (int k = 0; k < ladders.length; k++) {
					if (tiles[i][j] == ladders[k].getUpStepId()) {
						int w = i - 1;
						if (w > 0) {
							ladders[k].setDownStepId(tiles[i - 1][0] + random.nextInt(w * M - 1));
						} else if (N % 2 == 0) {
							ladders[k].setDownStepId(tiles[0][0] - random.nextInt(M - 1));
						} else {
							ladders[k].setDownStepId(tiles[0][0] + random.nextInt(M - 1));
						}
					}
				}
			}
		}

		// apples: we give each apple an id and a tile id and check that no apple is placed where
		// a snake's head is: if the random tile is a snake's head, we draw again until it is not;
		// we also give the apple a colour: a red apple adds points, a black one subtracts them
		for (int d = 0; d < apples.length; d++) {
			apples[d].setAppleId(d);
			apples[d].setAppleTileId(random.nextInt(N * M));
			int randomColor = random.nextInt(10);
			if (randomColor < 5) {
				apples[d].setColor("red");
			} else {
				apples[d].setColor("black");
			}
			int l = 1 + random.nextInt(3);
			apples[d].setPoints(5 * l);
			if (isSnakeHead(apples[d].getAppleTileId())) {
				int c = 1;
				while (c > 0) {
					c = 0;
					apples[d].setAppleTileId(random.nextInt(N * M));
					if (isSnakeHead(apples[d].getAppleTileId())) {
						c++;
					}
				}
			}
		}
	}

	// draws a tile above the row i, for the head of a snake (or the top of a ladder)
	private int drawHeadAbove(Random random, int i) {
		int v = i - 1;
		if (v > 0) {
			return tiles[i - 1][0] + random.nextInt((i - 1) * M - 1);
		} else if (N % 2 == 0) {
			return tiles[0][0] - random.nextInt(M - 1);
		} else {
			return tiles[0][0] + random.nextInt(M - 1);
		}
	}

	private boolean isSnakeHead(int tile) {
		for (int i = 0; i < snakes.length; i++) {
			if (snakes[i].getHeadId() == tile) {
				return true;
			}
		}
		return false;
	}

	public void createElementBoard() {
		// we first declare the three requested arrays
		String[][] elementBoardSnakes = new String[N][M];
		String[][] elementBoardLadders = new String[N][M];
		String[][] elementBoardApples = new String[N][M];
		// we fill the arrays as the assignment asks, printing them one by one at the same time
		System.out.println("snake head: SH. snake tail: ST ");
		System.out.println("ladder lower end: LD. ladder upper end: LU");
		System.out.println("red apple: AR. black apple: AB");

		// the snakes
		for (int i = 0; i < N; i++) {
			System.out.println();
			for (int j = 0; j < M; j++) {
				elementBoardSnakes[i][j] = "___";
				for (int k = 0; k < snakes.length; k++) {
					if (tiles[i][j] == snakes[k].getHeadId()) {
						elementBoardSnakes[i][j] = "SH" + snakes[k].getSnakeId();
						break;
					} else if (tiles[i][j] == snakes[k].getTailId()) {
						elementBoardSnakes[i][j] = "ST" + snakes[k].getSnakeId();
						break;
					}
				}
				System.out.print(elementBoardSnakes[i][j] + " ");
			}
		}
		System.out.println();
		System.out.println();
		System.out.println();

		// the ladders
		for (int i = 0; i < N; i++) {
			System.out.println();
			for (int j = 0; j < M; j++) {
				elementBoardLadders[i][j] = "___";
				for (int k = 0; k < ladders.length; k++) {
					if (tiles[i][j] == ladders[k].getUpStepId()) {
						elementBoardLadders[i][j] = "LD" + ladders[k].getLadderId();
						break;
					} else if (tiles[i][j] == ladders[k].getDownStepId()) {
						elementBoardLadders[i][j] = "LU" + ladders[k].getLadderId();
						break;
					}
				}
				System.out.print(elementBoardLadders[i][j] + " ");
			}
		}
		System.out.println();
		System.out.println();
		System.out.println();

		// the apples
		for (int i = 0; i < N; i++) {
			System.out.println();
			for (int j = 0; j < M; j++) {
				elementBoardApples[i][j] = "___";
				for (int k = 0; k < apples.length; k++) {
					if (tiles[i][j] == apples[k].getAppleTileId()) {
						if (apples[k].getColor().equals("black")) {
							elementBoardApples[i][j] = "AB" + apples[k].getAppleId();
						} else {
							elementBoardApples[i][j] = "AR" + apples[k].getAppleId();
						}
						break;
					}
				}
				System.out.print(elementBoardApples[i][j] + " ");
			}
		}
		System.out.println();
	}
}
