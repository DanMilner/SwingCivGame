package map.resources;

import main.Player;
import map.Tile;

import javax.swing.*;

public class Coal extends Tile {
    public Coal(int Xcoord, int Ycoord, Player owner) {
        super(Xcoord, Ycoord, owner);
        // TODO Auto-generated constructor stub
        this.tileImage = new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\terrain\\coal.png");
        this.type = "Coal";
    }
}