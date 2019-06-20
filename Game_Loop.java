package cmdmario_project;

import java.io.IOException;
import java.util.Random;
import javax.swing.JFrame;

public class Game_Loop {	//**ACHTUNG: DOKUMENT GESPERRT, BITTE KEINE ueNDERUNGEN DURCHFueHREN, BEVOR WIR UNS BESPROCHEN HABEN
	//**ACHTUNG: DOKUMENT GESPERRT, BITTE KEINE ueNDERUNGEN DURCHFueHREN, BEVOR WIR UNS BESPROCHEN HABEN
	//**ACHTUNG: DOKUMENT GESPERRT, BITTE KEINE ueNDERUNGEN DURCHFueHREN, BEVOR WIR UNS BESPROCHEN HABEN
	//**ACHTUNG: DOKUMENT GESPERRT, BITTE KEINE ueNDERUNGEN DURCHFueHREN, BEVOR WIR UNS BESPROCHEN HABEN
	//**ACHTUNG: DOKUMENT GESPERRT, BITTE KEINE ueNDERUNGEN DURCHFueHREN, BEVOR WIR UNS BESPROCHEN HABEN
	//**ACHTUNG: DOKUMENT GESPERRT, BITTE KEINE ueNDERUNGEN DURCHFueHREN, BEVOR WIR UNS BESPROCHEN HABEN
	//**ACHTUNG: DOKUMENT GESPERRT, BITTE KEINE ueNDERUNGEN DURCHFueHREN, BEVOR WIR UNS BESPROCHEN HABEN
	//**ACHTUNG: DOKUMENT GESPERRT, BITTE KEINE ueNDERUNGEN DURCHFueHREN, BEVOR WIR UNS BESPROCHEN HABEN
	//**ACHTUNG: DOKUMENT GESPERRT, BITTE KEINE ueNDERUNGEN DURCHFueHREN, BEVOR WIR UNS BESPROCHEN HABEN
	
	//**Allgemeine Frage: Wieso sind einige Funktionen/Variablen private und einige nicht? Eigentlich sollten alle public sein, spricht nichts dagegen, oder?**
	//**Variablen Namen muessen DRINGEND ueberarbeitet werden, sind teilweise zu unverstuendlich
	
	/*
	 * Aenderungen:
	 * -init() aufgeteilt in init() + gameLoop()
	 * -gameLoop() aufgeteilt in gameLoop() + handleTicks() -> eigene Funktion für alle Tick-basierten Ereignisse
	 * -boss_stage wird zu boss_fight
	 * -boss_fight: Spiel wird schneller und Abstand bis zum naechsten Boss vergrössert sich
	 * -zweite display_field() funktion ohne Parameter hinzugefuegt
	 * -boss_intro kompakter geschrieben
	 * -p_up/down/etc.. ersetzt durch handleControls + handleActions (kompakterer Code und leichter zu erweitern)
	 */

	//Einstellungen
	public static int x_value = 70;	//Breite der Spielfeld Matrix, Standard 50
	public static int y_value = 15;	//Laenge der Spielfeld Matrix, Standard 10
	public static int boss_hp = 3;	//Anzahl Leben des ersten Bosses
	public static int delay = 25;	//Zeitlicher Abstand in ms zwischen Bildschirmaktualisierungen
	public static String[] b_colors= {"color F0","color 0F", "color 1F"};	//Verschiedene Farben nach Levelabschlüssen
	public static int boss_frame = 100;	//Tick des naechsten Bosskampfes
	
	//Globale Variablen
	public static player p = new player();	//Player Objekt
	public static boss b = new boss();	//Boss Objekt
//	public static boolean pause = false;	
	public static int score = 0;	//Anfangspunktestand
	public static int ticks = 0;	// Anzahl an Schleifen-Iterationen
	public static game_object[][] field = new game_object[y_value][x_value];	//Erstellung der Spielfeld-Matrix
	public static Random randomGenerator = new Random();
	
	
	public static void main(String[] args) throws InterruptedException, IOException {
		init();
	}
	
	public static void init() throws InterruptedException, IOException {	//Initialisierung der Spielfeld-Matrix (Hintergrund, Spielerpositionierung, Raender, Ende
		//VORSCHLAG: init und gameLoop trennen, sonst passt der name nicht wirklich DONE
		
		//****INITIALISIERUNG****
		
		batchFunction("color a");	//cmd farbe ändern
		batchFunction("mode con: cols=" + (x_value+2) + " lines=" + (y_value+2));	//setzt richtige cmd groesse
		move_player();	//JFrame fuer KeyHandler einrichten

		//Spieler Koordinaten initialisieren
		p.setyPos(y_value / 2);
		p.setxPos(4);

		generate_field();	//NULL-Matrix mit Raendern sowie leeren Objekten füllen
		
		
		//****SPIEL-LOOP****
		gameLoop();	//Start der Spiele-Schleife

		//****ENDE****
		display_field("Game Over!");	//Anzeige von GameOver in ActionBar
		batchFunction("PAUSE");	//Auf Eingabe warten, bevor sich die Konsole schließt
		System.exit(0);	//Prozess des Spiels beenden

	}
	
	public static void gameLoop() throws InterruptedException, IOException{
		// Spielschleife lueuft solang Spieler mehr als 0 Leben hat
				while (p.getLives() > 0) {
					
					// Bewegt Spieler-Projektile bei jedem Frame
					move_projectiles();

					// Gibt Feld aus
					display_field();
					
					//Tick-bezogene Trigger prüfen
					handleTicks();

					// Bewegt Spieler an richtige Position
					field[p.getyPos()][p.getxPos()] = p;

					ticks++;

				}
	}
	
	public static void handleTicks() {
		if (ticks % 2 == 0) {	// Bewegt alle bewegbaren Objekte eine Stelle nach links (bei jedem zweiten Frame)
			move_field();	
		}
		
		if (ticks % 8 == 0 && ticks > 10) {	// Generiert an zufuelliger Stelle Gegner
			random_spawn_enemy();
		}
		
		if (ticks % 50 == 0 && ticks > 40) {	// Generiert an zufuelliger Stelle ein Hinderniss
			generate_obstacle();
		}
		
		if (ticks % 20 == 0) {
			score += 10;
		}

		if (ticks==boss_frame) {	//Wenn die Boss_frame Zahl erreicht wird, startet ein BossLevel
			try {
				boss_fight();
			} catch (Exception e) {
				display_field("Boss Error");
			}
		}
	}

	public static void generate_field() {	//Befuellt eine NULL-Spielfeld-Matrix

		for (int i = 0; i < y_value; i++) {
			for (int j = 0; j < x_value; j++) {
				if (i == 1 || i == y_value - 1) {		//Oberer und unterer Rand '~'
					field[i][j] = new game_object('~');
				} else if (j == x_value - 1 || j == 0) {	//Erste Spalte und letzte Spalte (Ruender ausgenommen) werden geleert -> also ' '
					field[i][j] = new game_object(' ');	//**Nach game_object Logik, wuere das hier ein Solides leeres Feld? Ist das so gedacht?**
				} else {									//Alle anderen Felder bekommen ein leeres GameObject zugeschrieben
					field[i][j] = new game_object();
				}
			}
		}

		field[p.getyPos()][p.getxPos()] = p;	//Platziert Spieler in der Matrix, mit angegebenen x und y Positionen

	}

	public static void boss_fight() throws InterruptedException, IOException {	//Level Abschluss Boss 
		
		clearField();	//Alle Objekte ausser den Player aus dem Bild gehen lassen

		field[p.getyPos()][p.getxPos()] = p;	//Spieler wird platziert
		
		boss_intro(400);

		while (b.getLifes() > 0 && p.getLives() > 0) {	//Solange beide Entitaeten noch am Leben sind, laeuft der Bossfight

			field[b.getyPos()][b.getxPos()] = b;	//Boss wird platziert
			field[p.getyPos()][p.getxPos()] = p;	//Spieler wird plaziert
			
			display_field("Boss: "+ b.getLifes());		//Frame mit Boss-Leben-Anzeige aktualisieren	
			move_projectiles();

			if (ticks % 2 == 0) {	//Bei jedem zweiten Tick schiesst der Boss ein Projektil
				enemy_projectile();
			}
			
			int random = randomGenerator.nextInt(4);
			
			if (random == 1 && b.getyPos() < y_value - 2) {				//Mit 25%er Wahrscheinlichkeit (Random kann 4 verschiende Werte annehmen) und solange der Boss noch einen Platz nach unten frei hat, bewegt er sich nach unten
				field[b.getyPos()][b.getxPos()] = new game_object();
				b.setyPos(b.getyPos() + 1);
			}

			if (random == 0 && b.getyPos() > 2) {						//wie obere Abfrage, nur mit der oberen Grenze
				field[b.getyPos()][b.getxPos()] = new game_object();
				b.setyPos(b.getyPos() - 1);
			}

			field[b.getyPos()][b.getxPos()] = new game_object();	//Boss wird am Ende jedes Durchgangs geloescht, sodass er weg ist, wenn er keine Leben mehr hat
			ticks++;
		}
		
		//Entweder Spieler oder Boss tot, dann:
		field[b.getyPos()][b.getxPos()] = new game_object(); //Boss entfernen, notwendig?
		score += 10000;	//Siegespraemie
		boss_hp += 2;	//Boss-Leben fuer naechsten Kampf um 2 erhoehen
		boss_frame = ticks+(boss_hp*100);		//Schritte bis zum nächsten Boss erhöhen
		batchFunction(b_colors[randomGenerator.nextInt(b_colors.length-1)]);	//Zufaellige Farbe aus Farben-Matrix wählen und in Konsole uebernehmen
	
		//Spiel verschnellern nach erfolgreichem Besiegen
		if(delay >= 10) {
			delay -= 5;	//Spiel um 2ms verschnellern, soalnge das delay noch über 10ms ist
		}
	}
	
	public static void clearField() throws InterruptedException {
		int counter = 0;
		
		while (counter < x_value && p.getLives() > 0) {	//Alle Objekte aus der Matrix schieben ohne neue zu erstellen, sodass die Matrix frei fuer den Bossfight wird
			move_field();
			move_projectiles();
			display_field();
			Thread.sleep(25);	//32ms delay, RUHE VOR DEM STURM
			field[p.getyPos()][p.getxPos()] = p;
			counter++;
		}
	}

	public static void boss_intro(int delay) throws InterruptedException, IOException { //Einleitender "Tanz" des Bosses **Muss der Screen nach jeder Bewegung gecleared werden? siehe display_field** ****Flag für "Spieler kann sich bewegen" einrichten und hier flase setzen

		b.setLifes(boss_hp);
		b.setyPos(y_value / 2);		
		b.setxPos(x_value - 5);
		field[b.getyPos()][b.getxPos()] = b;	//Boss wird platziert
		display_field();
		
		for(int i=0; i<4; i++) {	//For-Schleife fuer IntroTanz
			
			if(i==0 || i==3) {
				b.setSkin('<');
			}else if(i == 1) {
				b.setSkin('v');
			}else if(i == 2) {
				b.setSkin('^');
			}
			
			field[p.getyPos()][p.getxPos()] = p;
			display_field();
			Thread.sleep(500);
			
		}

		game_object obstacle = new game_object();
		obstacle.setCanMove(true);
		obstacle.setSkin('|');
		obstacle.setSolid(true);

		field[b.getyPos() + 3][b.getxPos() - 5] = obstacle;
		field[p.getyPos()][p.getxPos()] = p;
		display_field();
		field[b.getyPos() - 3][b.getxPos() - 5] = obstacle;
		field[p.getyPos()][p.getxPos()] = p;
		display_field();
		field[b.getyPos()][b.getxPos() - 7] = obstacle;
		field[p.getyPos()][p.getxPos()] = p;
		display_field();

	}

	public static void display_field(String additional_text) {	//Schreibt Objekt-Skins in einen String und gibt diesen aus & aktualisiert ActionBar mit zusuetzlichem Text **Evtl. Overloading funktion ohne String Parameter erstellen** DONE

		batchFunction("cls");	//Konsolenfenster leeren
		draw_actionbar("Lives: " + p.getLives() + "     Score: " + score + "      " + additional_text);	//**VORSCHLAG: Leerzeichen zwischen Score und Additional einfuegen?** DONE

		String field_string = "";

		for (int i = 0; i < y_value; i++) {
			field_string += "\n";
			for (int j = 0; j < x_value; j++) {
				field_string += field[i][j].getSkin();
			}
		}
		
		System.out.print(field_string);
		
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void display_field(){	//Schreibt Objekt-Skins in einen String und gibt diesen aus & aktualisiert ActionBar mit zusuetzlichem Text **Evtl. Overloading funktion ohne String Parameter erstellen** 

		batchFunction("cls");	//Konsolenfenster leeren
		draw_actionbar("Lives: " + p.getLives() + "     Score: " + score);	//**VORSCHLAG: Leerzeichen zwischen Score und Additional einfuegen?** DONE

		String field_string = "";

		for (int i = 0; i < y_value; i++) {
			field_string += "\n";
			for (int j = 0; j < x_value; j++) {
				field_string += field[i][j].getSkin();
			}
		}
		
		System.out.print(field_string);
		
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void generate_obstacle() {	//Erstellt Hindernis im Spiel (Wand) und spawnt diese am rechten Ende der Matrix

		Random rand = new Random();

		game_object obstacle = new game_object();
		obstacle.setCanMove(true);
		obstacle.setSkin('|');
		obstacle.setSolid(true);

		int y = rand.nextInt(y_value - 5) + 2;	//Spawnhoehe zufuellig zwischen 2 und yVal-5

		if (field[y][x_value - 2] instanceof player || field[y + 1][x_value - 2] instanceof player	|| field[y + 2][x_value - 2] instanceof player) {	
			//Hindernis wird 3 Felder hoch. Wenn Spieler in einem der drei Felder ist, wird er ein Feld zurueck gesetzt
			p.setxPos(p.getxPos() - 1);
		}

		field[y][x_value - 2] = obstacle;
		field[y + 1][x_value - 2] = obstacle;
		field[y + 2][x_value - 2] = obstacle;

	}

	private static void move_projectiles() {	//Geht Zeile fuer Zeile, angefangen bei der untersten, von rechts nach links die Matrix durch und testet auf muegliche Projektil-Kollisionen

		for (int y = y_value - 1; y > -1; y--) {
			for (int x = x_value - 1; x > -1; x--) {

				// Testen ob es sich bei Feld um Projektil handelt
				if (field[y][x] instanceof projectile) {
					if (((projectile) field[y][x]).isP_projectile() == true) {
						
						if (field[y][x + 1] instanceof enemy) {		//Gegner-Kollision: Projektil und Gegner werden zerstuert, und Spieler bekommt 50 Punkte
							field[y][x] = new game_object();
							field[y][x + 1] = new game_object();
							score += 50;

						} else if (field[y][x + 1].isSolid()) {	//Solid-Kollisionen
							
							if (field[y][x + 1] instanceof boss) {	//Boss-Kollision: Projektil wird zerstuert, Boss bekommt ein Leben abgezogen
								b.setLifes(b.getLifes() - 1);
								field[y][x] = new game_object();
							} else {								//Hindernis(Obstacle)-Kollision: Projektil wird geluescht	**IDEE: Projektil wird bei jedem Vorgang geluescht. Hier kann man irgendwie Code sparen spueter**
								field[y][x] = new game_object();
							}
							
						} else {								//Kollision mit leerem Raum: Projektil wird weiter nach vorne bewegt
							field[y][x + 1] = new projectile(true);
							field[y][x] = new game_object();

						}

						// Selber Test fuer Gegner Projektile
					}
				}

			}
		}

		for (int y = 0; y < y_value - 1; y++) {		//Zweiter Durchgang fuer Boss-Projektile
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

	private static void enemy_projectile() {	//Luesst Boss schieueen

		int x = b.getxPos();
		int y = b.getyPos();

		projectile pr = new projectile(false);

		if (field[y][x - 1].isSolid() == false) {
			field[y][x - 2] = pr;
		}
	}

	private static void move_field() {		//"Physik"-Engine, bewegt Objekte von rechts nach links und kuemmert sich um Kollisionen | Durchlueuft Matrix Zeile fuer Zeile, von oben nach unten, von links nach rechts | Ruender und ActionBar ausgeschlossen

		for (int i = 2; i < y_value - 1; i++) {
			for (int j = 1; j < x_value - 1; j++) {
				// Test ob gewuehltes Feld ein bewegbares ist
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

	public static void batchFunction(String func) {	//Fuehrt innerhalb der Windows-Konsole eine Batch-funktion aus
		try {
			new ProcessBuilder("cmd", "/c", func).inheritIO().start().waitFor(); //Ein neuer cmd-Prozess mit dem gewuenschten Befehl erbt die Ein- und Ausgabe unserer Konsolenanwendung (startet innerhalb unserer Konsole) und wartet, bis der Befehl abgeschlossen ist
		} catch (Exception E) {
			System.out.println(E);
		}
	}

	private static void move_player() {	//Erstellt 1x1 JFrame mit KeyHandler damit Eingaben ueber die Tastatur verarbeitet werden kuennen
		// BITTE NAMEN Ã„NDERN (zb. initJFrame)
		JFrame jf = new JFrame();

//		jf.setSize(1, 1);
//		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		jf.setLocationRelativeTo(null);
//
//		jf.addKeyListener(new KeyHandler());
//		jf.setVisible(true);
		
		jf.setSize(1,1);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLocation(0, 0); //positioniert jframe oben links
		jf.addKeyListener(new KeyHandler());
		jf.requestFocus();
		jf.setVisible(true);
	}

	public static void handleControls(char direction) {
		int addToX = 0;
		int addToY = 0;
		
		switch(direction) {
		case 'w':	addToY = -1;
		break;
		
		case 's':	addToY = 1;
		break;
		
		case 'a':	addToX = -1;
		break;
		
		case 'd':	addToX = 1;
		break;
		
		default:
		}
		
		int x = p.getxPos();	//Aktuelle x und y Werte des Spielers
		int y = p.getyPos();

		if (field[y + addToY][x + addToX].isSolid() == false) {	//Angesteuertes Feld nicht solide? Dann Steuerung mueglich
			p.setxPos(x + addToX);
			p.setyPos(y + addToY);
			field[y][x] = new game_object();
		}

		if (field[y + addToY][x] instanceof enemy) {	//Angesteuertes Feld ist Gegner? Steuerung nicht mueglich, Leben wird abgezogen und Gegner zerstuert
			p.setLives(p.getLives() - 1);
			field[y + addToY][x + addToX] = new game_object();
		}
		
	}

	public static void handleActions(char action) {
		
		switch(action) {
		
		case 's':	//shoot
			projectile pr = new projectile(true);

			if (p.getShot_counter() + 4 < ticks) {	//Es ist dem Spieler nur alle 4 Ticks mueglich zu schieueen
				if (field[p.getyPos()][p.getxPos() + 1].isSolid() == false) {	//Kollisions-Check direkt vor dem Spieler
					field[p.getyPos()][p.getxPos() + 1] = pr;
				}
				p.setShot_counter(ticks);
			}
			
		break;
		
		case 'x':	//exit
			display_field("Good Bye!");
			System.exit(0);
		break;
		default:
		}
	}
	
	public static void draw_actionbar(String message) {	//ActionBar zeichnen **NEU**
		int mlength = message.length();
		int padding = 0;
		
		for(int i=0; i<x_value; i++) {	//ActionBar leeren, um Ueberschreibungs-Konflikte zu vermeiden
			field[0][i] = new game_object(' ');
		}
		
		if(mlength < x_value-1) {	//Wenn die Nachricht weniger als die ganze Zeile einnimmt, wird ein Wert fuer die Zentrierung erstellt
			padding = ((x_value-1)-mlength)/2;
		}
		
		for(int i=0; i<mlength; i++) {
			field[0][i+padding] = new game_object(message.charAt(i));
		}
	}
}