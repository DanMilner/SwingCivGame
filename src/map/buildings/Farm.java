package map.buildings;

import main.Player;
import map.Tile;

import javax.swing.*;

public class Farm extends Tile {
    public Farm(int Xcoord, int Ycoord, Player owner) {
        super(Xcoord, Ycoord, owner);
        this.tileImage = new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\buildings\\Farm.png");
        this.type = "Farm";
        this.isOccupied = true;
        this.cost[0] = 5; //5 wood
    }
}
