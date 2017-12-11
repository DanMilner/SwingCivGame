package units;

import main.Player;

import javax.swing.*;

public class Catapult extends Unit {
    public Catapult(Player owner){
        super(owner);
        this.Img = new ImageIcon("textures\\unit\\Catapult.png");
        this.type = "Catapult";
        this.moves = 2;
        this.movesAvaliable = 2;
        this.cost[0] = 2; //costs 2 wood
        this.cost[1] = 2; //costs 2 iron
        this.cost[4] = 1; //costs 1 Copper
        this.cost[5] = 1; //costs 1 Stone
        this.cost[6] = 5; //costs 5 food
    }
}