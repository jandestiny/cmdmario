package cmdmario_project;

public class projectile extends game_object{

	private boolean p_projectile;
	
	public projectile(boolean p_projectile) {
		this.p_projectile = p_projectile;
		this.setCanMove(false);
		this.setSkin('-');
		this.setSolid(false);
	}

	public boolean isP_projectile() {
		return p_projectile;
	}

}
