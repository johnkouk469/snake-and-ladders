public class Apple {
	int appleId;
	int appleTileId;
	String color;
	int points;

	public Apple() {
		appleId = 0;
		appleTileId = 0;
		color = "";
		points = 0;
	}

	public Apple(int a, int id, String c, int p) {
		appleId = a;
		appleTileId = id;
		color = c;
		points = p;
	}

	// constructor that takes an Apple object as its argument
	public Apple(Apple a) {
		appleId = a.appleId;
		appleTileId = a.appleTileId;
		color = a.color;
		points = a.points;
	}

	public void setAppleId(int a) {
		appleId = a;
	}

	public void setAppleTileId(int id) {
		appleTileId = id;
	}

	public void setColor(String c) {
		color = c;
	}

	public void setPoints(int p) {
		points = p;
	}

	public int getAppleId() {
		return appleId;
	}

	public int getAppleTileId() {
		return appleTileId;
	}

	public String getColor() {
		return color;
	}

	public int getPoints() {
		return points;
	}
}
