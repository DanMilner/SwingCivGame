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
        this.resourceCost.put(ResourceTypes.WOOD, 2);
        this.resourceCost.put(ResourceTypes.IRON, 2);
        this.resourceCost.put(ResourceTypes.COPPER, 1);
        this.resourceCost.put(ResourceTypes.STONE, 1);
        this.resourceCost.put(ResourceTypes.FOOD, 5);
    }
}