
public class Player {
	int playerId;
	String name;
	int score;
	Board board;
	
	public Player() {
		playerId = 0;
		name = "";
		score = 0;
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

	public int[] move(int id, int die) {
		int newPosition = id + die;
		boolean flag;
		int[] result = new int[5];
		for (int i = 0; i < result.length; i++) {
			result[i] = 0;
		}
		
		do {
			flag = false;
			// check for snake' s head
			for (int j = 0; j < board.getSnakes().length ; j++) {
				 if (board.getSnakes()[j].getHeadId() == newPosition) {
					 flag = true;
					 newPosition = board.getSnakes()[j].getTailId();
					 result[1]++;
					 System.out.println(name + " was bitten by a snake");
					 break;
				 }
			 }
			
			// check for ladder's downstep 
			for (int j = 0; j < board.getLadders().length ; j++) {
				 if (board.getLadders()[j].getDownStepId() == newPosition) {
					 if (board.getLadders()[j].getBroken() == false) {
						 flag = true;
						 newPosition = board.getLadders()[j].getUpStepId();
						 result[2]++;
						 System.out.println(name + " climbed a ladder");
						 board.getLadders()[j].setBroken(true);
						 break;
					 }
				 }
			 }
			
			// check for apple
			for (int j = 0; j < board.getApples().length ; j++) {
				 if (board.getApples()[j].getAppleTileId() == newPosition) {
					 if (board.getApples()[j].getColor() == "red"){
					     score += board.getApples()[j].getPoints(); 
					     result[3]++;
					 }else {
						 score -= board.getApples()[j].getPoints();
						 result[4]++;
					 }
					 board.getApples()[j].setPoints(0); 
					 System.out.println(name + " ate a " + board.getApples()[j].getColor() + " apple");
					 break;
				 }
			 }
		} while(flag);

		result[0] = newPosition;
		return(result);
	}
	
}
