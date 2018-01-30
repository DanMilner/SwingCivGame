package map.buildings;

import main.ResourceTypes;

import javax.swing.*;

public class LumberMill extends Building {
    public LumberMill() {
        this.buildingImage = new ImageIcon("textures\\buildings\\Lumber_Mill.png");
        this.type = "Lumber Mill";
        this.resourceCost[ResourceTypes.STONE.ordinal()] = 1; //1 stone
        this.borderSize = 1;
        this.maxHealth = 300;
        this.currentHealth = maxHealth;

        this.harvestableResourceTypes = new boolean[ResourceTypes.getNumberOfResourceTypes()];

        this.harvestableResourceTypes[ResourceTypes.WOOD.ordinal()] = true;
    }
}

