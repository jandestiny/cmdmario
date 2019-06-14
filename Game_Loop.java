package cmdmario_project;

import java.io.IOException;
import java.util.Random;
import javax.swing.JFrame;

public class Game_Loop {	//**ACHTUNG: DOKUMENT GESPERRT, BITTE KEINE ÄNDERUNGEN DURCHFÜHREN, BEVOR WIR UNS BESPROCHEN HABEN
	//**ACHTUNG: DOKUMENT GESPERRT, BITTE KEINE ÄNDERUNGEN DURCHFÜHREN, BEVOR WIR UNS BESPROCHEN HABEN
	//**ACHTUNG: DOKUMENT GESPERRT, BITTE KEINE ÄNDERUNGEN DURCHFÜHREN, BEVOR WIR UNS BESPROCHEN HABEN
	//**ACHTUNG: DOKUMENT GESPERRT, BITTE KEINE ÄNDERUNGEN DURCHFÜHREN, BEVOR WIR UNS BESPROCHEN HABEN
	//**ACHTUNG: DOKUMENT GESPERRT, BITTE KEINE ÄNDERUNGEN DURCHFÜHREN, BEVOR WIR UNS BESPROCHEN HABEN
	//**ACHTUNG: DOKUMENT GESPERRT, BITTE KEINE ÄNDERUNGEN DURCHFÜHREN, BEVOR WIR UNS BESPROCHEN HABEN
	//**ACHTUNG: DOKUMENT GESPERRT, BITTE KEINE ÄNDERUNGEN DURCHFÜHREN, BEVOR WIR UNS BESPROCHEN HABEN
	//**ACHTUNG: DOKUMENT GESPERRT, BITTE KEINE ÄNDERUNGEN DURCHFÜHREN, BEVOR WIR UNS BESPROCHEN HABEN
	//**ACHTUNG: DOKUMENT GESPERRT, BITTE KEINE ÄNDERUNGEN DURCHFÜHREN, BEVOR WIR UNS BESPROCHEN HABEN
	
	//**Allgemeine Frage: Wieso sind einige Funktionen/Variablen private und einige nicht? Eigentlich sollten alle public sein, spricht nichts dagegen, oder?**
	//**Variablen Namen müssen DRINGEND überarbeitet werden, sind teilweise zu unverständlich

	public static void main(String[] args) throws InterruptedException, IOException {
		init();
	}
	
	//Globale Variablen
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

	private static game_object[][] field = new game_object[y_value][x_value];	//Erstellung der Spielfeld-Matrix

	public static void init() throws InterruptedException, IOException {	//Initialisierung der Spielfeld-Matrix (Hintergrund, Spielerpositionierung, Ränder, GAMELOOP, Ende

		batchFunction("color a");

		move_player();

		p.setyPos(y_value / 2);
		p.setxPos(4);

		// Erstellt leere Spielfeld Matrix
		// Game_Loop.field = new game_object[y_value][x_value];

		// Befï¿½llt Spielfeld Matrix mit Startkomponenten
		generate_field();

		// Spielschleife lï¿½uft solang Spieler mehr als 0 Leben hat
		while (p.getLives() > 0) {

			if (ticks % 20 == 0) {
				score += 10;
			}

			if (ticks==boss_frame) {	//Wenn die Boss_frame Zahl erreicht wird, startet ein BossLevel
				boss_stage();
			}

			// Bewegt Spieler Projektile
			move_projectiles();

			// Gibt Feld aus
			display_field("");

			if (ticks % 2 == 0) {

				// Bewegt alle bewegbaren Objekte eine Stelle nach links
				move_field();

				if (ticks % 8 == 0 && ticks > 10) {
					// Generiert an zufï¿½lliger Stelle Gegner
					random_spawn_enemy();
				}
				if (ticks % 50 == 0 && ticks > 40) {

					// Generiert an zufï¿½lliger Stelle ein Hinderniss
					generate_obstacle();
				}
			}
			// Fï¿½gt Tick Delay ein
			Thread.sleep(delay);

			// Testet ob Spiel pausiert wurde
			while (pause == true) {
				Thread.sleep(32);
			}

			// Lï¿½scht Konsoleninhalt
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

			// Bewegt Spieler an richtige Position
			field[p.getyPos()][p.getxPos()] = p;

			ticks++;

		}
		System.out.println("Game Over!");

		System.exit(0);

	}

	private static void generate_field() {	//Befüllt eine NULL-Spielfeld-Matrix

		for (int i = 0; i < y_value; i++) {
			for (int j = 0; j < x_value; j++) {
				if (i == 1 || i == y_value - 1) {		//Oberer und unterer Rand '~'
					field[i][j] = new game_object('~');
				} else if (j == x_value - 1 || j == 0) {	//Erste Spalte und letzte Spalte (Ränder ausgenommen) werden geleert -> also ' '
					field[i][j] = new game_object(' ');	//**Nach game_object Logik, wäre das hier ein Solides leeres Feld? Ist das so gedacht?**
				} else {									//Alle anderen Felder bekommen ein leeres GameObject zugeschrieben
					field[i][j] = new game_object();
				}
			}
		}

		field[p.getyPos()][p.getxPos()] = p;	//Platziert Spieler in der Matrix, mit angegebenen x und y Positionen

	}

	public static void boss_stage() throws InterruptedException, IOException {	//Level Abschluss Boss

		b.setLifes(boss_hp);

		int g = 0;	//**Wofür steht g?**

		while (g < x_value && p.getLives() > 0) {	//Alle Objekte aus der Matrix schieben ohne neue zu erstellen, sodass die Matrix frei für den Bossfight wird
			move_field();
			move_projectiles();
			display_field("");
			Thread.sleep(64);
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
			field[p.getyPos()][p.getxPos()] = p;
			g++;
		}

		Random r = new Random();

		field[p.getyPos()][p.getxPos()] = p;	//Spieler wird platziert

		b.setyPos(y_value / 2);		

		b.setxPos(x_value - 5);

		field[b.getyPos()][b.getxPos()] = b;	//Boss wird platziert

		boss_intro(400);

		while (b.getLifes() > 0 && p.getLives() > 0) {	//Solange beide Entitäten noch am Leben sind, läuft der Bossfight

			field[b.getyPos()][b.getxPos()] = b;	//Boss wird platziert

			int random = r.nextInt(4);

			field[p.getyPos()][p.getxPos()] = p;
			
			display_field("\tBoss: "+ b.getLifes());		//Frame mit Boss-Leben-Anzeige aktualisieren	

			Thread.sleep(32);
			new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

			move_projectiles();

			if (ticks % 2 == 0) {	//Bei jedem zweiten Tick schießt der Boss ein Projektil
				enemy_projectile();
			}

			if (random == 1 && b.getyPos() < y_value - 2) {				//Mit 25%er Wahrscheinlichkeit (Random kann 4 verschiende Werte annehmen) und solange der Boss noch einen Platz nach unten frei hat, bewegt er sich nach unten
				field[b.getyPos()][b.getxPos()] = new game_object();
				b.setyPos(b.getyPos() + 1);
			}

			if (random == 0 && b.getyPos() > 2) {						//Siehe obere IF-Abfrage
				field[b.getyPos()][b.getxPos()] = new game_object();
				b.setyPos(b.getyPos() - 1);

			}

			field[b.getyPos()][b.getxPos()] = new game_object();	//Boss wird am Ende jedes Durchgangs gelöscht, sodass er weg ist, wenn er keine Leben mehr hat
			ticks++;
		}

		field[b.getyPos()][b.getxPos()] = new game_object();
		score += 10000;
		boss_hp += 2;
		delay *= 0.75;
		
		boss_frame=ticks+100;		//**VORSCHLAG: anstatt den abstand zwischen den Boss-Leveln immer auf 100 zu haben, diese variabel machen. z.B. + (boss_hp*100)**
		
		Random c = new Random();	//**Man muss nicht jedes mal ein neues Random Objekt erstellen um eine neue zufällige Zahl zu erzeugen, evtl bei Globalen Variablen ein Random-Objekt erstellen und dann wiederverwenden**
		int rc = c.nextInt(b_colors.length-1);	//Zufällige Farbe für nächstes Level wählen
		batchFunction(b_colors[rc]);	//Farbe übernehmen
	}

	private static void boss_intro(int delay) throws InterruptedException, IOException { //Einleitender "Tanz" des Bosses **Muss der Screen nach jeder Bewegung gecleared werden? siehe display_field**

		display_field("");
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		b.setSkin('<');
		field[p.getyPos()][p.getxPos()] = p;
		display_field("");
		Thread.sleep(delay);
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		b.setSkin('v');
		field[p.getyPos()][p.getxPos()] = p;
		display_field("");
		Thread.sleep(delay);
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		b.setSkin('>');
		field[p.getyPos()][p.getxPos()] = p;
		display_field("");
		Thread.sleep(delay);
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		b.setSkin('^');
		field[p.getyPos()][p.getxPos()] = p;
		display_field("");
		Thread.sleep(delay);
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		b.setSkin('<');
		field[p.getyPos()][p.getxPos()] = p;
		display_field("");
		Thread.sleep(delay);
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

		game_object obstacle = new game_object();
		obstacle.setCanMove(true);
		obstacle.setSkin('|');
		obstacle.setSolid(true);

		field[b.getyPos() + 3][b.getxPos() - 5] = obstacle;
		field[p.getyPos()][p.getxPos()] = p;
		display_field("");
		Thread.sleep(delay);
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor(); //haha test
		field[b.getyPos() - 3][b.getxPos() - 5] = obstacle;
		field[p.getyPos()][p.getxPos()] = p;
		display_field("");
		Thread.sleep(delay);
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
		field[b.getyPos()][b.getxPos() - 7] = obstacle;
		field[p.getyPos()][p.getxPos()] = p;
		display_field("");
		Thread.sleep(delay);
		new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();

	}

	private static void display_field(String additional_text) {	//Schreibt Objekt-Skins in einen String und gibt diesen aus & aktualisiert ActionBar mit zusätzlichem Text **Evtl. Overloading funktion ohne String Parameter erstellen** 

		//**batchfunction(cls) sollte hier jedes mal passieren, spart an anderen Stellen viel Code**
		draw_actionbar("\tLives: " + p.getLives() + "\tScore: " + score+additional_text);	//**VORSCHLAG: Leerzeichen zwischen Score und Additional einfügen?**

		String field_string = "";

		for (int i = 0; i < y_value; i++) {
			field_string += "\n";
			for (int j = 0; j < x_value; j++) {
				field_string += field[i][j].getSkin();
			}
		}
		System.out.println(field_string);
	}

	public static void generate_obstacle() {	//Erstellt Hindernis im Spiel (Wand) und spawnt diese am rechten Ende der Matrix

		Random rand = new Random();

		game_object obstacle = new game_object();
		obstacle.setCanMove(true);
		obstacle.setSkin('|');
		obstacle.setSolid(true);

		int y = rand.nextInt(y_value - 5) + 2;	//Spawnhöhe zufällig zwischen 2 und yVal-5

		if (field[y][x_value - 2] instanceof player || field[y + 1][x_value - 2] instanceof player	|| field[y + 2][x_value - 2] instanceof player) {	
			//Hindernis wird 3 Felder hoch. Wenn Spieler in einem der drei Felder ist, wird er ein Feld zurück gesetzt
			p.setxPos(p.getxPos() - 1);
		}

		field[y][x_value - 2] = obstacle;
		field[y + 1][x_value - 2] = obstacle;
		field[y + 2][x_value - 2] = obstacle;

	}

	private static void move_projectiles() {	//Geht Zeile für Zeile, angefangen bei der untersten, von rechts nach links die Matrix durch und testet auf mögliche Projektil-Kollisionen

		for (int y = y_value - 1; y > -1; y--) {
			for (int x = x_value - 1; x > -1; x--) {

				// Testen ob es sich bei Feld um Projektil handelt
				if (field[y][x] instanceof projectile) {
					if (((projectile) field[y][x]).isP_projectile() == true) {
						
						if (field[y][x + 1] instanceof enemy) {		//Gegner-Kollision: Projektil und Gegner werden zerstört, und Spieler bekommt 50 Punkte
							field[y][x] = new game_object();
							field[y][x + 1] = new game_object();
							score += 50;

						} else if (field[y][x + 1].isSolid()) {	//Solid-Kollisionen
							
							if (field[y][x + 1] instanceof boss) {	//Boss-Kollision: Projektil wird zerstört, Boss bekommt ein Leben abgezogen
								b.setLifes(b.getLifes() - 1);
								field[y][x] = new game_object();
							} else {								//Hindernis(Obstacle)-Kollision: Projektil wird gelöscht	**IDEE: Projektil wird bei jedem Vorgang gelöscht. Hier kann man irgendwie Code sparen später**
								field[y][x] = new game_object();
							}
							
						} else {								//Kollision mit leerem Raum: Projektil wird weiter nach vorne bewegt
							field[y][x + 1] = new projectile(true);
							field[y][x] = new game_object();

						}

						// Selber Test fï¿½r Gegner Projektile
					}
				}

			}
		}

		for (int y = 0; y < y_value - 1; y++) {		//Zweiter Durchgang für Boss-Projektile
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

	private static void enemy_projectile() {	//Lässt Boss schießen

		int x = b.getxPos();
		int y = b.getyPos();

		projectile pr = new projectile(false);

		if (field[y][x - 1].isSolid() == false) {
			field[y][x - 2] = pr;
		}
	}

	private static void move_field() {		//"Physik"-Engine, bewegt Objekte von rechts nach links und kümmert sich um Kollisionen | Durchläuft Matrix Zeile für Zeile, von oben nach unten, von links nach rechts | Ränder und ActionBar ausgeschlossen

		for (int i = 2; i < y_value - 1; i++) {
			for (int j = 1; j < x_value - 1; j++) {
				// Test ob gewï¿½hltes Feld ein bewegbares ist
				if (field[i][j].isCanMove()) {							//Objekt bewegbar

					// Testen ob es sich um Hinderniss handelt
					if (field[i][j].isSolid()) {						//Objekt bewegbar und solide
						if (field[i][j - 1] instanceof player) {		//Objekt bewegbar und solide + Kollision mit Spieler am linken Spielfeldrand
							if (p.getxPos() == 1) {		
								p.setLives(0);
							} else {									//Objekt bewegbar und solide + Kollision mit Spieler nicht am linken Rand
								p.setyPos(i);
								p.setxPos(j - 2);
								field[i][j - 1] = new game_object();
								field[i][j - 1] = field[i][j];
								field[i][j] = new game_object();
							}
						} else {										//Objekt bewegbar und solide + Kollision ohne Spieler
							field[i][j - 1] = field[i][j];
							field[i][j] = new game_object();
						}
					}

					// Test ob move_to Feld der Spieler ist
					else if (field[i][j - 1] instanceof player) {				//Objekt bewegbar und NICHT-solide + Kollision mit Spieler
						field[i][j] = new game_object();
						p.setLives(p.getLives() - 1);
					} else {													//Objekt bewegbar und NICHT-solide + Kollision mit anderen Objekten
						if (field[i][j - 1] instanceof projectile == false) {	//Objekt bewegbar und NICHT-solide + Kollision mit NICHT-Projektil **Bitte bei dieser IF-Abfrage die '!' Negation anstatt ==false verwenden**
							field[i][j - 1] = field[i][j];
							field[i][j] = new game_object();
						}		//Kollisionen mit Projektilen werden in move_projectiles-Funktion behandelt, deswegen ist hier kein else erforderlich
					}
				}
			}
		}

		for (int i = 0; i < y_value; i++) {	//Erste Spalte der Matrix wird geleert (Die Objekte fliegen links aus der Matrix raus
			field[i][0] = new game_object(' ');
		}

	}

	private static void random_spawn_enemy() {	//Spawnt ein Gegner-Objekt am rechten Rand der Matrix

		Random r = new Random();

		int random = r.nextInt(y_value - 3) + 2;

		if (field[random][x_value - 2] instanceof player) {	//Befindet sich der Spieler an der zu spawnenden Stelle, wird er einen Platz weiter nach links geschoben
			p.setxPos(p.getxPos() - 1);
			field[random][x_value - 2] = new enemy();

		} else {
			field[random][x_value - 2] = new enemy();
		}
	}

	public static void batchFunction(String func) {	//Führt innerhalb der Windows-Konsole eine Batch-funktion aus
		try {
			new ProcessBuilder("cmd", "/c", func).inheritIO().start().waitFor(); //Ein neuer cmd-Prozess mit dem gewünschten Befehl erbt die Ein- und Ausgabe unserer Konsolenanwendung (startet innerhalb unserer Konsole) und wartet, bis der Befehl abgeschlossen ist
		} catch (Exception E) {
			System.out.println(E);
		}
	}

	private static void move_player() {	//Erstellt 1x1 JFrame mit KeyHandler damit Eingaben über die Tastatur verarbeitet werden können
		JFrame jf = new JFrame();

		jf.setSize(1, 1);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLocationRelativeTo(null);

		jf.addKeyListener(new KeyHandler());
		jf.setVisible(true);
	}

	public static void P_UP() {	//Spieler-Bewegung **VORSCHLAG: Aus vier Funktionen eine machen**

		int x = p.getxPos();	//Aktuelle x und y Werte des Spielers

		int y = p.getyPos();

		if (field[y - 1][x].isSolid() == false) {	//Angesteuertes Feld nicht solide? Dann Steuerung möglich
			p.setxPos(x);
			p.setyPos(y - 1);
			field[y][x] = new game_object();

		}

		if (field[y - 1][x] instanceof enemy) {	//Angesteuertes Feld ist Gegner? Steuerung nicht möglich, Leben wird abgezogen und Gegner zerstört
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

	public static void P_SHOOT() {	//Spieler schießt

		projectile pr = new projectile(true);

		if (p.getShot_counter() + 4 < ticks) {	//Es ist dem Spieler nur alle 4 Ticks möglich zu schießen
			if (field[p.getyPos()][p.getxPos() + 1].isSolid() == false) {	//Kollisions-Check direkt vor dem Spieler
				field[p.getyPos()][p.getxPos() + 1] = pr;
			}
			p.setShot_counter(ticks);
		}
	}

	public static void P_PAUSE() {	//Spieler pausiert das Spiel **Vielleicht auf Spiel beenden ändern?**
		if (pause == true) {
			pause = false;
		} else {
			pause = true;
		}
	}

	@SuppressWarnings("unused")
	private static void draw_actionbar_OLD(String s) {	//ActionBar zeichnen **VERALTET, ich habe mir mal die Freiheit genommen, meine Funktion hier drunter einzufügen**

		if (s.length() < x_value) {
			for (int i = 0; i < s.length(); i++) {
				field[0][i] = new game_object(s.charAt(i));
			}
		}
	}
	
	public static void draw_actionbar(String message) {	//ActionBar zeichnen **NEU**
		int mlength = message.length();
		int padding = 0;
		
		for(int i=0; i<x_value; i++) {	//ActionBar leeren, um Ueberschreibungs-Konflikte zu vermeiden
			field[0][i] = new game_object(' ');
		}
		
		if(mlength < x_value-1) {	//Wenn die Nachricht weniger als die ganze Zeile einnimmt, wird ein Wert für die Zentrierung erstellt
			padding = ((x_value-1)-mlength)/2;
		}
		
		for(int i=0; i<mlength; i++) {
			field[0][i+padding] = new game_object(message.charAt(i));
		}
	}
}