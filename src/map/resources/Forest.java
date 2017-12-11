package map.resources;

import main.Player;
import map.Tile;

import javax.swing.*;

public class Forest extends Tile {
    public Forest(int Xcoord, int Ycoord, Player owner) {
        super(Xcoord, Ycoord, owner);
        // TODO Auto-generated constructor stub
        this.tileImage = new ImageIcon("textures\\terrain\\Tree.png");
        this.type = "Forest";
        //this.Resource = true; //tress can be removed by farms
    }
}