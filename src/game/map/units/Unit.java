package game.map.units;

import game.map.Constructable;
import game.gameModel.Player;
import game.map.resources.ResourceTypes;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public abstract class Unit {

    int maxMoves;
    int remainingMoves;
    int attackRange = 0;
    int attackDamage = 0;
    int maxHealth = 0;
    int currentHealth;
    ImageIcon imageIcon;
    Constructable type;
    private Player owner;
    Map<ResourceTypes, Integer> resourceCost = new HashMap<>();
    ArrayList<Constructable> buttonList;

    Unit(Player owner) {
        this.owner = owner;
    }

    public ArrayList<Constructable> getButtonList() {
        return buttonList;
    }

    public ImageIcon getImage() {
        return imageIcon;
    }

    public void setRemainingMoves(int remainingMoves) {
        this.remainingMoves = remainingMoves;
    }

    public int getRemainingMoves() {
        return remainingMoves;
    }

    public void resetMoves() {
        remainingMoves = maxMoves;
    }

    public Player getOwner() {
        return owner;
    }

    public Constructable getType() {
        return type;
    }

    public Map<ResourceTypes, Integer> getResourceCostMap(){
        return resourceCost;
    }

    public int getAttackRange() {
        return attackRange;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void reduceCurrentHealthBy(int attackDamage) {
        currentHealth -= attackDamage;
    }
}