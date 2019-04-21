package bg.sofia.uni.fmi.mjt.battleshipsonline;

import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class Player {
	private Board board;
	private Socket socket;
	private int live;
	private boolean isShipsOnBoard;
	private boolean isMyTurn;
	private boolean isOnPlay;
	private List<Point> historyOfAttacks;
	private String name;
 
	public Player(Board board, Socket socket, String name) {
		this.board = board;
		this.socket = socket;
		live = Constants.PLAYER_LIVES;
		isShipsOnBoard = false;
		isMyTurn = false;
		isOnPlay = false;
		historyOfAttacks = new LinkedList<>();
		this.name = name;
	}

	public Board getBoard() {
		return board;
	}

	public Socket getSocket() {
		return socket;
	}

	public int getLive() {
		return live;
	}

	public void decrementLive() {
		live -= 1;
	}

	public boolean isInTurn() {
		return isMyTurn;
	}

	public boolean isShipsOnBoard() {
		return isShipsOnBoard;
	}

	public boolean isOnPlay() {
		return isOnPlay;
	}
	
	public List<Point> getHistory(){
		return historyOfAttacks;
	}

	public Point getLastMove() {
		return historyOfAttacks.get(historyOfAttacks.size() - 1);
	}
	
	public String getName() {
		return name;
	}

	public void changePlayingStatus() {
		isOnPlay = isOnPlay ? false : true;
	}
	
	public void setPlayingStatus(boolean status) {
		this.isOnPlay = status;
	}

	public void changeTurn() {
		isMyTurn = isMyTurn ? false : true;
	}

	public void changeShipStatus() {
		isShipsOnBoard = isShipsOnBoard ? false : true;
	}

	public void resetStats() {
		board = new Board();
		live = Constants.PLAYER_LIVES;
		isShipsOnBoard = false;
		isMyTurn = false;
		isOnPlay = false;
		historyOfAttacks = new LinkedList<>();
	}
}
