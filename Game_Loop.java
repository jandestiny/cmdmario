package cmdmario_project;

import java.io.IOException;
import java.util.Random;

import javax.swing.JFrame;

public class Game_Loop {

	public static void main(String[] args) throws InterruptedException, IOException {
		init();
	}

	private static player p = new player();
	private static int x_value = 50;
	private static int y_value = 10;
	private static int boss_hp = 3;
	private static boss b = new boss();
	private static boolean pause = false;
	private static int score = 0;
	private static int delay = 32;
	private static String[] b_colors= {"color F0","color 0F", "color 1F"};
	private static int boss_frame = 100;

	// Anzahl an Ticks die Loop durchlaufen hat
	private static int ticks = 0;

	private static game_object[][] field = new game_object[y_value][x_value];

	public static void init() throws InterruptedException, IOException {

		batchFunction("color a");

		move_player();

		p.setyPos(y_value / 2);
		p.setxPos(4);

		// Erstellt leere Spielfeld Matrix
		// Game_Loop.field = new game_object[y_value][x_value];

		// Befüllt Spielfeld Matrix mit Startkomponenten
		generate_field();

		// Spielschleife läuft solang Spieler mehr als 0 Leben hat
		while (p.getLives() > 0) {

			if (ticks % 20 == 0) {
				score += 10;
			}

			if (ticks==boss_frame) {
				boss_stage();
			}

			// Bewegt Spieler Projektile
			move_projectiles();

			// Gibt Feld aus
			display_field();

			if (ticks % 2 == 0) {

				// Bewegt alle bewegbaren Objekte eine Stelle nach links
				move_field();

				if (ticks % 8 == 0 && ticks > 10) {
					// Generiert an zufälliger Stelle Gegner
					random_spawn_enemy();
				}
				if (ticks % 50 == 0 && ticks > 40) {

					// Generiert an zufälliger Stelle ein Hinderniss
					generate_obstacle();
				}
			}
			// Fügt Tick Delay ein
			Thread.sleep(delay);

			// Testet ob Spiel pausiert wurde
			while (pause == true) {
				Thread.sleep(32);
			}

			// Löscht Konsoleninhalt
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

			// Bewegt Spieler an richtige Position
			field[p.getyPos()][p.getxPos()] = p;

			ticks++;

		}
		System.out.println("Game Over!");

		System.exit(0);

	}

	private static void generate_field() {

		for (int i = 0; i < y_value; i++) {
			for (int j = 0; j < x_value; j++) {
				if (i == 1 || i == y_value - 1) {
					field[i][j] = new game_object('~');
				} else if (j == x_value - 1 || j == 0) {
					field[i][j] = new game_object(' ');
				} else {
					field[i][j] = new game_object();
				}
			}
		}

		field[p.getyPos()][p.getxPos()] = p;

	}

	public static void boss_stage() throws InterruptedException, IOException {

		b.setLifes(boss_hp);

		int g = 0;

		while (g < x_value) {

			move_field();
			move_projectiles();
			display_field();
			Thread.sleep(64);
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			field[p.getyPos()][p.getxPos()] = p;
			g++;
		}

		Random r = new Random();

		field[p.getyPos()][p.getxPos()] = p;

		b.setyPos(y_value / 2);

		b.setxPos(x_value - 5);

		field[b.getyPos()][b.getxPos()] = b;

		boss_intro(400);

		while (b.getLifes() > 0 && p.getLives() > 0) {

			field[b.getyPos()][b.getxPos()] = b;

			int random = r.nextInt(4);

			field[p.getyPos()][p.getxPos()] = p;
			display_field();

			Thread.sleep(32);
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

			move_projectiles();

			if (ticks % 2 == 0) {
				enemy_projectile();
			}

			if (random == 1 && b.getyPos() < y_value - 2) {
				field[b.getyPos()][b.getxPos()] = new game_object();
				b.setyPos(b.getyPos() + 1);
			}

			if (random == 0 && b.getyPos() > 2) {
				field[b.getyPos()][b.getxPos()] = new game_object();
				b.setyPos(b.getyPos() - 1);

			}

			field[b.getyPos()][b.getxPos()] = new game_object();
			ticks++;
		}

		field[b.getyPos()][b.getxPos()] = new game_object();
		score += 10000;
		boss_hp += 2;
		delay *= 0.75;
		
		boss_frame=ticks+100;
		
		Random c = new Random();
		int rc = c.nextInt(b_colors.length-1);
		batchFunction(b_colors[rc]);
	}

	private static void boss_intro(int delay) throws InterruptedException, IOException {

		display_field();
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		b.setSkin('<');
		field[p.getyPos()][p.getxPos()] = p;
		display_field();
		Thread.sleep(delay);
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		b.setSkin('v');
		field[p.getyPos()][p.getxPos()] = p;
		display_field();
		Thread.sleep(delay);
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		b.setSkin('>');
		field[p.getyPos()][p.getxPos()] = p;
		display_field();
		Thread.sleep(delay);
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		b.setSkin('^');
		field[p.getyPos()][p.getxPos()] = p;
		display_field();
		Thread.sleep(delay);
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		b.setSkin('<');
		field[p.getyPos()][p.getxPos()] = p;
		display_field();
		Thread.sleep(delay);
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

		game_object obstacle = new game_object();
		obstacle.setCanMove(true);
		obstacle.setSkin('|');
		obstacle.setSolid(true);

		field[b.getyPos() + 3][b.getxPos() - 5] = obstacle;
		field[p.getyPos()][p.getxPos()] = p;
		display_field();
		Thread.sleep(delay);
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		field[b.getyPos() - 3][b.getxPos() - 5] = obstacle;
		field[p.getyPos()][p.getxPos()] = p;
		display_field();
		Thread.sleep(delay);
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		field[b.getyPos()][b.getxPos() - 7] = obstacle;
		field[p.getyPos()][p.getxPos()] = p;
		display_field();
		Thread.sleep(delay);
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

	}

	private static void display_field() {

		draw_actionbar("\tLives: " + p.getLives() + "\tScore: " + score);

		String field_string = "";

		for (int i = 0; i < y_value; i++) {
			field_string += "\n";
			for (int j = 0; j < x_value; j++) {
				field_string += field[i][j].getSkin();
			}
		}
		System.out.println(field_string);
	}

	public static void generate_obstacle() {

		Random rand = new Random();

		game_object obstacle = new game_object();
		obstacle.setCanMove(true);
		obstacle.setSkin('|');
		obstacle.setSolid(true);

		int y = rand.nextInt(y_value - 5) + 2;

		if (field[y][x_value - 2] instanceof player || field[y + 1][x_value - 2] instanceof player
				|| field[y + 2][x_value - 2] instanceof player) {
			p.setxPos(p.getxPos() - 1);
		}

		field[y][x_value - 2] = obstacle;
		field[y + 1][x_value - 2] = obstacle;
		field[y + 2][x_value - 2] = obstacle;

	}

	private static void move_projectiles() {

		for (int y = y_value - 1; y > -1; y--) {
			for (int x = x_value - 1; x > -1; x--) {

				// Testen ob es sich bei Feld um Projektil handelt
				if (field[y][x] instanceof projectile) {
					if (((projectile) field[y][x]).isP_projectile() == true) {
						// Testen ob es sich um Nachbarfeld um Gegner handelt
						if (field[y][x + 1] instanceof enemy) {
							field[y][x] = new game_object();
							field[y][x + 1] = new game_object();
							score += 50;

							// Testen ob es sich bei Nachbarfeld um Hinderniss handelt
						} else if (field[y][x + 1].isSolid()) {
							if (field[y][x + 1] instanceof boss) {
								b.setLifes(b.getLifes() - 1);
								field[y][x] = new game_object();
							} else {
								field[y][x] = new game_object();
							}
							// Ansonsten Projektil nach vorne bewegen
						} else {
							field[y][x + 1] = new projectile(true);
							field[y][x] = new game_object();

						}

						// Selber Test für Gegner Projektile
					}
				}

			}
		}

		for (int y = 0; y < y_value - 1; y++) {
			for (int x = 1; x < x_value - 1; x++) {
				if (field[y][x] instanceof projectile) {
					if (((projectile) field[y][x]).isP_projectile() == false) {
						if (field[y][x - 1] instanceof player) {
							field[y][x] = new game_object();
							p.setLives(p.getLives() - 1);

						} else if (field[y][x - 1].isSolid()) {
							field[y][x] = new game_object();

						} else {
							field[y][x - 1] = new projectile(false);
							field[y][x] = new game_object();

						}

					}
				}
			}
		}

	}

	private static void enemy_projectile() {

		int x = b.getxPos();
		int y = b.getyPos();

		projectile pr = new projectile(false);

		if (field[y][x - 1].isSolid() == false) {
			field[y][x - 2] = pr;
		}
	}

	private static void move_field() {

		for (int i = 2; i < y_value - 1; i++) {
			for (int j = 1; j < x_value - 1; j++) {
				// Test ob gewähltes Feld ein bewegbars ist
				if (field[i][j].isCanMove()) {

					// Testen ob es sich um Hinderniss handelt
					if (field[i][j].isSolid()) {
						if (field[i][j - 1] instanceof player) {
							if (p.getxPos() == 1) {
								p.setLives(0);
							} else {
								field[i][j - 1] = new game_object();
								p.setyPos(i);
								p.setxPos(j - 2);
								field[i][j - 1] = field[i][j];
								field[i][j] = new game_object();
							}
						} else {
							field[i][j - 1] = field[i][j];
							field[i][j] = new game_object();
						}
					}

					// Test ob move_to Feld der Spieler ist
					else if (field[i][j - 1] instanceof player) {
						field[i][j] = new game_object();
						p.setLives(p.getLives() - 1);
					} else {
						if (field[i][j - 1] instanceof projectile == false) {
							field[i][j - 1] = field[i][j];
							field[i][j] = new game_object();
						}
					}
				}
			}
		}

		for (int i = 0; i < y_value; i++) {
			field[i][0] = new game_object(' ');
		}

	}

	private static void random_spawn_enemy() {

		Random r = new Random();

		int random = r.nextInt(y_value - 3) + 2;

		if (field[random][x_value - 2] instanceof player) {
			p.setxPos(p.getxPos() - 1);
			field[random][x_value - 2] = new enemy();

		} else {
			field[random][x_value - 2] = new enemy();
		}
	}

	public static void batchFunction(String func) {
		try {
			new ProcessBuilder("cmd", "/c", func).inheritIO().start().waitFor(); // Erklärung finden
		} catch (Exception E) {
			System.out.println(E);
		}
	}

	private static void move_player() {
		JFrame jf = new JFrame();

		jf.setSize(1, 1);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLocationRelativeTo(null);

		jf.addKeyListener(new KeyHandler());
		jf.setVisible(true);
	}

	public static void P_UP() {

		int x = p.getxPos();

		int y = p.getyPos();

		if (field[y - 1][x].isSolid() == false) {
			p.setxPos(x);
			p.setyPos(y - 1);
			field[y][x] = new game_object();

		}

		if (field[y - 1][x] instanceof enemy) {
			p.setLives(p.getLives() - 1);
			field[y][x] = new game_object();
		}
	}

	public static void P_DOWN() {

		int x = p.getxPos();

		int y = p.getyPos();

		if (field[y + 1][x].isSolid() == false) {
			p.setxPos(x);
			p.setyPos(y + 1);
			field[y][x] = new game_object();
		}

		if (field[y + 1][x] instanceof enemy) {
			p.setLives(p.getLives() - 1);
			field[y][x] = new game_object();
		}
	}

	public static void P_LEFT() {

		int x = p.getxPos();

		int y = p.getyPos();

		if (field[y][x - 1].isSolid() == false) {
			p.setxPos(x - 1);
			p.setyPos(y);
			field[y][x] = new game_object();
		}

		if (field[y][x - 1] instanceof enemy) {
			p.setLives(p.getLives() - 1);
			field[y][x] = new game_object();
		}
	}

	public static void P_RIGHT() {

		int x = p.getxPos();

		int y = p.getyPos();

		if (field[y][x + 1].isSolid() == false) {
			p.setxPos(x + 1);
			p.setyPos(y);
			field[y][x] = new game_object();
		}

		if (field[y][x + 1] instanceof enemy) {
			p.setLives(p.getLives() - 1);
			field[y][x] = new game_object();
		}
	}

	public static void P_SHOOT() {

		projectile pr = new projectile(true);

		if (p.getShot_counter() + 4 < ticks) {
			if (field[p.getyPos()][p.getxPos() + 1].isSolid() == false) {
				field[p.getyPos()][p.getxPos() + 1] = pr;
			}
			p.setShot_counter(ticks);
		}
	}

	public static void P_PAUSE() {
		if (pause == true) {
			pause = false;
		} else {
			pause = true;
		}
	}

	private static void draw_actionbar(String s) {

		if (s.length() < x_value) {
			for (int i = 0; i < s.length(); i++) {
				field[0][i] = new game_object(s.charAt(i));
			}
		}
	}
}