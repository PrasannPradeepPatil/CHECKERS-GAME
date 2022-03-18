public enum CellElements {
	RED('r'), 
	WHITE('w'), 
	EMPTY('-');

	private final char SYMBOL;

	CellElements(char symbol) {
		this.SYMBOL = symbol;
	}

	public char getSymbol() {
		return this.SYMBOL;
	}
}
