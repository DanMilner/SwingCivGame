package main;

import java.io.IOException;

import javax.swing.SwingUtilities;

public class Main {
	public static void main(String[] args) {
		Game game = new Game();
//		GuiManager GUI = new GuiManager(game);

		SwingUtilities.invokeLater(() -> {
            new GuiManager(game);
        });
	}
}
