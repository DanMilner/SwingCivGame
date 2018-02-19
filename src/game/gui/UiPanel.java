package game.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class UiPanel extends JPanel {

    UiPanel() {
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(1950, 125));
    }

    @Override
    protected void paintComponent(Graphics g) {
        BufferedImage backgroundImage = null;
        try {
            backgroundImage = ImageIO.read(new File("textures\\backgrounds\\UI_texture.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, null);
    }
}
