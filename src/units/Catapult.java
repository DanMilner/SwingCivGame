package units;

import main.Player;
import main.ResourceTypes;

import javax.swing.*;

public class Catapult extends Unit {
    public Catapult(Player owner) {
        super(owner);
        this.imageIcon = new ImageIcon("textures\\unit\\Catapult.png");
        this.type = "Catapult";
        this.maxMoves = 2;
        this.remainingMoves = 2;
        this.attackRange = 2;
        this.resourceCost[ResourceTypes.WOOD] = 2; //costs 2 wood
        this.resourceCost[ResourceTypes.IRON] = 2; //costs 2 iron
        this.resourceCost[ResourceTypes.COPPER] = 1; //costs 1 Copper
        this.resourceCost[ResourceTypes.STONE] = 1; //costs 1 Stone
        this.resourceCost[ResourceTypes.FOOD] = 5; //costs 5 food
    }
}