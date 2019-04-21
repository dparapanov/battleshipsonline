package bg.sofia.uni.fmi.mjt.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import bg.sofia.uni.fmi.mjt.battleshipsonline.Constants;

public class CommandValidator {

	PrintWriter writeToServer;
	Socket socket;

	public void validate(String input) {
		String[] tokens = input.strip().split(Constants.SPACE_SPLITTER);
		String command = tokens[0];
		if (socket != null && socket.isClosed()) {
			if (command.equals(Constants.CONNECT_COMMAND) && tokens.length == Constants.MAXIMUM_COMMAND_LENGTH) {
				String username = tokens[1];
				connect(username);
			} else {
				System.out.println("You have been disconnected! Try to connect again with valid connect command!");
			}
		} else if (command.equals(Constants.CONNECT_COMMAND) && socket == null) {
			if (tokens.length == Constants.MAXIMUM_COMMAND_LENGTH) {
				String username = tokens[1];
				connect(username);
			} else {
				System.out.println("Invalid command to connect");
			}
		} else if (command.equals(Constants.DISCONNECT_COMMAND)) {
			if (socket == null || socket.isClosed()) {
				System.out.println("=> cannot disconnect, try to connect first");
			} else {
				writeToServer.println(input);
			}
		} else if (socket != null) {
			writeToServer.println(input);
		} else {
			System.out.println(input);
		}
	}

	private void connect(String username) {
		try {
			socket = new Socket("localhost", Constants.PORT);
			writeToServer = new PrintWriter(socket.getOutputStream(), true);
			System.out.println( 
					"=> trying to connect to server running on localhost:" + Constants.PORT + " as " + username);
			writeToServer.println(username);
			ClientRunnable clientRunnable = new ClientRunnable(socket);
			new Thread(clientRunnable).start();
		} catch (IOException e) {
			System.out.println("Cannot connect!");
			System.out.println(e.getMessage());
		}
	}
}
