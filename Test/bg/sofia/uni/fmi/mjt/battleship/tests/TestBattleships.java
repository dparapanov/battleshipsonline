package bg.sofia.uni.fmi.mjt.battleship.tests;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.net.Socket;

import org.junit.Test;

import bg.sofia.uni.fmi.mjt.battleshipsonline.Board;
import bg.sofia.uni.fmi.mjt.battleshipsonline.Constants;
import bg.sofia.uni.fmi.mjt.battleshipsonline.Field;
import bg.sofia.uni.fmi.mjt.battleshipsonline.Game;
import bg.sofia.uni.fmi.mjt.battleshipsonline.Player;
import bg.sofia.uni.fmi.mjt.battleshipsonline.Point;
import bg.sofia.uni.fmi.mjt.battleshipsonline.Ship;
import bg.sofia.uni.fmi.mjt.battleshipsonline.ShipField;
public class TestBattleships {

	@Test
	public void testValidPositionOnBoard() {
		Board board = new Board();
		String validPoint = "A1";
		Boolean expected = true;
		Boolean actual = board.validPoint(validPoint);
		assertEquals(expected, actual);
	}
 
	@Test
	public void testInvalidPositionOnBoard() {
		Board board = new Board();
		String validPoint = "A110";
		Boolean expected = false;
		Boolean actual = board.validPoint(validPoint);
		assertEquals(expected, actual);
	}
 
	@Test
	public void checkPrintBoard() throws IOException {
		Board board = new Board();
		String expected = "       YOUR BOARD\r\n" + 
				"   1 2 3 4 5 6 7 8 9 10\n" + 
				"   _ _ _ _ _ _ _ _ _ _ \n" + 
				"A |_|_|_|_|_|_|_|_|_|_|\r\n" + 
				"B |_|_|_|_|_|_|_|_|_|_|\r\n" + 
				"C |_|_|_|_|_|_|_|_|_|_|\r\n" + 
				"D |_|_|_|_|_|_|_|_|_|_|\r\n" + 
				"E |_|_|_|_|_|_|_|_|_|_|\r\n" + 
				"F |_|_|_|_|_|_|_|_|_|_|\r\n" + 
				"G |_|_|_|_|_|_|_|_|_|_|\r\n" + 
				"H |_|_|_|_|_|_|_|_|_|_|\r\n" + 
				"I |_|_|_|_|_|_|_|_|_|_|\r\n" + 
				"J |_|_|_|_|_|_|_|_|_|_|\r\n";
		Writer writer = new StringWriter();
		PrintWriter outWriter = new PrintWriter(new BufferedWriter(writer));
		board.printBoard(false, outWriter);
		outWriter.flush();
		outWriter.close();
		writer.flush();
		writer.close();
		String actual = writer.toString();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testPlacingShips() throws IOException {
		Board board = new Board();
		String expected = "Place your ship number 1 Carrier of size 5\n" + 
				"Choose horizontal (H) or vertical (V): \r\n" + 
				"Please give starting point(A-J1-10)(Example: A1)\r\n" + 
				"You have succesfully place your Carrier of size 5!\n\n" + 
				"Place your ship number 2 Battleship of size 4\n" + 
				"Choose horizontal (H) or vertical (V): \r\n" + 
				"Please give starting point(A-J1-10)(Example: A1)\r\n" + 
				"You have succesfully place your Battleship of size 4!\n\n";

		Writer writer = new StringWriter();
		PrintWriter outWriter = new PrintWriter(writer);
		
		String command = "V\r\n" + "A1\r\n" + "V\r\n" +"A6\r\n";
		InputStream stream = new ByteArrayInputStream(command.getBytes());
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		board.placeShips(outWriter, br);
		outWriter.flush();
		outWriter.close();
		writer.flush();
		writer.close();
		String actual = writer.toString();
		assertEquals(expected, actual);
	}
	
	@Test
	public void testGameClass() {
		String gameName = "my-game";
		String playerName = "user";
		Board b = new Board();
		Socket socket = null;
		Player creator = new Player(b,socket,playerName);
		Game game = new Game(gameName,creator);
		String actual = game.getName();
		assertEquals(gameName, actual);
	}
	
	@Test
	public void checkEqualsPoints() {
		Point p1 = new Point('A', 1);
		Point p2 = new Point('A', 1);
		Boolean expected = true;
		Boolean actual = p1.equals(p2);
		assertEquals(expected, actual);
	}
	
	@Test
	public void checkShipFieldHitIcon() {
		Ship ship = new Ship(Constants.CARRIER_NAME,Constants.CARRIER_LIVES);
		Field f1 = new ShipField(ship);
		char expected = f1.getIcon(false);
		char actual = '*';
		assertEquals(expected, actual);
	}
	
	@Test
	public void checkShipFieldHitIconChanged() {
		Ship ship = new Ship(Constants.CARRIER_NAME,Constants.CARRIER_LIVES);
		Field f1 = new ShipField(ship);
		Writer writer = new StringWriter();
		PrintWriter outWriter = new PrintWriter(writer);
		f1.shootAt(outWriter);
		char expected = f1.getIcon(false);
		char actual = 'X';
		assertEquals(expected, actual);
	}
}
