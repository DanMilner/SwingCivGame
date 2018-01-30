package map.buildings;

import main.ResourceTypes;

import javax.swing.*;

public class Tower extends Building {
    public Tower() {
        this.buildingImage = new ImageIcon("textures\\buildings\\tower.png");
        this.type = "Tower";
        this.resourceCost[ResourceTypes.WOOD.ordinal()] = 7; //7 wood
        this.resourceCost[ResourceTypes.IRON.ordinal()] = 1; //1 iron
        this.resourceCost[ResourceTypes.STONE.ordinal()] = 5; //5 stone
        this.borderSize = 2;
        this.maxHealth = 500;
        this.currentHealth = maxHealth;
    }
}
