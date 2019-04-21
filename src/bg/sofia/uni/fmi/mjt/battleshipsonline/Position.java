package bg.sofia.uni.fmi.mjt.battleshipsonline;

import java.util.LinkedList;
import java.util.List;

public class Position {
	private List<Point> points;

	public Position() {
		points = new LinkedList<>();
	}

	public List<Point> getPoints() {
		return points;
	} 

	public void setPoints(Point startingPoint, int size, char direction) {
		points.add(startingPoint);

		switch (direction) {
		case 'H':
			for (int i = 1; i < size; i++) {
				Point p = new Point(startingPoint.getBoardX(), startingPoint.getBoardY() + i);
				points.add(p);
			}
			break;
		case 'V':
			for (int i = 1; i < size; i++) {
				Point p = new Point((char) (startingPoint.getBoardX() + i), startingPoint.getBoardY());
				points.add(p);
			}
			break;
		}
	}

}
