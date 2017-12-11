package map.resources;

import main.Player;
import map.Tile;

import javax.swing.*;

public class Mountain extends Tile {
    public Mountain(int Xcoord, int Ycoord, Player owner) {
        super(Xcoord, Ycoord, owner);
        // TODO Auto-generated constructor stub
        this.tileImage = new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\terrain\\Mountain.png");
        this.type = "Mountain";
        this.isOccupied = true;
    }
}