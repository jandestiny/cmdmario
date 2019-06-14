package cmdmario_project;

public class player extends game_object {

	private int lives;

	private int shot_counter = 0;
	
	public player() {
		super();
		this.lives = 3;
		this.setCanMove(false);
		this.setSkin('>');
		this.setSolid(false);
	}

	public int getLives() {
		return lives;
	}

	public void setLives(int lives) {
		this.lives = lives;
	}

	public int getShot_counter() {
		return shot_counter;
	}

	public void setShot_counter(int shot_counter) {
		this.shot_counter = shot_counter;
	}
	
	
}
