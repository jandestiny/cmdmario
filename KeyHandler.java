package cmdmario_project;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {
	
	/*
	 * Aenderungen:
	 * -Auf neue Eingabefunktionen umgestellt
	 * -Spam-Schutz eingebaut (Wenn der letzte Tastendruck weniger als 50ms her ist, wird er nicht ausgefuehrt)
	 */
	
	double time = System.currentTimeMillis();

	@Override
	public void keyPressed(KeyEvent arg0) {
		if(System.currentTimeMillis()-time < 50) {
			//Anti-Spam
		}else {
			if (arg0.getKeyCode() == KeyEvent.VK_W || arg0.getKeyCode() == KeyEvent.VK_UP) {
			//Game_Loop.P_UP();
			Game.handleControls('w');
		} else if (arg0.getKeyCode() == KeyEvent.VK_S || arg0.getKeyCode() == KeyEvent.VK_DOWN) {
			//Game_Loop.P_DOWN();
			Game.handleControls('s');
		} else if (arg0.getKeyCode() == KeyEvent.VK_A || arg0.getKeyCode() == KeyEvent.VK_LEFT) {
			//Game_Loop.P_LEFT();
			Game.handleControls('a');
		} else if (arg0.getKeyCode() == KeyEvent.VK_D || arg0.getKeyCode() == KeyEvent.VK_RIGHT) {
			//Game_Loop.P_RIGHT();
			Game.handleControls('d');
		} else if (arg0.getKeyCode() == KeyEvent.VK_SPACE) {
			//Game_Loop.P_SHOOT();
			Game.handleActions('s');
		} else if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
			//Game_Loop.P_PAUSE();
			Game.handleActions('x');
		}
		}

		time = System.currentTimeMillis();
		
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub

	}

}
