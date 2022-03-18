

import java.util.Scanner;



public class Main {
	/*
	1. Move diagonally 1 space
	2. Move diagonally over opponets goti onto an empty space
	3. Move diagonally over opponets goti onto an empty space and again move diagonally over opponents goti to empty space 
	4. If there is a jump you must jump r
	*/


	public static void main(String[] args) {

		//Display input details for the user
		System.out.println("Enter your moves in the form \"startRow,startColumn;endRow,endColumn\" "
							+ "OR"
							+"Enter your moves in the form \"xStart,yStsart;xEnd,yEnd\" "
							+ "where bottom left is (0,0)" 
							);



		String redName = "red";
		String whiteName = "white";
		// Create the two players for this board.
		Player redPlayer = new Player(redName, TileState.RED);
		Player whitePlayer = new Player(whiteName, TileState.WHITE);

		// Create and display the game board.
		Board board = new Board(redPlayer, whitePlayer);
		board.displayBoard();

		// Begin prompting players for moves until the game ends.
		while (board.isGameRunning()) {
			Scanner scanner = new Scanner(System.in);
			board.promptPlayerMove(scanner);
			board.displayBoard();
		}

		// Close resources.
		//scanner.close();
	}
}
