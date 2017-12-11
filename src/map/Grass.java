package map;

import main.Player;

import javax.swing.*;

class Grass extends Tile {

    Grass(int Xcoord, int Ycoord, Player owner) {
        super(Xcoord, Ycoord, owner);
        // TODO Auto-generated constructor stub
        this.tileImage = new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\terrain\\Grass.jpg");
        this.type = "Grass";
    }
}
