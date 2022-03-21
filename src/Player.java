public class Player {

	private final String PLAYER_NAME;
	private final CellElements PLAYER_COLOR;

	public Player(final String name, final CellElements color) {
		this.PLAYER_NAME = name;
		this.PLAYER_COLOR = color;
	}

	public String getName() {
		return this.PLAYER_NAME;
	}

	public CellElements getColor() {
		return this.PLAYER_COLOR;
	}
}
