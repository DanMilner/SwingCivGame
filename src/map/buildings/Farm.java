package map.buildings;

import main.ResourceTypes;

import javax.swing.*;


public class Farm extends Building {
    public Farm() {
        this.buildingImage = new ImageIcon("textures\\buildings\\Farm.png");
        this.type = "Farm";
        this.resourceCost[ResourceTypes.WOOD] = 5; //5 wood
    }
}
