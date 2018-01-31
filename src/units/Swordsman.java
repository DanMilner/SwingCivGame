package units;

import main.Player;
import main.ResourceTypes;

import javax.swing.*;

public class Swordsman extends Unit {
    public Swordsman(Player owner) {
        super(owner);
        this.imageIcon = new ImageIcon("textures\\unit\\Swordsman.png");
        this.type = "Swordsman";
        this.maxMoves = 3;
        this.remainingMoves = maxMoves;
        this.attackRange = 1;
        this.maxHealth = 100;
        this.currentHealth = maxHealth;
        this.attackDamage = 40;
        this.resourceCost.put(ResourceTypes.IRON, 2);
        this.resourceCost.put(ResourceTypes.FOOD, 2);
    }
}