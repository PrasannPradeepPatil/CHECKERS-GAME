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
