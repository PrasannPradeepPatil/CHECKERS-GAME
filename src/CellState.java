public enum CellState {
	RED('r'), 
	WHITE('w'), 
	EMPTY('-');

	private final char SYMBOL;

	CellState(char symbol) {
		this.SYMBOL = symbol;
	}

	public char getSymbol() {
		return this.SYMBOL;
	}
}
