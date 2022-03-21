

import java.util.*;

public class Board {


	
	private final int totalColumns = 8;
	private final int totalRows = 8;
	private final CellState[][] board ;

	private final Player[] players;
	private final List<PlayerMove> currentPlayerMoves;
	private List<int[]> availableJumpPositions = new ArrayList<>();
	private int currentPlayerIndex = 0;
	private List<PiecePosition>[] piecePositionInfoArray= new List[2];


	public Board(final Player red, final Player white) {
		board = new CellState[totalColumns][totalRows];
		players = new Player[2];
		players[0] = red;
		players[1] = white;
		currentPlayerMoves = new ArrayList<PlayerMove>();
		Arrays.setAll(piecePositionInfoArray, element -> new ArrayList<>());
		resetBoard();
	}

	public void displayInputDetails(){
		System.out.println("Enter your moves in the form \"startRow,startColumn;endRow,endColumn\" "
							+ "OR"
							+"Enter your moves in the form \"xStart,yStsart;xEnd,yEnd\" "
							+ "where bottom left is (0,0)" 
							);
	}


	public boolean isGameRunning() {
		Player currentPlayer = players[currentPlayerIndex];
		int playerNo = 0;
		int directionModifier = -1;

		if (currentPlayer.getColor() == CellState.RED) {
			playerNo = 1;
			directionModifier = 1;
		}

		for (PiecePosition piecePosition : piecePositionInfoArray[playerNo]) {
			int targetOneX = piecePosition.xCoordinate - 1;
			int targetTwoX = piecePosition.xCoordinate + 1;
			int targetThreeX = piecePosition.xCoordinate - 2;
			int targetFourX = piecePosition.xCoordinate + 2;
			int targetY = piecePosition.yCoordinate + directionModifier;
			int targetJumpY = piecePosition.yCoordinate + (2 * directionModifier);


			String targetOne = piecePosition.xCoordinate + "," + (piecePosition.yCoordinate) + ";" + targetOneX + ","
					+ targetY;
			String targetTwo = piecePosition.xCoordinate + "," + (piecePosition.yCoordinate) + ";" + targetTwoX + ","
					+ targetY;
			String targetThree = piecePosition.xCoordinate + "," + (piecePosition.yCoordinate) + ";" + targetThreeX + ","
					+ targetJumpY;
			String targetFour = piecePosition.xCoordinate + "," + (piecePosition.yCoordinate) + ";" + targetFourX + ","
					+ targetJumpY;
			Optional<PlayerMove> targetOneOption = parseValidMove(targetOne);
			Optional<PlayerMove> targetTwoOption = parseValidMove(targetTwo);
			Optional<PlayerMove> targetThreeOption = parseValidMove(targetThree);
			Optional<PlayerMove> targetFourOption = parseValidMove(targetFour);

			if ((targetOneOption.isPresent() || targetTwoOption.isPresent() || targetThreeOption.isPresent() || targetFourOption.isPresent())) {
				return true;
			}
		}


		return false;
	}

	private void resetBoard() {
		for(int i=0 ; i <=totalRows -1;i++){
			for(int j=0 ; j <= totalColumns-1;j++){
				if(i == 0 || i == 2){
					if(j%2 != 0){
						board[j][i] = CellState.WHITE;
					}
					else{
						board[j][i] = CellState.EMPTY;
					}
				}
			    else if(i == 1){
					if(j%2 == 0){
						board[j][i] = CellState.WHITE;
					}
					else{
						board[j][i] = CellState.EMPTY;
					}
				}
				else if(i == 3 || i == 4){
					board[j][i]  = CellState.EMPTY;
				}
				else if(i == 5 || i == 7){
					if(j %2 == 0){
						board[j][i] = CellState.RED;
					}
					else{
						board[j][i] = CellState.EMPTY;
					}
				}
			    else if(i == 6){
					if(j %2 != 0){
						board[j][i] = CellState.RED;
					}
					else{
						board[j][i] = CellState.EMPTY;
					}
				}


			}
		}
	}

	public void displayBoard() {
		for (int i = 0; i <= totalRows-1; i++) {

			System.out.print(totalRows - i -1  + " " +  "|");
			for (int j = 0; j <= totalColumns-1; j++) {
				CellState tile = board[j][i];
				System.out.print(tile.getSymbol() + "|");
			}
			System.out.print("\n");
		}
		System.out.println("   0 1 2 3 4 5 6 7");
	}

	public void currentPlayerMoveDetails() {

		Scanner sc = new Scanner(System.in);

		// Tell the players who we're asking input from.
		Player currentPlayer = players[currentPlayerIndex];
		CellState currentPlayerColour = currentPlayer.getColor();
		String currentPlayerName = currentPlayer.getName();
		System.out.println("Please input " + currentPlayer.getName()+ "'s move");

		int playerNo = 0;

		if (currentPlayer.getColor() == CellState.RED)
		{
			playerNo = 1;
		}


		//For all of the current players peice find all peices which have valid jumps(if a peice can jump it has to jump)
		availableJumpPositions.clear();
		for (PiecePosition piecePosition : piecePositionInfoArray[playerNo])
		{
			boolean currentPieceCanJump = this.currentPeiceCanJump(piecePosition.xCoordinate, piecePosition.yCoordinate);
			if (currentPieceCanJump) {
				availableJumpPositions.add(new int[]{piecePosition.xCoordinate,piecePosition.yCoordinate});
			}
		}

		//while the current player has valid moves
		boolean currentPlayerHasValidMoves = true;
		while (currentPlayerHasValidMoves) {
			//Keep asking till user enters a valid move
			String moveInput = sc.nextLine();
			Optional<PlayerMove> moveOption = parseValidMove(moveInput);
			while (!moveOption.isPresent()) {
				System.out.println("Please enter a valid move, "+ currentPlayerName + ": ");
				moveInput = sc.nextLine();
				moveOption = parseValidMove(moveInput);
			}
			PlayerMove moveValid = moveOption.get();

			// Execute valid move
			currentMoveExecute(moveValid);

			// Check if the valid move has follow up valid moves.
			currentPlayerHasValidMoves = currentMoveHasFollowUpMoves(moveValid);

			// If the player still has moves, tell them.
			if (currentPlayerHasValidMoves) {
				System.out.println();
				System.out.println(currentPlayerName + "still has valid move so its" + currentPlayerName + "'s turn again");
			}
		}

		// swap the player
		currentPlayerIndex = (currentPlayerIndex == 1)?0:1;
		currentPlayerMoves.clear();
		availableJumpPositions.clear();
	}

	private Optional<PlayerMove> parseValidMove(String moveString) {

		//Split input on ";" and check if there are 2 splits
		String[] movesList = moveString.split(";");
		if (movesList.length != 2) {return Optional.empty();}

		//Split the moveList on "," and check if there are 2 splits
		String[] startPosition = (movesList[0]).split(",");
		String[] endPosition = (movesList[1]).split(",");
		if (startPosition.length != 2 || endPosition.length != 2) {return Optional.empty();}

		//Check cell co-ordinates
		try {
			int sourceX = Integer.parseInt(startPosition[0]);
			int sourceY = Integer.parseInt(startPosition[1]);
			int destinationX = Integer.parseInt(endPosition[0]);
			int destinationY = Integer.parseInt(endPosition[1]);

			Player currentPlayer = players[currentPlayerIndex];
			CellState currentPlayerColour = currentPlayer.getColor();

			//Check cell co-ordinates are in board's range
			if (sourceX >= 0 && sourceX <= 7 && sourceY >= 0 && sourceY <= 7 && destinationX >= 0 && destinationX <= 7 && destinationY >= 0 && destinationY <= 7) {

				//Check cell co-ordinates start and end are not same ie check if user doesnt move from and to same tile
				if (sourceX != destinationX && sourceY != destinationY) {


					//Check if the moving peice and current player peice is the same
					CellState movingPieceColour = board[sourceX][(totalRows- 1) - sourceY];
					if (currentPlayerColour != movingPieceColour) {return Optional.empty();}

					//check is player moves on empty slot
					CellState destinationColour = board[destinationX][(totalRows - 1)- destinationY];
					if (destinationColour != CellState.EMPTY) {return Optional.empty();}

					//Decide the direction Choice
					//RED moves up the board hence +1 , WHITE moves down it hence -1.
					int directionChoice = 0;
					if (currentPlayerColour == CellState.RED) {directionChoice = 1;}
					else {directionChoice = -1;}

					// Figure out if this move is valid for distance reasons.
					// This restricts each piece to its four valid destinationColours.
					int xDist = Math.abs(sourceX - destinationX);
					int expectedY;
					boolean isJump = false;
					if (xDist == 1) {
						expectedY = sourceY + directionChoice;
					}
					 else if (xDist == 2) {
						isJump = true;
						expectedY = sourceY + (2 * directionChoice);
					}
					else {
						return Optional.empty();
					}

					// Make sure we moved in the right direction.
					if (expectedY != destinationY) {
						return Optional.empty();
					}

					// If this is a jump, make sure we actually playerJumped over an
					// opponent's piece.
					PlayerMove validMove = new PlayerMove(sourceX, sourceY, destinationX, destinationY);
					if (isJump) {
						int opponentY = (expectedY - directionChoice);
						int opponentX;
						if (sourceX > destinationX) {
							opponentX = sourceX - 1;
						} else {
							opponentX = sourceX + 1;
						}

						// Actually perform the check to see opponent state.
						CellState opponentPiece = board[opponentX][(totalRows
								- 1) - opponentY];
						if ((currentPlayerColour == CellState.RED
								&& opponentPiece != CellState.WHITE)
								|| (currentPlayerColour == CellState.WHITE
										&& opponentPiece != CellState.RED)) {
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
					for (PlayerMove m : currentPlayerMoves) {
						int mX = m.destinationX;
						int mY = m.destinationY;
						if (destinationX == mX && destinationY == mY) {
							isValidOption = true;
							break;
						}
					}

					// If we found jump-capable pieces, is this one?
					// And did it jump?
					if (availableJumpPositions.size() > 0) {
						boolean wasJumper = false;
						for (int[] position : availableJumpPositions) {
							int posX = position[0];
							int posY = position[1];
							if (posX == sourceX && posY == sourceY) {
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
					if (currentPlayerMoves.size() == 0 || isValidOption) {
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

	private boolean currentMoveHasFollowUpMoves(PlayerMove move) {

		// If this piece just playerJumped it might have follow-up jumps.
		if (move.playerJumped) {

			// Find the two possible locations this piece could still jump to.
			Player currentPlayer = players[currentPlayerIndex];
			CellState currentPlayerColour = currentPlayer.getColor();

			//Decide the direction Choice
			//RED moves up the board hence +1 , WHITE moves down it hence -1.
			int directionChoice = 0;
			if (currentPlayerColour == CellState.RED) {directionChoice = 1;}
			else {directionChoice = -1;}

			//Decide the 2 destination moves
			int destinationX = move.destinationX;
			int destinationY = move.destinationY;
			int destinationX_1 = destinationX - 2;
			int destinationX_2 = destinationX + 2;
			int destinationY_1 = destinationY + (2 * directionChoice);
			String destination_1 = destinationX + "," + destinationY + ";" + destinationX_1 + ","+ destinationY_1;
			String destination_2 = destinationX + "," + destinationY + ";" + destinationX_2 + ","+ destinationY_1;
			Optional<PlayerMove> destination_1_Move = parseValidMove(destination_1);
			Optional<PlayerMove> destination_2_Move = parseValidMove(destination_2);

			boolean availableMoves = false;
			if (destination_1_Move.isPresent()) {
				currentPlayerMoves.add(destination_1_Move.get());
				availableMoves = true;
			}
			if (destination_2_Move.isPresent()) {
				currentPlayerMoves.add(destination_2_Move.get());
				availableMoves = true;
			}
			return availableMoves;
		}

		return false;
	}

	private void currentMoveExecute(PlayerMove move) {
		int sourceX = move.sourceX;
		int sourceY = move.sourceY;
		int destinationX = move.destinationX;
		int destinationY = move.destinationY;

		// Move peice by changing the co-ordinates of the board
		CellState piece = board[sourceX][(totalRows - 1) - sourceY];
		this.board[destinationX][(totalRows - 1) - destinationY] = piece;
		this.board[sourceX][(totalRows - 1) - sourceY] = CellState.EMPTY;

		// Remove the captures peice when player moves
		boolean playerJumped = move.playerJumped;
		if (playerJumped) {
			int capturedX = move.capturedX;
			int capturedY = move.capturedY;
			this.board[capturedX][(totalRows - 1)- capturedY] = CellState.EMPTY;
		}
	}

	private boolean currentPeiceCanJump(int pieceX, int pieceY) {

		Player currentPlayer = players[currentPlayerIndex];
		CellState currentPlayerColour = currentPlayer.getColor();

		//Decide the direction Choice
		//RED moves up the board hence +1 , WHITE moves down it hence -1.
		int directionChoice = 0;
		if (currentPlayerColour == CellState.RED) {directionChoice = 1;}
		else {directionChoice = -1;}

		int destinationX_1 = pieceX - 2;
		int destinationX_2 = pieceX + 2;
		int destinationY_1 = pieceY + (2 * directionChoice);
		String destination_1 = pieceX + "," + pieceY + ";" + destinationX_1 + ","+ destinationY_1;
		String destination_2 = pieceX + "," + pieceY + ";" + destinationX_2 + ","+ destinationY_1;
		Optional<PlayerMove> destination_1_Move = parseValidMove(destination_1);
		Optional<PlayerMove> destination_2_Move = parseValidMove(destination_2);

		return (destination_1_Move.isPresent() || destination_2_Move.isPresent());
	}

}
