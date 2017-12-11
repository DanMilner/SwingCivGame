package main;

import java.io.IOException;

import javax.swing.SwingUtilities;

public class Main {
	public static void main(String[] args) {
		Game game = new Game();
//		CivGUI GUI = new CivGUI(game);

		SwingUtilities.invokeLater(() -> {
            try {
                new CivGUI(game);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
	}
}
