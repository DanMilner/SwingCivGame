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

        this.resourceCost.put(ResourceTypes.COPPER, 2);
        this.resourceCost.put(ResourceTypes.WOOD, 10);
        this.resourceCost.put(ResourceTypes.IRON, 1);

        this.buttonList = new ArrayList<>();
        buttonList.add("Catapult");
    }
}