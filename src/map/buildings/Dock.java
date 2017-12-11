package map.buildings;

import javax.swing.*;

public class Dock extends Building {
    public Dock() {
        this.buildingImage = new ImageIcon("textures\\buildings\\shipyard.png");
        this.type = "Dock";
        this.resourceCost[0] = 10; //10 wood
    }
}