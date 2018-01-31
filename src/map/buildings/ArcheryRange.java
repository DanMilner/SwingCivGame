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

        this.resourceCost.put(ResourceTypes.WOOD, 10);
        this.resourceCost.put(ResourceTypes.STONE, 2);


        this.buttonList = new ArrayList<>();
        buttonList.add("Archer");
    }
}