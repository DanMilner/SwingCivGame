package map.buildings;

import main.Player;
import map.Tile;

import javax.swing.*;
import java.util.ArrayList;

public class City extends Tile {
    public City(int Xcoord, int Ycoord, Player owner) {
        super(Xcoord, Ycoord, owner);
        this.tileImage = new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\buildings\\village.png");
        this.type = "City";
        this.isOccupied = true;
        this.cost[0] = 20; //20 wood
        this.cost[5] = 10; //10 stone
        this.hasCityConnection = true;
        this.borderSize = 3;

        this.buttonList = new ArrayList<>();
        buttonList.add("Settler");
        buttonList.add("Builder");
        buttonList.add("Swordsman");
        buttonList.add("Archer");
        buttonList.add("Knight");
    }
}