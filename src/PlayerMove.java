public class PlayerMove {

	public int sourceX;
	public int sourceY;
	public int destinationX;
	public int destinationY;

	public boolean playerJumped = false;
	public int capturedX = -1;
	public int capturedY = -1;

	public PlayerMove(int sourceX,int sourceY, int destinationX,final int destinationY) {
		this.sourceX = sourceX;
		this.sourceY = sourceY;
		this.destinationX = destinationX;
		this.destinationY = destinationY;
	}
}
