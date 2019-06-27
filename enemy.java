package cmdmario_project;

public class Enemy extends Game_object {

	public Enemy() {
		super();
		
		this.setSkin('{');
		this.setCanMove(true);
		this.setSolid(false);
	}
}
