package map.buildings;

import main.ResourceTypes;

import javax.swing.*;

public class Mine extends Building {
    public Mine() {
        this.buildingImage = new ImageIcon("textures\\buildings\\Quarry.png");
        this.type = "Mine";
        this.resourceCost[ResourceTypes.WOOD.ordinal()] = 2; //2 wood
        this.borderSize = 1;
        this.harvestableResourceTypes = new boolean[ResourceTypes.getNumberOfResourceTypes()];
        this.maxHealth = 300;
        this.currentHealth = maxHealth;

        this.harvestableResourceTypes[ResourceTypes.STONE.ordinal()] = true;
        this.harvestableResourceTypes[ResourceTypes.IRON.ordinal()] = true;
        this.harvestableResourceTypes[ResourceTypes.COPPER.ordinal()] = true;
        this.harvestableResourceTypes[ResourceTypes.GOLD.ordinal()] = true;
        this.harvestableResourceTypes[ResourceTypes.COAL.ordinal()] = true;
        this.harvestableResourceTypes[ResourceTypes.DIAMONDS.ordinal()] = true;
    }
}

