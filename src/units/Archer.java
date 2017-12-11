package units;

import main.Player;

import javax.swing.*;

public class Archer extends Unit {
    public Archer(Player owner){
        super(owner);
        this.Img = new ImageIcon("textures\\unit\\Archer.png");
        this.type = "Archer";
        this.moves = 3;
        this.movesAvaliable = 3;
        this.cost[0] = 1; //costs 1 wood
        this.cost[1] = 1; //costs 1 iron
        this.cost[6] = 2; //costs 2 food
    }
}