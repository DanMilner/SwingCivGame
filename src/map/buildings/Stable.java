package map.buildings;

import map.Constructable;
import map.resources.ResourceTypes;

import javax.swing.*;
import java.util.ArrayList;

public class Stable extends Building {
    public Stable() {
        ImageIcon icon = new ImageIcon("textures\\buildings\\Stable.png");

        super.setImageIcon(icon);
        this.type = Constructable.STABLE;
        this.maxHealth = 400;
        this.currentHealth = maxHealth;

        this.resourceCost.put(ResourceTypes.WOOD, 10);
        this.resourceCost.put(ResourceTypes.GOLD, 2);

        this.buttonList = new ArrayList<>();
        buttonList.add(Constructable.KNIGHT);
    }
}