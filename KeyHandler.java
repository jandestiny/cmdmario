package cmdmario_project;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

	@Override
	public void keyPressed(KeyEvent arg0) {

		if (arg0.getKeyCode() == KeyEvent.VK_W || arg0.getKeyCode() == KeyEvent.VK_UP) {
			Game_Loop.P_UP();
		} else if (arg0.getKeyCode() == KeyEvent.VK_S || arg0.getKeyCode() == KeyEvent.VK_DOWN) {
			Game_Loop.P_DOWN();
		} else if (arg0.getKeyCode() == KeyEvent.VK_A || arg0.getKeyCode() == KeyEvent.VK_LEFT) {
			Game_Loop.P_LEFT();
		} else if (arg0.getKeyCode() == KeyEvent.VK_D || arg0.getKeyCode() == KeyEvent.VK_RIGHT) {
			Game_Loop.P_RIGHT();
		} else if (arg0.getKeyCode() == KeyEvent.VK_SPACE) {
			Game_Loop.P_SHOOT();
		} else if (arg0.getKeyCode() == KeyEvent.VK_ESCAPE) {
			Game_Loop.P_PAUSE();
		}
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
