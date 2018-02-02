package map.units;

import map.Constructable;
import main.Player;
import map.resources.ResourceTypes;

import javax.swing.*;
import java.util.ArrayList;

public class Settler extends Unit {

    public Settler(Player owner) {
        super(owner);
        this.imageIcon = new ImageIcon("textures\\unit\\settler.png");
        this.type = Constructable.SETTLER;
        this.maxMoves = 2;
        this.remainingMoves = maxMoves;
        this.maxHealth = 10;
        this.currentHealth = maxHealth;
        this.resourceCost.put(ResourceTypes.FOOD, 20);
        this.buttonList = new ArrayList<>();
        buttonList.add(Constructable.CITY);
    }
}
