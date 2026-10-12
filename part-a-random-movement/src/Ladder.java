public class Ladder {
	int ladderId;
	int upStepId; // the tile where the ladder starts
	int downStepId; // the tile where the ladder ends (higher up)
	boolean broken;

	public Ladder() {
		ladderId = 0;
		upStepId = 0;
		downStepId = 0;
		broken = false;
	}

	public Ladder(int l, int u, int d, boolean b) {
		ladderId = l;
		upStepId = u;
		downStepId = d;
		broken = b;
	}

	// constructor that takes a Ladder object as its argument
	public Ladder(Ladder l) {
		ladderId = l.ladderId;
		upStepId = l.upStepId;
		downStepId = l.downStepId;
		broken = l.broken;
	}

	public void setLadderId(int l) {
		ladderId = l;
	}

	public void setUpStepId(int u) {
		upStepId = u;
	}

	public void setDownStepId(int d) {
		downStepId = d;
	}

	public void setBroken(boolean b) {
		broken = b;
	}

	public int getLadderId() {
		return ladderId;
	}

	public int getUpStepId() {
		return upStepId;
	}

	public int getDownStepId() {
		return downStepId;
	}

	public boolean getBroken() {
		return broken;
	}
}
