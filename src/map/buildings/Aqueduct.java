package map.buildings;

import javax.swing.*;

public class Aqueduct extends Building {
    public Aqueduct() {
        this.buildingImage = new ImageIcon("textures\\buildings\\aqueduct.jpg");
        this.type = "Aqueduct";
        this.maxHealth = 0;
        this.currentHealth = maxHealth;
    }
}