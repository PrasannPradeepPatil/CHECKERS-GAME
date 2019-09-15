package us.rockhopper.tim.checkers;

/**
 * Represents the state of any given cell on the checkers board: what color
 * piece it might contain and whether or not it is empty.
 * 
 * @author Tim Clancy
 * @version 1.0.0
 * @date 10.5.2017
 */
public enum TileState {
	RED('r'), WHITE('w'), EMPTY('-');

	private final char SYMBOL;

	TileState(char symbol) {
		this.SYMBOL = symbol;
	}

	public char getSymbol() {
		return this.SYMBOL;
	}
}
