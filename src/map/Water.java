package map;

import main.Player;

import javax.swing.*;

class Water extends Tile {
    Water(int Xcoord, int Ycoord, Player owner) {
        super(Xcoord, Ycoord, owner);
        // TODO Auto-generated constructor stub
        this.tileImage = new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\terrain\\Water.jpg");
        this.type = "Water";
        this.isOccupied = true;
    }
}