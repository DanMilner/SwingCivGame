package main;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BoardPanel extends JPanel {

    private BufferedImage imageTop;

    BoardPanel(int MAPSIZE) throws IOException {
        imageTop = ImageIO.read(new File("textures\\backgrounds\\OldMap4.jpg"));
        this.setLayout(new BorderLayout());
        this.setPreferredSize(new Dimension(MAPSIZE*50, MAPSIZE*50));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(imageTop, 0, 0, null);
    }
}
