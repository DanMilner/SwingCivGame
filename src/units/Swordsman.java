package units;

import main.Player;

import javax.swing.*;

public class Swordsman extends Unit {
    public Swordsman(Player owner){
        super(owner);
        this.imageIcon = new ImageIcon("textures\\unit\\Swordsman.png");
        this.type = "Swordsman";
        this.maxMoves = 3;
        this.remainingMoves = 3;
        this.resourceCost[1] = 2; //costs 2 iron
        this.resourceCost[6] = 2; //costs 2 food
    }
}