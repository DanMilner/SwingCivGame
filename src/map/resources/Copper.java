package map.resources;

import main.Player;
import map.Tile;

import javax.swing.*;

public class Copper extends Tile {
    public Copper(int Xcoord, int Ycoord, Player owner) {
        super(Xcoord, Ycoord, owner);
        // TODO Auto-generated constructor stub
        this.tileImage = new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\terrain\\copper.png");
        this.type = "Copper";
    }
}