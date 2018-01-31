package units;

import main.Player;
import main.ResourceTypes;

import javax.swing.*;

public class Knight extends Unit {

    public Knight(Player owner) {
        super(owner);
        this.imageIcon = new ImageIcon("textures\\unit\\Knight.png");
        this.type = "Knight";
        this.maxMoves = 6;
        this.remainingMoves = maxMoves;
        this.maxHealth = 150;
        this.currentHealth = maxHealth;
        this.attackRange = 1;
        this.attackDamage = 60;
        this.resourceCost.put(ResourceTypes.GOLD, 2);
        this.resourceCost.put(ResourceTypes.FOOD, 5);
    }
}