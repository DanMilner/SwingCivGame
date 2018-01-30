package map.buildings;

import main.ResourceTypes;

import javax.swing.*;
import java.util.ArrayList;

public class ArcheryRange extends Building {
    public ArcheryRange() {
        ImageIcon icon = new ImageIcon("textures\\buildings\\ArcheryRange.png");

        super.setImageIcon(icon);
        this.type = "ArcheryRange";
        this.maxHealth = 400;
        this.currentHealth = maxHealth;

        this.resourceCost[ResourceTypes.WOOD.ordinal()] = 10; //10 Wood
        this.resourceCost[ResourceTypes.STONE.ordinal()] = 2; //2 Stone

        this.buttonList = new ArrayList<>();
        buttonList.add("Archer");
    }
}