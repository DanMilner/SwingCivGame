package units;

import main.Player;

import javax.swing.*;

public class Catapult extends Unit {
    public Catapult(Player owner){
        super(owner);
        this.imageIcon = new ImageIcon("textures\\unit\\Catapult.png");
        this.type = "Catapult";
        this.maxMoves = 2;
        this.remainingMoves = 2;
        this.resourceCost[0] = 2; //costs 2 wood
        this.resourceCost[1] = 2; //costs 2 iron
        this.resourceCost[4] = 1; //costs 1 Copper
        this.resourceCost[5] = 1; //costs 1 Stone
        this.resourceCost[6] = 5; //costs 5 food
    }
}