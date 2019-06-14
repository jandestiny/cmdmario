package cmdmario_project;

public class boss extends game_object {

	private int lifes;

	public boss() {
		super();
		this.setCanMove(false);
		this.setSolid(true);
		
	}

	public int getLifes() {
		return lifes;
	}

	public void setLifes(int lifes) {
		this.lifes = lifes;
	}
	
}
