package map.buildings;

import javax.swing.*;

public class Mine extends Building {
    public Mine() {
        this.buildingImage = new ImageIcon("textures\\buildings\\Quarry.png");
        this.type = "Mine";
        this.resourceCost[0] = 2; //2 wood
        this.borderSize = 1;
    }
}

