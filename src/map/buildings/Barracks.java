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

        this.resourceCost.put(ResourceTypes.WOOD, 15);
        this.resourceCost.put(ResourceTypes.STONE, 3);

        this.buttonList = new ArrayList<>();
        buttonList.add("Swordsman");
    }
}