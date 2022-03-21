

import javax.swing.*;
import java.net.PortUnreachableException;
import java.util.*;

/**
 * This class contains the state of the checkers board, as well as methods for
 * taking player input to simulate moves.
 * 
 * @author Tim Clancy
 * @version 1.0.0
 * @date 10.5.2017
 */
public class Board {


	
	private final int BOARD_WIDTH = 8;
	private final int BOARD_HEIGHT = 8;
	private final CellElements[][] BOARD_STATE ;

	private final Player[] PLAYERS;
	private final List<PlayerMove> CURRENT_PLAYER_MOVES;
	private List<Integer[]> jumpPositions = new ArrayList<>();
	private int CURRENT_PLAYER = 0;
	private List<PiecePosition>[] piecePositionInfoArray= new List[2];

	public Board(final Player red, final Player white) {
		BOARD_STATE = new CellElements[BOARD_WIDTH][BOARD_HEIGHT];
		PLAYERS = new Player[2];
		PLAYERS[0] = red;
		PLAYERS[1] = white;
		CURRENT_PLAYER_MOVES = new ArrayList<PlayerMove>();
		Arrays.setAll(piecePositionInfoArray, element -> new ArrayList<>());

		resetGame();
	}



		private void resetGame() {
			for(int i=0 ; i <=BOARD_HEIGHT -1;i++){
				for(int j=0 ; j <= BOARD_WIDTH-1;j++){
					if(i == 0 || i == 2){
						if(j%2 != 0){
							BOARD_STATE[j][i] = CellElements.WHITE;
							piecePositionInfoArray[0].add(new PiecePosition(j,BOARD_HEIGHT -1 - i));
						}
						else{
							BOARD_STATE[j][i] = CellElements.EMPTY;
						}
					}
					else if(i == 1){
						if(j%2 == 0){
							BOARD_STATE[j][i] = CellElements.WHITE;
							piecePositionInfoArray[0].add(new PiecePosition(j,BOARD_HEIGHT -1 - i));
						}
						else{
							BOARD_STATE[j][i] = CellElements.EMPTY;
						}
					}
					else if(i == 3 || i == 4){
						BOARD_STATE[j][i]  = CellElements.EMPTY;
					}
					else if(i == 5 || i == 7){
						if(j %2 == 0){
							BOARD_STATE[j][i] = CellElements.RED;
							piecePositionInfoArray[1].add(new PiecePosition(j,BOARD_HEIGHT -1 - i));
						}
						else{
							BOARD_STATE[j][i] = CellElements.EMPTY;
						}
					}
					else if(i == 6){
						if(j %2 != 0){
							BOARD_STATE[j][i] = CellElements.RED;
							piecePositionInfoArray[1].add(new PiecePosition(j,BOARD_HEIGHT -1 - i));
						}
						else{
							BOARD_STATE[j][i] = CellElements.EMPTY;
						}
					}


				}
			}

	
	
	}

	public void displayBoard() {
		for (int i = 0; i <= BOARD_HEIGHT-1; i++) {
		    
			System.out.print(BOARD_HEIGHT - i -1  + " " +  "|");
			for (int j = 0; j <= BOARD_WIDTH-1; j++) {
				CellElements tile = BOARD_STATE[j][i];
				System.out.print(tile.getSymbol() + "|");
			}
			System.out.print("\n");
		}
		System.out.println("   0 1 2 3 4 5 6 7");
	}

	public void displayInputDetails(){
		System.out.println("Enter your moves in the form \"startRow,startColumn;endRow,endColumn\" "
							+ "OR"
							+"Enter your moves in the form \"xStart,yStsart;xEnd,yEnd\" "
							+ "where bottom left is (0,0)" 
							);
	}


	public boolean isGameRunning() {
//		return true;

		// TODO: this is where I ran out of time.
		// I would check, for the player whose turn it is, whether any of their
		// pieces have valid moves. If not, then the game must be over.
		Player currentPlayer = PLAYERS[CURRENT_PLAYER];
		int playerNo = 0;
		int directionModifier = -1;

		if(currentPlayer.getColor() == CellElements.RED) {
			playerNo = 1;
			directionModifier = 1;
		}

		int count = 0;

		for(PiecePosition piecePosition: piecePositionInfoArray[playerNo])
		{
			int targetOneX = piecePosition.xCoordinate - 1;
			int targetTwoX = piecePosition.xCoordinate + 1;
			int targetThreeX = piecePosition.xCoordinate - 2;
			int targetFourX = piecePosition.xCoordinate + 2;
			int targetY = piecePosition.yCoordinate +  directionModifier;
			int targetJumpY = piecePosition.yCoordinate +  (2*directionModifier);


			String targetOne = piecePosition.xCoordinate  + "," + (piecePosition.yCoordinate) + ";" + targetOneX + ","
					+ targetY;
			String targetTwo = piecePosition.xCoordinate  + "," + (piecePosition.yCoordinate) + ";" + targetTwoX + ","
					+ targetY;
			String targetThree = piecePosition.xCoordinate  + "," + ( piecePosition.yCoordinate) + ";" + targetThreeX + ","
					+ targetJumpY;
			String targetFour = piecePosition.xCoordinate  + "," + (piecePosition.yCoordinate) + ";" + targetFourX + ","
					+ targetJumpY;
			Optional<PlayerMove> targetOneOption = parseValidMove(targetOne);
			Optional<PlayerMove> targetTwoOption = parseValidMove(targetTwo);
			Optional<PlayerMove> targetThreeOption = parseValidMove(targetThree);
			Optional<PlayerMove> targetFourOption = parseValidMove(targetFour);

			if ((targetOneOption.isPresent() || targetTwoOption.isPresent() || targetThreeOption.isPresent() || targetFourOption.isPresent()))
			{
				return true;
			}
		}


		return false;

	}

	public void promptPlayerMove(Scanner prompt) {

		// Tell the players who we're asking input from.
		Player currentPlayer = PLAYERS[CURRENT_PLAYER];
		CellElements playerColor = currentPlayer.getColor();
		String currentPlayerName = currentPlayer.getName();
		System.out.println("It is " + currentPlayer.getName()
				+ "'s turn. Please specify a move: ");

		// If the player has a piece that can jump, they must choose
		// that piece. Find all of the player's pieces which have
		// eligible jumps.
		jumpPositions.clear();
		int playerNo = 0;

		if(currentPlayer.getColor() == CellElements.RED)
		{
			playerNo = 1;
		}

		for(PiecePosition piecePosition: piecePositionInfoArray[playerNo])
		{
			boolean canJump = this.canPieceJump(piecePosition.xCoordinate, piecePosition.yCoordinate);
			if (canJump)
			{
				Integer[] jumpPosition = new Integer[2];
				jumpPosition[0] = piecePosition.xCoordinate;
				jumpPosition[1] = piecePosition.yCoordinate;
				jumpPositions.add(jumpPosition);
			}

		}

		// Keep asking the player to make moves until they run out of follow-up
		// moves (more jumps in the series).
		boolean hasMoves = true;
		while (hasMoves) {

			// Keep asking this player for input until they've given us an
			// answer which represents a valid move.
			String moveString = prompt.nextLine();
			Optional<PlayerMove> moveOption = parseValidMove(moveString);
			while (!moveOption.isPresent()) {
				System.out.println("Please enter a valid move, "
						+ currentPlayerName + ": ");
				moveString = prompt.nextLine();
				moveOption = parseValidMove(moveString);
			}
			PlayerMove validMove = moveOption.get();

			// Execute the player's move.
			executeMove(validMove);

			// Check if this player still has follow-up moves.
			hasMoves = hasFollowUpMoves(validMove);

			// If the player still has moves, tell them.
			if (hasMoves) {
				System.out.println();
				System.out.println("Continue, " + currentPlayerName);
			}
		}

		// Pass control of next turn to the other player.
		swapPlayerControl();
		CURRENT_PLAYER_MOVES.clear();
		jumpPositions.clear();
	}

	protected void swapPlayerControl() {
		CURRENT_PLAYER = (1 - CURRENT_PLAYER);
	}

	private boolean hasFollowUpMoves(PlayerMove move) {

		// If this piece just playerJumped it might have follow-up jumps.
		if (move.playerJumped) {

			// Find the two possible locations this piece could still jump to.
			Player currentPlayer = PLAYERS[CURRENT_PLAYER];
			CellElements playerColor = currentPlayer.getColor();
			int directionModifier = 0;
			if (playerColor == CellElements.RED) {
				directionModifier = 1;
			} else {
				directionModifier = -1;
			}

			int endX = move.endX;
			int endY = move.endY;
			int targetOneX = endX - 2;
			int targetTwoX = endX + 2;
			int targetY = endY + (2 * directionModifier);
			String targetOne = endX + "," + endY + ";" + targetOneX + ","
					+ targetY;
			String targetTwo = endX + "," + endY + ";" + targetTwoX + ","
					+ targetY;
			Optional<PlayerMove> targetOneOption = parseValidMove(targetOne);
			Optional<PlayerMove> targetTwoOption = parseValidMove(targetTwo);

			boolean availableMoves = false;
			if (targetOneOption.isPresent()) {
				CURRENT_PLAYER_MOVES.add(targetOneOption.get());
				availableMoves = true;
			}
			if (targetTwoOption.isPresent()) {
				CURRENT_PLAYER_MOVES.add(targetTwoOption.get());
				availableMoves = true;
			}
			return availableMoves;
		}

		return false;
	}

	protected void executeMove(PlayerMove move) {
		int startX = move.startX;
		int startY = move.startY;
		int endX = move.endX;
		int endY = move.endY;

		// Move the piece to its new location.
		CellElements piece = BOARD_STATE[startX][(BOARD_HEIGHT - 1) - startY];
		this.BOARD_STATE[endX][(BOARD_HEIGHT - 1) - endY] = piece;
		this.BOARD_STATE[startX][(BOARD_HEIGHT - 1) - startY] = CellElements.EMPTY;
		PiecePosition currentPiecePos = new PiecePosition(startX, startY);
		if(piece == CellElements.WHITE)
		{
			piecePositionInfoArray[0].add(new PiecePosition(endX, endY));
			piecePositionInfoArray[0].remove(currentPiecePos);
		}
		else
		{
			piecePositionInfoArray[1].add(new PiecePosition(endX, endY));
			piecePositionInfoArray[1].remove(currentPiecePos);
		}

		// Remove whatever piece it might have killed.
		boolean playerJumped = move.playerJumped;
		if (playerJumped) {
			int capturedX = move.capturedX;
			int capturedY = move.capturedY;
			this.BOARD_STATE[capturedX][(BOARD_HEIGHT - 1)
					- capturedY] = CellElements.EMPTY;
			PiecePosition capturedPiecePos = new PiecePosition(capturedX, capturedY);

			if(piece == CellElements.WHITE)
			{
				piecePositionInfoArray[0].remove(capturedPiecePos);
			}
			else
			{
				piecePositionInfoArray[1].remove(capturedPiecePos);
			}
		}
	}

	// Checks whether or not the piece at position x,y can jump.
	private boolean canPieceJump(int pieceX, int pieceY) {

		Player currentPlayer = PLAYERS[CURRENT_PLAYER];
		int directionModifier = currentPlayer.getColor() == CellElements.RED ? 1 : -1;


		int targetOneX = pieceX - 2;
		int targetTwoX = pieceX + 2;
		int targetY = pieceY + (2 * directionModifier);
		String targetOne = pieceX + "," + pieceY + ";" + targetOneX + ","
				+ targetY;
		String targetTwo = pieceX + "," + pieceY + ";" + targetTwoX + ","
				+ targetY;
		Optional<PlayerMove> targetOneOption = parseValidMove(targetOne);
		Optional<PlayerMove> targetTwoOption = parseValidMove(targetTwo);

		return (targetOneOption.isPresent() || targetTwoOption.isPresent());
	}

	protected Optional<PlayerMove> parseValidMove(String moveString) {

		// Check if the input can split into two cells.
		String[] splitMove = moveString.split(";");
		if (splitMove.length != 2 || (splitMove[0]).split(",").length !=2 || (splitMove[1]).split(",").length !=2) {
			return Optional.empty();
		}

		// Check if the two cells can split into two coordinates.
		String[] cellFrom = (splitMove[0]).split(",");
		String[] cellTo = (splitMove[1]).split(",");

		// Check if the cell coordinates can be properly parsed.
		try {
			int startX = Integer.parseInt(cellFrom[0]);
			int startY = Integer.parseInt(cellFrom[1]);
			int endX = Integer.parseInt(cellTo[0]);
			int endY = Integer.parseInt(cellTo[1]);

			Player currentPlayer = PLAYERS[CURRENT_PLAYER];
			CellElements playerColor = currentPlayer.getColor();

			// Check to see if these coordinates are valid on the board.
			if (startX >= 0 && startX <= 7 && startY >= 0 && startY <= 7 && endX >= 0
					&& endX <= 7 && endY >= 0 && endY <= 7) {

				// Prevent the user from moving from and to the same tile.
				if (startX != endX && startY != endY) {

					// Make sure this player is trying to move their own piece.
					// The offset value is to translate the origin for the
					// players. They would expect 0,0 to be bottom-left.
					CellElements movingPiece = BOARD_STATE[startX][(BOARD_HEIGHT
							- 1) - startY];
					if (playerColor != movingPiece) {
						return Optional.empty();
					}

					// Make sure this player is trying to move to an empty spot.
					CellElements destination = BOARD_STATE[endX][(BOARD_HEIGHT - 1)
							- endY];
					if (destination != CellElements.EMPTY) {
						return Optional.empty();
					}

					// Figure out what direction this player is allowed to move
					// in. RED moves up the board, WHITE moves down it.
					int directionModifier = 0;
					if (playerColor == CellElements.RED) {
						directionModifier = 1;
					} else {
						directionModifier = -1;
					}

					// Figure out if this move is valid for distance reasons.
					// This restricts each piece to its four valid destinations.
					int xDist = Math.abs(startX - endX);
					int expectedY;
					boolean isJump = false;
					if (xDist == 1) {
						expectedY = startY + directionModifier;
					} else if (xDist == 2) {
						isJump = true;
						expectedY = startY + (2 * directionModifier);
					} else {
						return Optional.empty();
					}

					// Make sure we moved in the right direction.
					if (expectedY != endY) {
						return Optional.empty();
					}

					// If this is a jump, make sure we actually playerJumped over an
					// opponent's piece.
					PlayerMove validMove = new PlayerMove(startX, startY, endX, endY);
					if (isJump) {
						int opponentY = (expectedY - directionModifier);
						int opponentX;
						if (startX > endX) {
							opponentX = startX - 1;
						} else {
							opponentX = startX + 1;
						}

						// Actually perform the check to see opponent state.
						CellElements opponentPiece = BOARD_STATE[opponentX][(BOARD_HEIGHT
								- 1) - opponentY];
						if ((playerColor == CellElements.RED
								&& opponentPiece != CellElements.WHITE)
								|| (playerColor == CellElements.WHITE
										&& opponentPiece != CellElements.RED)) {
							return Optional.empty();
						} else {
							validMove.capturedX = opponentX;
							validMove.capturedY = opponentY;
							validMove.playerJumped = true;
						}
					}

					// We've passed all potential invalidity checks, but the
					// player might be trying to follow a jump with a valid move
					// that's not directly in the jump's chain. We need to
					// restrict their valid moves in this case.
					boolean isValidOption = false;
					for (PlayerMove m : CURRENT_PLAYER_MOVES) {
						int mX = m.endX;
						int mY = m.endY;
						if (endX == mX && endY == mY) {
							isValidOption = true;
							break;
						}
					}

					// If we found jump-capable pieces, is this one?
					// And did it jump?
					if (jumpPositions.size() > 0) {
						boolean wasJumper = false;
						for (Integer[] position : jumpPositions) {
							int posX = position[0];
							int posY = position[1];
							if (posX == startX && posY == startY) {
								wasJumper = true;
								break;
							}
						}
						if (!wasJumper) {
							return Optional.empty();
						} else if (!isJump) {
							return Optional.empty();
						}
					}

					// The player either has complete freedom to choose this
					// move, or it's part of a jump chain. If they're trying to
					// cheat their way out of a jump chain, then this is not a
					// valid move.
					if (CURRENT_PLAYER_MOVES.size() == 0 || isValidOption) {
						return Optional.of(validMove);
					} else {
						return Optional.empty();
					}
				}
			}
		} catch (NumberFormatException e) {
			return Optional.empty();
		}

		// This input failed to parse into a valid move.
		return Optional.empty();
	}
}
