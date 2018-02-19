package game.map.buildings;

import game.map.Constructable;

import javax.swing.*;

public class Aqueduct extends Building {
    public Aqueduct() {
        this.buildingImage = new ImageIcon("textures\\buildings\\aqueduct.jpg");
        this.type = Constructable.AQUEDUCT;
        this.maxHealth = 0;
        this.currentHealth = maxHealth;
    }
}