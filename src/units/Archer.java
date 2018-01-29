package units;

import main.Player;
import main.ResourceTypes;

import javax.swing.*;

public class Archer extends Unit {
    public Archer(Player owner) {
        super(owner);
        this.imageIcon = new ImageIcon("textures\\unit\\Archer.png");
        this.type = "Archer";
        this.maxMoves = 3;
        this.remainingMoves = maxMoves;
        this.maxHealth = 50;
        this.currentHealth = maxHealth;
        this.attackRange = 4;
        this.attackDamage = 30;
        this.resourceCost[ResourceTypes.WOOD] = 1; //costs 1 wood
        this.resourceCost[ResourceTypes.IRON] = 1; //costs 1 iron
        this.resourceCost[ResourceTypes.FOOD] = 2; //costs 2 food
    }
}