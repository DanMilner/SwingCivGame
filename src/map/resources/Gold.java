package map.resources;

import main.Player;
import map.Tile;

import javax.swing.*;

public class Gold extends Tile {
    public Gold(int Xcoord, int Ycoord, Player owner) {
        super(Xcoord, Ycoord, owner);
        // TODO Auto-generated constructor stub
        this.tileImage = new ImageIcon("textures\\terrain\\gold.png");
        this.type = "Gold";
    }
}