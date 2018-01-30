package map.buildings;

import main.ResourceTypes;

import javax.swing.*;
import java.util.ArrayList;

public class Stable extends Building {
    public Stable() {
        ImageIcon icon = new ImageIcon("textures\\buildings\\Stable.png");

        super.setImageIcon(icon);
        this.type = "Stable";
        this.maxHealth = 400;
        this.currentHealth = maxHealth;

        this.resourceCost[ResourceTypes.WOOD.ordinal()] = 10; //10 wood
        this.resourceCost[ResourceTypes.GOLD.ordinal()] = 2; //2 Gold

        this.buttonList = new ArrayList<>();
        buttonList.add("Knight");
    }
}