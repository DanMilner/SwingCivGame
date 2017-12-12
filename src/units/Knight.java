package units;

import main.Player;

import javax.swing.*;

public class Knight extends Unit {

    public Knight(Player owner){
        super(owner);
        this.imageIcon = new ImageIcon("textures\\unit\\Knight.png");
        this.type = "Knight";
        this.maxMoves = 6;
        this.remainingMoves = 6;
        this.resourceCost[2] = 2; //costs 2 gold
        this.resourceCost[6] = 5; //costs 5 food
    }
}