package map.buildings;

import javax.swing.*;

public class Road extends Building {
    public Road() {
        this.buildingImage = new ImageIcon("textures/roads/Road.png");
        this.type = "Road";
        this.borderSize = 0;
        this.maxHealth = 0;
        this.currentHealth = maxHealth;
    }

    public ImageIcon getImage(boolean North, boolean South, boolean East, boolean West) {
        if (North && South && East && West) {
            return new ImageIcon("textures\\roads\\CrossRoad.jpg");
        } else if (North && South && East) {
            return new ImageIcon("textures\\roads\\TriRight.jpg");
        } else if (North && South && West) {
            return new ImageIcon("textures\\roads\\TriLeft.jpg");
        } else if (North && East && West) {
            return new ImageIcon("textures\\roads\\TriTop.jpg");
        } else if (South && East && West) {
            return new ImageIcon("textures\\roads\\TriBottom.jpg");
        } else if (South && West) {
            return new ImageIcon("textures\\roads\\BottomLeft.jpg");
        } else if (South && East) {
            return new ImageIcon("textures\\roads\\BottomRight.jpg");
        } else if (North && West) {
            return new ImageIcon("textures\\roads\\TopLeft.jpg");
        } else if (North && East) {
            return new ImageIcon("textures\\roads\\TopRight.jpg");
        } else if (North || South) {
            return new ImageIcon("textures\\roads\\RoadVertical.jpg");
        } else {
            return new ImageIcon("textures\\roads\\RoadHorizontal.jpg");
        }
    }
}

