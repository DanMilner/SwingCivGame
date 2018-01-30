package map.buildings;

import main.ResourceTypes;

import javax.swing.*;
import java.util.ArrayList;

public class Workshop extends Building {
    public Workshop() {
        ImageIcon icon = new ImageIcon("textures\\buildings\\Workshop.png");

        super.setImageIcon(icon);
        this.type = "Workshop";
        this.maxHealth = 400;
        this.currentHealth = maxHealth;

        this.resourceCost[ResourceTypes.COPPER.ordinal()] = 2; //20 Copper
        this.resourceCost[ResourceTypes.WOOD.ordinal()] = 10; //10 Wood
        this.resourceCost[ResourceTypes.IRON.ordinal()] = 1; //1 Iron

        this.buttonList = new ArrayList<>();
        buttonList.add("Catapult");
    }
}