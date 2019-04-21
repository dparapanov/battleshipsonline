package bg.sofia.uni.fmi.mjt.battleshipsonline;

public class Game {
	private String name;
	private Player creator;
	private Player opponent;
	private GameStatus status;
	 
	public Game(String name, Player creator) {
		this.name = name;
		this.creator = creator;
		status = GameStatus.PENDING;
	}
	
	public String getName() {
		return name;
	}
	
	public Player getCreator() {
		return creator;
	}
	
	public Player getOpponent() {
		return opponent;
	}
	
	public GameStatus getStatus() {
		return status;
	}
	
	public void setStatus(GameStatus status) {
		this.status = status;
	}

	public void setOpponent(Player opponent) {
		this.opponent = opponent;
	}
}
