package units;

import main.Player;
import main.ResourceTypes;

import javax.swing.*;

public class Catapult extends Unit {
    public Catapult(Player owner) {
        super(owner);
        this.imageIcon = new ImageIcon("textures\\unit\\Catapult.png");
        this.type = "Catapult";
        this.maxMoves = 8;
        this.remainingMoves = maxMoves;
        this.maxHealth = 200;
        this.currentHealth = maxHealth;
        this.attackRange = 2;
        this.attackDamage = 800;
        this.resourceCost[ResourceTypes.WOOD.ordinal()] = 2; //costs 2 wood
        this.resourceCost[ResourceTypes.IRON.ordinal()] = 2; //costs 2 iron
        this.resourceCost[ResourceTypes.COPPER.ordinal()] = 1; //costs 1 Copper
        this.resourceCost[ResourceTypes.STONE.ordinal()] = 1; //costs 1 Stone
        this.resourceCost[ResourceTypes.FOOD.ordinal()] = 5; //costs 5 food
    }
}