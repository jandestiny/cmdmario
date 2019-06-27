package cmdmario_project;

public class Projectile extends Game_object{

	private boolean player_projectile;
	
	public Projectile(boolean p_projectile) {
		this.player_projectile = p_projectile;
		this.setCanMove(false);
		this.setSkin('-');
		this.setSolid(false);
	}

	public boolean isPlayer_projectile() {
		return player_projectile;
	}

}
