package cmdmario_project;

import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;

public class Game {	//**ACHTUNG: DOKUMENT GESPERRT, BITTE KEINE ueNDERUNGEN DURCHFueHREN, BEVOR WIR UNS BESPROCHEN HABEN
	//**ACHTUNG: DOKUMENT GESPERRT, BITTE KEINE ueNDERUNGEN DURCHFueHREN, BEVOR WIR UNS BESPROCHEN HABEN
	//**ACHTUNG: DOKUMENT GESPERRT, BITTE KEINE ueNDERUNGEN DURCHFueHREN, BEVOR WIR UNS BESPROCHEN HABEN
	//**ACHTUNG: DOKUMENT GESPERRT, BITTE KEINE ueNDERUNGEN DURCHFueHREN, BEVOR WIR UNS BESPROCHEN HABEN
	//**ACHTUNG: DOKUMENT GESPERRT, BITTE KEINE ueNDERUNGEN DURCHFueHREN, BEVOR WIR UNS BESPROCHEN HABEN
	//**ACHTUNG: DOKUMENT GESPERRT, BITTE KEINE ueNDERUNGEN DURCHFueHREN, BEVOR WIR UNS BESPROCHEN HABEN
	//**ACHTUNG: DOKUMENT GESPERRT, BITTE KEINE ueNDERUNGEN DURCHFueHREN, BEVOR WIR UNS BESPROCHEN HABEN
	//**ACHTUNG: DOKUMENT GESPERRT, BITTE KEINE ueNDERUNGEN DURCHFueHREN, BEVOR WIR UNS BESPROCHEN HABEN
	//**ACHTUNG: DOKUMENT GESPERRT, BITTE KEINE ueNDERUNGEN DURCHFueHREN, BEVOR WIR UNS BESPROCHEN HABEN
	
	/*
	 * Aenderungen:
	 * -init() aufgeteilt in init() + gameLoop()
	 * -gameLoop() aufgeteilt in gameLoop() + handleTicks() -> eigene Funktion f�r alle Tick-basierten Ereignisse
	 * -boss_stage wird zu boss_fight
	 * -boss_fight: Spiel wird schneller und Abstand bis zum naechsten Boss vergr�ssert sich
	 * -zweite display_field() funktion ohne Parameter hinzugefuegt
	 * -boss_intro kompakter geschrieben
	 * -p_up/down/etc.. ersetzt durch handleControls + handleActions (kompakterer Code und leichter zu erweitern)
	 * -alle 'private' Variablen und Funktionen auf 'public' gesetzt
	 * -Player.ControlsEnabled eingefügt : siehe player Klasse + handleControls()
	 * -Verhalten des Bosses nun in "bossAI"(alt)/"bossAI2"(neu) -> muss noch kommentiert werden
	 * -menu() eingefügt
	 * 
	 * -Variablen Namensänderung:
	 * 	x_value, y_value -> xLength, yLength
	 *  boss_frame -> ticksToBoss
	 *  b_colors -> cmdColors
	 *  p, b -> player, boss
	 *  move_player() -> initJFrame()
	 *  generate_field() -> initField()
	 *  random_spawn_enemy() -> spawnEnemy()
	 *  p_projectile -> Player_projectile
	 *  
	 *  -Klassen Namensaenderung:
	 *   Game_Loop -> Game
	 *   game_object - > Game_object
	 *   enemy -> Enemy
	 *   player -> Player
	 *   projectile -> Projectile
	 *   boss -> Boss
	 */

	//Einstellungen
	public static int xLength = 70;	//Breite der Spielfeld Matrix, Standard 50
	public static int yLength = 15;	//Laenge der Spielfeld Matrix, Standard 10
	public static int boss_hp = 3;	//Anzahl Leben des ersten Bosses
	public static int delay = 30;	//Zeitlicher Abstand in ms zwischen Bildschirmaktualisierungen
	public static String[] cmdColors= {"color F0","color 0F", "color 1F"};	//Verschiedene Farben nach Levelabschl�ssen
	public static int ticksToBoss = 500;	//Tick des naechsten Bosskampfes
	
	//Globale Variablen
	public static Player player = new Player();	//Player Objekt
	public static Boss boss = new Boss();	//Boss Objekt
//	public static boolean pause = false;	
	public static int score = 0;	//Anfangspunktestand
	public static int ticks = 0;	// Anzahl an Schleifen-Iterationen
	public static Game_object[][] field = new Game_object[yLength][xLength];	//Erstellung der Spielfeld-Matrix
	public static Random randomGenerator = new Random();
	public static JFrame jf;	//Ermoeglicht Zugriff auf JFrame an jeder Stelle im Programm
	public static Scanner scan = new Scanner(System.in);
	
	//Tick Variablen
	public static int field_ticks = 2;
	public static int obstacle_ticks = 20;	//default 50
	public static int score_ticks = 20;	
	public static int enemy_ticks = 12;	//default 8
	
	
	public static void main(String[] args) throws InterruptedException, IOException {
		menu();
	}
	
	public static void init() throws InterruptedException, IOException {	//Initialisierung der Spielfeld-Matrix (Hintergrund, Spielerplayer.sitionierung, Raender, Ende
		
		//****INITIALISIERUNG****
		batchFunction("cls");	//cmd fenster leeren
		batchFunction("color a");	//cmd farbe �ndern
		batchFunction("mode con: cols=" + (xLength+2) + " lines=" + (yLength+2));	//setzt richtige cmd fenster groesse
		

		//Spieler Koordinaten & leben initialisieren
		player.setyPos(yLength / 2);
		player.setxPos(4);
		player.setLives(3);
		score = 0;

		initField();	//NULL-Matrix mit Raendern sowie leeren Objekten f�llen
		jf = initJFrame();	//Erstellt JFrame für KeyHandler Input
		
		//****SPIEL-LOOP****
		gameLoop();	//Start der Spiele-Schleife

		jf.dispose();

	}
	
	public static void menu() throws InterruptedException, IOException {
		char option = ' ';
		
			batchFunction("cls");
			System.out.println("************************\nWELCOME TO CMD-MARIO V1.0\n************************\n");
			System.out.print("Play - press p\nExit - press x\n\nInput: ");
			option = scan.next().charAt(0);
			scan.nextLine();
			
			switch(option) {
			case 'p': 
				init();
			    menu();
			    break;
			
			case 'x': 	
				System.exit(0);	//Prozess des Spiels beenden
				break;
			
			default:
			}
		
	}
	
	public static void gameLoop() throws InterruptedException, IOException{
		// Spielschleife lueuft solang Spieler mehr als 0 Leben hat
				while (player.getLives() > 0) {
					
					// Bewegt Spieler-Projektile bei jedem Frame
					move_projectiles();

					// Gibt Feld aus
					display_field();
					
					//Tick-bezogene Trigger pr�fen
					handleTicks();

					// Bewegt Spieler an richtige Position
					field[player.getyPos()][player.getxPos()] = player;
					
					//JFrame fokussieren
					jf.requestFocus();

					ticks++;
					
					Thread.sleep(delay);

				}
				
				display_field("Game Over!");	//Anzeige von GameOver in ActionBar
				Thread.sleep(2000);
	}
	
	public static void handleTicks() {
		if (ticks % field_ticks == 0) {	// Bewegt alle bewegbaren Objekte eine Stelle nach links (bei jedem zweiten Frame)
			move_field();	
		}
		
		if (ticks % enemy_ticks == 0 && ticks > 10) {	// Generiert an zufuelliger Stelle Gegner
			spawnEnemy();
		}
		
		if (ticks % obstacle_ticks == 0 && ticks > 40) {	// Generiert an zufaelliger Stelle ein Hindernis
			generate_obstacle();
		}
		
//		if (ticks % score_ticks == 0) {
//			score += 10;
//		}

		if (ticks==ticksToBoss) {	//Wenn die TicksToBoss Zahl erreicht wird, startet ein BossLevel
			try {
				boss_fight();
			} catch (Exception e) {
				display_field("Boss Error");
			}
		}
	}

	public static void initField() {	//Befuellt eine NULL-Spielfeld-Matrix

		for (int i = 0; i < yLength; i++) {
			for (int j = 0; j < xLength; j++) {
				if (i == 1 || i == yLength - 1) {		//Oberer und unterer Rand '~'
					field[i][j] = new Game_object('~');
				} else if (j == xLength - 1 || j == 0) {	//Erste Spalte und letzte Spalte (Ruender ausgenommen) werden geleert -> also ' '
					field[i][j] = new Game_object(' ');	
				} else {									//Alle anderen Felder bekommen ein leeres GameObject zugeschrieben
					field[i][j] = new Game_object();
				}
			}
		}

		field[player.getyPos()][player.getxPos()] = player;	//Platziert Spieler in der Matrix, mit angegebenen x und y Positionen

	}

	public static void boss_fight() throws InterruptedException, IOException {	//Level Abschluss Boss 
		
		clear_field();	//Alle Objekte ausser den Player aus dem Bild gehen lassen

		field[player.getyPos()][player.getxPos()] = player;	//Spieler wird platziert
		
		boss_intro(400);

		while (boss.getLifes() > 0 && player.getLives() > 0) {	//Solange beide Entitaeten noch am Leben sind, laeuft der Bossfight

			field[boss.getyPos()][boss.getxPos()] = boss;	//Boss wird platziert
			field[player.getyPos()][player.getxPos()] = player;	//Spieler wird plaziert
			
			display_field("Boss: "+ boss.getLifes());		//Frame mit Boss-Leben-Anzeige aktualisieren	
			move_projectiles();

			bossAI2();
			
			field[boss.getyPos()][boss.getxPos()] = new Game_object();	//Boss wird am Ende jedes Durchgangs geloescht, sodass er weg ist, wenn er keine Leben mehr hat
			ticks++;
			
			Thread.sleep(delay);
		}
		
		//Entweder Spieler oder Boss tot, dann:
		field[boss.getyPos()][boss.getxPos()] = new Game_object(); //Boss entfernen, notwendig?
		score += 10000;	//Siegespraemie
		
		boss_hp += 2;	//Boss-Leben fuer naechsten Kampf um 2 erhoehen
		ticksToBoss = ticks+(boss_hp*100);		//Schritte bis zum n�chsten Boss erh�hen
		batchFunction(cmdColors[randomGenerator.nextInt(cmdColors.length-1)]);	//Zufaellige Farbe aus Farben-Matrix w�hlen und in Konsole uebernehmen
	
		//Spiel verschnellern nach erfolgreichem Besiegen
		
//		if(delay >= 10) {
//			delay -= 5;	//Spiel um 2ms verschnellern, solange das delay noch �ber 10ms ist
//		}
	}
	
	public static void bossAI() {	//Verhalten des Bosses im Kampf
		if (ticks % 2 == 0) {	//Bei jedem zweiten Tick schiesst der Boss ein Projektil
			enemy_projectile();
		}
		
		int random = randomGenerator.nextInt(4);
		
		if (random == 1 && boss.getyPos() < yLength - 2) {				//Mit 25%er Wahrscheinlichkeit (Random kann 4 verschiende Werte annehmen) und solange der Boss noch einen Platz nach unten frei hat, bewegt er sich nach unten
			field[boss.getyPos()][boss.getxPos()] = new Game_object();
			boss.setyPos(boss.getyPos() + 1);
		}

		if (random == 0 && boss.getyPos() > 2) {						//wie obere Abfrage, nur mit der oberen Grenze
			field[boss.getyPos()][boss.getxPos()] = new Game_object();
			boss.setyPos(boss.getyPos() - 1);
		}
	}
	
	public static void bossAI2() {	//Verhalten des Bosses im Kampf
		if (ticks % 10 == 0) {	//Bei jedem zweiten Tick schiesst der Boss ein Projektil
			enemy_projectile();
		}
		
		int random = randomGenerator.nextInt(10);
		
		if(boss.getyPos() == player.getyPos()) {
			if(!(random == 0)) {
				if(random <= 5) {
					if(boss.getyPos() < yLength - 2) {
						field[boss.getyPos()][boss.getxPos()] = new Game_object();
						boss.setyPos(boss.getyPos() + 1);
					}
				}else {
					if(boss.getyPos() > 2) {
						field[boss.getyPos()][boss.getxPos()] = new Game_object();
						boss.setyPos(boss.getyPos() - 1);
						}
					}
				}
			}
		}
	
	public static void clear_field() throws InterruptedException {
		int counter = 0;
		
		while (counter < xLength && player.getLives() > 0) {	//Alle Objekte aus der Matrix schieben ohne neue zu erstellen, sodass die Matrix frei fuer den Bossfight wird
			move_field();
			move_projectiles();
			display_field("BOSS INCOMING!");
			Thread.sleep(25);	//32ms delay, RUHE VOR DEM STURM
			field[player.getyPos()][player.getxPos()] = player;
			counter++;
		}
	}

	public static void boss_intro(int delay) throws InterruptedException, IOException { //Einleitender "Tanz" des Bosses

		player.setControlsEnabled(false);
		
		boss.setLifes(boss_hp);
		boss.setyPos(yLength / 2);		
		boss.setxPos(xLength - 5);
		field[boss.getyPos()][boss.getxPos()] = boss;	//Boss wird platziert
		display_field();
		
		for(int i=0; i<4; i++) {	//For-Schleife fuer IntroTanz
			
			if(i==0 || i==3) {
				boss.setSkin('<');
			}else if(i == 1) {
				boss.setSkin('v');
			}else if(i == 2) {
				boss.setSkin('^');
			}
			
			field[player.getyPos()][player.getxPos()] = player;
			display_field();
			Thread.sleep(500);
			
		}

		Game_object obstacle = new Game_object();
		obstacle.setCanMove(true);
		obstacle.setSkin('|');
		obstacle.setSolid(true);

		field[boss.getyPos() + 3][boss.getxPos() - 5] = obstacle;
		field[player.getyPos()][player.getxPos()] = player;
		display_field();
		field[boss.getyPos() - 3][boss.getxPos() - 5] = obstacle;
		field[player.getyPos()][player.getxPos()] = player;
		display_field();
		field[boss.getyPos()][boss.getxPos() - 7] = obstacle;
		field[player.getyPos()][player.getxPos()] = player;
		display_field();
		
		player.setControlsEnabled(true);

	}

	public static void display_field(String additional_text) {	//Schreibt Objekt-Skins in einen String und gibt diesen aus & aktualisiert ActionBar mit zusuetzlichem Text 

		batchFunction("cls");	//Konsolenfenster leeren
		draw_actionbar("Lives: " + player.getLives() + "     Score: " + score + "      " + additional_text);	

		String field_string = "";

		for (int i = 0; i < yLength; i++) {
			field_string += "\n";
			for (int j = 0; j < xLength; j++) {
				field_string += field[i][j].getSkin();
			}
		}
		
		System.out.print(field_string);
	}
	
	public static void display_field(){	//Schreibt Objekt-Skins in einen String und gibt diesen aus & aktualisiert ActionBar mit zusuetzlichem Text  

		batchFunction("cls");	//Konsolenfenster leeren
		draw_actionbar("Lives: " + player.getLives() + "     Score: " + score);	

		String field_string = "";

		for (int i = 0; i < yLength; i++) {
			field_string += "\n";
			for (int j = 0; j < xLength; j++) {
				field_string += field[i][j].getSkin();
			}
		}
		
		System.out.print(field_string);
	}

	public static void generate_obstacle() {	//Erstellt Hindernis im Spiel (Wand) und spawnt diese am rechten Ende der Matrix

		Random rand = new Random();

		Game_object obstacle = new Game_object();
		obstacle.setCanMove(true);
		obstacle.setSkin('|');
		obstacle.setSolid(true);

		int y = rand.nextInt(yLength - 5) + 2;	//Spawnhoehe zufuellig zwischen 2 und yVal-5

		if (field[y][xLength - 2] instanceof Player || field[y + 1][xLength - 2] instanceof Player	|| field[y + 2][xLength - 2] instanceof Player) {	
			//Hindernis wird 3 Felder hoch. Wenn Spieler in einem der drei Felder ist, wird er ein Feld zurueck gesetzt
			player.setxPos(player.getxPos() - 1);
		}

		field[y][xLength - 2] = obstacle;
		field[y + 1][xLength - 2] = obstacle;
		field[y + 2][xLength - 2] = obstacle;

	}

	public static void move_projectiles() {	//Geht Zeile fuer Zeile, angefangen bei der untersten, von rechts nach links die Matrix durch und testet auf muegliche Projektil-Kollisionen

		for (int y = yLength - 1; y > -1; y--) {
			for (int x = xLength - 1; x > -1; x--) {

				// Testen ob es sich bei Feld um Projektil handelt
				if (field[y][x] instanceof Projectile) {
					if (((Projectile) field[y][x]).isPlayer_projectile() == true) {
						
						if (field[y][x + 1] instanceof Enemy) {		//Gegner-Kollision: Projektil und Gegner werden zerstuert, und Spieler bekommt 50 Punkte
							field[y][x] = new Game_object();
							field[y][x + 1] = new Game_object();
							score += 50;

						} else if (field[y][x + 1].isSolid()) {	//Solid-Kollisionen
							
							if (field[y][x + 1] instanceof Boss) {	//Boss-Kollision: Projektil wird zerstuert, Boss bekommt ein Leben abgezogen
								boss.setLifes(boss.getLifes() - 1);
								field[y][x] = new Game_object();
							} else {								//Hindernis(Obstacle)-Kollision: Projektil wird geluescht	**IDEE: Projektil wird bei jedem Vorgang geluescht. Hier kann man irgendwie Code sparen spueter**
								field[y][x] = new Game_object();
							}
							
						} else {								//Kollision mit leerem Raum: Projektil wird weiter nach vorne bewegt
							field[y][x + 1] = new Projectile(true);
							field[y][x] = new Game_object();

						}

						// Selber Test fuer Gegner Projektile
					}
				}

			}
		}

		for (int y = 0; y < yLength - 1; y++) {		//Zweiter Durchgang fuer Boss-Projektile
			for (int x = 1; x < xLength - 1; x++) {
				if (field[y][x] instanceof Projectile) {
					if (((Projectile) field[y][x]).isPlayer_projectile() == false) {
						if (field[y][x - 1] instanceof Player) {
							field[y][x] = new Game_object();
							player.setLives(player.getLives() - 1);

						} else if (field[y][x - 1].isSolid()) {
							field[y][x] = new Game_object();

						} else {
							field[y][x - 1] = new Projectile(false);
							field[y][x] = new Game_object();

						}

					}
				}
			}
		}

	}

	public static void enemy_projectile() {	//Laesst Boss schiessen

		int x = boss.getxPos();
		int y = boss.getyPos();

		Projectile pr = new Projectile(false);

		if (field[y][x - 1].isSolid() == false) {
			field[y][x - 2] = pr;
		}
	}

	public static void move_field() {		//"Physik"-Engine, bewegt Objekte von rechts nach links und kuemmert sich um Kollisionen | Durchlueuft Matrix Zeile fuer Zeile, von oben nach unten, von links nach rechts | Ruender und ActionBar ausgeschlossen

		for (int i = 2; i < yLength - 1; i++) {
			for (int j = 1; j < xLength - 1; j++) {
				// Test oboss.gewuehltes Feld ein bewegbares ist
				if (field[i][j].isCanMove()) {							//Objekt bewegbar

					// Testen ob es sich um Hinderniss handelt
					if (field[i][j].isSolid()) {						//Objekt bewegbar und solide
						if (field[i][j - 1] instanceof Player) {		//Objekt bewegbar und solide + Kollision mit Spieler am linken Spielfeldrand
							if (player.getxPos() == 1) {		
								player.setLives(0);
							} else {									//Objekt bewegbar und solide + Kollision mit Spieler nicht am linken Rand
								player.setyPos(i);
								player.setxPos(j - 2);
								field[i][j - 1] = new Game_object();
								field[i][j - 1] = field[i][j];
								field[i][j] = new Game_object();
							}
						} else {										//Objekt bewegbar und solide + Kollision ohne Spieler
							field[i][j - 1] = field[i][j];
							field[i][j] = new Game_object();
						}
					}

					// Test ob move_to Feld der Spieler ist
					else if (field[i][j - 1] instanceof Player) {				//Objekt bewegbar und NICHT-solide + Kollision mit Spieler
						field[i][j] = new Game_object();
						player.setLives(player.getLives() - 1);
					} else {													//Objekt bewegbar und NICHT-solide + Kollision mit anderen Objekten
						if (!(field[i][j - 1] instanceof Projectile)) {	//Objekt bewegbar und NICHT-solide + Kollision mit NICHT-Projektil 
							field[i][j - 1] = field[i][j];
							field[i][j] = new Game_object();
						}		//Kollisionen mit Projektilen werden in move_projectiles-Funktion behandelt, deswegen ist hier kein else erforderlich
					}
				}
			}
		}

		for (int i = 0; i < yLength; i++) {	//Erste Spalte der Matrix wird geleert (Die Objekte fliegen links aus der Matrix raus
			field[i][0] = new Game_object(' ');
		}

	}

	public static void spawnEnemy() {	//Spawnt ein Gegner-Objekt am rechten Rand der Matrix

		Random r = new Random();

		int random = r.nextInt(yLength - 3) + 2;

		if (field[random][xLength - 2] instanceof Player) {	//Befindet sich der Spieler an der zu spawnenden Stelle, wird er einen Platz weiter nach links geschoben
			player.setxPos(player.getxPos() - 1);
			field[random][xLength - 2] = new Enemy();

		} else {
			field[random][xLength - 2] = new Enemy();
		}
	}

	public static void batchFunction(String func) {	//Fuehrt innerhalb der Windows-Konsole eine Batch-funktion aus
		try {
			new ProcessBuilder("cmd", "/c", func).inheritIO().start().waitFor(); //Ein neuer cmd-Prozess mit dem gewuenschten Befehl erbt die Ein- und Ausgabe unserer Konsolenanwendung (startet innerhalb unserer Konsole) und wartet, bis der Befehl abgeschlossen ist
		} catch (Exception E) {
			System.out.println(E);
		}
	}

	public static JFrame initJFrame() {	//Erstellt 1x1 JFrame mit KeyHandler damit Eingaben ueber die Tastatur verarbeitet werden koennen
		JFrame jf = new JFrame();
		jf.isAlwaysOnTop();
		jf.setSize(1,1);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setLocation(0, 0); //positioniert jframe oben links
		jf.addKeyListener(new KeyHandler());
		jf.requestFocus();
		jf.setVisible(true);
		return jf;
	}

	public static void handleControls(char direction) {
		int addToX = 0;
		int addToY = 0;
		
		if(!player.getControlsEnabled()) {
			direction = 'x';	//Wenn Controls nicht enabled sind, dann wird der Parameter direction auf 'x' geaendert, sodass der switch defaulted und keine Richtungsaenderung passiert
		}
		
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
		
		int x = player.getxPos();	//Aktuelle x und y Werte des Spielers
		int y = player.getyPos();

		if (field[y + addToY][x + addToX].isSolid() == false) {	//Angesteuertes Feld nicht solide? Dann Steuerung mueglich
			player.setxPos(x + addToX);
			player.setyPos(y + addToY);
			field[y][x] = new Game_object();
		}

		if (field[y + addToY][x] instanceof Enemy) {	//Angesteuertes Feld ist Gegner? Steuerung nicht mueglich, Leben wird abgezogen und Gegner zerstuert
			player.setLives(player.getLives() - 1);
			field[y + addToY][x + addToX] = new Game_object();
		}
		
	}

	public static void handleActions(char action) {
		
		switch(action) {
		
		case 's':	//shoot
			Projectile pr = new Projectile(true);

			if (player.getShot_counter() + 6 < ticks) {	//Es ist dem Spieler nur alle 4 Ticks mueglich zu schieueen
				if (field[player.getyPos()][player.getxPos() + 1].isSolid() == false) {	//Kollisions-Check direkt vor dem Spieler
					field[player.getyPos()][player.getxPos() + 1] = pr;
				}
				player.setShot_counter(ticks);
			}
			
		break;
		
		case 'x':	//exit
			player.setLives(0);
		break;
		default:
		}
	}
	
	public static void draw_actionbar(String message) {	//ActionBar zeichnen
		int mlength = message.length();
		int padding = 0;
		
		for(int i=0; i<xLength; i++) {	//ActionBar leeren, um Ueberschreibungs-Konflikte zu vermeiden
			field[0][i] = new Game_object(' ');
		}
		
		if(mlength < xLength-1) {	//Wenn die Nachricht weniger als die ganze Zeile einnimmt, wird ein Wert fuer die Zentrierung erstellt
			padding = ((xLength-1)-mlength)/2;
		}
		
		for(int i=0; i<mlength; i++) {
			field[0][i+padding] = new Game_object(message.charAt(i));
		}
	}
}