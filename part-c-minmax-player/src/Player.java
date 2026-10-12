public class Player {
	int playerId;
	String name;
	int score;
	Board b;
	
	public Player() {
		playerId = 0;
		name = "";
		score = 0;
	}
	
	public Player(int id, String n, int s, Board b) {
		playerId = id;
		name = n;
		score = s;
		b = b;
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
		return b;
	}

	public void setBoard(Board board) {
		this.b = board;
	}

	public int[] move(int id, int die, Board b) {
		int newPosition = id + die;
		boolean flag;
		int[] result = new int[5];
		for (int i = 0; i < result.length; i++) {
			result[i] = 0;
		}
		
		do {
			flag = false;
			// check for snake' s head
			for (int j = 0; j < b.getSnakes().length ; j++) {
				 if (b.getSnakes()[j].getHeadId() == newPosition) {
					 flag = true;
					 newPosition = b.getSnakes()[j].getTailId();
					 result[1]++;
					 System.out.println(name + " was bitten by a snake");
					 break;
				 }
			 }
			
			// check for ladder's downstep 
			for (int j = 0; j < b.getLadders().length ; j++) {
				 if (b.getLadders()[j].getDownStepId() == newPosition) {
					 if (b.getLadders()[j].getBroken() == false) {
						 flag = true;
						 newPosition = b.getLadders()[j].getUpStepId();
						 result[2]++;
						 System.out.println(name + " climbed a ladder");
						 b.getLadders()[j].setBroken(true);
						 break;
					 }
				 }
			 }
			
			// check for apple
			for (int j = 0; j < b.getApples().length ; j++) {
				 if (b.getApples()[j].getAppleTileId() == newPosition) {
					 if (b.getApples()[j].getColor() == "red"){
					     score += b.getApples()[j].getPoints(); 
					     result[3]++;
					 }else {
						 score -= b.getApples()[j].getPoints();
						 result[4]++;
					 }
					 b.getApples()[j].setPoints(0); 
					 System.out.println(name + " ate a " + b.getApples()[j].getColor() + " apple");
					 break;
				 }
			 }
		} while(flag);

		result[0] = newPosition;
		return(result);
	}
	
}
