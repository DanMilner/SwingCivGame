package map.buildings;

import main.Player;
import map.Tile;

import javax.swing.*;

public class Aqueduct extends Tile {
    public Aqueduct(int Xcoord, int Ycoord, Player owner) {
        super(Xcoord, Ycoord, owner);
        // TODO Auto-generated constructor stub
        this.tileImage = new ImageIcon("textures\\buildings\\aqueduct.jpg");
        this.type = "Aqueduct";
    }
}