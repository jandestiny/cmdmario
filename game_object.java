package cmdmario_project;

public class game_object {

	private int xPos, yPos;
	private boolean canMove, isSolid;
	private char skin = ' ';
	
	
	//empty field constructor **FRAGE: das hier f�hrt meiner Meinung nach zu Verwirrung und erleichtert nur sehr wenig Schreibarbeit. Wirkt so, als w�rde man ein GameObject ohne Skin erstellen**
	public game_object() {
		super();
		this.isSolid = false;
		this.setSkin(' ');
	}

	
	//Spielfeldrand 
	public game_object(char skin) {
		super();
		this.skin = skin;
		this.setSolid(true);
		this.setCanMove(false);
	}
		
		
	public int getxPos() {
		return xPos;
	}
	public void setxPos(int xPos) {
		this.xPos = xPos;
	}
	public int getyPos() {
		return yPos;
	}
	public void setyPos(int yPos) {
		this.yPos = yPos;
	}
	public boolean isCanMove() {
		return canMove;
	}
	public void setCanMove(boolean canMove) {
		this.canMove = canMove;
	}
	public boolean isSolid() {
		return isSolid;
	}
	public void setSolid(boolean isSolid) {
		this.isSolid = isSolid;
	}
	public char getSkin() {
		return skin;
	}
	public void setSkin(char skin) {
		this.skin = skin;
	}
	
	
}
