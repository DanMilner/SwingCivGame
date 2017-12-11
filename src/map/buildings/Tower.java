package map.buildings;

import javax.swing.*;

public class Tower extends Building {
    public Tower() {
        this.buildingImage = new ImageIcon("textures\\buildings\\tower.png");
        this.type = "Tower";
        this.resourceCost[0] = 7; //7 wood
        this.resourceCost[1] = 1; //1 iron
        this.resourceCost[5] = 5; //5 stone
        this.borderSize = 2;
    }
}
