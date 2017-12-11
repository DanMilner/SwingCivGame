package units;

import main.Player;

import javax.swing.*;

public class Knight extends Unit {

    public Knight(Player owner){
        super(owner);
        this.Img = new ImageIcon("C:\\Users\\Daniel\\workspace\\Civ\\textures\\unit\\Knight.png");
        this.type = "Knight";
        this.moves = 6;
        this.movesAvaliable = 6;
        this.cost[2] = 2; //costs 2 gold
        this.cost[6] = 5; //costs 5 food
    }
}