public class PlayerMove {

	public int startX;
	public int startY;
	public int endX;
	public int endY;

	public boolean playerJumped = false;
	public int capturedX = -1;
	public int capturedY = -1;

	public PlayerMove(int startX,int startY, int endX,final int endY) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
	}
}
