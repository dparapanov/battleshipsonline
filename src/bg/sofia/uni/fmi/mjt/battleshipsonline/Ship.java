package bg.sofia.uni.fmi.mjt.battleshipsonline;

import java.io.PrintWriter;

public class Ship {
	private String name;
	private int lives;
	private boolean isSunk;
	private Position position;

	public Ship(String name, int lives) {
		this.name = name;
		this.lives = lives;
		isSunk = false;
		position = new Position();
	} 

	public String getName() {
		return name;
	}

	public int getLives() {
		return lives;
	}

	public boolean getSunk() {
		return isSunk;
	}

	public Position getPosition() {
		return position;
	}

	public void hit(PrintWriter pw) {
		if (lives == 1) {
			pw.printf("Perfect shot! You have destroyed %s\n", name);
			lives--;
		} else if (lives > 0) {
			pw.printf("Good shot! The %s was hit\n", name);
			lives--;
		} else {
			pw.printf("Ship %s is destroyed\n", name);
		}
	}

	public void setSunk(boolean isSunk) {
		this.isSunk = isSunk;
	}

	public void setPosition(Position pos) {
		this.position = pos;
	}

	public Status getState() {
		if (lives == 0) {
			return Status.DESTROYED;
		} else if (lives == position.getPoints().size()) {
			return Status.NO_HIT;
		} else {
			return Status.HIT;
		}
	}

}
