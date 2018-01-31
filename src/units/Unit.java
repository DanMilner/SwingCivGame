package units;

import main.Player;
import main.ResourceTypes;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public abstract class Unit {

    int maxMoves;
    int remainingMoves;
    int attackRange = 0;
    int attackDamage = 0;
    int maxHealth = 0;
    int currentHealth;
    ImageIcon imageIcon;
    String type;
    private Player owner;
    Map<ResourceTypes, Integer> resourceCost = new HashMap<>();
    private Iterator resourceIterator;
    private Map.Entry pair;

    ArrayList<String> buttonList;

    Unit(Player owner) {
        this.owner = owner;
    }

    public ArrayList<String> getButtonList() {
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

    public String getType() {
        return type;
    }

    public void setUpResourceIterator(){
        resourceIterator = resourceCost.entrySet().iterator();
    }

    public boolean hasNextResourceCost(){
        if (resourceIterator.hasNext()){
            pair = (Map.Entry)resourceIterator.next();
            return true;
        }else{
            return false;
        }
    }

    public int getNextValue(){
        return (int) pair.getValue();
    }

    public ResourceTypes getNextType(){
        return (ResourceTypes) pair.getKey();
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
