package map.buildings;

import main.ResourceTypes;

import javax.swing.*;
import java.util.ArrayList;

public class Barracks extends Building {
    public Barracks() {
        ImageIcon icon = new ImageIcon("textures\\buildings\\Barracks.png");

        super.setImageIcon(icon);
        this.type = "Barracks";
        this.maxHealth = 400;
        this.currentHealth = maxHealth;

        this.resourceCost[ResourceTypes.WOOD.ordinal()] = 15; //15 wood
        this.resourceCost[ResourceTypes.STONE.ordinal()] = 3; //3 stone

        this.buttonList = new ArrayList<>();
        buttonList.add("Swordsman");
    }
}