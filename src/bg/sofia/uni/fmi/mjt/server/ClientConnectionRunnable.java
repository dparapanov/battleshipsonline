package bg.sofia.uni.fmi.mjt.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;
import java.util.Set;

import bg.sofia.uni.fmi.mjt.battleshipsonline.Player;
import bg.sofia.uni.fmi.mjt.battleshipsonline.Point;
import bg.sofia.uni.fmi.mjt.battleshipsonline.Status;
import bg.sofia.uni.fmi.mjt.battleshipsonline.Constants;
import bg.sofia.uni.fmi.mjt.battleshipsonline.Game;
import bg.sofia.uni.fmi.mjt.battleshipsonline.GameStatus;

public class ClientConnectionRunnable implements Runnable {
	private String username;
	private Socket socket;
	private Server server;

	public ClientConnectionRunnable(String username, Socket socket, Server server) {
		this.username = username;
		this.socket = socket;
		this.server = server;
	}
 
	@Override
	public void run() {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);) {
			writer.println(Constants.AVAILABLE_COMMANDS);
			while (true) {
				writer.println(Constants.MENU_COMMAND);
				String commandInput = reader.readLine();
				processCommand(commandInput, writer, reader);
			}
		} catch (IOException e) {
			System.out.println(username + " has disconnected from server");
			filterGames();
			System.out.println(e.getMessage());
		} finally {
			server.getAllUsers().remove(username);
		}
	}

	private void filterGames() {
		for (Map.Entry<String, Game> pair : server.getAllGames().entrySet()) {
			if (!server.getAllUsers().containsKey((pair.getValue().getCreator().getName()))) {
				server.getAllGames().remove(pair.getKey());
			}
		}
	}

	private void processCommand(String commandInput, PrintWriter writer, BufferedReader reader) throws IOException {

		if (commandInput != null) {
			String[] tokens = commandInput.strip().split(Constants.SPACE_SPLITTER);
			String command = tokens[0];

			switch (command) {
			case Constants.CREATE_GAME_COMMAND:
				if (tokens.length == 2) {
					String gameName = tokens[1];
					createGame(gameName, writer, reader);
				} else {
					writer.println("Invalid create-game command");
				}
				break;
			case Constants.JOIN_GAME_COMMAND:
				if (tokens.length == 1) {
					joinRandomGame(writer);
				} else if (tokens.length == 2) {
					String roomName = tokens[1];
					joinGame(roomName, writer);
				} else {
					writer.println("Invalid join-game command");
				}
				break;
			case Constants.ACTIVE_PLAYERS_COMMAND:
				if (tokens.length == 1) {
					printActiveUsers(writer);
				} else {
					writer.println("Invalid command");
				}
				break;
			case Constants.DISCONNECT_COMMAND:
				if (tokens.length == 1) {
					disconnectFromServer(writer);
				} else {
					writer.println("Invalid disconnect command");
				}
				break;
			case Constants.HELP_GAME_COMMAND:
				if (tokens.length == 1) {
					getAvailableCommands(writer);
				} else {
					writer.println("Invalid help command");
				}
				break;
			case Constants.LIST_GAMES_COMMAND:
				if (tokens.length == 1) {
					listGames(writer);
				} else {
					writer.println("Invalid list-users command");
				}
				break;
			default:
				writer.println(command + " is not a valid operation!");
				break;
			}
		}
	}

	private void joinGame(String gameName, PrintWriter writer) throws IOException {
		Player opponent = null;
		if (server.getAllGames().containsKey(gameName)
				&& server.getAllGames().get(gameName).getStatus().equals(GameStatus.PENDING)) {
			server.getAllGames().get(gameName).setOpponent(server.getUser(username));
			opponent = server.getAllGames().get(gameName).getCreator();
			server.getAllGames().get(gameName).setStatus(GameStatus.IN_PROGRESS);
			PrintWriter pw = new PrintWriter(opponent.getSocket().getOutputStream(), true);
			pw.println("PLAYERS: 2/2, type \"start\" to start the game");

			writer.println("Waiting for creator to Enter the Game!");
			if (server.getAllGames().get(gameName) != null) {
				server.getUser(username).changePlayingStatus();
				playing(gameName, server.getUser(username), opponent);
				server.getUser(username).resetStats();
			} else {
				writer.println("The room was closed! Join another!");
			}

		} else {
			writer.println("The room is not available!");
		}
	}

	private void getAvailableCommands(PrintWriter writer) {
		writer.println(Constants.AVAILABLE_COMMANDS);
	}

	private void listGames(PrintWriter writer) {
		String leftAlignFormat = "| %-8s | %-10s | %-11s | %-7s |%n";

		writer.format("+----------+------------+-------------+---------+%n");
		writer.format("| NAME     | CREATOR    | STATUS      | PLAYERS |%n");
		writer.format("+----------+------------+-------------+---------+%n");
		String numOfPlayers = null;
		for (Map.Entry<String, Game> pair : server.getAllGames().entrySet()) {

			if (pair.getValue().getStatus().equals(GameStatus.IN_PROGRESS)) {
				numOfPlayers = "2/2";
			} else {
				numOfPlayers = "1/2";
			}

			writer.format(leftAlignFormat, pair.getKey(), pair.getValue().getCreator().getName(),
					pair.getValue().getStatus().toString(), numOfPlayers);
		}

		writer.format("+----------+------------+-------------+---------+%n");
	}

	private void joinRandomGame(PrintWriter writer) throws IOException {
		Player opponent = null;
		String serverName = null;
		for (Map.Entry<String, Game> pair : server.getAllGames().entrySet()) {
			if (pair.getValue().getStatus().equals(GameStatus.PENDING)) {
				server.getAllGames().get(pair.getKey()).setOpponent(server.getUser(username));
				opponent = pair.getValue().getCreator();
				serverName = pair.getKey();
				server.getAllGames().get(serverName).setStatus(GameStatus.IN_PROGRESS);
				break;
			}
		}
		if (opponent != null) {
			PrintWriter pw = new PrintWriter(opponent.getSocket().getOutputStream(), true);
			pw.println("PLAYERS: 2/2, type \"start\" to start the game");

			writer.println("Waiting for creator to Enter the Game!");
			if (server.getAllGames().get(serverName) != null) {
				server.getUser(username).changePlayingStatus();
				playing(serverName, server.getUser(username), opponent);
				server.getUser(username).resetStats();
			} else {
				writer.println("The room was closed! Join another!");
			}

		} else {
			writer.println("There is not available games!");
		}
	}

	private void createGame(String gameName, PrintWriter writer, BufferedReader reader) throws IOException {
		Game game = new Game(gameName, server.getUser(username));
		server.getAllGames().put(gameName, game);
		writer.println("Waiting for other player to connect!");
		while (true) {
			String command = reader.readLine();
			if (server.getAllGames().get(gameName).getOpponent() != null) {
				if (command.equals(Constants.START_COMMAND)) {
					server.getAllGames().get(gameName).setStatus(GameStatus.IN_PROGRESS);
					server.getUser(username).changePlayingStatus();
					server.getUser(username).changeTurn();
					playing(gameName, server.getUser(username), server.getAllGames().get(gameName).getOpponent());
					break;
				} else {
					writer.println("You have to press 'start' command!");
				}
			} else if (command.equals(Constants.EXIT_COMMAND)) {
				writer.println("You have exit the game!");
				server.getAllGames().remove(gameName);
				break;
			} else {
				writer.println("Still waiting for other player!");
			}
		}
		server.getUser(username).resetStats();
	}

	private synchronized void playing(String gameName, Player currentPlayer, Player opponent) throws IOException {
			PrintWriter pw1 = new PrintWriter(currentPlayer.getSocket().getOutputStream(), true);
			BufferedReader re1 = new BufferedReader(new InputStreamReader(currentPlayer.getSocket().getInputStream()));
			PrintWriter pw2 = new PrintWriter(opponent.getSocket().getOutputStream(), true);

			placeShipsOnBoard(currentPlayer, opponent, pw1, re1);

			if (server.getAllUsers().containsKey(opponent.getName())) {
				pw1.println("You have succesfully place your ships!");
				while (currentPlayer.isOnPlay()) {
					if (opponent.getSocket().isClosed()) {
						currentPlayer.getBoard().printBoard(false, pw1);
						opponent.getBoard().printBoard(true, pw1);
						pw1.println("You have won!");
						pw1.println("The opponent has left the game!");
						if (server.getAllUsers().containsKey(opponent.getName())) {
							server.getAllUsers().remove(opponent.getName());
						}
						break;
					} else if (!opponent.isOnPlay()) {
						currentPlayer.getBoard().printBoard(false, pw1);
						opponent.getBoard().printBoard(true, pw1);
						pw1.println("The game is over! You have lost!");
						currentPlayer.changePlayingStatus();
						break;
					} else if (opponent.getLive() == 0) {
						currentPlayer.getBoard().printBoard(false, pw1);
						opponent.getBoard().printBoard(true, pw1);
						pw1.println("Congratulations! You have won the game");
						currentPlayer.changePlayingStatus();
						break;
					} else {
						if (currentPlayer.isInTurn()) {
							currentPlayer.getBoard().printBoard(false, pw1);
							opponent.getBoard().printBoard(true, pw1);
							if (opponent.getHistory().isEmpty()) {
								pw1.println("\nYou are first to shoot! Enter your turn: ");
							} else {
								pw1.printf("%s's last turn was %s\n", opponent.getName(),
										opponent.getLastMove().getPointToString());
								pw1.println(Constants.TURN_COMMAND);
							}

							String point = re1.readLine();
							while (true) {
								if (opponent.getBoard().validPoint(point)) {
									Point p = new Point(point.charAt(0), Integer.parseInt(point.substring(1)));
									if (opponent.getBoard().hitAt(p, pw1)) {
										if (opponent.getBoard().getGrid()[p.getX()][p.getY()].getStatus()
												.equals(Status.HIT_WATER)) {
											currentPlayer.getHistory().add(p);
											currentPlayer.changeTurn();
											opponent.changeTurn();
											break;
										} else {
											pw2.println("You have been hit");
											currentPlayer.getHistory().add(p);
											opponent.decrementLive();
											currentPlayer.changeTurn();
											opponent.changeTurn();
											break;
										}

									} else {
										point = re1.readLine();
									}
								} else {
									pw1.println("Invalid point, try again!");
									point = re1.readLine();
								}

							}
						} else {
							try {
								pw1.println("Waiting for opponent to shoot!\n");
								while (!currentPlayer.isInTurn() && opponent.isOnPlay()) {
									wait(1000);
									if (!server.getAllUsers().containsKey(opponent.getName())) {
										break;
									}
								}
								notifyAll();
								if (opponent.getSocket().isClosed()) {
									currentPlayer.getBoard().printBoard(false, pw1);
									opponent.getBoard().printBoard(true, pw1);
									pw1.println("The opponent has left the game!");
									break;
								}
							} catch (InterruptedException e) {
								System.out.println(e.getMessage());
								break;
							}
						}
					}
				}
			}
			server.getAllGames().remove(gameName);
	}

	private synchronized void placeShipsOnBoard(Player currentPlayer, Player opponent, PrintWriter pw1, BufferedReader re1)
			throws IOException {
			String name = opponent.getName();
			try {
				while (!opponent.isOnPlay()) {
					wait(1000);
					if (!server.getAllUsers().containsKey(name)) {
						break;
					}
				}
				notifyAll();
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}

			if (server.getAllUsers().containsKey(name)) {
				pw1.println("The game has started!");
				currentPlayer.getBoard().printBoard(false, pw1);
				while (!(currentPlayer.isShipsOnBoard() && opponent.isShipsOnBoard())) {
					if (!currentPlayer.isShipsOnBoard()) {
						currentPlayer.getBoard().placeShips(pw1, re1);
						currentPlayer.changeShipStatus();
					} else {
						try {
							pw1.println("Waiting for opponent to place ships!\n");
							while (!opponent.isShipsOnBoard()) {
								wait(1000);
								if (opponent.getSocket().isClosed()) {
									break;
								}
							}
							notifyAll();
							if (opponent.getSocket().isClosed()) {
								pw1.println("The opponent has left the game!");
								break;
							}
						} catch (InterruptedException e) {
							System.out.println(e.getMessage());
							break;
						}
					}
				}
			} else {
				pw1.println("The opponent has left the game!");
			}
	}

	private void disconnectFromServer(PrintWriter writer) throws IOException {
		server.deleteUser(username);
		writer.println(Constants.DISCONNECT_COMMAND);
		socket.close();
	}

	private void printActiveUsers(PrintWriter writer) {
		Set<String> activeUsers = server.getActiveUsers();
		if (activeUsers.size() == 1) {
			writer.println("Nobody else is online now.");
		} else {
			activeUsers.stream().filter(user -> user != username).forEach(user -> writer.println(user));
		}
	}
}
