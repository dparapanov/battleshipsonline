package bg.sofia.uni.fmi.mjt.client;

import java.util.Scanner;

public class GameClient {
	public static void main(String[] args) {
		new GameClient().run();
	}

	private void run() {
		try (Scanner scanner = new Scanner(System.in)) {
			String input;
			CommandValidator validator = new CommandValidator();
			while (scanner.hasNextLine()) {
				input = scanner.nextLine();
				validator.validate(input);
			}
		}
	}
}