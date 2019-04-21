package bg.sofia.uni.fmi.mjt.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import bg.sofia.uni.fmi.mjt.battleshipsonline.Board;
import bg.sofia.uni.fmi.mjt.battleshipsonline.Constants;
import bg.sofia.uni.fmi.mjt.battleshipsonline.Game;
import bg.sofia.uni.fmi.mjt.battleshipsonline.Player;

public class Server {
	private Map<String, Player> users = new HashMap<>();
	private Map<String, Game> games = new HashMap<>();

	public Map<String, Player> getAllUsers() {
		synchronized (users) {
			return users;
		}
	}

	public Map<String, Game> getAllGames() {
		synchronized (users) {
			return games;
		}
	}

	public Player getUser(String username) {
		synchronized (users) {
			return users.get(username);
		}
	}

	public Set<String> getActiveUsers() {
		synchronized (users) {
			return users.keySet();
		}
	}

	public void deleteUser(String username) {
		synchronized (users) {
			users.remove(username);
		}
	}

	public void start(int port) {
		while (true) {
			try (ServerSocket serverSocket = new ServerSocket(port)) {
				System.out.printf("Server is running on localhost:%d%n", port);

				Socket socket = serverSocket.accept();

				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				String username = reader.readLine();

				PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
				
				if (users.containsKey(username)) {
					writer.println(Constants.DISCONNECT_COMMAND);
					//socket.close();
					continue;
				}
				writer.println("You have successfully connected to the server");
				System.out.println("A client connected to server " + socket.getInetAddress());
				Board board = new Board();
				Player player = new Player(board, socket, username);

				users.put(username, player);
				System.out.println(username);

				ClientConnectionRunnable runnable = new ClientConnectionRunnable(username, socket, this);
				new Thread(runnable).start();
			} catch (IOException e) {
				System.out.println("Maybe another server is running on port " + port);
				throw new RuntimeException();
			}
		}
	}

	public static void main(String[] args) {
		new Server().start(Constants.PORT);
	}
}
