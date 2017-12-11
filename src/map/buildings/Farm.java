package map.buildings;

import javax.swing.*;

public class Farm extends Building {
    public Farm() {
        this.buildingImage = new ImageIcon("textures\\buildings\\Farm.png");
        this.type = "Farm";
        this.resourceCost[0] = 5; //5 wood
    }
}
