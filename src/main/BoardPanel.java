package main;

import javax.swing.*;
import java.awt.*;

class BoardPanel extends JPanel {
    BoardPanel(int MAPSIZE) {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(MAPSIZE * 50, MAPSIZE * 50));
    }
}
