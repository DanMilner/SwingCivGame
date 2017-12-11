package map.buildings;

import javax.swing.*;

public class LumberMill extends Building {
    public LumberMill() {
        this.buildingImage = new ImageIcon("textures\\buildings\\Lumber_Mill.png");
        this.type = "Lumber Mill";
        this.resourceCost[5] = 1; //1 stone
        this.borderSize = 1;
    }
}

