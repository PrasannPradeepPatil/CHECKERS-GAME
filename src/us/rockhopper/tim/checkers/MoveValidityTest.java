package us.rockhopper.tim.checkers;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;

import org.junit.Test;

/**
 * This class tests a variety of moves on simulated checker boards to make sure
 * the game logic is functioning as intended.
 * 
 * @author Tim Clancy
 * @version 1.0.0
 * @date 10.5.2017
 */
public class MoveValidityTest {

	@Test
	public void testInvalidInput() {
		// Establish a board for executing test moves on.
		Board testBoard = new Board(new Player("a", TileState.RED),
				new Player("b", TileState.WHITE));
		Optional<Move> moveOption = testBoard.parseValidMove("invalid");
		assertFalse(moveOption.isPresent());
	}

	@Test
	public void testInvalidCells() {
		// Establish a board for executing test moves on.
		Board testBoard = new Board(new Player("a", TileState.RED),
				new Player("b", TileState.WHITE));
		Optional<Move> moveOption = testBoard.parseValidMove("cellA;cellB");
		assertFalse(moveOption.isPresent());
	}

	@Test
	public void testInvalidCoordinates() {
		// Establish a board for executing test moves on.
		Board testBoard = new Board(new Player("a", TileState.RED),
				new Player("b", TileState.WHITE));
		Optional<Move> moveOption = testBoard.parseValidMove("a,b;c,d");
		assertFalse(moveOption.isPresent());
	}

	@Test
	public void testValidRedMove() {
		// Establish a board for executing test moves on.
		Board testBoard = new Board(new Player("a", TileState.RED),
				new Player("b", TileState.WHITE));
		Optional<Move> moveOption = testBoard.parseValidMove("4,2;3,3");
		assertTrue(moveOption.isPresent());
	}

	@Test
	public void testInvalidCollidingRedMove() {
		// Establish a board for executing test moves on.
		Board testBoard = new Board(new Player("a", TileState.RED),
				new Player("b", TileState.WHITE));
		Optional<Move> moveOption = testBoard.parseValidMove("0,0;1,1");
		assertFalse(moveOption.isPresent());
	}

	@Test
	public void testInvalidSameCoordinates() {
		// Establish a board for executing test moves on.
		Board testBoard = new Board(new Player("a", TileState.RED),
				new Player("b", TileState.WHITE));
		Optional<Move> moveOption = testBoard.parseValidMove("4,2;4,2");
		assertFalse(moveOption.isPresent());
	}

	@Test
	public void testInvalidWhiteMove() {
		// Establish a board for executing test moves on.
		Board testBoard = new Board(new Player("a", TileState.RED),
				new Player("b", TileState.WHITE));
		Optional<Move> redMoveOption = testBoard.parseValidMove("4,2;3,3");
		assertTrue(redMoveOption.isPresent());
		testBoard.executeMove(redMoveOption.get());
		testBoard.swapPlayerControl();

		Optional<Move> whiteMoveOption = testBoard.parseValidMove("2,2;1,3");
		assertFalse(whiteMoveOption.isPresent());
	}

	@Test
	public void testValidWhiteMove() {
		// Establish a board for executing test moves on.
		Board testBoard = new Board(new Player("a", TileState.RED),
				new Player("b", TileState.WHITE));
		Optional<Move> redMoveOption = testBoard.parseValidMove("4,2;3,3");
		assertTrue(redMoveOption.isPresent());
		testBoard.executeMove(redMoveOption.get());
		testBoard.swapPlayerControl();

		Optional<Move> whiteMoveOption = testBoard.parseValidMove("1,5;0,4");
		assertTrue(whiteMoveOption.isPresent());
	}

	@Test
	public void testValidRedJump() {
		// Establish a board for executing test moves on.
		Board testBoard = new Board(new Player("a", TileState.RED),
				new Player("b", TileState.WHITE));
		Optional<Move> redMoveOption = testBoard.parseValidMove("4,2;3,3");
		assertTrue(redMoveOption.isPresent());
		testBoard.executeMove(redMoveOption.get());
		testBoard.swapPlayerControl();
		Optional<Move> whiteMoveOption = testBoard.parseValidMove("5,5;4,4");
		assertTrue(whiteMoveOption.isPresent());
		testBoard.executeMove(whiteMoveOption.get());
		testBoard.swapPlayerControl();

		// The jump.
		redMoveOption = testBoard.parseValidMove("3,3;5,5");
		assertTrue(redMoveOption.isPresent());
	}

	@Test
	public void testInvalidRedJump() {
		// Establish a board for executing test moves on.
		Board testBoard = new Board(new Player("a", TileState.RED),
				new Player("b", TileState.WHITE));
		Optional<Move> redMoveOption = testBoard.parseValidMove("4,2;3,3");
		assertTrue(redMoveOption.isPresent());
		testBoard.executeMove(redMoveOption.get());
		testBoard.swapPlayerControl();
		Optional<Move> whiteMoveOption = testBoard.parseValidMove("5,5;6,4");
		assertTrue(whiteMoveOption.isPresent());
		testBoard.executeMove(whiteMoveOption.get());
		testBoard.swapPlayerControl();

		// The jump.
		redMoveOption = testBoard.parseValidMove("3,3;5,5");
		assertFalse(redMoveOption.isPresent());
	}

	@Test
	public void testInvalidCollidingRedJump() {
		// Establish a board for executing test moves on.
		Board testBoard = new Board(new Player("a", TileState.RED),
				new Player("b", TileState.WHITE));
		Optional<Move> redMoveOption = testBoard.parseValidMove("4,2;3,3");
		assertTrue(redMoveOption.isPresent());
		testBoard.executeMove(redMoveOption.get());
		testBoard.swapPlayerControl();
		Optional<Move> whiteMoveOption = testBoard.parseValidMove("3,5;4,4");
		assertTrue(whiteMoveOption.isPresent());
		testBoard.executeMove(whiteMoveOption.get());
		testBoard.swapPlayerControl();

		// The jump.
		redMoveOption = testBoard.parseValidMove("3,3;5,5");
		assertFalse(redMoveOption.isPresent());
	}
}
