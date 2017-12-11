package map.buildings;

import main.Player;
import map.Tile;

import javax.swing.*;

public class Tower extends Tile {
    public Tower(int Xcoord, int Ycoord, Player owner) {
        super(Xcoord, Ycoord, owner);
        this.tileImage = new ImageIcon("textures\\buildings\\tower.png");
        this.type = "Tower";
        this.isOccupied = true;
        this.cost[0] = 7; //7 wood
        this.cost[1] = 1; //1 iron
        this.cost[5] = 5; //5 stone
        this.borderSize = 2;
    }
}
