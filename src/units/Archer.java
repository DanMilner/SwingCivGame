package units;

import main.Player;

import javax.swing.*;

public class Archer extends Unit {
    public Archer(Player owner){
        super(owner);
        this.imageIcon = new ImageIcon("textures\\unit\\Archer.png");
        this.type = "Archer";
        this.maxMoves = 3;
        this.remainingMoves = 3;
        this.resourceCost[0] = 1; //costs 1 wood
        this.resourceCost[1] = 1; //costs 1 iron
        this.resourceCost[6] = 2; //costs 2 food
    }
}