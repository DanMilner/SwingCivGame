package map.buildings;

import javax.swing.*;

public class Wheat extends Building {
    public Wheat() {
        this.buildingImage = new ImageIcon("textures\\terrain\\farmFood.jpg");
        this.type = "Wheat";
        this.maxHealth = 0;
        this.currentHealth = maxHealth;
    }
}
