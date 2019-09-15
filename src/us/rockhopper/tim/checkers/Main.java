package us.rockhopper.tim.checkers;

import java.util.Scanner;

/**
 * The main entrypoint for our checkers program.
 * 
 * @author Tim Clancy
 * @version 1.0.0
 * @date 10.5.2017
 */
public class Main {

	private final static String INTRO = "Welcome to checkers! "
			+ "Enter your moves in the form \"a,b;c,d\" "
			+ "to move the piece located at cell (a,b) on the board to cell "
			+ "(c,d). The bottom-left cell is (0,0)!";

	public static void main(String[] args) {

		// Display our game's introduction.
		System.out.println(INTRO);

		// Prompt to ask for the two players.
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please enter the name of the RED player: ");
		String redName = scanner.nextLine();
		System.out.println("Please enter the name of the WHITE player: ");
		String whiteName = scanner.nextLine();

		// Create the two players for this board.
		Player redPlayer = new Player(redName, TileState.RED);
		Player whitePlayer = new Player(whiteName, TileState.WHITE);

		// Create and display the game board.
		Board board = new Board(redPlayer, whitePlayer);
		board.displayBoard();

		// Begin prompting players for moves until the game ends.
		while (board.isGameRunning()) {
			board.promptPlayerMove(scanner);
			board.displayBoard();
		}

		// Close resources.
		scanner.close();
	}
}
