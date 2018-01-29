package map.buildings;

import main.ResourceTypes;

import javax.swing.*;

public class Dock extends Building {
    public Dock() {
        this.buildingImage = new ImageIcon("textures\\buildings\\shipyard.png");
        this.type = "Dock";
        this.resourceCost[ResourceTypes.WOOD] = 10; //10 wood
        this.maxHealth = 300;
        this.currentHealth = maxHealth;
    }
}