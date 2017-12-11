package map.resources;

import main.Player;
import map.Tile;

import javax.swing.*;

public class Wheat extends Tile {
    public Wheat(int Xcoord, int Ycoord, Player owner) {
        super(Xcoord, Ycoord, owner);
        // TODO Auto-generated constructor stub
        this.tileImage = new ImageIcon("textures\\terrain\\farmFood.jpg");
        this.type = "Wheat";
    }
}
