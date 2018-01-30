package map.buildings;

import main.ResourceTypes;

import javax.swing.*;

public class Farm extends Building {
    public Farm() {
        this.buildingImage = new ImageIcon("textures\\buildings\\Farm.png");
        this.type = "Farm";
        this.resourceCost[ResourceTypes.WOOD.ordinal()] = 5; //5 wood
        this.maxHealth = 150;
        this.currentHealth = maxHealth;
    }
}
