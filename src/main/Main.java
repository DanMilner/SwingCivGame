package main;

import java.io.IOException;

import javax.swing.SwingUtilities;

public class Main {
	public static void main(String[] args) {
		Game game = new Game();
//		GuiManager GUI = new GuiManager(game);

		SwingUtilities.invokeLater(() -> {
            try {
                new GuiManager(game);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
	}
}
