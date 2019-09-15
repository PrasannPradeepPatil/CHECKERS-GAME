package us.rockhopper.tim.checkers;

/**
 * This class is just a holder of data specifying the characteristics for a
 * given checker piece's jump.
 * 
 * @author Tim Clancy
 * @version 1.0.0
 * @date 10.5.2017
 */
public class Move {

	public int fromX;
	public int fromY;
	public int toX;
	public int toY;

	public boolean jumped = false;
	public int killedX = -1;
	public int killedY = -1;

	public Move(final int fromX, final int fromY, final int toX,
			final int toY) {
		this.fromX = fromX;
		this.fromY = fromY;
		this.toX = toX;
		this.toY = toY;
	}
}
