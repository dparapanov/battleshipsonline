package bg.sofia.uni.fmi.mjt.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import bg.sofia.uni.fmi.mjt.battleshipsonline.Constants;

public class ClientRunnable implements Runnable {
	private Socket socket;

	public ClientRunnable(Socket socket) {
		this.socket = socket;
	}

	@Override
	public void run() {
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);) {
			String input;
			while (true) {
				if (socket.isClosed()) {
					System.out.println("You have been disconnected!");
					return;
				} else {

					input = reader.readLine();
					if (input != null) {
						if (input.equals(Constants.DISCONNECT_COMMAND)) {
							socket.close();
						} else if (input.equals(Constants.MENU_COMMAND)) {
							System.out.print("menu> ");
						} else if (input.equals(Constants.TURN_COMMAND)) {
							System.out.print("Enter your turn: ");
						} else {
							System.out.println(input);
						}
					} else {
						System.out.println("Invalid Input error");
					}
				}
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
}
