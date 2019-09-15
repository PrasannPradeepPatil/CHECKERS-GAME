package us.rockhopper.tim.checkers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

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
	private final TileState[][] BOARD_STATE;

	private final Player[] PLAYERS;
	private final List<Move> CURRENT_PLAYER_MOVES;
	private List<Integer[]> JUMPERS = new ArrayList<>();
	private int CURRENT_PLAYER = 0;

	public Board(final Player red, final Player white) {
		BOARD_STATE = new TileState[BOARD_WIDTH][BOARD_HEIGHT];
		PLAYERS = new Player[2];
		PLAYERS[0] = red;
		PLAYERS[1] = white;
		CURRENT_PLAYER_MOVES = new ArrayList<Move>();
		resetGame();
	}

	// Initialize the checker board to a valid starting state.
	private void resetGame() {
		boolean rowOffset = false;
		for (int h = 0; h < BOARD_HEIGHT; ++h) {
			for (int w = 0; w < BOARD_WIDTH; ++w) {

				if (h < 3) {
					if (rowOffset) {
						if (w % 2 == 0) {
							BOARD_STATE[w][h] = TileState.WHITE;
						} else {
							BOARD_STATE[w][h] = TileState.EMPTY;
						}
					} else {
						if (w % 2 != 0) {
							BOARD_STATE[w][h] = TileState.WHITE;
						} else {
							BOARD_STATE[w][h] = TileState.EMPTY;
						}
					}
				} else if (h < 5) {
					BOARD_STATE[w][h] = TileState.EMPTY;
				} else {
					if (rowOffset) {
						if (w % 2 == 0) {
							BOARD_STATE[w][h] = TileState.RED;
						} else {
							BOARD_STATE[w][h] = TileState.EMPTY;
						}
					} else {
						if (w % 2 != 0) {
							BOARD_STATE[w][h] = TileState.RED;
						} else {
							BOARD_STATE[w][h] = TileState.EMPTY;
						}
					}
				}
			}
			rowOffset = !rowOffset;
		}
	}

	public void displayBoard() {
		for (int h = 0; h < BOARD_HEIGHT; ++h) {
			System.out.print("|");
			for (int w = 0; w < BOARD_WIDTH; ++w) {
				TileState tile = BOARD_STATE[w][h];
				System.out.print(tile.getSymbol() + "|");
			}
			System.out.print("\n");
		}
	}

	public boolean isGameRunning() {
		return true;

		// TODO: this is where I ran out of time.
		// I would check, for the player whose turn it is, whether any of their
		// pieces have valid moves. If not, then the game must be over.
	}

	public void promptPlayerMove(Scanner prompt) {

		// Tell the players who we're asking input from.
		Player currentPlayer = PLAYERS[CURRENT_PLAYER];
		TileState playerColor = currentPlayer.getColor();
		String currentPlayerName = currentPlayer.getName();
		System.out.println("It is " + currentPlayer.getName()
				+ "'s turn. Please specify a move: ");

		// If the player has a piece that can jump, they must choose
		// that piece. Find all of the player's pieces which have
		// eligible jumps.
		JUMPERS.clear();
		for (int w = 0; w < BOARD_WIDTH; ++w) {
			for (int h = 0; h < BOARD_HEIGHT; ++h) {
				TileState tile = BOARD_STATE[w][h];
				if (playerColor == tile) {
					boolean canJump = this.canPieceJump(w, 7 - h);
					if (canJump) {
						Integer[] jumpPosition = new Integer[2];
						jumpPosition[0] = w;
						jumpPosition[1] = (7 - h);
						JUMPERS.add(jumpPosition);
					}
				}
			}
		}

		// Keep asking the player to make moves until they run out of follow-up
		// moves (more jumps in the series).
		boolean hasMoves = true;
		while (hasMoves) {

			// Keep asking this player for input until they've given us an
			// answer which represents a valid move.
			String moveString = prompt.nextLine();
			Optional<Move> moveOption = parseValidMove(moveString);
			while (!moveOption.isPresent()) {
				System.out.println("Please enter a valid move, "
						+ currentPlayerName + ": ");
				moveString = prompt.nextLine();
				moveOption = parseValidMove(moveString);
			}
			Move validMove = moveOption.get();

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
	}

	protected void swapPlayerControl() {
		CURRENT_PLAYER = (1 - CURRENT_PLAYER);
	}

	private boolean hasFollowUpMoves(Move move) {

		// If this piece just jumped it might have follow-up jumps.
		if (move.jumped) {

			// Find the two possible locations this piece could still jump to.
			Player currentPlayer = PLAYERS[CURRENT_PLAYER];
			TileState playerColor = currentPlayer.getColor();
			int directionModifier = 0;
			if (playerColor == TileState.RED) {
				directionModifier = 1;
			} else {
				directionModifier = -1;
			}

			int toX = move.toX;
			int toY = move.toY;
			int targetOneX = toX - 2;
			int targetTwoX = toX + 2;
			int targetY = toY + (2 * directionModifier);
			String targetOne = toX + "," + toY + ";" + targetOneX + ","
					+ targetY;
			String targetTwo = toX + "," + toY + ";" + targetTwoX + ","
					+ targetY;
			Optional<Move> targetOneOption = parseValidMove(targetOne);
			Optional<Move> targetTwoOption = parseValidMove(targetTwo);

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

	protected void executeMove(Move move) {
		int fromX = move.fromX;
		int fromY = move.fromY;
		int toX = move.toX;
		int toY = move.toY;

		// Move the piece to its new location.
		TileState piece = BOARD_STATE[fromX][(BOARD_HEIGHT - 1) - fromY];
		this.BOARD_STATE[toX][(BOARD_HEIGHT - 1) - toY] = piece;
		this.BOARD_STATE[fromX][(BOARD_HEIGHT - 1) - fromY] = TileState.EMPTY;

		// Remove whatever piece it might have killed.
		boolean jumped = move.jumped;
		if (jumped) {
			int killedX = move.killedX;
			int killedY = move.killedY;
			this.BOARD_STATE[killedX][(BOARD_HEIGHT - 1)
					- killedY] = TileState.EMPTY;
		}
	}

	// Checks whether or not the piece at position x,y can jump.
	private boolean canPieceJump(int pieceX, int pieceY) {

		Player currentPlayer = PLAYERS[CURRENT_PLAYER];
		TileState playerColor = currentPlayer.getColor();
		int directionModifier = 0;
		if (playerColor == TileState.RED) {
			directionModifier = 1;
		} else {
			directionModifier = -1;
		}

		int targetOneX = pieceX - 2;
		int targetTwoX = pieceX + 2;
		int targetY = pieceY + (2 * directionModifier);
		String targetOne = pieceX + "," + pieceY + ";" + targetOneX + ","
				+ targetY;
		String targetTwo = pieceX + "," + pieceY + ";" + targetTwoX + ","
				+ targetY;
		Optional<Move> targetOneOption = parseValidMove(targetOne);
		Optional<Move> targetTwoOption = parseValidMove(targetTwo);

		return (targetOneOption.isPresent() || targetTwoOption.isPresent());
	}

	protected Optional<Move> parseValidMove(String moveString) {

		// Check if the input can split into two cells.
		String[] splitMove = moveString.split(";");
		if (splitMove.length != 2) {
			return Optional.empty();
		}

		// Check if the two cells can split into two coordinates.
		String[] cellFrom = (splitMove[0]).split(",");
		String[] cellTo = (splitMove[1]).split(",");
		if (cellFrom.length != 2 || cellTo.length != 2) {
			return Optional.empty();
		}

		// Check if the cell coordinates can be properly parsed.
		try {
			int fromX = Integer.parseInt(cellFrom[0]);
			int fromY = Integer.parseInt(cellFrom[1]);
			int toX = Integer.parseInt(cellTo[0]);
			int toY = Integer.parseInt(cellTo[1]);

			Player currentPlayer = PLAYERS[CURRENT_PLAYER];
			TileState playerColor = currentPlayer.getColor();

			// Check to see if these coordinates are valid on the board.
			if (fromX >= 0 && fromX <= 7 && fromY >= 0 && fromY <= 7 && toX >= 0
					&& toX <= 7 && toY >= 0 && toY <= 7) {

				// Prevent the user from moving from and to the same tile.
				if (fromX != toX && fromY != toY) {

					// Make sure this player is trying to move their own piece.
					// The offset value is to translate the origin for the
					// players. They would expect 0,0 to be bottom-left.
					TileState movingPiece = BOARD_STATE[fromX][(BOARD_HEIGHT
							- 1) - fromY];
					if (playerColor != movingPiece) {
						return Optional.empty();
					}

					// Make sure this player is trying to move to an empty spot.
					TileState destination = BOARD_STATE[toX][(BOARD_HEIGHT - 1)
							- toY];
					if (destination != TileState.EMPTY) {
						return Optional.empty();
					}

					// Figure out what direction this player is allowed to move
					// in. RED moves up the board, WHITE moves down it.
					int directionModifier = 0;
					if (playerColor == TileState.RED) {
						directionModifier = 1;
					} else {
						directionModifier = -1;
					}

					// Figure out if this move is valid for distance reasons.
					// This restricts each piece to its four valid destinations.
					int xDist = Math.abs(fromX - toX);
					int expectedY;
					boolean isJump = false;
					if (xDist == 1) {
						expectedY = fromY + directionModifier;
					} else if (xDist == 2) {
						isJump = true;
						expectedY = fromY + (2 * directionModifier);
					} else {
						return Optional.empty();
					}

					// Make sure we moved in the right direction.
					if (expectedY != toY) {
						return Optional.empty();
					}

					// If this is a jump, make sure we actually jumped over an
					// opponent's piece.
					Move validMove = new Move(fromX, fromY, toX, toY);
					if (isJump) {
						int opponentY = (expectedY - directionModifier);
						int opponentX;
						if (fromX > toX) {
							opponentX = fromX - 1;
						} else {
							opponentX = fromX + 1;
						}

						// Actually perform the check to see opponent state.
						TileState opponentPiece = BOARD_STATE[opponentX][(BOARD_HEIGHT
								- 1) - opponentY];
						if ((playerColor == TileState.RED
								&& opponentPiece != TileState.WHITE)
								|| (playerColor == TileState.WHITE
										&& opponentPiece != TileState.RED)) {
							return Optional.empty();
						} else {
							validMove.killedX = opponentX;
							validMove.killedY = opponentY;
							validMove.jumped = true;
						}
					}

					// We've passed all potential invalidity checks, but the
					// player might be trying to follow a jump with a valid move
					// that's not directly in the jump's chain. We need to
					// restrict their valid moves in this case.
					boolean isValidOption = false;
					for (Move m : CURRENT_PLAYER_MOVES) {
						int mX = m.toX;
						int mY = m.toY;
						if (toX == mX && toY == mY) {
							isValidOption = true;
							break;
						}
					}

					// If we found jump-capable pieces, is this one?
					// And did it jump?
					if (JUMPERS.size() > 0) {
						boolean wasJumper = false;
						for (Integer[] position : JUMPERS) {
							int posX = position[0];
							int posY = position[1];
							if (posX == fromX && posY == fromY) {
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
