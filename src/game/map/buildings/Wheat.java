package game.map.buildings;

import game.map.Constructable;

import javax.swing.*;

public class Wheat extends Building {
    public Wheat() {
        this.buildingImage = new ImageIcon("textures\\terrain\\farmFood.jpg");
        this.type = Constructable.WHEAT;
        this.maxHealth = 0;
        this.currentHealth = maxHealth;
    }
}
