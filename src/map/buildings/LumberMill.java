package map.buildings;

import main.Player;
import map.Tile;

import javax.swing.*;

public class LumberMill extends Tile {
    public LumberMill(int Xcoord, int Ycoord, Player owner) {
        super(Xcoord, Ycoord, owner);
        this.tileImage = new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\buildings\\Lumber_Mill.png");
        this.type = "Lumber Mill";
        this.isOccupied = true;
        this.resourceType = 0; //wood
        this.cost[5] = 1; //1 stone
        this.borderSize = 1;
    }
}

