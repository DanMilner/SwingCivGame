package map.units;

import map.Constructable;
import main.Player;
import map.resources.ResourceTypes;

import javax.swing.*;

public class Knight extends Unit {

    public Knight(Player owner) {
        super(owner);
        this.imageIcon = new ImageIcon("textures\\unit\\Knight.png");
        this.type = Constructable.KNIGHT;
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