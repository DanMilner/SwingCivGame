package map.buildings;

import main.Player;
import map.Tile;

import javax.swing.*;

public class Mine extends Tile {
    public Mine(int Xcoord, int Ycoord, Player owner) {
        super(Xcoord, Ycoord, owner);
        this.tileImage = new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\buildings\\Quarry.png");
        this.type = "Mine";
        this.isOccupied = true;
        this.cost[0] = 2; //2 wood
        this.borderSize = 1;
    }
}

