

import java.util.Scanner;



public class Main {
	/*
	1. Move diagonally 1 space
	2. Move diagonally over opponets goti onto an empty space
	3. Move diagonally over opponets goti onto an empty space and again move diagonally over opponents goti to empty space 
	4. If there is a jump you must jump 
	*/


	public static void main(String[] args) {

		// Create red and white player				
		Player redPlayer = new Player("red", CellElements.RED);
		Player whitePlayer = new Player("white", CellElements.WHITE);

		// Create and display the checkers board.
		Board board = new Board(redPlayer, whitePlayer);
		board.displayInputDetails();
		board.displayBoard();

		// Begin prompting players for moves until the game ends.
		while (board.isGameRunning()) {
			Scanner scanner = new Scanner(System.in);
			board.promptPlayerMove(scanner);
			board.displayBoard();
		}
	}
}
