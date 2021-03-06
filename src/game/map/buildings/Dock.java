package game.map.buildings;

import game.map.Constructable;
import game.map.resources.ResourceTypes;

import javax.swing.*;

public class Dock extends Building {
    public Dock() {
        this.buildingImage = new ImageIcon("textures\\buildings\\shipyard.png");
        this.type = Constructable.DOCK;
        this.resourceCost.put(ResourceTypes.WOOD, 10);

        this.maxHealth = 300;
        this.currentHealth = maxHealth;
    }
}