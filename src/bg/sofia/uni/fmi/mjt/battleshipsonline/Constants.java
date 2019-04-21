package bg.sofia.uni.fmi.mjt.battleshipsonline;

public class Constants {

	public static final int PLAYER_LIVES = 9;

	public static final String CARRIER_NAME = "Carrier";
	public static final String BATTLESHIP_NAME = "Battleship";
	public static final String SUBMARINE_NAME = "Submarine";
	public static final String DESTROYER_NAME = "Destroyer";

	public static final int CARRIER_LIVES = 5;
	public static final int BATTLESHIP_LIVES = 4;
	public static final int SUBMARINE_LIVES = 3;
	public static final int DESTROYER_LIVES = 2;

	public static final int CARRIER_NUMBERS = 1;
	public static final int BATTLESHIP_NUMBERS = 2;
	public static final int SUBMARINE_NUMBERS = 3;
	public static final int DESTROYER_NUMBERS = 4;

	public static final char SHIP_FIELD_ICON = '*';
	public static final char HIT_SHIP_ICON = 'X';
	public static final char HIT_EMPTY_FIELD_ICON = 'O';
	public static final char NO_HIT_ICON = '_';

	public static final String FIELD_TYPE_WATER = "water";
	public static final String FIELD_TYPE_SHIP = "ship";

	public static final String YOUR_BOARD = "YOUR BOARD";
	public static final String ENEMY_BOARD = "ENEMY BOARD";

	public static final int PORT = 8888;
	public static final String LOCALHOST = "localhost";
	public static final String SPACE_SPLITTER = "\\s+";
	public static final String CONNECT_COMMAND = "connect";
	public static final String DISCONNECT_COMMAND = "disconnect";
	public static final String LIST_GAMES_COMMAND = "list-games";
	public static final String CREATE_GAME_COMMAND = "create-game";
	public static final String JOIN_GAME_COMMAND = "join-game";
	public static final String SAVE_GAME_COMMAND = "save-game";
	public static final String LOAD_GAME_COMMAND = "load-game";
	public static final String DELETE_GAME_COMMAND = "delete-game";
	public static final String ACTIVE_PLAYERS_COMMAND = "active-players";
	public static final String HELP_GAME_COMMAND = "help";
	public static final String START_COMMAND = "start";
	public static final String EXIT_COMMAND = "exit";
	public static final String MENU_COMMAND = "menu";
	public static final String TURN_COMMAND = "turn";
	public static final String AVAILABLE_COMMANDS = "Available commands:\r\n" + "	create-game <game-name>\r\n"
			+ "	join-game [<game-name>]\r\n" + "	active-players\r\n" + "	help\r\n" + "	list-games\r\n"
			+ "	disconnect\r\n";

	public static final int MAXIMUM_COMMAND_LENGTH = 2;
	public static final int MINIMUM_COMMAND_LENGTH = 1;

	private Constants() {

	}
}
