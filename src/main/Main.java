package main;

import javax.swing.SwingUtilities;

public class Main {
	public static void main(String[] args) {
		GameController gameController = new GameController();
//		GuiManager GUI = new GuiManager(gameController);

		SwingUtilities.invokeLater(() -> new GuiManager(gameController));
	}
}
