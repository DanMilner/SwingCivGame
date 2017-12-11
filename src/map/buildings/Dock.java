package map.buildings;

import main.Player;
import map.Tile;

import javax.swing.*;

public class Dock extends Tile {
    public Dock(int Xcoord, int Ycoord, Player owner) {
        super(Xcoord, Ycoord, owner);
        this.tileImage = new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\buildings\\shipyard.png");
        this.type = "Dock";
        this.cost[0] = 10; //10 wood
    }
}