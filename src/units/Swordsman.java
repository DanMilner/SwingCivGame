package units;

import main.Player;

import javax.swing.*;

public class Swordsman extends Unit {
    public Swordsman(Player owner){
        super(owner);
        this.Img = new ImageIcon("textures\\unit\\Swordsman.png");
        this.type = "Swordsman";
        this.moves = 3;
        this.movesAvaliable = 3;
        this.cost[1] = 2; //costs 2 iron
        this.cost[6] = 2; //costs 2 food
    }
}