package us.rockhopper.tim.checkers;

/**
 * Specifies a player of this game, which just has a name and a color right now.
 * Later we could use something like this to track the "score" of either player.
 * 
 * @author Tim Clancy
 * @version 1.0.0
 * @date 10.5.2017
 */
public class Player {

	private final String NAME;
	private final TileState COLOR;

	public Player(final String name, final TileState color) {
		this.NAME = name;
		this.COLOR = color;
	}

	public String getName() {
		return this.NAME;
	}

	public TileState getColor() {
		return this.COLOR;
	}
}
