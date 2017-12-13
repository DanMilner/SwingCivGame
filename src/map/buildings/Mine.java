package map.buildings;

import main.ResourceTypes;

import javax.swing.*;

public class Mine extends Building {
    public Mine() {
        this.buildingImage = new ImageIcon("textures\\buildings\\Quarry.png");
        this.type = "Mine";
        this.resourceCost[ResourceTypes.WOOD] = 2; //2 wood
        this.borderSize = 1;
    }
}

