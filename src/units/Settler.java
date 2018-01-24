package units;

import main.Player;
import main.ResourceTypes;

import javax.swing.*;
import java.util.ArrayList;

public class Settler extends Unit {

    public Settler(Player owner) {
        super(owner);
        this.imageIcon = new ImageIcon("textures\\unit\\settler.png");
        this.type = "Settler";
        this.maxMoves = 2;
        this.remainingMoves = 2;
        this.attackRange = 0;
        this.resourceCost[ResourceTypes.FOOD] = 20; //20 food

        this.buttonList = new ArrayList<>();
        buttonList.add("City");
    }
}
