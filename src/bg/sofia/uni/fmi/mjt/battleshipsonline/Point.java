package bg.sofia.uni.fmi.mjt.battleshipsonline;

import java.util.Objects;

public class Point {
	private int x;
	private int y;
	private final char getIndexX = 'A';
	private final int getIndexY = 1;

	public Point(char x, int y) throws IllegalArgumentException {
		if (!(x >= 'A' && x <= 'J') || !(y >= 1 && y <= 10)) {
			throw new IllegalArgumentException();
		} 
		this.x = x - getIndexX;
		this.y = y - getIndexY;
	}

	public int getX() {
		return x;
	}

	public char getBoardX() {
		return (char) (getIndexX + x);
	}

	public int getY() {
		return y;
	}

	public int getBoardY() {
		return getIndexY + y;
	}

	public String getPointToString() {
		String result = Character.toString(getBoardX()) + Integer.toString(getBoardY());
		return result;
	}
	
	@Override
	public boolean equals(Object o) {
	    if (this == o)
	        return true;
	    if (o == null)
	        return false;
	    if (getClass() != o.getClass())
	        return false;
	    Point point = (Point) o;
	    return Objects.equals(x, point.x)
	            && Objects.equals(y, point.y);
	}
}
