package map.buildings;

import main.ResourceTypes;

import javax.swing.*;

public class Tower extends Building {
    public Tower() {
        this.buildingImage = new ImageIcon("textures\\buildings\\tower.png");
        this.type = "Tower";
        this.resourceCost[ResourceTypes.WOOD] = 7; //7 wood
        this.resourceCost[ResourceTypes.IRON] = 1; //1 iron
        this.resourceCost[ResourceTypes.STONE] = 5; //5 stone
        this.borderSize = 2;
    }
}
