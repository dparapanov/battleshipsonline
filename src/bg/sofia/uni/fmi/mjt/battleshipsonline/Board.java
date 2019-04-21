package bg.sofia.uni.fmi.mjt.battleshipsonline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

public class Board {
	private static final int BOARD_SIZE = 10;
	private static final String HORIZONTAL = "H";
	private static final String VERTICAL = "V";

	private List<Ship> ships;
	private Field[][] board;

	private boolean checkFreePosition(Point point, int sizeOfShip, String direction) {
		if (direction.equals(HORIZONTAL) && (point.getY() >= 0 && point.getY() <= BOARD_SIZE - sizeOfShip)) {
			for (int i = point.getY(); i < point.getY() + sizeOfShip; i++) {
				if (board[point.getX()][i].getType().equals(Constants.FIELD_TYPE_SHIP)) {
					return false;
				}
			}
			return true;
		} else if (direction.equals(VERTICAL) && (point.getX() >= 0 && point.getX() <= BOARD_SIZE - sizeOfShip)) {
			for (int i = point.getX(); i < point.getX() + sizeOfShip; i++) {
				if (board[i][point.getY()].getType().equals(Constants.FIELD_TYPE_SHIP)) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	private boolean validStartingPoint(String point, int sizeOfShip, String direction) {
		if (!validPoint(point)) {
			return false;
		}
		Point p = new Point(point.charAt(0), Integer.parseInt(point.substring(1)));

		return checkFreePosition(p, sizeOfShip, direction);

	}

	private void place(String point, Ship ship, String direction) {
		Point p = new Point(point.charAt(0), Integer.parseInt(point.substring(1)));
		ship.getPosition().setPoints(p, ship.getLives(), direction.charAt(0));
		if (direction.equals(HORIZONTAL)) {
			for (int i = p.getY(); i < p.getY() + ship.getLives(); i++) {
				board[p.getX()][i] = new ShipField(ship);
			}
		} else {
			for (int i = p.getX(); i < p.getX() + ship.getLives(); i++) {
				board[i][p.getY()] = new ShipField(ship);
			}
		}
	}

	private void createShip(String name, int size, int lives) {
		for (int i = 0; i < size; i++) {
			ships.add(new Ship(name, lives));
		}
	}

	public Board() {
		this.board = new Field[BOARD_SIZE][BOARD_SIZE];
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				board[i][j] = new WaterField();
			}
		}
		ships = new LinkedList<>();
		createShip(Constants.CARRIER_NAME, Constants.CARRIER_NUMBERS, Constants.CARRIER_LIVES);
		// createShip(Constants.BATTLESHIP_NAME, Constants.BATTLESHIP_NUMBERS,
		// Constants.BATTLESHIP_LIVES);
		createShip(Constants.BATTLESHIP_NAME, 1, Constants.BATTLESHIP_LIVES);
		// createShip(Constants.SUBMARINE_NAME, Constants.SUBMARINE_NUMBERS,
		// Constants.SUBMARINE_LIVES);
		// createShip(Constants.DESTROYER_NAME, Constants.DESTROYER_NUMBERS,
		// Constants.DESTROYER_LIVES);
		// createShip(Constants.DESTROYER_NAME, 2,
		// Constants.DESTROYER_LIVES);
	}

	public void printBoard(boolean isEnemy, PrintWriter pw1) {
		if (isEnemy) {
			pw1.println("      " + Constants.ENEMY_BOARD);
		} else {
			pw1.println("       " + Constants.YOUR_BOARD);
		}
		pw1.print("   ");
		for (int i = 1; i <= BOARD_SIZE; i++) {
			if (i == 10) {
				pw1.print(i);
			} else {
				pw1.print(i + " ");
			}
		}
		pw1.print("\n");
		pw1.print("  ");
		for (int i = 0; i < board.length; i++) {
			if (i == 0) {
				pw1.print(" ");
			}
			pw1.print("_" + " ");
		}
		pw1.print("\n");
		for (int i = 0; i < BOARD_SIZE; i++) {
			pw1.print((char) ('A' + i) + " ");
			for (int j = 0; j < BOARD_SIZE; j++) {
				if (j == BOARD_SIZE - 1) {
					pw1.print("|" + board[i][j].getIcon(isEnemy) + "|");
				} else {
					pw1.print("|" + board[i][j].getIcon(isEnemy));
				}
			}
			pw1.println();
		}
	}

	public synchronized void placeShips(PrintWriter writer, BufferedReader reader) throws IOException {
		int currentShip = 1;
		for (Ship ship : ships) {
			writer.printf("Place your ship number %d %s of size %d\n", currentShip, ship.getName(), ship.getLives());
			writer.println("Choose horizontal (H) or vertical (V): ");
			while (true) {
				String direction = reader.readLine().strip();
				if (direction.equals(HORIZONTAL) || direction.equals(VERTICAL)) {
					while (true) {
						writer.println("Please give starting point(A-J1-10)(Example: A1)");
						String startingPoint = reader.readLine().strip();
						if (validStartingPoint(startingPoint, ship.getLives(), direction)) {
							place(startingPoint, ship, direction);
							break;
						} else {
							writer.println("Invalid point, try again!");
						}
					}
					break;
				} else {

					writer.println("Invalid input! Try again!");
				}
			}
			currentShip++;
			writer.printf("You have succesfully place your %s of size %d!\n\n", ship.getName(), ship.getLives());
		}
	}

	public boolean validPoint(String point) {
		if (point.length() == 2) {
			if ((point.charAt(0) >= 'A' && point.charAt(0) <= 'J')
					&& (point.charAt(1) >= '1' && point.charAt(1) <= '9')) {
				return true;
			} else {
				return false;
			}
		} else if (point.length() == 3) {
			if ((point.charAt(0) >= 'A' && point.charAt(0) <= 'J') && (point.substring(1).equals("10"))) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public boolean hitAt(Point p, PrintWriter pw) {
		return board[p.getX()][p.getY()].shootAt(pw);
	}

	public Field[][] getGrid() {
		return board;
	}
}
