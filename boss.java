package cmdmario_project;

public class Boss extends Game_object {

	private int lifes;

	public Boss() {
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
